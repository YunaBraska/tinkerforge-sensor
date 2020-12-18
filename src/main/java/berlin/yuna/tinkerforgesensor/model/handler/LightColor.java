package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.threads.Color;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_C;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_LUX;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_RGB;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_TEMPERATURE;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightColor extends SensorHandler<BrickletColor> {

    private BrickletColor.Config ledConfig;

    public LightColor(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletColor> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletColor> init() {
        config.put(THRESHOLD_PREFIX + COLOR_RGB, 2);
        config.put(THRESHOLD_PREFIX + COLOR_C, 2);
        config.put(THRESHOLD_PREFIX + COLOR_LUX, 256);
        config.put(THRESHOLD_PREFIX + COLOR_TEMPERATURE, 128);
        device.addColorListener(this::sendEvent);
        device.addIlluminanceListener(value -> sendEvent(COLOR_LUX, value * 700 / ledConfig.gain / ledConfig.integrationTime));
        device.addColorTemperatureListener(value -> sendEvent(COLOR_TEMPERATURE, (long) value));
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickletColor> initConfig() {
        handleConnection(() -> {
            config.put(CONFIG_HIGH_CONTRAST, 1);
            config.put(CONFIG_INFO_LED_STATUS, device.isLightOn() == 0 ? 1 : 0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletColor> runTest() {
        int before = getConfig(CONFIG_INFO_LED_STATUS).intValue();
        handleConnection(() -> {
            for (int i = 0; i < 7; i++) {
                if (i % 2 == 0) {
                    triggerFunctionA(1);
                } else {
                    triggerFunctionA(0);
                }
                Thread.sleep(128);
            }
            triggerFunctionA(before);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletColor> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletColor> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, device::lightOn);
        } else if (value == LED_OFF.bit) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, device::lightOff);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletColor> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> {
            ledConfig = device.getConfig();
            device.setColorCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds);
            device.setIlluminanceCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds);
            device.setColorTemperatureCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds);
        });
        return this;
    }

    private void sendEvent(final int r, final int g, final int b, final long c) {
        Color color = new Color(r / 256, g / 256, b / 256);
        color = getConfig(CONFIG_HIGH_CONTRAST).intValue() == 1 ? Color.convertToHighContrast(color) : color;
        sendEvent(COLOR_RGB, color.getRGB());
        sendEvent(COLOR_C, c / 256);
    }

}
