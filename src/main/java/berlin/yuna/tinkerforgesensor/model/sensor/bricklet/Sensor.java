package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.exception.DeviceNotSupportedException;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.SensorTypeHelper;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.getDevice;
import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.getSensor;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import static java.lang.Character.toLowerCase;
import static java.lang.String.format;

//TODO: matthias@tinkerforge.com contact at first beta

/**
 * Generic wrapper for {@link Device} to have generic methods and behavior on all sensors
 *
 * @param <T> sensor type like a driver to tell the sensor how to handle the {@link Device} - important are methods like {@link Sensor<T>#initListener()}
 */
public abstract class Sensor<T extends Device> {

    int port = -1;
    private Sensor parent;

    public final String uid;
    public final String name;
    public final T device;
    public final ConcurrentHashMap<ValueType, RollingList<Long>> values = new ConcurrentHashMap<>();
    public final SensorTypeHelper sensorTypeHelper;

    private final boolean hasStatusLed;
    private final String connectionUid;
    private final char position;

    protected boolean isBrick;
    protected static ConcurrentHashMap<Sensor, AtomicLong> lastRuns = new ConcurrentHashMap<>();

    /**
     * List of {@link Consumer} for getting all {@link Sensor<T>Event}
     */
    public final CopyOnWriteArrayList<Consumer<SensorEvent>> consumerList = new CopyOnWriteArrayList<>();

    //TODO: move to config or sensorRegistry
    protected final int SENSOR_VALUE_LIMIT = 99;

    /**
     * @param consumer to notify {@link Consumer} with {@link Sensor<T>Event} at {@link Sensor<T>#sendEvent(ValueType, Long)}
     * @return {@link Sensor<T>}
     */
    public Sensor<T> addListener(final Consumer<SensorEvent> consumer) {
        consumerList.add(consumer);
        return this;
    }

    /**
     * Creates new {@link Sensor}
     *
     * @param device {@link Device} to wrap with {@link Sensor}
     * @param uid    for unique identifier
     * @return {@link Sensor}
     */
    public static Sensor<? extends Device> newInstance(final Device device, final String uid) throws NetworkConnectionException {
        return getSensor(device.getClass()).newInstance(device, uid);

    }

    /**
     * Creates new {@link Sensor}
     *
     * @param deviceIdentifier {@link Device} identifier
     * @param uid              for unique identifier like {@link com.tinkerforge.BrickletBarometerV2#DEVICE_IDENTIFIER}
     * @param connection       {@link IPConnection} for {@link Device}
     * @return {@link Sensor}
     */
    public static Sensor<? extends Device> newInstance(final Integer deviceIdentifier, final String uid, final IPConnection connection) throws DeviceNotSupportedException, NetworkConnectionException {
        final DeviceFactory deviceFactory = getDevice(deviceIdentifier);
        if (deviceFactory == null) {
            throw new DeviceNotSupportedException(format("Device [%s] uid [%s] is not supported yet", deviceIdentifier, uid));
        }
        return newInstance(deviceFactory.newInstance(uid, connection), uid);
    }

    /**
     * Constructor
     *
     * @param device connected original {@link Device}
     * @param uid    cached uid of {@link Device#getIdentity()}
     */
    public Sensor(final T device, final String uid) throws NetworkConnectionException {
        this(device, uid, false);
    }

    /**
     * Constructor
     *
     * @param device       connected original {@link Device}
     * @param uid          cached uid of {@link Device#getIdentity()}
     * @param hasStatusLed for automation to know if its worth to call status led function e.g. {@link Sensor<T>#ledStatusOn()}, {@link Sensor<T>#ledStatusOff()}, {@link Sensor<T>#ledStatusHeartbeat()}, {@link Sensor<T>#ledStatus()}
     */
    protected Sensor(final T device, final String uid, final boolean hasStatusLed) throws NetworkConnectionException {
        this.uid = uid;
        this.name = device.getClass().getSimpleName();
        this.device = device;
        this.hasStatusLed = hasStatusLed;
        try {
            final Device.Identity identity = device.getIdentity();
            this.connectionUid = identity.connectedUid;
            this.position = identity.position;
            initPort();
            initListener();
        } catch (TimeoutException | NotConnectedException e) {
            throw new NetworkConnectionException(e);
        }
        sensorTypeHelper = new SensorTypeHelper(this);
    }

    /**
     * Tells if the sensor is real/present. The sensors are fake sensors as long as the connection is not established
     *
     * @return true if the sensor is present
     */
    public boolean isPresent() {
        return !(device instanceof DummyDevice);
    }

    /**
     * This method should not be called to often as this slows down the sensors
     * Checks if the sensor is {@link Sensor#isPresent()} and checks the connection by {@link Sensor#port}
     *
     * @return true if the sensor is present and the port refresh was successfully as the refresh needs that the {@link Device} is answering
     */
    public boolean isConnected() {
        return isPresent() && port != -1;
    }

    /**
     * Compares the sensor with {@link Sensor<T>} or {@link Device}
     *
     * @return true if the is a type of {@link Sensor<T>} or {@link Device}
     */
    public boolean isClassType(final Class<?>... sensorOrDevices) {
        for (Class<?> sensorOrDevice : sensorOrDevices) {
            if (getClass() == sensorOrDevice || getType() == sensorOrDevice) {
                return true;
            }
        }
        return false;
    }

    public boolean is(final Sensor sensor) {
        return sensor != null && sensor.uid.equals(uid);
    }

    public SensorTypeHelper type() {
        return sensorTypeHelper;
    }

    /**
     * For automation to know if its worth to call status led function
     *
     * @return true if the Sensor {@link Device} has a status led
     */
    public boolean hasLedStatus() {
        return hasStatusLed;
    }

    /**
     * Tells if the Sensor is a brick or bricklet calculated from {@link Sensor<T>#refreshPortE()}
     *
     * @return true if sensor is a brick
     */
    public boolean isBrick() {
        return isBrick;
    }

    /**
     * @param value some object like a "howdy" string for {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> value(final Object value);

    /**
     * @param values some objects like a "howdy", "howdy2" string for {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> value(final Object... values) {
        for (Object value : values) {
            value(value);
        }
        return this;
    }

    /**
     * @param value some value for status led like {@link LedStatusType#LED_STATUS_HEARTBEAT} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> ledStatus(final Integer value);

    /**
     * @param value some value additional led like {@link LedStatusType#LED_ADDITIONAL_ON} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> ledAdditional(final Integer value);

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS} like the {@link com.tinkerforge.BrickletMotionDetectorV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledStatus() {
        return ledStatus(LED_STATUS.bit);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_HEARTBEAT} like the {@link com.tinkerforge.BrickletRGBLEDButton} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledStatusHeartbeat() {
        return ledStatus(LED_STATUS_HEARTBEAT.bit);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_ON} like the {@link com.tinkerforge.BrickMaster} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledStatusOn() {
        return ledStatus(LED_STATUS_ON.bit);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_OFF} like the {@link com.tinkerforge.BrickMaster} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledStatusOff() {
        return ledStatus(LED_STATUS_OFF.bit);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_ON} like the display of {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledAdditionalOn() {
        return ledAdditional(LED_ADDITIONAL_ON.bit);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_HEARTBEAT} like the flash led of {@link com.tinkerforge.BrickletColor} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledAdditionalOff() {
        return ledAdditional(LED_ADDITIONAL_OFF.bit);
    }

    /**
     * Additional led try to show {@link LedStatusType#LED_ADDITIONAL_HEARTBEAT} like the {@link com.tinkerforge.BrickletDualButtonV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledAdditionalHeartbeat() {
        return ledAdditional(LED_ADDITIONAL_HEARTBEAT.bit);
    }

    /**
     * Additional led try to show {@link LedStatusType#LED_ADDITIONAL_STATUS} like the {@link com.tinkerforge.BrickletDualButtonV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledAdditionalStatus() {
        return ledAdditional(LED_ADDITIONAL_STATUS.bit);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_ON} like display of {@link com.tinkerforge.BrickletSegmentDisplay4x7} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> ledBrightness(final Integer value) {
        return ledAdditional(value);
    }

    /**
     * Gets the value of {@link ValueType}
     *
     * @param valueType to get from {@link Sensor<T>#values}
     * @return value from sensor of type {@link ValueType} or 0L if not found
     */
    public Long value(final ValueType valueType) {
        return value(valueType, 0L);
    }

    /**
     * Gets the value of {@link ValueType}
     *
     * @param valueType to get from {@link Sensor<T>#values}
     * @param fallback  will return this if no value with {@link ValueType} were found
     * @return value from sensor of type {@link ValueType} or fallback if no value with {@link ValueType} were found
     */
    public Long value(final ValueType valueType, final Long fallback) {
        final RollingList<Long> valueList = values.get(valueType);
        return valueList == null || valueList.isEmpty() || valueList.getLast() == null ? fallback : valueList.getLast();
    }

    /**
     * Connected port first number represents the brick and the second the connected port
     *
     * @return port starts at 0
     */
    public int port() {
        return port;
    }

    /**
     * Refreshes/Searches the {@link Sensor<T>#port} by calculating from parent {@link Sensor<T>#refreshPortE()}
     * Should not be called to often to not interrupt the sensors
     */
    private void initPort() {
        if ("0".equals(connectionUid)) {
            isBrick = true;
            port = 10;
        } else if (isDigit(position)) {
            isBrick = true;
            port = (getNumericValue(position) + 1) * 10;
        } else {
            isBrick = false;
            port = (((int) toLowerCase(position)) - 96) + (parent == null ? 0 : parent.port);
        }
    }


    /**
     * Relink parent and sets port if given Sensor were the parent
     *
     * @param sensorList of possible parents
     */
    public void linkParents(final List<Sensor> sensorList) {
        for (Sensor sensorCopy : new ArrayList<>(sensorList)) {
            linkParent(sensorCopy);
        }
    }

    /**
     * Relink parent and sets port if given Sensor were the parent
     *
     * @param sensor to link as parent
     */
    public void linkParent(final Sensor sensor) {
        if (sensor.uid.equals(connectionUid)) {
            parent = sensor;
            initPort();
        }
    }

    /**
     * Internal api to send {@link Sensor<T>Event} to the listeners and calculates {@link Sensor<T>#percentageOccur(ArrayList, Long)} from value if the event should be send
     *
     * @param valueType type of value to send
     * @param value     value to send
     * @return {@link Sensor<T>#port}
     */
    protected Sensor<T> sendEvent(final ValueType valueType, final Long value) {
        final RollingList<Long> timeSeries = values.computeIfAbsent(valueType, item -> new RollingList<>(SENSOR_VALUE_LIMIT));
        if (timeSeries.addAndCheckIfItsNewPeak(value)) {
            consumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(this, value, valueType)));
        }
        return this;
    }

    protected abstract Sensor<T> initListener() throws TimeoutException, NotConnectedException;

    /**
     * Compares the sensor with {@link Device}
     *
     * @return true if the is connected with class type of {@link Device}
     */
    protected boolean isDevice(final Class<? extends Device> device) {
        return this.device.getClass() == device;
    }

    /**
     * Compares the sensor with {@link Sensor<T>}
     *
     * @return true if the is a type of {@link Sensor<T>}
     */
    protected boolean isSensor(final Class<? extends Sensor> sensor) {
        return this.getClass() == sensor;
    }

    /**
     * @return class type of {@link Device}
     */
    public Class<? extends Device> getType() {
        return device.getClass();
    }


    /**
     * Flashing status led
     *
     * @return {@link Sensor<T>}
     */
    public Sensor<T> flashLed() {
        try {
            if (this.hasLedStatus()) {
                for (int i = 0; i < 7; i++) {
                    if (i % 2 == 0) {
                        this.ledStatusOn();
                    } else {
                        this.ledStatusOff();
                    }
                    Thread.sleep(128);
                }
                this.ledStatus();
            }
        } catch (Exception ignore) {
        }
        return this;
    }

    /**
     * TODO: FIXME: is this still needed?
     * BetaMethod to slow down communication with the bricks and not overload them
     *
     * @param sensor used to identify which {@link Sensor} is waiting the threshold is taken from {@link SensorRegistry#CALLBACK_PERIOD}
     */
    protected void waitToNextRun(final Sensor sensor) {
        final AtomicLong lastRun = lastRuns.computeIfAbsent(sensor, value -> new AtomicLong(System.currentTimeMillis() + (CALLBACK_PERIOD * 2)));
        while ((lastRun.get() + CALLBACK_PERIOD) > System.currentTimeMillis()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        lastRun.set(System.currentTimeMillis());
        lastRuns.put(sensor, lastRun);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sensor sensor = (Sensor) o;
        return Objects.equals(uid, sensor.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "port='" + port + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * This is needed for the {@link SensorRegistry} to instantiate {@link Sensor#newInstance(Integer, String, IPConnection)} new {@link Sensor}
     */
    @FunctionalInterface
    public interface SensorFactory {
        Sensor<? extends Device> newInstance(final Device device, final String uid) throws NetworkConnectionException;
    }

    @FunctionalInterface
    public interface DeviceFactory {
        Device newInstance(final String uid, final IPConnection connection);
    }

    /**
     * Generic led function enum for all {@link Sensor} types, can be used also in {@link Sensor#ledStatus(Integer)} or {@link Sensor#ledAdditional(Integer)}
     */
    public enum LedStatusType {
        LED_NONE(-1),
        LED_STATUS_OFF(0),
        LED_STATUS_ON(1),
        LED_STATUS_HEARTBEAT(2),
        LED_STATUS(3),

        LED_ADDITIONAL_OFF(0),
        LED_ADDITIONAL_ON(1),
        LED_ADDITIONAL_HEARTBEAT(2),
        LED_ADDITIONAL_STATUS(3),

        LED_CUSTOM(0);

        public final int bit;

        LedStatusType(final int bit) {
            this.bit = bit;
        }
    }
}
