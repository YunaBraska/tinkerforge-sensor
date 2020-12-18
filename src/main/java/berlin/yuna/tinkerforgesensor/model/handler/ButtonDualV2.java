package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.LedState;
import com.tinkerforge.BrickletDualButtonV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_ON;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ButtonDualV2 extends SensorHandler<BrickletDualButtonV2> {

    private static final String CONFIG_BUTTON_0 = BUTTON + "_0";
    private static final String CONFIG_BUTTON_1 = BUTTON + "_1";
    private static final String CONFIG_LED_0 = "LED_0";
    private static final String CONFIG_LED_1 = "LED_1";

    public ButtonDualV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> send(final Object value) {
        if (value instanceof LedState) {
            final LedState state = (LedState) value;
            final int ledState0 = state.getId() == 0 ? translateState(state.getState()) : getConfig(CONFIG_LED_0).intValue();
            final int ledState1 = state.getId() == 1 ? translateState(state.getState()) : getConfig(CONFIG_LED_1).intValue();
            setLedState(ledState0, ledState1);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> init() {
        device.addStateChangedListener((buttonL, buttonR, ledL, ledR) -> {
            final int button0 = invert(buttonL);
            final int button1 = invert(buttonR);
            if (button0 != getConfig(CONFIG_BUTTON_0).intValue()) {
                sendEvent(button0 == 1 ? BUTTON_PRESSED : BUTTON_RELEASED, 0);
            } else if (button1 != getConfig(CONFIG_BUTTON_1).intValue()) {
                sendEvent(button1 == 1 ? BUTTON_PRESSED : BUTTON_RELEASED, 1);
            }
            setConfig(buttonL, buttonR, ledL, ledR);
        });
        return setRefreshPeriod(69);
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> initConfig() {
        handleConnection(() -> {
            BrickletDualButtonV2.ButtonState buttonState = device.getButtonState();
            BrickletDualButtonV2.LEDState ledState = device.getLEDState();
            setConfig(buttonState.buttonL, buttonState.buttonR, ledState.ledL, ledState.ledR);
            setConfig(CONFIG_LED_STATUS, device.getStatusLEDConfig());
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> triggerFunctionA(int value) {
        setLedState(translateState(value), translateState(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletDualButtonV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setStateChangedCallbackConfiguration(milliseconds >= 1));
        return this;
    }

    private void setLedState(int ledState0, int ledState1) {
        if (ledState0 != getConfig(CONFIG_LED_0).intValue() || ledState1 != getConfig(CONFIG_LED_1).intValue()) {
            setConfig(CONFIG_LED_0, ledState0);
            setConfig(CONFIG_LED_1, ledState1);
            handleConnection(() -> device.setLEDState(ledState0, ledState1));
        }
    }

    private void setConfig(int buttonL, int buttonR, int ledL, int ledR) {
        setConfig(CONFIG_BUTTON_0, invert(buttonL));
        setConfig(CONFIG_BUTTON_1, invert(buttonR));

        setConfig(CONFIG_LED_0, ledL);
        setConfig(CONFIG_LED_1, ledR);
    }

    private int invert(int button) {
        return button == 0 ? 1 : 0;
    }

    private int translateState(final int state) {
        if (state == LED_OFF.bit) {
            //3 = Off: LED off (auto toggle is disabled).
            return 3;
        } else if (state == LED_ON.bit) {
            //2 = On: LED on (auto toggle is disabled).
            return 2;
        } else if (state == LED_HEARTBEAT.bit) {
            //0 = AutoToggleOn: Auto toggle enabled and LED on.
            return 0;
        } else if (state == LED_STATUS.bit) {
            //1 = AutoToggleOff: Auto toggle enabled and LED off.
            return 1;
        }
        return 3;
    }

}
