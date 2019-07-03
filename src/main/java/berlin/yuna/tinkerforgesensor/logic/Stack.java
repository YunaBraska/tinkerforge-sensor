package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.builder.Sensors;
import berlin.yuna.tinkerforgesensor.model.builder.Values;
import berlin.yuna.tinkerforgesensor.model.exception.DeviceNotSupportedException;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.TimeoutExecutor.execute;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_ALREADY_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_DISCONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_RECONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.PING;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.asyncStop;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.createAsync;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.createLoop;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.isEmpty;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;
import static java.lang.String.format;

/**
 * <h3>{@link Stack} implements {@link Closeable}</h3><br />
 * <i>Stack connection and event handler</i><br />
 */
public class Stack implements Closeable {

    /**
     * <h3>{@link Stack#connection}</h3>
     * IPConnection connection for {@link Sensor} in {@link Stack}
     */
    public IPConnection connection = new IPConnection();

    /**
     * <h3>{@link Stack#sensorEventConsumerList}</h3>
     * List of {@link Consumer} for getting all {@link SensorEvent}
     */
    public final List<Consumer<SensorEvent>> sensorEventConsumerList = new CopyOnWriteArrayList<>();

    private final SensorList<Sensor> sensorList = new SensorList<>();

    private final String host;
    private final String password;
    private final Integer port;
    private long lastConnect = System.currentTimeMillis();
    private final int timeoutMs = 3000;
    private final boolean ignoreConnectionError;
    private final String connectionHandlerName = getClass().getSimpleName() + "_" + UUID.randomUUID();
    private final String pingConnectionHandlerName = "Ping_" + connectionHandlerName;

    /**
     * <h3>Dummy Stack</h3>
     * Dummy {@link Stack}, wont connect to any {@link Sensor} as the host is not set
     *
     * @throws NetworkConnectionException should never happen
     */
    public Stack() throws NetworkConnectionException {
        this(null, null, null);
    }

    /**
     * <h3>Stack(host, port)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host for {@link Stack#connection}
     * @param port for {@link Stack#connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port) throws NetworkConnectionException {
        this(host, port, null, false);
    }

    /**
     * <h3>Stack(host, port, ignoreConnectionError)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host                  for {@link Stack#connection}
     * @param port                  for {@link Stack#connection}
     * @param ignoreConnectionError ignores any {@link NetworkConnectionException} and tries to auto reconnect
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port, final boolean ignoreConnectionError) throws NetworkConnectionException {
        this(host, port, null, ignoreConnectionError);
    }

    /**
     * <h3>Stack(host, port, password)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host     for {@link Stack#connection}
     * @param port     for {@link Stack#connection}
     * @param password for {@link Stack#connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port, final String password) throws NetworkConnectionException {
        this(host, port, password, false);
    }

    /**
     * <h3>Stack(host, port, password, ignoreConnectionError)</h3>
     * Auto connects and auto {@link Closeable} {@link Stack#close()} {@link Sensor}s and manages the {@link Stack#sensorList} by creating {@link Thread}
     *
     * @param host                  for {@link Stack#connection}
     * @param port                  for {@link Stack#connection}
     * @param password              for {@link Stack#connection}
     * @param ignoreConnectionError ignores any {@link NetworkConnectionException} and tries to auto reconnect
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public Stack(final String host, final Integer port, final String password, final boolean ignoreConnectionError) throws NetworkConnectionException {
        this.host = host;
        this.password = password;
        this.port = port;
        this.ignoreConnectionError = ignoreConnectionError;
        connect();
    }

    /**
     * <h3>connect</h3>
     * connects to given host - this method will be called from {@link Stack} constructor
     *
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public void connect() throws NetworkConnectionException {
        connection = new IPConnection();
        connection.setAutoReconnect(true);
        connection.setTimeout(timeoutMs);
        connection.addDisconnectedListener(event -> handleConnect(event, true));
        connection.addConnectedListener(event -> handleConnect(event, false));
        connection.addEnumerateListener(this::doPlugAndPlay);
        sensorList.clear();
        if (host != null) {
            final Object result = execute(timeoutMs, () -> {
                connection.connect(host, port);
                if (!isEmpty(password)) {
                    connection.authenticate(password);
                }
                return true;
            });
            if (!ignoreConnectionError && result instanceof Throwable) {
                throw new NetworkConnectionException((Throwable) result);
            }
        }
//        createLoop(connectionHandlerName, timeoutMs, run -> checkConnection());
        createLoop(pingConnectionHandlerName, 8, run -> sendEvent(sensorList.getDefault(), System.currentTimeMillis(), PING));
    }

    /**
     * <h3>isConnecting</h3>
     *
     * @return true if stack is connecting to the sensors
     */
    public boolean isConnecting() {
        return (lastConnect + (timeoutMs / 2)) > System.currentTimeMillis();
    }

    /**
     * <h3>disconnect</h3>
     * disconnects all {@link Sensor} from the given host and removes the sensors from {@link Stack#sensorList}
     */
    public synchronized void disconnect() {
        asyncStop(pingConnectionHandlerName, connectionHandlerName);
        execute(timeoutMs + 256, () -> {
            try {
                sensorList.clear();
                connection.disconnect();
            } catch (Exception ignored) {
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
        for (Sensor sensor : sensors()) {
            for (ValueType valueType : ValueType.values()) {
                final Long value = sensor.values().get(valueType, null);
                if (value != null) {
                    final int valueSize = valueType.toString().length() + 1;
                    lineHead.append(format("%" + valueSize + "s |", valueType));
                    lineValue.append(format("%" + valueSize + "s |", value));
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
        return new Sensors(sensorList);
    }

    /**
     * <h3>values</h3>
     *
     * @return List of sensors {@link Values}
     */
    public Values values() {
        return new Values(sensorList);
    }

    private void doPlugAndPlay(
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
                createAsync("Connect_" + uid, run -> initSensor(uid, deviceIdentifier, DEVICE_CONNECTED));
                break;
            case ENUMERATION_TYPE_CONNECTED:
                createAsync("Connect_" + uid, run -> initSensor(uid, deviceIdentifier, DEVICE_RECONNECTED));
                break;
            case ENUMERATION_TYPE_DISCONNECTED:
                final Sensor sensor = sensorList.stream().filter(entity -> entity.uid.equals(uid)).findFirst().orElse(null);
                sendEvent(sensor, 2L, DEVICE_DISCONNECTED);
                sensorList.remove(sensor);
                break;
        }
    }

    private void initSensor(final String uid, final int deviceIdentifier, final ValueType enumerationType) {
        lastConnect = System.currentTimeMillis();
        try {
            final Sensor sensor = Sensor.newInstance(deviceIdentifier, uid, connection);
            final Optional<Sensor> previousSensor = sensorList.stream().filter(s -> s.equals(sensor)).findFirst();
            if (previousSensor.isPresent() && previousSensor.get().isConnected()) {
                sendEvent(sensor, 42L, DEVICE_ALREADY_CONNECTED);
            } else {
                sensor.flashLed();
                sensorList.add(sensor);
                sensorList.linkParent(sensor);
                sendEvent(sensor, 42L, enumerationType);
                sensor.addListener(sensorEvent -> sensorEventConsumerList.forEach(sensorConsumer -> sensorConsumer.accept((SensorEvent) sensorEvent)));
            }
        } catch (DeviceNotSupportedException | NetworkConnectionException e) {
            System.err.println(format("doPlugAndPlay [ERROR] uid [%s] [%s]", uid, e.getMessage()));
        }
    }

    private void sendEvent(final Sensor sensor, final long value, final ValueType valueType) {
        if (sensor != null) {
            sensorEventConsumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(sensor, value, valueType)));
        }
    }

    private void handleConnect(final short connectionEvent, final boolean disconnectEvent) {
        if (disconnectEvent) {
            switch (connectionEvent) {
                case IPConnection.DISCONNECT_REASON_REQUEST:
                case IPConnection.DISCONNECT_REASON_ERROR:
                case IPConnection.DISCONNECT_REASON_SHUTDOWN:
                    //Clear list fast at USB interruption
                    disconnect();
                    sendEvent(sensorList.getDefault(), (long) connectionEvent, DEVICE_DISCONNECTED);
                    break;
            }
        } else {
            try {
                connection.enumerate();
            } catch (Exception ignored) {
            }

            switch (connectionEvent) {
                case IPConnection.CONNECT_REASON_REQUEST:
                    sendEvent(sensorList.getDefault(), (long) connectionEvent, DEVICE_CONNECTED);
                    break;
                case IPConnection.CONNECT_REASON_AUTO_RECONNECT:
                    sendEvent(sensorList.getDefault(), (long) connectionEvent, DEVICE_RECONNECTED);
                    break;
            }
        }
    }


//FIXME: old connection handler - can it be reactivated?
//    private synchronized void checkConnection() {
//        sensorList.waitForUnlock(timeoutMs);
//        if (sensorList.isEmpty()) {
//            deviceSearch();
//        } else if (deviceSearch + timeoutMs < currentTimeMillis()) {
//            for (Sensor sensor : sensorList) {
//                try {
//                    sensor.refreshPortE();
//                } catch (TimeoutException | NotConnectedException e) {
//                    sendEvent(sensor, 404, DEVICE_TIMEOUT);
//                    sensorList.remove(sensor);
//                    deviceSearch();
//                }
//            }
//        }
//    }

//    private void deviceSearch() {
//        if (deviceSearch + timeoutMs < currentTimeMillis()) {
//            try {
//                preventDeviceSearch();
//                sendEvent(sensorList.getDefault(), 1, DEVICE_SEARCH);
//                disconnect();
//                preventDeviceSearch();
//                connect();
//            } catch (NetworkConnectionException e) {
//                System.err.println(format("[ERROR] RECOVER [deviceSearch] [%s]", e.getClass().getSimpleName()));
//            } finally {
//                preventDeviceSearch();
//            }
//        }
//    }
//
//    private void stopThread(final Thread thread) {
//        TimeoutExecutor.execute(timeoutMs / 2, () -> {
//            try {
//                preventDeviceSearch();
//                thread.interrupt();
//                thread.join();
//            } catch (Exception ignore) {
//                return false;
//            } finally {
//                preventDeviceSearch();
//            }
//            return true;
//        });
//    }
}
