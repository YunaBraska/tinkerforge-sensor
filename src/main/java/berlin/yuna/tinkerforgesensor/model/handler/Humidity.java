package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.Device;

/**
 * <li>{@link ValueType#HUMIDITY} [x / 100.0 = %RH]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Humidity extends SensorHandler<BrickletHumidity> {

    public Humidity(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletHumidity> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidity> init() {
        config.put(THRESHOLD_PREFIX + ValueType.HUMIDITY, 24);
        device.addHumidityListener(value -> sendEvent(ValueType.HUMIDITY, value * 10));
        return setRefreshPeriod(-1);
    }

    @Override
    public SensorHandler<BrickletHumidity> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidity> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidity> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidity> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHumidity> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> device.setHumidityCallbackPeriod((milliseconds < 1 ? 1000 : milliseconds)));
        return this;
    }

}
