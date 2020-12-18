package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import berlin.yuna.tinkerforgesensor.exception.SensorInitialisationException;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.Device;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class SensorHandler<D> {

    protected final Sensor sensor;
    protected final D device;
    protected final Map<String, Number> config = new ConcurrentHashMap<>();
    public static final String CONFIG_VOLUME = "CONFIG_VOLUME";
    public static final String CONFIG_FREQUENCY = "CONFIG_FREQUENCY";
    public static final String CONFIG_HIGH_CONTRAST = "CONFIG_HIGH_CONTRAST";
    public static final String CONFIG_COLOR = "CONFIG_COLOR";
    public static final String CONFIG_POSITION = "CONFIG_POSITION";
    public static final String CONFIG_POSITION_HOLD = "CONFIG_POSITION_HOLD";
    public static final String CONFIG_BRIGHTNESS = "CONFIG_BRIGHTNESS";
    public static final String CONFIG_LED_STATUS = "CONFIG_LED_STATUS";
    public static final String CONFIG_INFO_LED_STATUS = "CONFIG_INFO_LED_STATUS";
    public static final String CONFIG_FUNCTION_A = "CONFIG_LED_SECOND";
    public static final String THRESHOLD_PREFIX = "THRESHOLD_";

    @SuppressWarnings("unchecked")
    protected SensorHandler(final Sensor sensor, final Device device) {
        this.sensor = sensor;
        this.device = (D) device;
    }

    public Number getConfig(final String key) {
        return config.get(key);
    }

    public Number setConfig(final String key, final Number value) {
        return config.put(key, value);
    }

    public abstract SensorHandler<D> init();

    public abstract SensorHandler<D> setRefreshPeriod(final int milliseconds);

    public abstract SensorHandler<D> initConfig();

    public abstract SensorHandler<D> runTest();

    public boolean hasStatusLed() {
        return config.containsKey(CONFIG_LED_STATUS);
    }

    public SensorHandler<D> setStatusLed(final int value) {
        return hasStatusLed() ? setStatusLedHandler(value) : this;
    }

    protected abstract SensorHandler<D> setStatusLedHandler(final int value);

    public abstract SensorHandler<D> triggerFunctionA(final int value);

    public abstract SensorHandler<D> send(final Object value);

    public int getBrightness() {
        return config.getOrDefault(CONFIG_BRIGHTNESS, 0).intValue();
    }

    public boolean hasBrightness() {
        return config.containsKey(CONFIG_BRIGHTNESS);
    }

    public SensorHandler<D> setBrightness(final int brightness) {
        return hasBrightness() ? triggerFunctionA(brightness) : this;
    }

    public boolean hasHighContrast() {
        return config.getOrDefault(CONFIG_HIGH_CONTRAST, -1).intValue() != -1;
    }

    public SensorHandler<D> setHighContrast(boolean highContrast) {
        if (hasHighContrast()) {
            config.put(CONFIG_HIGH_CONTRAST, highContrast ? 1 : 0);
        }
        return this;
    }

    public boolean hasFrequency() {
        return config.getOrDefault(CONFIG_FREQUENCY, -1).intValue() != -1;
    }

    public SensorHandler<D> setFrequency(int volume) {
        return hasFrequency() ? triggerFunctionA(volume) : this;
    }

    public int getFrequency() {
        return config.getOrDefault(CONFIG_FREQUENCY, 0).intValue();
    }

    public boolean hasVolume() {
        return config.getOrDefault(CONFIG_VOLUME, -1).intValue() != -1;
    }

    public SensorHandler<D> setVolume(int volume) {
        if (hasVolume()) {
            setConfig(CONFIG_VOLUME, volume);
        }
        return this;
    }

    public int getVolume() {
        return config.getOrDefault(CONFIG_VOLUME, 0).intValue();
    }

    public Sensor sensor() {
        return sensor;
    }

    public D device() {
        return device;
    }

    protected SensorHandler<D> animateStatuesLed() {
        if (sensor.hasStatusLed()) {
            handleConnection(() -> {
                final int before = getConfig(CONFIG_LED_STATUS).intValue();
                for (int i = 0; i < 7; i++) {
                    if (i % 2 == 0) {
                        sensor.setStatusLedOn();
                    } else {
                        sensor.setStatusLedOff();
                    }
                    Thread.sleep(128);
                }
                setStatusLed(before);
            });
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <D> SensorHandler<D> initHandler(final Sensor sensor, final Class<? extends SensorHandler<?>> handlerClass, final Device device) {
        try {
            return (SensorHandler<D>) handlerClass.getConstructors()[0].newInstance(sensor, device);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SensorInitialisationException("Unable to initialise handler [" + handlerClass.getSimpleName() + "] sensor [" + sensor + "]", e);
        }
    }

    protected SensorHandler<D> sendEvent(final ValueType type, final Number value) {
        applyOnNewThreshold(type.name(), value.longValue(), () -> sensor.getStack().sendEvent(new SensorEvent(sensor, value, type)));
        return this;
    }

    protected SensorHandler<D> applyOnNewThreshold(final String key, final long value, final RunThrowable function) {
        if (hasThresholdChange(key, value)) {
            function.run();
            config.put(key, value);
        }
        return this;
    }

    protected SensorHandler<D> applyOnNewValue(final String key, final long value, final RunThrowable function) {
        if (config.getOrDefault(key, -1).longValue() != value) {
            applyOnNewThreshold(key, value, function);
        }
        return this;
    }

    private boolean hasThresholdChange(final String key, final long value) {
        long threshold = config.getOrDefault(THRESHOLD_PREFIX + key, -1).longValue();
        return threshold < -1 || diff(key, value) >= threshold;
    }

    private long diff(final String key, final long value) {
        long diff = config.getOrDefault(key, 0).longValue();
        return diff > value ? diff - value : value - diff;
    }

}
