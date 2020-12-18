package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.exception.ConnectionException;
import berlin.yuna.tinkerforgesensor.model.Registry;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.helper.GetSensor;
import berlin.yuna.tinkerforgesensor.util.ThreadUtil;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;

import java.io.Closeable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.tinkerforge.IPConnectionBase.CONNECTION_STATE_DISCONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;
import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import static java.lang.Character.toLowerCase;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

//TODO Stack graceful shutdown on interrupt?
//TODO Authentication https://www.tinkerforge.com/de/doc/Software/IPConnection_Java.html#authenticate
public class Stack implements Closeable {

    private String connectionKey;
    private final Set<Sensor> sensors = ConcurrentHashMap.newKeySet();
    private final Set<Consumer<SensorEvent>> listeners = ConcurrentHashMap.newKeySet();
    private static final Map<String, IPConnection> connections = new ConcurrentHashMap<>();

    public Stack connect() {
        return connect("localhost", 4223);
    }

    public Stack connect(final String host) {
        return connect(host, 4223);
    }

    public Stack connect(final String host, final int port) {
        close();
        connectionKey = host + ":" + port;
        connections.computeIfAbsent(connectionKey, s -> newConnection(host, port));
        return this;
    }

    public boolean isConnected() {
        return getConnection().getConnectionState() != CONNECTION_STATE_DISCONNECTED;
    }

    public IPConnection getConnection() {
        return connectionKey == null ? new IPConnection() : connections.getOrDefault(connectionKey, new IPConnection());
    }

    public Set<Sensor> getSensors() {
        return new HashSet<>(sensors);
    }

    public Set<Sensor> getSensors(final Predicate<? super Sensor> filter) {
        return sensors.stream().filter(filter).collect(Collectors.toSet());
    }

    public List<Sensor> getSensorsSorted() {
        return sensors.stream().sorted().collect(toList());
    }

    public List<Sensor> getSensorsSorted(final Predicate<? super Sensor> filter) {
        return sensors.stream().filter(filter).sorted().collect(toList());
    }

    public Sensor getSensor(final int index, final Class<?>... types) {
        final List<Sensor> result = getSensorList(types);
        return result.isEmpty() || result.size() < index ? null : result.get(index);
    }

    public List<Sensor> getSensorList(final Class<?>... types) {
        return sensors.stream().filter(sensor -> stream(types).anyMatch(sensor::is)).sorted().collect(toList());
    }

    public Stack addListener(final Consumer<SensorEvent> listener) {
        listeners.add(listener);
        return this;
    }

    public Sensor findSensor(String uid) {
        return sensors.stream().filter(sensor -> sensor.getUid().equals(uid)).findFirst().orElse(null);
    }

    public void disconnect() {
        close();
    }

    public void sendEvent(final SensorEvent event) {
        listeners.forEach(listener -> listener.accept(event));
    }

    public GetSensor get(){
        return new GetSensor(this);
    }

    private void connectionListener(
            final String uid,
            final String parentUid,
            final char position,
            final short[] hardwareVersion,
            final short[] firmwareVersion,
            final int id,
            final short enumerationType
    ) {
        Registry.findById(id).ifPresentOrElse(register -> {
            if (enumerationType == ENUMERATION_TYPE_CONNECTED || enumerationType == ENUMERATION_TYPE_AVAILABLE) {
                connectSensor(uid, parentUid, position, id, register);
            } else if (enumerationType == ENUMERATION_TYPE_DISCONNECTED) {
                sendEvent(new SensorEvent(findSensor(uid), 0, ValueType.DEVICE_DISCONNECTED));
            }
        }, () -> sendEvent(new SensorEvent(null, id, ValueType.DEVICE_UNKNOWN)));
    }

    private void connectSensor(final String uid, final String parentUid, final char position, final int id, Registry.Register register) {
        ThreadUtil.createAsync("Connect_" + uid, run -> {
            Sensor sensor = findSensor(uid);
            if (sensor == null) {
                sensor = new Sensor(id, uid, this, position, parentUid, register.getType(), register.getHandler());
                sensors.add(sensor);
                setPorts();
                sendEvent(new SensorEvent(sensor.handler().initConfig().init().runTest().sensor(), 1, ValueType.DEVICE_CONNECTED));
            } else {
                sendEvent(new SensorEvent(sensor, 1, ValueType.DEVICE_RECONNECTED));
            }
        });
    }

    private void setPorts() {
        new HashSet<>(sensors).stream().sorted().forEach(sensor -> {
            if ("0".equals(sensor.getParentUid())) {
                sensor.isBrick(true);
                sensor.setPort(10);
            } else if (isDigit(sensor.getPosition())) {
                sensor.isBrick(true);
                sensor.setPort((getNumericValue(sensor.getPosition()) + 1) * 10);
            } else {
                sensor.isBrick(false);
                sensor.setPort((((int) toLowerCase(sensor.getPosition())) - 96) + sensor.getParent().map(Sensor::getPort).orElse(0));
            }
        });
    }

    private IPConnection newConnection(final String host, final int port) {
        final IPConnection connection = createIPConnection();
        try {
            connection.connect(host, port);
            connection.addEnumerateListener(this::connectionListener);
            connection.setAutoReconnect(true);
            connection.enumerate();
        } catch (NetworkException e) {
            throw new ConnectionException("Unable to connect host [" + host + "] port [" + port + "]", e);
        } catch (NotConnectedException | AlreadyConnectedException ignored) {
        }
        return connection;
    }

    protected IPConnection createIPConnection() {
        return new IPConnection();
    }

    @Override
    public void close() {
        try {
            getConnection().disconnect();
            connections.remove(connectionKey);
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stack stack = (Stack) o;

        return Objects.equals(connectionKey, stack.connectionKey);
    }

    @Override
    public int hashCode() {
        return connectionKey != null ? connectionKey.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Stack{" +
                "connectedTo='" + connectionKey + '\'' +
                '}';
    }


}
