package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DISTANCE;

/**
 * <li>{@link ValueType#DISTANCE} [x / 10.0 = cm]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DistanceIR extends SensorHandler<BrickletDistanceIR> {

    public DistanceIR(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletDistanceIR> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIR> init() {
        config.put(THRESHOLD_PREFIX + DISTANCE, 2);
        device.addDistanceListener(value -> sendEvent(DISTANCE, value));
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickletDistanceIR> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIR> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIR> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIR> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIR> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setDistanceCallbackPeriod(milliseconds < 1 ? 0 : milliseconds));
        return this;
    }

}
