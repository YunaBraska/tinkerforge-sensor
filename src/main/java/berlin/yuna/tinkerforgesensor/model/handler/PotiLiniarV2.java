package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletLinearPotiV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.PERCENTAGE;

/**
 * <li>{@link ValueType#PERCENTAGE} [x = %]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PotiLiniarV2 extends SensorHandler<BrickletLinearPotiV2> {

    public PotiLiniarV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> init() {
        device.addPositionListener(value -> sendEvent(PERCENTAGE, value));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletLinearPotiV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> handleConnection(() -> device.setPositionCallbackConfiguration(milliseconds < 1 ? 4 : milliseconds, true, 'x', 0, 0)));
        return this;
    }

}
