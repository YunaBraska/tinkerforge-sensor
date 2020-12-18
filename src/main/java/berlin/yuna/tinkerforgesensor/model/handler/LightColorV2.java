package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.threads.Color;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletColorV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightColorV2 extends SensorHandler<BrickletColorV2> {

    private BrickletColorV2.Configuration ledConfig;

    public LightColorV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletColorV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletColorV2> init() {
        config.put(THRESHOLD_PREFIX + ValueType.COLOR_RGB, 2);
        config.put(THRESHOLD_PREFIX + ValueType.COLOR_C, 2);
        config.put(THRESHOLD_PREFIX + ValueType.COLOR_LUX, 256);
        config.put(THRESHOLD_PREFIX + ValueType.COLOR_TEMPERATURE, 128);
        device.addColorListener(this::sendEvent);
        device.addIlluminanceListener(value -> sendEvent(ValueType.COLOR_LUX, value * 700 / ledConfig.gain / ledConfig.integrationTime));
        device.addColorTemperatureListener(value -> sendEvent(ValueType.COLOR_TEMPERATURE, (long) value));
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickletColorV2> initConfig() {
        RunThrowable.handleConnection(() -> {
            config.put(CONFIG_HIGH_CONTRAST, 1);
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_INFO_LED_STATUS, device.getLight() ? 1 : 0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletColorV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletColorV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletColorV2> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, () -> device.setLight(true));
        } else if (value == LedStatusType.LED_OFF.bit) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, () -> device.setLight(false));
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletColorV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            ledConfig = device.getConfiguration();
            device.setColorCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true);
            device.setIlluminanceCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
            device.setColorTemperatureCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
        });
        return this;
    }

    private void sendEvent(final int r, final int g, final int b, final long c) {
        Color color = new Color(r / 256, g / 256, b / 256);
        color = getConfig(CONFIG_HIGH_CONTRAST).intValue() == 1 ? Color.convertToHighContrast(color) : color;
        sendEvent(ValueType.COLOR_RGB, color.getRGB());
        sendEvent(ValueType.COLOR_C, c / 256);
    }

}
