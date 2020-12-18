package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletRotaryPotiV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.PERCENTAGE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ROTARY;

/**
 * <li>{@link ValueType#ROTARY} [x = number]</li>
 * <li>{@link ValueType#PERCENTAGE} [x = number]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PotiRotaryV2 extends SensorHandler<BrickletRotaryPotiV2> {

    public PotiRotaryV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> init() {
        device.addPositionListener(value -> {
            sendEvent(ROTARY, value);
            sendEvent(PERCENTAGE, ((value + 150) * 100) / 300);
        });
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryPotiV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> handleConnection(() -> device.setPositionCallbackConfiguration(milliseconds < 1 ? 4 : milliseconds, true, 'x', 0, 0)));
        return this;
    }

}
