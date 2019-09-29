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
import com.tinkerforge.IPConnection;
import com.tinkerforge.TinkerforgeException;

import java.util.ArrayList;
import java.util.HashMap;
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
 * @param <T> sensor type like a driver to tell the sensor how to handle the {@link Device} - important are methods like {@link Sensor#initListener()}
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

    private final String connectionUid;
    private final char position;

    //TODO: HashMap only when using lock?
    //TODO: Number Array instead of list?
    private final ConcurrentHashMap<ValueType, RollingList<List<Number>>> valueMap = new ConcurrentHashMap<>();
    private long lastCall = nanoTime();

    /**
     * List of {@link Consumer} for getting all {@link SensorEvent}
     */
    public final CopyOnWriteArrayList<Consumer<SensorEvent>> consumerList = new CopyOnWriteArrayList<>();

    //TODO: move to config or sensorRegistry
    protected final int SENSOR_VALUE_LIMIT = 99;

    /**
     * @param consumer to notify {@link Consumer} with {@link SensorEvent} at {@link Sensor#sendEventUnchecked(SensorEvent)}
     * @return {@link Sensor}
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
        return !(this instanceof Default);
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
     * For automation to know if its worth to call ledStatus_setStatus functions. Value is taken from {@link Sensor#initLedConfig()}
     *
     * @return true if the Sensor {@link Device} has ledStatus_setStatus
     */
    public boolean hasLedStatus() {
        return ledStatus != null && ledStatus != LED_NONE;
    }

    /**
     * For automation to know if its worth to call ledAdditional functions. Value is taken from {@link Sensor#initLedConfig()}
     *
     * @return true if the Sensor {@link Device} has ledAdditional
     */
    public boolean hasLedAdditional() {
        return ledAdditional != null && ledAdditional != LED_NONE;
    }

    /**
     * Tells if the Sensor is a brick or bricklet calculated from {@link Sensor#initPort}
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
     * @return current {@link Sensor}
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
     * @return current {@link Sensor}
     */
    public abstract Sensor<T> send(final Object value);

    /**
     * Sets the refresh rate for the sensor values (e.g. for power issues)
     *
     * @param perSec hast to be in range of 0 to 1000 (0 = listen only on changes)
     *               <br> Some old {@link Device} does't have the option 0 and will fall back to period callback
     * @return current {@link Sensor}
     */
    public synchronized Sensor<T> refreshLimit(final int perSec) {
        if (perSec > 0 && perSec <= 1000) {
            refreshPeriod(1000 / perSec);
        }
        return this;
    }

    /**
     * Sets the refresh period directly to the {@link Device} - its safer to use the method {@link Sensor#refreshLimit(int)}
     *
     * @param milliseconds callBack period
     * @return current {@link Sensor}
     */
    public abstract Sensor<T> refreshPeriod(final int milliseconds);

    /**
     * @param values some objects like a "howdy", "howdy2" string for {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor}
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

    //TODO: move to values builder
    @Deprecated
    public Number getValue(final ValueType valueType, final int timeIndex, final int valueIndex, final Number fallback) {
        final Number value = getValue(valueType, timeIndex, valueIndex);
        return value == null ? fallback : value;
    }

    @Deprecated
    public Number getValue(final ValueType valueType, final int timeIndex, final Number fallback) {
        final Number value = getValue(valueType, timeIndex);
        return value == null ? fallback : value;
    }

    @Deprecated
    public Number getValue(final ValueType valueType, final Number fallback) {
        final Number value = getValue(valueType);
        return value == null ? fallback : value;
    }

    @Deprecated
    public Number getValue(final ValueType valueType) {
        return getValue(valueType, -1, -1);
    }

    @Deprecated
    public Number getValue(final ValueType valueType, final int timeIndex) {
        return getValue(valueType, timeIndex, -1);
    }

    public Number getValue(final ValueType valueType, final int timeIndex, final int valueIndex) {
        final List<Number> numbers = getValueList(valueType, timeIndex);
        if (valueIndex > -1) {
            return numbers.size() > valueIndex ? numbers.get(valueIndex) : null;
        }
        return numbers.isEmpty() ? null : numbers.get(0);
    }

    public List<Number> getValueList(final ValueType valueType) {
        return getValueList(valueType, -1);
    }

    public List<Number> getValueList(final ValueType valueType, final int timeIndex) {
        final RollingList<List<Number>> timeValueList = getTimeValueList(valueType);
        if (timeIndex > -1) {
            //TODO: cloneList needed?
            return timeValueList.size() > timeIndex ? timeValueList.cloneList().get(timeIndex) : new ArrayList<>();
        }
        return timeValueList.isEmpty() ? new ArrayList<>() : timeValueList.getLast();
    }

    public List<Number> getValueListVertical(final ValueType valueType) {
        return getValueListVertical(valueType, -1);
    }

    public List<Number> getValueListVertical(final ValueType valueType, final int valueIndex) {
        final List<Number> result = new ArrayList<>();
        if (valueMap.containsKey(valueType)) {
            for (List<Number> values : valueMap.get(valueType)) {
                if (valueIndex > -1 & values.size() > valueIndex) {
                    result.add(values.get(0));
                } else if (valueIndex < 0) {
                    result.add(values.get(0));
                }
            }
        }
        return result;
    }

    public RollingList<List<Number>> getTimeValueList(final ValueType valueType) {
        return valueMap.containsKey(valueType) ? new RollingList<>(valueMap.get(valueType)) : new RollingList<>(SENSOR_VALUE_LIMIT);
    }

    public HashMap<ValueType, Long> valueMap() {
        return new HashMap<>(0);
    }

    //Number                                getValue(Sensor, type, int timeIndex, int valueIndex)
    //List<Number>                          getValueList(Sensor, type, int timeIndex)
    //RollingList<List<Number>>             getValueTimeList(Sensor, type)
    //Map<Type RollingList<List<Number>>>   getValueTimeListForType(sensor);
    //getValueTimeListForTypeAndSensor(sensor);

    /**
     * Loads the led configurations for ledStatus_setStatus and ledAdditional
     *
     * @return current {@link Sensor}
     */
    public abstract Sensor<T> initLedConfig();

    /**
     * @return {@link LedStatusType} state or null if ledStatus_setStatus is not present
     */
    public LedStatusType getLedStatus() {
        return ledStatus;
    }

    /**
     * @return {@link LedStatusType} state or null if ledAdditional is not present
     */
    public LedStatusType getLedAdditional() {
        return ledAdditional;
    }

    /**
     * @param ledStatusType for status led like {@link LedStatusType#LED_STATUS_HEARTBEAT} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor}
     */
    public Sensor<T> setLedStatus(final LedStatusType ledStatusType) {
        return setLedStatus(ledStatusType.bit);
    }

    /**
     * @param value for status led like {@link LedStatusType#LED_STATUS_HEARTBEAT} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor}
     */
    public abstract Sensor<T> setLedStatus(final Integer value);

    /**
     * @param ledStatusType additional led like {@link LedStatusType#LED_ADDITIONAL_ON} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor}
     */
    public Sensor<T> ledAdditional(final LedStatusType ledStatusType) {
        return ledAdditional(ledStatusType.bit);
    }

    /**
     * @param value additional led like {@link LedStatusType#LED_ADDITIONAL_ON} which the sensor could process - else it just should ignore it
     * @return current {@link Sensor}
     */
    public abstract Sensor<T> ledAdditional(final Integer value);

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS} like the {@link com.tinkerforge.BrickletMotionDetectorV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> ledStatus_setStatus() {
        return setLedStatus(LED_STATUS);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_HEARTBEAT} like the {@link com.tinkerforge.BrickletRGBLEDButton} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> ledStatus_setHeartbeat() {
        return setLedStatus(LED_STATUS_HEARTBEAT);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_ON} like the {@link com.tinkerforge.BrickMaster} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> ledStatus_setOn() {
        return setLedStatus(LED_STATUS_ON);
    }

    /**
     * Status led try to show {@link LedStatusType#LED_STATUS_OFF} like the {@link com.tinkerforge.BrickMaster} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> ledStatus_setOff() {
        return setLedStatus(LED_STATUS_OFF);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_ON} like the display of {@link com.tinkerforge.BrickletLCD20x4} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> ledAdditional_setOn() {
        return ledAdditional(LED_ADDITIONAL_ON);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_HEARTBEAT} like the flash led of {@link com.tinkerforge.BrickletColor} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> ledAdditional_setOff() {
        return ledAdditional(LED_ADDITIONAL_OFF);
    }

    /**
     * Additional led try to show {@link LedStatusType#LED_ADDITIONAL_HEARTBEAT} like the {@link com.tinkerforge.BrickletDualButtonV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> setLedAdditional_Heartbeat() {
        return ledAdditional(LED_ADDITIONAL_HEARTBEAT);
    }

    /**
     * Additional led try to show {@link LedStatusType#LED_ADDITIONAL_STATUS} like the {@link com.tinkerforge.BrickletDualButtonV2} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> setLedAdditional_Status() {
        return ledAdditional(LED_ADDITIONAL_STATUS);
    }

    /**
     * led try to show {@link LedStatusType#LED_ADDITIONAL_ON} like display of {@link com.tinkerforge.BrickletSegmentDisplay4x7} which the sensor could process - else it just should ignore it
     *
     * @return current {@link Sensor}
     */
    public Sensor<T> setLedBrightness(final Integer value) {
        return ledAdditional(value);
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
     * Refreshes/Searches the {@link Sensor#port} by calculating from parent {@link Sensor#initPort()}
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
     * Internal api to send {@link SensorEvent} to the listeners and calculates peaks
     *
     * @param valueType type of send to send
     * @param values    send to send
     * @param unchecked send without any checks
     * @return {@link Sensor#port}
     */
    protected Sensor<T> sendEvent(final ValueType valueType, final List<Number> values, final boolean unchecked) {
        final RollingList<List<Number>> timeSeries = valueMap.computeIfAbsent(valueType, item -> new RollingList<>(SENSOR_VALUE_LIMIT));
        if ((unchecked && timeSeries.add(values)) || timeSeries.addAndCheckIfItsNewPeak(values)) {
            consumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(this, values, valueType)));
        }
        return this;
    }

    /**
     * Internal api to send {@link SensorEvent} to the listeners and calculates peaks
     *
     * @param valueType type of send to send
     * @param value     to send
     * @return {@link Sensor#port}
     */
    protected Sensor<T> sendEvent(final ValueType valueType, final Number value) {
        return sendEvent(valueType, singletonList(value), false);
    }

    /**
     * Internal api to send {@link SensorEvent} to the listeners and calculates peaks
     *
     * @param valueType type of send to send
     * @param value     to send
     * @param unchecked send without any checks
     * @return {@link Sensor#port}
     */
    protected Sensor<T> sendEvent(final ValueType valueType, final Number value, final boolean unchecked) {
        return sendEvent(valueType, singletonList(value), unchecked);
    }

    protected Sensor<T> sendEventUnchecked(final SensorEvent sensorEvent) {
        return sendEvent(sensorEvent.getValueType(), sensorEvent.getValues(), true);
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
     * Compares the sensor with {@link Sensor}
     *
     * @return true if the is a type of {@link Sensor}
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
     * @return {@link Sensor}
     */
    public Sensor<T> flashLed() {
        try {
            if (this.hasLedStatus()) {
                for (int i = 0; i < 7; i++) {
                    if (i % 2 == 0) {
                        this.ledStatus_setOn();
                    } else {
                        this.ledStatus_setOff();
                    }
                    Thread.sleep(128);
                }
                this.ledStatus_setStatus();
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
     * Generic led function enum for all {@link Sensor} types, can be used also in {@link Sensor#setLedStatus(Integer)} or {@link Sensor#ledAdditional(Integer)}
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
