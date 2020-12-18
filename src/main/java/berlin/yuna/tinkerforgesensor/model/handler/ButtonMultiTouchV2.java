package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletMultiTouchV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_RELEASED;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ButtonMultiTouchV2 extends SensorHandler<BrickletMultiTouchV2> {

    private static final String CONFIG_BUTTON = BUTTON + "_";

    public ButtonMultiTouchV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> init() {
        device.addTouchStateListener(touchStates -> {
            for (int i = 0; i < touchStates.length; i++) {
                final int state = touchStates[i] ? 1 : 0;
                if (state != getConfig(CONFIG_BUTTON + i).intValue()) {
                    sendEvent(state == 1 ? BUTTON_PRESSED : BUTTON_RELEASED, i);
                    setConfig(CONFIG_BUTTON + i, state);
                }
            }
        });
        return setRefreshPeriod(69);
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> initConfig() {
        handleConnection(() -> {
            device.recalibrate();
            setConfig(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            boolean[] touchStates = device.getTouchState();
            for (int i = 0; i < touchStates.length; i++) {
                setConfig(CONFIG_BUTTON + i, touchStates[i] ? 1 : 0);
            }
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMultiTouchV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setTouchStateCallbackConfiguration(milliseconds >= 1 ? 1 : 0, true));
        return this;
    }
}
