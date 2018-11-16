package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.CryptoException;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_DISCONNECTED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_RECONNECTED;
import static com.tinkerforge.DeviceFactory.createDevice;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;

public class SensorListener implements Closeable {

    /**
     * IPConnection
     */
    public final IPConnection connection = new IPConnection();
    public final SensorList<Sensor> sensorList = new SensorList<>();
    public final List<Consumer<SensorEvent>> sensorEventConsumerList = new CopyOnWriteArrayList<>();

    public SensorListener(final String host, final Integer port) throws NetworkException, AlreadyConnectedException, NotConnectedException, TimeoutException, CryptoException {
        this(host, port, null);
    }

    public SensorListener(final String host, final Integer port, final String password) throws NetworkException, AlreadyConnectedException, NotConnectedException, TimeoutException, CryptoException {
        sensorList.add(new Sensor(new DummyDevice(), null).addListener(sensorEventConsumerList));
        connection.setAutoReconnect(true);
        connection.addEnumerateListener(this::doPlugAndPlay);
//        connection.addConnectedListener(event -> handleConnect(event, false));
//        connection.addDisconnectedListener(event -> handleConnect(event, true));
        connection.setTimeout(2500);
        connection.connect(host, port);
        if (password != null && !password.trim().equals("")) {
            connection.authenticate(password);
        }
        connection.enumerate();
    }

    private void handleConnect(final short connectionEvent, boolean disconnectEvent) {
        try {
            if (disconnectEvent) {
                switch (connectionEvent) {
                    case IPConnection.DISCONNECT_REASON_REQUEST:
                    case IPConnection.DISCONNECT_REASON_ERROR:
                    case IPConnection.DISCONNECT_REASON_SHUTDOWN:
                        sensorList.clear();
                        sendEvent(null, (long) connectionEvent, DEVICE_DISCONNECTED);
                        break;
                }
            } else {
                switch (connectionEvent) {
                    case IPConnection.CONNECT_REASON_REQUEST:
                        connection.enumerate();
                        sendEvent(null, (long) connectionEvent, DEVICE_CONNECTED);
                        break;

                    case IPConnection.CONNECT_REASON_AUTO_RECONNECT:
                        sendEvent(null, (long) connectionEvent, DEVICE_RECONNECTED);
                        connection.enumerate();
                        break;
                }
            }
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        }
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
        try {
            //MetaFile (DeviceProvider is) needed for Java 1.6 ServiceLoader
            Sensor sensor;
            long listenerPeriod = 100;
            switch (enumerationType) {
                case ENUMERATION_TYPE_AVAILABLE:
                    sensor = new Sensor(createDevice(deviceIdentifier, uid, connection), findParent(connectedUid)).addListener(sensorEventConsumerList);
                    sensorList.add(sensor);
                    sendEvent(sensor, 0L, DEVICE_CONNECTED);
                    break;
                case ENUMERATION_TYPE_CONNECTED:
                    sensor = new Sensor(createDevice(deviceIdentifier, uid, connection), findParent(connectedUid)).addListener(sensorEventConsumerList);
                    sensorList.add(sensor);
                    sendEvent(sensor, 1L, DEVICE_RECONNECTED);
                    break;
                case ENUMERATION_TYPE_DISCONNECTED:
                    sensor = sensorList.stream().filter(entity -> entity.uid.equals(uid)).findFirst().orElse(null);
                    sensorList.remove(sensor);
                    sendEvent(sensor, 2L, DEVICE_DISCONNECTED);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Sensor findParent(String connectedUid) throws TimeoutException, NotConnectedException {
        Sensor parent = null;
        for (Sensor sensor : sensorList) {
            if (sensor.device.getIdentity().uid.equals(connectedUid)) {
                parent = sensor;
            }
        }
        return parent;
    }

    private void sendEvent(Sensor sensor, long l, ValueType deviceConnected) {
        sensorEventConsumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(sensor, l, deviceConnected)));
    }

    @Override
    public void close() {
        try {
            connection.disconnect();
        } catch (NotConnectedException ignored) {
        }
    }
}
