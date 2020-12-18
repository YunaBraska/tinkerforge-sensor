package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletJoystickV2;
import com.tinkerforge.Device;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class JoystickV2 extends SensorHandler<BrickletJoystickV2> {

    public JoystickV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletJoystickV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletJoystickV2> init() {
        device.addPositionListener((x, y) -> {
            sendEvent(ValueType.CURSOR_MOVE_X, x);
            sendEvent(ValueType.CURSOR_MOVE_Y, y);
        });
        device.addPressedListener(pressed -> {
            if (pressed) {
                sendEvent(ValueType.CURSOR_PRESSED, 1);
            } else {
                sendEvent(ValueType.CURSOR_RELEASED, 0);
            }
        });
        return setRefreshPeriod(10);
    }

    @Override
    public SensorHandler<BrickletJoystickV2> initConfig() {
        RunThrowable.handleConnection(() -> setConfig(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletJoystickV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletJoystickV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletJoystickV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletJoystickV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            device.calibrate();
            device.setPositionCallbackConfiguration(milliseconds < 1 ? 4 : milliseconds, true);
            device.setPressedCallbackConfiguration(milliseconds < 1 ? 4 : milliseconds, true);
        });
        return this;
    }
}
