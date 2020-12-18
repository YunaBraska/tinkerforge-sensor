package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletHumidityV2;
import com.tinkerforge.Device;

/**
 * <li>{@link ValueType#HUMIDITY} [x / 100.0 = %RH]</li>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class HumidityV2 extends SensorHandler<BrickletHumidityV2> {

    public HumidityV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletHumidityV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidityV2> init() {
        config.put(THRESHOLD_PREFIX + ValueType.HUMIDITY, 24);
        config.put(THRESHOLD_PREFIX + ValueType.TEMPERATURE, 8);
        device.addHumidityListener(value -> sendEvent(ValueType.HUMIDITY, value));
        device.addTemperatureListener(value -> sendEvent(ValueType.TEMPERATURE, value));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletHumidityV2> initConfig() {
        RunThrowable.handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidityV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidityV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidityV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidityV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            device.setTemperatureCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
            device.setHumidityCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
        });
        return this;
    }

}
