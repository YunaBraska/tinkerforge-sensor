package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.BrickletDistanceUSV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.ValueType.DISTANCE;

/**
 * <li>{@link ValueType#DISTANCE} [x / 10.0 = cm]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DistanceUsV2 extends SensorHandler<BrickletDistanceUSV2> {

    public DistanceUsV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> init() {
        config.put(THRESHOLD_PREFIX + DISTANCE, 40);
        device.addDistanceListener(value -> sendEvent(DISTANCE, value * 10));
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> initConfig() {
        RunThrowable.handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_FUNCTION_A, device.getDistanceLEDConfig());
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> triggerFunctionA(int value) {
        applyOnNewValue(CONFIG_FUNCTION_A, value, () -> device.setDistanceLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletDistanceUSV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> device.setDistanceCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0));
        return this;
    }

}
