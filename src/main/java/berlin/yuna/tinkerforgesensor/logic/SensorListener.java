package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.exception.DeviceNotSupportedException;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TinkerforgeThread;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

//TODO: sensor.type.is
//TODO: value.type.is
//TODO: sensorList.searchValue.is
//TODO: sensorList.searchType.is
public class SensorListener implements Closeable {

    /**
     * IPConnection connection of the {@link Sensor} and for enumerating available {@link Sensor}
     */
    public IPConnection connection = new IPConnection();

    /**
     * SensorList holds all connected {@link Sensor} managing that list
     * This list is never empty as and contains {@link SensorList#getDefault()} as fallback
     */
    public final SensorList<Sensor> sensorList = new SensorList<>();

    /**
     * List of {@link Consumer} for getting all {@link SensorEvent}
     */
    public final List<Consumer<SensorEvent>> sensorEventConsumerList = new CopyOnWriteArrayList<>();

    private final String host;
    private final String password;
    private final Integer port;
    private long lastConnect = System.currentTimeMillis();
    private final int timeoutMs = 3000;
    private final boolean ignoreConnectionError;
    private final String connectionHandlerName = getClass().getSimpleName() + "_" + UUID.randomUUID();
    private final String pingConnectionHandlerName = "Ping_" + connectionHandlerName;

    /**
     * Dummy Sensorlist, wont connect to any {@link Sensor} as the host is not set
     *
     * @throws NetworkConnectionException should never happen
     */
    public SensorListener() throws NetworkConnectionException {
        this(null, null, null);
    }

    /**
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread}
     *
     * @param host for {@link SensorListener#connection}
     * @param port for {@link SensorListener#connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public SensorListener(final String host, final Integer port) throws NetworkConnectionException {
        this(host, port, null, false);
    }

    /**
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread}
     *
     * @param host                  for {@link SensorListener#connection}
     * @param port                  for {@link SensorListener#connection}
     * @param ignoreConnectionError ignores any {@link NetworkConnectionException} and tries to auto reconnect
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public SensorListener(final String host, final Integer port, final boolean ignoreConnectionError) throws NetworkConnectionException {
        this(host, port, null, ignoreConnectionError);
    }

    /**
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread}
     *
     * @param host     for {@link SensorListener#connection}
     * @param port     for {@link SensorListener#connection}
     * @param password for {@link SensorListener#connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public SensorListener(final String host, final Integer port, final String password) throws NetworkConnectionException {
        this(host, port, password, false);
    }

    /**
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread}
     *
     * @param host                  for {@link SensorListener#connection}
     * @param port                  for {@link SensorListener#connection}
     * @param password              for {@link SensorListener#connection}
     * @param ignoreConnectionError ignores any {@link NetworkConnectionException} and tries to auto reconnect
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public SensorListener(final String host, final Integer port, final String password, final boolean ignoreConnectionError) throws NetworkConnectionException {
        this.host = host;
        this.password = password;
        this.port = port;
        this.ignoreConnectionError = ignoreConnectionError;
        connect();
    }

    /**
     * connects to given host - this method will be called from {@link SensorListener} constructor
     *
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public void connect() throws NetworkConnectionException {
        connection = new IPConnection();
        connection.setAutoReconnect(true);
        connection.setTimeout(timeoutMs);
        connection.addDisconnectedListener(event -> handleConnect(event, true));
        connection.addConnectedListener(event -> handleConnect(event, false));
        sensorList.clear();
        if (host != null) {
            final Object result = execute(timeoutMs, () -> {
                if (!isEmpty(password)) {
                    connection.authenticate(password);
                }
                connection.connect(host, port);
                connection.addEnumerateListener(this::doPlugAndPlay);
                connection.enumerate();
                return true;
            });
            if (!ignoreConnectionError && result instanceof Throwable) {
                throw new NetworkConnectionException((Throwable) result);
            }
        }
//        createLoop(connectionHandlerName, timeoutMs, run -> checkConnection());
        createLoop(pingConnectionHandlerName, 8, run -> sendEvent(sensorList.getDefault(), System.currentTimeMillis(), PING));
    }

    public boolean isConnecting() {
        return (lastConnect + (timeoutMs / 2)) > System.currentTimeMillis();
    }

    /**
     * disconnects all {@link Sensor} from the given host see {@link SensorListener#close()}
     */
    public synchronized void disconnect() {
        close();
    }

    /**
     * disconnects all {@link Sensor} from the given host and removes the sensors from {@link SensorListener#sensorList}
     */
    @Override
    public void close() {
        asyncStop(pingConnectionHandlerName, connectionHandlerName);
        execute(timeoutMs + 256, () -> {
            try {
                sensorList.clear();
                connection.disconnect();
            } catch (Exception ignored) {
            }
            return true;
        });

        execute(timeoutMs + 256, () -> {
            try {
                List<TinkerforgeThread> tinkerforgeThreads;
                do {
                    tinkerforgeThreads = Thread.getAllStackTraces().keySet().stream().filter(thread -> thread.getClass().getPackage().getName().startsWith(IPConnection.class.getPackage().getName())).map(thread -> (TinkerforgeThread) thread).collect(Collectors.toList());
                    tinkerforgeThreads.forEach(thread -> thread.setStop(true));
                    for (TinkerforgeThread tinkerforgeThread : tinkerforgeThreads) {
                        tinkerforgeThread.join();
                    }
                } while (!tinkerforgeThreads.isEmpty());
                sensorList.clear();
            } catch (Exception ignored) {
            }
            return true;
        });
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
