package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletBarometerV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.TEMPERATURE;

/**
 * <li>{@link ValueType#AIR_PRESSURE} [x / 1000.0 = mbar]</li>
 * <li>{@link ValueType#ALTITUDE} [x / 1000.0 = m]</li>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class BarometerV2 extends SensorHandler<BrickletBarometerV2> {

    public BarometerV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletBarometerV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometerV2> init() {
        config.put(THRESHOLD_PREFIX + ALTITUDE, 256);
        config.put(THRESHOLD_PREFIX + AIR_PRESSURE, 128);
        config.put(THRESHOLD_PREFIX + TEMPERATURE, 8);
        device.addAltitudeListener(value -> sendEvent(ALTITUDE, (long) value * 10));
        device.addAirPressureListener(value -> sendEvent(AIR_PRESSURE, (long) value));
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletBarometerV2> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometerV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometerV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometerV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometerV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> {
            device.setAltitudeCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
            device.setAirPressureCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
            device.setTemperatureCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
        });
        return this;
    }

}
