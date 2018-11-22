package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.io.Closeable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.TimeoutExecutor.execute;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_DISCONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_RECONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_SEARCH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.createLoop;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.isEmpty;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.loops;
import static com.tinkerforge.DeviceFactory.createDevice;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

public class SensorListener implements Closeable {

    /**
     * IPConnection connection of the {@link Sensor} and for enumerating available {@link Sensor}
     */
    public IPConnection connection = new IPConnection();

    /**
     * SensorList holds all connected {@link Sensor} {@link SensorListener#checkConnection()} managing that list
     * This list is never empty as and contains {@link SensorListener#defaultSensor} as fallback
     */
    public final SensorList<Sensor> sensorList = new SensorList<>();

    /**
     * List of {@link Consumer} for getting all {@link SensorEvent} see {@link SensorRegistration#sendEvent(List, ValueType, Sensor, Long)}
     */
    public final List<Consumer<SensorEvent>> sensorEventConsumerList = new CopyOnWriteArrayList<>();

    private long deviceSearch = currentTimeMillis();

    private final String host;
    private final String password;
    private final Integer port;
    private final Sensor defaultSensor;
    private final int timeoutMs = 3000;
    private final boolean ignoreConnectionError;
    private final String ConnectionHandlerName = getClass().getSimpleName() + "_" + UUID.randomUUID();

    /**
     * Dummy Sensorlist, wont connect to any {@link Sensor} as the host is not set
     *
     * @throws NetworkConnectionException should never happen
     */
    public SensorListener() throws NetworkConnectionException {
        this(null, null, null);
    }

    /**
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread} with {@link SensorListener#checkConnection()}
     *
     * @param host for {@link SensorListener#connection}
     * @param port for {@link SensorListener#connection}
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public SensorListener(final String host, final Integer port) throws NetworkConnectionException {
        this(host, port, null, false);
    }

    /**
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread} with {@link SensorListener#checkConnection()}
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
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread} with {@link SensorListener#checkConnection()}
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
     * Auto connects and auto {@link Closeable} {@link SensorListener#close()} {@link Sensor}s and manages the {@link SensorListener#sensorList} by creating {@link Thread} with {@link SensorListener#checkConnection()}
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

        DummyDevice dummyDevice = new DummyDevice();
        this.defaultSensor = new Sensor(dummyDevice, null, dummyDevice.getIdentity().uid).addListener(sensorEventConsumerList);
        sensorList.add(defaultSensor);
        connect();
    }

    /**
     * connects to given host - this method will be called from {@link SensorListener} constructor
     *
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public synchronized void connect() throws NetworkConnectionException {
        connection = new IPConnection();
        connection.setAutoReconnect(true);
        connection.addEnumerateListener(this::doPlugAndPlay);
        connection.setTimeout(timeoutMs - 256);
        connection.addConnectedListener(event -> handleConnect(event, false));
        connection.addDisconnectedListener(event -> handleConnect(event, true));
        if (host != null) {
            Object result = execute(timeoutMs - 256, () -> {
                connection.connect(host, port);
                if (!isEmpty(password)) {
                    connection.authenticate(password);
                }
                connection.enumerate();
                return true;
            });
            if (!ignoreConnectionError && result instanceof Throwable) {
                throw new NetworkConnectionException((Throwable) result);
            }
        }
        createLoop(ConnectionHandlerName, timeoutMs, run -> checkConnection());
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
        execute(timeoutMs + 256, () -> {
            try {
                preventDeviceSearch();
                clearSensorList();
                connection.setAutoReconnect(false);
                connection.disconnect();
            } catch (Exception ignored) {
            } finally {
                preventDeviceSearch();
            }
            return true;
        });
    }

    private synchronized void clearSensorList() throws InterruptedException {
        try {
            sensorList.lock(timeoutMs);
            sensorList.clear();
            sensorList.add(defaultSensor);
        } finally {
            sensorList.unlock();
        }
    }

    private synchronized void checkConnection() {
        System.out.println(format("[INFO] RunningPrograms [%s] SensorSize [%s]", loops.size(), sensorList.size()));
        sensorList.waitForUnlock(timeoutMs);
        if (sensorList.size() == 1) {
            deviceSearch();
        } else if (deviceSearch + timeoutMs < currentTimeMillis()) {
            for (Sensor sensor : sensorList) {
                try {
                    sensor.refreshPortE();
                } catch (TimeoutException | NotConnectedException e) {
                    sendEvent(sensor, 404, DEVICE_TIMEOUT);
                    sensorList.remove(sensor);
                    deviceSearch();
                }
            }
        }
    }

    private void deviceSearch() {
        if (deviceSearch + timeoutMs < currentTimeMillis()) {
            try {
                preventDeviceSearch();
                sendEvent(defaultSensor, 1, DEVICE_SEARCH);
//                connection.enumerate();
                disconnect();
                preventDeviceSearch();
                connect();
            } catch (NetworkConnectionException e) {
                System.err.println(format("[ERROR] RECOVER [deviceSearch] [%s]", e.getClass().getSimpleName()));
            } finally {
                preventDeviceSearch();
            }
        }
    }

    private void preventDeviceSearch() {
        deviceSearch = currentTimeMillis();
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
        preventDeviceSearch();
        Sensor sensor = defaultSensor;
        try {
            //MetaFile (DeviceProvider is) needed for Java 1.6 ServiceLoader
            switch (enumerationType) {
                case ENUMERATION_TYPE_AVAILABLE:
                    sensor = new Sensor(createDevice(deviceIdentifier, uid, connection), findParent(connectedUid), uid);
                    initSensor(sensor, 0L, DEVICE_CONNECTED);
                    break;
                case ENUMERATION_TYPE_CONNECTED:
                    sensor = new Sensor(createDevice(deviceIdentifier, uid, connection), findParent(connectedUid), uid);
                    initSensor(sensor, 1L, DEVICE_RECONNECTED);
                    break;
                case ENUMERATION_TYPE_DISCONNECTED:
                    sensor = sensorList.stream().filter(entity -> entity.uid.equals(uid)).findFirst().orElse(null);
                    sendEvent(sensor, 2L, DEVICE_DISCONNECTED);
                    sensorList.remove(sensor);
                    break;
            }
        } catch (TimeoutException to) {
            if (sensor != null && !sensor.is(DummyDevice.class)) {
                initSensor(sensor, 208, DEVICE_RECONNECTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSensor(final Sensor sensor, final long value, final ValueType valueType) {
        if (sensorList.contains(sensor)) {
            return;
        }
        sensorList.add(sensor);
        sensor.addListener(sensorEventConsumerList);
        initLed(sensor);
        sendEvent(sensor, value, valueType);
    }

    private void initLed(Sensor sensor) {
        if (sensor.hasStatusLed) {
            for (int i = 0; i < 7; i++) {
                preventDeviceSearch();
                if (i % 2 == 0) {
                    sensor.ledStatusOn();
                } else {
                    sensor.ledStatusOff();
                }
                try {
                    Thread.sleep(128);
                } catch (InterruptedException ignore) {
                }
            }
            sensor.ledStatus();
        }
    }

    private Sensor findParent(final String connectedUid) {
        Sensor parent = null;
        for (Sensor sensor : sensorList) {
            if (sensor.uid.equals(connectedUid)) {
                parent = sensor;
            }
        }
        return parent;
    }

    private void sendEvent(final Sensor sensor, final long value, final ValueType valueType) {
        if (sensor != null) {
            sensorEventConsumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(sensor, value, valueType)));
        }
    }

    private synchronized void handleConnect(final short connectionEvent, boolean disconnectEvent) {
        if (disconnectEvent) {
            switch (connectionEvent) {
                case IPConnection.DISCONNECT_REASON_REQUEST:
                case IPConnection.DISCONNECT_REASON_ERROR:
                case IPConnection.DISCONNECT_REASON_SHUTDOWN:
                    //Clear list fast at USB interruption
                    disconnect();
                    sendEvent(defaultSensor, (long) connectionEvent, DEVICE_DISCONNECTED);
                    break;
            }
        } else {
            switch (connectionEvent) {
                case IPConnection.CONNECT_REASON_REQUEST:
                    sendEvent(defaultSensor, (long) connectionEvent, DEVICE_CONNECTED);
                    break;
                case IPConnection.CONNECT_REASON_AUTO_RECONNECT:
                    sendEvent(defaultSensor, (long) connectionEvent, DEVICE_RECONNECTED);
                    break;
            }
        }
    }
}
