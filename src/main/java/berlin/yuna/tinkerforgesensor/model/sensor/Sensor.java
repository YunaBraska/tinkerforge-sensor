package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.builder.Compare;
import berlin.yuna.tinkerforgesensor.model.builder.Values;
import berlin.yuna.tinkerforgesensor.model.exception.DeviceNotSupportedException;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.IPConnection;
import com.tinkerforge.TinkerforgeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.getDevice;
import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.getSensor;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import static java.lang.Character.toLowerCase;
import static java.lang.String.format;
import static java.lang.System.nanoTime;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Generic wrapper for {@link Device} to have generic methods and behavior on all sensors
 *
 * @param <T> sensor type like a driver to tell the sensor how to handle the {@link Device} - important are methods like {@link Sensor<T>#initListener()}
 */
public abstract class Sensor<T extends Device> {

    protected int port = -1;
    protected Sensor parent;
    protected boolean isBrick;
    protected LedStatusType ledStatus;
    protected LedStatusType ledAdditional;

    public final String uid;
    public final String name;
    public final T device;

    //    private final boolean hasStatusLed;
    private final String connectionUid;
    private final char position;
    private final ConcurrentHashMap<ValueType, RollingList<Long>> valueMap = new ConcurrentHashMap<>();
    private long lastCall = nanoTime();

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
    protected Sensor(final T device, final String uid) throws NetworkConnectionException {
        this.uid = uid;
        this.name = device.getClass().getSimpleName();
        this.device = device;
        try {
            final Device.Identity identity = device.getIdentity();
            this.connectionUid = identity.connectedUid;
            this.position = identity.position;
            initPort();
            initLedConfig();
            initListener();
        } catch (TinkerforgeException e) {
            throw new NetworkConnectionException(e);
        }
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
     * @return {@link Compare} with predefined compare methods
     */
    public Compare compare() {
        return new Compare(this);
    }

    /**
     * For automation to know if its worth to call setLedStatus_Status functions. Value is taken from {@link Sensor#initLedConfig()}
     *
     * @return true if the Sensor {@link Device} has setLedStatus_Status
     */
    public boolean hasLedStatus() {
        return ledStatus != null && ledStatus != LED_NONE;
    }

    /**
     * For automation to know if its worth to call setLedAdditional functions. Value is taken from {@link Sensor#initLedConfig()}
     *
     * @return true if the Sensor {@link Device} has setLedAdditional
     */
    public boolean hasLedAdditional() {
        return ledAdditional != null && ledAdditional != LED_NONE;
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
     * Same method as {@link Sensor#valueMap} with limitation of 1 call per millisecond
     *
     * @param limitPerSec sets message limit per seconds (hast to be > 0 and < 1000000000) else default method {@link Sensor#send(Object...)} will be called
     * @param values      some objects like a "howdy", 123, Color.GREEN which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public synchronized Sensor<T> sendLimit(final long limitPerSec, final Object... values) {
        if (limitPerSec < 1 || limitPerSec > 1000000000) {
            send(values);
        } else if (lastCall + (1000000000 / limitPerSec) < nanoTime()) {
            lastCall = nanoTime();
            send(values);
        }
        return this;
    }

    /**
     * @param value some object like a "howdy" string for {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> send(final Object value);

    /**
     * Sets the refresh rate for the sensor values (e.g. for power issues)
     *
     * @param perSec hast to be in range of 0 to 1000 (0 = listen only on changes)
     *               <br /> Some old {@link com.tinkerforge.Device} does't have the option 0 and will fall back to period callback
     * @return current {@link Sensor<T>}
     */
    public synchronized Sensor<T> refreshLimit(final int perSec) {
        if (perSec > 0 && perSec <= 1000) {
            refreshPeriod(1000 / perSec);
        }
        return this;
    }

    /**
     * Sets the refresh period directly to the {@link com.tinkerforge.Device} - its safer to use the method {@link Sensor#refreshLimit(int)}
     *
     * @param milliseconds callBack period
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> refreshPeriod(final int milliseconds);

    /**
     * @param values some objects like a "howdy", "howdy2" string for {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> send(final Object... values) {
        for (Object value : values) {
            send(value);
        }
        return this;
    }

    public Values values() {
        return new Values(singletonList(this));
    }

    public ConcurrentHashMap<ValueType, RollingList<Long>> valueMap() {
        return valueMap;
    }

    /**
     * Loads the led configurations for setLedStatus_Status and setLedAdditional
     *
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> initLedConfig();

    /**
     * @return {@link LedStatusType} state or null if setLedStatus_Status is not present
     */
    public LedStatusType getLedStatus() {
        return ledStatus;
    }

    /**
     * @return {@link LedStatusType} state or null if setLedAdditional is not present
     */
    public LedStatusType getLedAdditional() {
        return ledAdditional;
    }

    /**
     * @param ledStatusType for status led like {@link LedStatusType#LED_STATUS_HEARTBEAT} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedStatus(final LedStatusType ledStatusType) {
        return setLedStatus(ledStatusType.bit);
    }

    /**
     * @param value for status led like {@link LedStatusType#LED_STATUS_HEARTBEAT} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> setLedStatus(final Integer value);

    /**
     * @param ledStatusType additional led like {@link LedStatusType#LED_ADDITIONAL_ON} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedAdditional(final LedStatusType ledStatusType) {
        return setLedAdditional(ledStatusType.bit);
    }

    /**
     * @param value additional led like {@link LedStatusType#LED_ADDITIONAL_ON} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor<T>}
     */
    public abstract Sensor<T> setLedAdditional(final Integer value);

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS} like the {@link com.tinkerforge.BrickletMotionDetectorV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedStatus_Status() {
        return setLedStatus(LED_STATUS);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_HEARTBEAT} like the {@link com.tinkerforge.BrickletRGBLEDButton} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedStatus_Heartbeat() {
        return setLedStatus(LED_STATUS_HEARTBEAT);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_ON} like the {@link com.tinkerforge.BrickMaster} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedStatus_On() {
        return setLedStatus(LED_STATUS_ON);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_OFF} like the {@link com.tinkerforge.BrickMaster} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedStatus_Off() {
        return setLedStatus(LED_STATUS_OFF);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_ON} like the display of {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedAdditional_On() {
        return setLedAdditional(LED_ADDITIONAL_ON);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_HEARTBEAT} like the flash led of {@link com.tinkerforge.BrickletColor} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedAdditional_Off() {
        return setLedAdditional(LED_ADDITIONAL_OFF);
    }

    /**
     * Additional led try to show {@link LedStatusType#LED_ADDITIONAL_HEARTBEAT} like the {@link com.tinkerforge.BrickletDualButtonV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedAdditional_Heartbeat() {
        return setLedAdditional(LED_ADDITIONAL_HEARTBEAT);
    }

    /**
     * Additional led try to show {@link LedStatusType#LED_ADDITIONAL_STATUS} like the {@link com.tinkerforge.BrickletDualButtonV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedAdditional_Status() {
        return setLedAdditional(LED_ADDITIONAL_STATUS);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_ON} like display of {@link com.tinkerforge.BrickletSegmentDisplay4x7} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor<T>}
     */
    public Sensor<T> setLedBrightness(final Integer value) {
        return setLedAdditional(value);
    }

    /**
     * Gets the send of {@link ValueType}
     *
     * @param valueType to get from {@link Sensor<T>#values}
     * @return send from sensor of type {@link ValueType} or 0L if not found
     */
    public Long send(final ValueType valueType) {
        return send(valueType, 0L);
    }

    /**
     * Gets the send of {@link ValueType}
     *
     * @param valueType to get from {@link Sensor<T>#values}
     * @param fallback  will return this if no send with {@link ValueType} were found
     * @return send from sensor of type {@link ValueType} or fallback if no send with {@link ValueType} were found
     */
    public Long send(final ValueType valueType, final Long fallback) {
        final RollingList<Long> valueList = valueMap.get(valueType);
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
     * Internal api to send {@link Sensor<T>Event} to the listeners and calculates {@link Sensor<T>#percentageOccur(ArrayList, Long)} from send if the event should be send
     *
     * @param valueType type of send to send
     * @param value     send to send
     * @return {@link Sensor<T>#port}
     */
    protected Sensor<T> sendEvent(final ValueType valueType, final Long value) {
        final RollingList<Long> timeSeries = valueMap.computeIfAbsent(valueType, item -> new RollingList<>(SENSOR_VALUE_LIMIT));
        if (timeSeries.addAndCheckIfItsNewPeak(value)) {
            consumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(this, value, valueType)));
        }
        return this;
    }

    protected abstract Sensor<T> initListener() throws TinkerforgeException;

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
                        this.setLedStatus_On();
                    } else {
                        this.setLedStatus_Off();
                    }
                    Thread.sleep(128);
                }
                this.setLedStatus_Status();
            }
        } catch (Exception ignore) {
        }
        return this;
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
     * Generic led function enum for all {@link Sensor} types, can be used also in {@link Sensor#setLedStatus(Integer)} or {@link Sensor#setLedAdditional(Integer)}
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

        public static LedStatusType ledStatusTypeOf(final int bit) {
            for (LedStatusType status : asList(LED_NONE, LED_STATUS_ON, LED_STATUS_OFF, LED_STATUS_HEARTBEAT, LED_STATUS)) {
                if (bit == status.bit) {
                    return status;
                }
            }
            return LED_NONE;
        }

        public static LedStatusType ledAdditionalTypeOf(final int bit) {
            for (LedStatusType status : asList(LED_NONE, LED_ADDITIONAL_ON, LED_ADDITIONAL_OFF, LED_ADDITIONAL_HEARTBEAT, LED_ADDITIONAL_STATUS)) {
                if (bit == status.bit) {
                    return status;
                }
            }
            return LED_NONE;
        }
    }
}
