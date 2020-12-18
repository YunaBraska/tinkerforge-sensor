package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ALTITUDE;

/**
 * <li>{@link ValueType#AIR_PRESSURE} [x / 1000.0 = mbar]</li>
 * <li>{@link ValueType#ALTITUDE} [x / 1000.0 = m]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Barometer extends SensorHandler<BrickletBarometer> {

    public Barometer(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletBarometer> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometer> init() {
        config.put(THRESHOLD_PREFIX + ALTITUDE, 256);
        config.put(THRESHOLD_PREFIX + AIR_PRESSURE, 128);
        device.addAltitudeListener(value -> sendEvent(ALTITUDE, (long) value * 10));
        device.addAirPressureListener(value -> sendEvent(AIR_PRESSURE, (long) value));
        return setRefreshPeriod(512);
    }

    @Override
    public SensorHandler<BrickletBarometer> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometer> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometer> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometer> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletBarometer> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> {
            device.setAltitudeCallbackPeriod(milliseconds < 1 ? 0 : milliseconds);
            device.setAirPressureCallbackPeriod(milliseconds < 1 ? 0 : milliseconds);
        });
        return this;
    }

}
