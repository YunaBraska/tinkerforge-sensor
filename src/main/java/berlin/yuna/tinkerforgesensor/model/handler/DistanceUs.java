package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.ValueType.DISTANCE;

/**
 * <li>{@link ValueType#DISTANCE} [x / 10.0 = cm]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DistanceUs extends SensorHandler<BrickletDistanceUS> {

    public DistanceUs(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletDistanceUS> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUS> init() {
        config.put(THRESHOLD_PREFIX + DISTANCE, 20);
        device.addDistanceListener(value -> sendEvent(DISTANCE, value * 10));
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickletDistanceUS> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUS> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUS> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUS> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUS> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> device.setDistanceCallbackPeriod(milliseconds < 1 ? 0 : milliseconds));
        return this;
    }

}
