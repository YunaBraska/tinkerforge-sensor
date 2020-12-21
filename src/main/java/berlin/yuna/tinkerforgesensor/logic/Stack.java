package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.exception.ConnectionException;
import berlin.yuna.tinkerforgesensor.model.Registry;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.helper.GetSensor;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.createAsync;
import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.waitFor;
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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Stack implements Closeable {

    private String connectionKey;
    private boolean postStart = true;
    private final Set<Sensor> sensors = ConcurrentHashMap.newKeySet();
    private final Set<String> initUids = ConcurrentHashMap.newKeySet();
    private final Set<Consumer<SensorEvent>> listeners = ConcurrentHashMap.newKeySet();
    private final Exception[] exceptions = new Exception[]{null};
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
        createAsync(
                Stack.class.getSimpleName() + ".connect:" + UUID.randomUUID(),
                run -> connections.computeIfAbsent(connectionKey,
                        s -> newConnection(host, port))
        );

        validateConnectionStatus(host, port, waitFor(10000, () -> exceptions[0] != null || (initUids.isEmpty() && !sensors.isEmpty())));
        return setPorts();
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

    public Stack disconnect() {
        close();
        return this;
    }

    public Stack sendEvent(final SensorEvent event) {
        listeners.forEach(listener -> listener.accept(event));
        return this;
    }

    public GetSensor get() {
        return new GetSensor(this);
    }

    public boolean hasPostStart() {
        return postStart;
    }

    public Stack setPostStart(boolean postStart) {
        this.postStart = postStart;
        return this;
    }

    private Stack connectionListener(
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
        return this;
    }

    private Stack connectSensor(final String uid, final String parentUid, final char position, final int id, Registry.Register register) {
        createAsync("Connect_" + uid, run -> {
            Sensor sensor = findSensor(uid);
            if (sensor == null) {
                initUids.add(uid);
                sensor = new Sensor(id, uid, this, position, parentUid, register.getType(), register.getHandler());
                sensors.add(sensor);
                setPorts();
                if (postStart) {
                    sendEvent(new SensorEvent(sensor.handler().initConfig().init().runTest().sensor(), 1,
                            ValueType.DEVICE_CONNECTED));
                } else {
                    sendEvent(new SensorEvent(sensor.handler().initConfig().init().sensor(), 1, ValueType.DEVICE_CONNECTED));
                }
                initUids.remove(uid);
            } else {
                sendEvent(new SensorEvent(sensor, 1, ValueType.DEVICE_RECONNECTED));
            }
        });
        return this;
    }

    private Stack setPorts() {
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
        return this;
    }

    private IPConnection newConnection(final String host, final int port) {
        final IPConnection connection = createIPConnection();
        try {
            connection.connect(host, port);
            connection.addEnumerateListener(this::connectionListener);
            connection.setAutoReconnect(true);
            connection.enumerate();
        } catch (NetworkException e) {
            exceptions[0] = e;
        } catch (NotConnectedException | AlreadyConnectedException ignored) {
        }
        return connection;
    }

    protected IPConnection createIPConnection() {
        return new IPConnection();
    }

    private void validateConnectionStatus(final String host, final int port, final boolean connected) {
        if (!connected || exceptions[0] != null) {
            throw new ConnectionException(
                    "Unable to connect host [" + host + "] port [" + port + "]"
                            + (!connected ? " no sensor was connected" : ""),
                    exceptions[0] != null ? exceptions[0] : null
            );
        }
    }

    @Override
    public void close() {
        try {
            connections.remove(connectionKey);
            sensors.clear();
            getConnection().disconnect();
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
