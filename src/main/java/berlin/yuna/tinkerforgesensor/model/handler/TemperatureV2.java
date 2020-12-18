package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.BrickletTemperatureV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.ValueType.TEMPERATURE;

/**
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TemperatureV2 extends SensorHandler<BrickletTemperatureV2> {

    public TemperatureV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> init() {
        config.put(THRESHOLD_PREFIX + TEMPERATURE, 8);
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, value));
        return setRefreshPeriod(16);
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> initConfig() {
        RunThrowable.handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperatureV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> device.setTemperatureCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0));
        return this;
    }

}
