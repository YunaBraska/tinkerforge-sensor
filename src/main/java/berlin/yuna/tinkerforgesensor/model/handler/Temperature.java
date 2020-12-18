package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.TEMPERATURE;

/**
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Temperature extends SensorHandler<BrickletTemperature> {

    public Temperature(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletTemperature> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperature> init() {
        config.put(THRESHOLD_PREFIX + TEMPERATURE, 8);
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, value));
        return setRefreshPeriod(1000);
    }

    @Override
    public SensorHandler<BrickletTemperature> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperature> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperature> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperature> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTemperature> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setTemperatureCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds));
        return this;
    }

}
