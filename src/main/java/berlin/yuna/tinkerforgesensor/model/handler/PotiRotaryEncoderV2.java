package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletRotaryEncoderV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ROTARY;

/**
 * <li>{@link ValueType#ROTARY} [x = number]</li>
 * <li>{@link ValueType#BUTTON_PRESSED} [0] = Pressed</li>
 * <li>{@link ValueType#BUTTON_RELEASED} [0] = Released</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PotiRotaryEncoderV2 extends SensorHandler<BrickletRotaryEncoderV2> {

    public PotiRotaryEncoderV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> init() {
        device.addCountListener(value -> sendEvent(ROTARY, value));
        device.addPressedListener(() -> sendEvent(BUTTON_PRESSED, 0));
        device.addReleasedListener(() -> sendEvent(BUTTON_RELEASED, 0));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletRotaryEncoderV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> handleConnection(() -> device.setCountCallbackConfiguration(milliseconds < 1 ? 4 : milliseconds, true, 'x', 0, 0)));
        return this;
    }

}
