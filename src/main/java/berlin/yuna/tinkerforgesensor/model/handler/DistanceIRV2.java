package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletDistanceIRV2;
import com.tinkerforge.Device;

/**
 * <li>{@link ValueType#DISTANCE} [x / 10.0 = cm]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DistanceIRV2 extends SensorHandler<BrickletDistanceIRV2> {

    public DistanceIRV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> init() {
        config.put(THRESHOLD_PREFIX + ValueType.DISTANCE, 2);
        device.addDistanceListener(value -> sendEvent(ValueType.DISTANCE, (value)));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> initConfig() {
        RunThrowable.handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_FUNCTION_A, device.getDistanceLEDConfig());
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> triggerFunctionA(int value) {
        applyOnNewValue(CONFIG_FUNCTION_A, value, () -> device.setDistanceLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceIRV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> device.setDistanceCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0));
        return this;
    }

}
