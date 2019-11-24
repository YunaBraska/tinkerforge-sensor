package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.Connection;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.builder.Sensors;
import berlin.yuna.tinkerforgesensor.model.builder.Values;
import berlin.yuna.tinkerforgesensor.model.exception.DeviceNotSupportedException;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.LocalAudio;
import berlin.yuna.tinkerforgesensor.model.sensor.LocalControl;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static berlin.yuna.tinkerforgesensor.model.type.TimeoutExecutor.execute;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_ALREADY_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_DISCONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_RECONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.PING;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.createAsync;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.createLoop;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.isEmpty;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.loops;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

/**
 * <h3>{@link Stack} implements {@link Closeable}</h3><br>
 * <i>Stack connection and event handler</i><br>
 */
public class Stack implements Closeable {

    /**
     * <h3>{@link Stack#connections}</h3>
     * Connection for {@link Sensor} in {@link Stack}
     */
    private final HashMap<String, SensorList<Sensor>> sensorList = new HashMap<>();
    private final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    /**
     * <h3>{@link Stack#consumers}</h3>
     * List of {@link Consumer} for getting all {@link SensorEvent}
     */
    public final List<Consumer<SensorEvent>> consumers = new CopyOnWriteArrayList<>();


    private long lastConnect = System.currentTimeMillis();
    private final int timeoutMs = 3000;
    private final String connectionHandlerName = getClass().getSimpleName() + "_" + UUID.randomUUID();
    private final String pingConnectionHandlerName = "Ping_" + connectionHandlerName;
    private static final int MAX_PRINT_VALUES = 50;

    /**
     * <h3>Dummy Stack</h3>
     * Dummy {@link Stack}, wont connect to any {@link Sensor} as the host is not set
     *
     * @throws NetworkConnectionException should never happen
     */
    public Stack() throws NetworkConnectionException {
        this(new Connection(null, null, null));
    }

    /**
     * <h3>Stack(host, port)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host for {@link Connection}
     * @param port for {@link Connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port) throws NetworkConnectionException {
        this(new Connection(host, port, null, false));
    }

    /**
     * <h3>Stack(host, port, ignoreConnectionError)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host                  for {@link Connection}
     * @param port                  for {@link Connection}
     * @param ignoreConnectionError ignores any {@link NetworkConnectionException} and tries to auto reconnect
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port, final boolean ignoreConnectionError) throws NetworkConnectionException {
        this(new Connection(host, port, null, ignoreConnectionError));
    }

    /**
     * <h3>Stack(host, port, password)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host     for {@link Connection}
     * @param port     for {@link Connection}
     * @param password for {@link Connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port, final String password) throws NetworkConnectionException {
        this(new Connection(host, port, password, false));
    }

    /**
     * <h3>Stack(host, port, password, ignoreConnectionError)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param connection for {@link Stack#connections}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final Connection connection) throws NetworkConnectionException {
        addStack(connection);
    }

    /**
     * <h3>addStack</h3>
     * adds another stack to the current one
     *
     * @param connection for {@link Stack#connections}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public void addStack(final Connection connection) throws NetworkConnectionException {
        if (connection == null) {
            throw new RuntimeException("Connection [null] is not allowed");
        } else {
            if (connections.containsValue(connection)) {
                System.err.println(format("Already exists connection [%s]", connection));
                disconnect(connection.getStackId());
            }
            connections.put(connection.getStackId(), connection);
            connect(connection.getStackId());
        }
    }

    /**
     * <h3>connect</h3>
     * connects to given host - this method will be called from {@link Stack} constructor
     *
     * @param stackId {@link Stack} connectionId
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    private void connect(final String stackId) throws NetworkConnectionException {
        final Connection connection = connections.get(stackId);
        final IPConnection ipConnection = connection.getIpConnection();
        ipConnection.setAutoReconnect(true);
        ipConnection.setTimeout(timeoutMs);
        ipConnection.addDisconnectedListener(event -> handleConnect(stackId, event, true));
        ipConnection.addConnectedListener(event -> handleConnect(stackId, event, false));
        ipConnection.addEnumerateListener(
                (uid, connectedUid, position, hardwareVersion, firmwareVersion, deviceIdentifier, enumerationType)
                        -> doPlugAndPlay(stackId, uid, connectedUid, position, hardwareVersion, firmwareVersion, deviceIdentifier, enumerationType)
        );
        sensorList.computeIfAbsent(stackId, sensorList -> new SensorList<>());
        clearSensorList(stackId);
        if (connection.getHost() != null) {
            final Object result = execute(timeoutMs, () -> {
                ipConnection.connect(connection.getHost(), connection.getPort());
                if (!isEmpty(connection.getPassword())) {
                    ipConnection.authenticate(connection.getPassword());
                }
                return true;
            });
            if (!connection.isIgnoreConnectionError() && result instanceof Throwable) {
                throw new NetworkConnectionException((Throwable) result);
            }
        }
//        createLoop(connectionHandlerName, timeoutMs, run -> checkConnection());
        if (!loops.containsKey(pingConnectionHandlerName)) {
            createLoop(pingConnectionHandlerName, 8, run -> sendEvent(sensorList.get(stackId).getDefault(), System.currentTimeMillis(), PING));
        }
    }

    /**
     * <h3>isConnecting</h3>
     *
     * @return true if stack is connecting to the sensors
     */
    public boolean isConnecting() {
        return (lastConnect + (timeoutMs / 2)) > System.currentTimeMillis();
    }


    private synchronized void disconnect() {
        connections.forEach((key, value) -> disconnect(value.getStackId()));
    }

    /**
     * <h3>disconnect</h3>
     * disconnects a stack {@link Sensor} from the given host and removes their sensors from {@link Stack#sensorList}
     *
     * @param stackId Stack to disconnect
     */
    public synchronized void disconnect(final String stackId) {
        execute(timeoutMs + 256, () -> {
            try {
                clearSensorList(stackId);
                if (connections.containsKey(stackId)) {
                    connections.get(stackId).getIpConnection().disconnect();
                }
            } catch (Exception ignored) {
            } finally {
                connections.remove(stackId);
            }
            return true;
        });
    }

    /**
     * disconnects all {@link Sensor} from the given host see {@link Stack#disconnect()}
     */
    @Override
    public void close() {
        disconnect();
    }

    /**
     * <h3>valuesToString</h3>
     *
     * @return all values from all sensors as string
     */
    public String valuesToString() {
        final StringBuilder lineHead = new StringBuilder();
        final StringBuilder lineValue = new StringBuilder();
        final List<ValueType> IGNORE_TYPES = asList(BUTTON_PRESSED, BUTTON_RELEASED);
        for (Sensor sensor : sensors()) {
            for (ValueType valueType : ValueType.values()) {
                if (IGNORE_TYPES.contains(valueType)) {
                    continue;
                }
                final List<Long> values = sensor.values().getList(valueType, -1);
                if (!values.isEmpty()) {
                    String value = values.stream().map(Object::toString).collect(joining(", "));
                    value = value.length() > MAX_PRINT_VALUES ? value.substring(0, MAX_PRINT_VALUES) : value;
                    final int valueLength = value.length();
                    final int typeLength = valueType.toString().length();
                    final int fieldSize = (Math.max(valueLength, typeLength)) + 1;
                    lineHead.append(center(" " + valueType, fieldSize)).append(" |");
                    lineValue.append(format("%" + fieldSize + "s |", value));
                }
            }
        }
        lineHead.append(format("%9s |", "Sensors"));
        lineValue.append(format("%9s |", sensors().size()));
        return System.lineSeparator() + lineHead.toString() + System.lineSeparator() + lineValue.toString();
    }

    /**
     * <h3>sensors</h3>
     *
     * @return List of sensors {@link Sensors}
     */
    public Sensors sensors() {
        return sensorList.values().stream().flatMap(List::stream).collect(Collectors.toCollection(Sensors::new));
    }

    /**
     * <h3>values</h3>
     *
     * @return List of sensors {@link Values}
     */
    public Values values() {
        return sensorList.values().stream().flatMap(List::stream).collect(Collectors.toCollection(Values::new));
    }

    private void doPlugAndPlay(
            final String stackId,
            final String uid,
            final String connectedUid,
            final char position,
            final short[] hardwareVersion,
            final short[] firmwareVersion,
            final int deviceIdentifier,
            final short enumerationType
    ) {
        switch (enumerationType) {
            case ENUMERATION_TYPE_AVAILABLE:
                createAsync("Connect_" + uid, run -> initSensor(stackId, uid, deviceIdentifier, DEVICE_CONNECTED));
                break;
            case ENUMERATION_TYPE_CONNECTED:
                createAsync("Connect_" + uid, run -> initSensor(stackId, uid, deviceIdentifier, DEVICE_RECONNECTED));
                break;
            case ENUMERATION_TYPE_DISCONNECTED:
                final Sensor sensor = sensorList.get(stackId).stream().filter(entity -> entity.uid.equals(uid)).findFirst().orElse(null);
                sendEvent(sensor, 2L, DEVICE_DISCONNECTED);
                sensorList.get(stackId).remove(sensor);
                break;
        }
    }

    private void initSensor(final String stackId, final String uid, final int deviceIdentifier, final ValueType enumerationType) {
        lastConnect = System.currentTimeMillis();
        try {
            final Sensor sensor = Sensor.newInstance(deviceIdentifier, uid, connections.get(stackId).getIpConnection());
            final Optional<Sensor> previousSensor = sensorList.get(stackId).stream().filter(s -> s.equals(sensor)).findFirst();
            if (previousSensor.isPresent() && previousSensor.get().isConnected()) {
                sendEvent(sensor, 42L, DEVICE_ALREADY_CONNECTED);
            } else {
                sensor.flashLed();
                sensorList.get(stackId).add(sensor);
                sensorList.get(stackId).linkParent(sensor);
                sendEvent(sensor, 42L, enumerationType);
                sensor.addListener(sensorEvent -> consumers.forEach(sensorConsumer -> sensorConsumer.accept((SensorEvent) sensorEvent)));
            }
        } catch (DeviceNotSupportedException | NetworkConnectionException e) {
            System.err.println(format("doPlugAndPlay [ERROR] uid [%s] [%s]", uid, e.getMessage()));
        }
    }

    private void sendEvent(final Sensor sensor, final long value, final ValueType valueType) {
        if (sensor != null) {
            consumers.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(sensor, value, valueType)));
        }
    }

    private void handleConnect(final String stackId, final short connectionEvent, final boolean disconnectEvent) {
        if (disconnectEvent) {
            switch (connectionEvent) {
                case IPConnection.DISCONNECT_REASON_REQUEST:
                case IPConnection.DISCONNECT_REASON_ERROR:
                case IPConnection.DISCONNECT_REASON_SHUTDOWN:
                    clearSensorList(stackId);
                    sendEvent(sensorList.get(stackId).getDefault(), (long) connectionEvent, DEVICE_DISCONNECTED);
                    break;
            }
        } else {
            try {
                connections.get(stackId).getIpConnection().enumerate();
            } catch (Exception ignored) {
            }

            switch (connectionEvent) {
                case IPConnection.CONNECT_REASON_REQUEST:
                    sendEvent(sensorList.get(stackId).getDefault(), (long) connectionEvent, DEVICE_CONNECTED);
                    break;
                case IPConnection.CONNECT_REASON_AUTO_RECONNECT:
                    sendEvent(sensorList.get(stackId).getDefault(), (long) connectionEvent, DEVICE_RECONNECTED);
                    break;
            }
        }
    }

    private void clearSensorList(final String stackId) {
        final SensorList<Sensor> sensorList = this.sensorList.get(stackId);
        sensorList.forEach(sensor -> sensor.refreshPeriod(0));
        sensorList.clear();
        try {
            final Sensor sensor = sensorList.getDefault();
            final LocalControl localControl = new LocalControl(sensor.device, sensor.uid);
            sensorList.add(new LocalAudio(sensor.device, sensor.uid));
            sensorList.add(localControl);
            localControl.addListener(sensorEvent -> consumers.forEach(sensorConsumer -> sensorConsumer.accept((SensorEvent) sensorEvent)));
        } catch (NetworkConnectionException ignored) {
        }
    }

    //TODO: move to utils
    private String center(final String text, final int length) {
        if (text.length() < length) {
            final int left = (length - text.length()) / 2;
            final int right = (length - text.length()) - left;
            final String leftS = IntStream.range(0, left).mapToObj(i -> " ").collect(joining(""));
            final String rightS = IntStream.range(0, right).mapToObj(i -> " ").collect(joining(""));
            return leftS + text + rightS;
        }
        return text;
    }
}
