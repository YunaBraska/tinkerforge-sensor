package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletDualButtonV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.Arrays;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static java.util.Arrays.asList;

/**
 * <h3>{@link ButtonDual}</h3>
 * <i>Two tactile buttons with built-in blue LEDs</i>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BUTTON_PRESSED} [1] = Pressed</li>
 * <li>{@link ValueType#BUTTON_RELEASED} [0] = Released</li>
 * <li>{@link ValueType#BUTTON} [0/1,0/1] = 2x Button Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Dual_Button.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting button state from second button (0=Released, 1= pressed)</h6>
 * <code>sensor.values().button(1);</code>
 * <h6>Getting button state list of 0/1 (0=Released, 1= pressed) value for each button</h6>
 * <code>sensor.values().button_List();</code>
 * <h6>Switch first led on</h6>
 * <code>sensor.send(1);</code>
 * <h6>Switch first led off</h6>
 * <code>sensor.send(-1);</code>
 * <h6>Switch first led on and second led off</h6>
 * <code>sensor.send(1, -2);</code>
 * <code>sensor.send(true, false);</code>
 * <h6>(Auto) Set LEDs on</h6>
 * <code>sensor.ledAdditional_setOn();</code>
 * <h6>(Auto) Set LEDs off</h6>
 * <code>sensor.ledAdditional_setOff();</code>
 * <h6>(Auto) Set LEDs active on press</h6>
 * <code>sensor.setLedAdditional_Status();</code>
 * <h6>(Auto) Set LEDs active on release</h6>
 * <code>sensor.setLedAdditional_Heartbeat();</code>
 */
public class ButtonDual extends Sensor<BrickletDualButtonV2> {

    private int buttonL;
    private int buttonR;

    private int ledL;
    private int ledR;

    public ButtonDual(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletDualButtonV2) device, uid);
    }

    @Override
    protected Sensor<BrickletDualButtonV2> initListener() {
        device.addStateChangedListener((buttonL, buttonR, ledL, ledR) -> {
            sendEvent(buttonL, buttonR);
        });
        refreshPeriod(69);
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> send(final Object... values) {
        if (values.length > 1 && values[0] instanceof Boolean && values[1] instanceof Boolean) {
            final boolean ledL = (Boolean) values[0];
            final boolean ledR = (Boolean) values[1];
            send(ledL? 1 : -1, ledR? 2 : -2);
        } else {
            Arrays.stream(values).forEach(this::send);
        }
        return this;
    }

    public Sensor<BrickletDualButtonV2> send(final Object value) {
        if (value instanceof Number) {
            final int led = ((Number) value).intValue();
            setLed(led);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                ledStatus = LED_STATUS_HEARTBEAT;
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                ledStatus = LED_STATUS;
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ADDITIONAL_OFF.bit) {
                ledAdditional = LED_ADDITIONAL_OFF;
                device.setLEDState(3, 3);
            } else if (value == LED_ADDITIONAL_ON.bit) {
                ledAdditional = LED_ADDITIONAL_ON;
                device.setLEDState(2, 2);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                ledAdditional = LED_ADDITIONAL_HEARTBEAT;
                device.setLEDState(0, 0);
            } else if (value == LED_ADDITIONAL_STATUS.bit) {
                ledAdditional = LED_ADDITIONAL_STATUS;
                device.setLEDState(1, 1);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_ADDITIONAL_OFF;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> flashLed() {
        try {
            ledAdditional_setOff();
            Thread.sleep(128);
            ledAdditional_setOn();
            Thread.sleep(128);
            ledAdditional_setOff();
            Thread.sleep(128);
            send(true, false);
            Thread.sleep(128);
            send(-1, 2);
            Thread.sleep(128);
            setLedAdditional_Status();
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> refreshPeriod(final int milliseconds) {
        try {
            buttonL = device.getButtonState().buttonL;
            buttonR = device.getButtonState().buttonR;
            sendEvent(buttonL, buttonR);

            if (milliseconds < 1) {
                device.setStateChangedCallbackConfiguration(false);
            } else {
                device.setStateChangedCallbackConfiguration(true);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private void sendEvent(final int buttonL, final int buttonR) {
        int changed = 0;
        if (this.buttonL != buttonL) {
            this.buttonL = buttonL;
            changed = buttonL;
        } else if (this.buttonR != buttonR) {
            this.buttonR = buttonR;
            changed = buttonR;
        }

        sendEvent((changed == 1 ? BUTTON_RELEASED : BUTTON_PRESSED), (changed == 1 ? 0 : 1), true);
        sendEvent(BUTTON, asList((buttonL == 1 ? 0 : 1), (buttonR == 1 ? 0 : 1)), true);
    }

    private void setLed(final int led) {
        try {
            ledR = (led == 1 ? 3 : (led == -1 ? 2 : ledR));
            ledL = (led == 2 ? 3 : (led == -2 ? 2 : ledL));
            device.setLEDState(ledL, ledR);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }
}
