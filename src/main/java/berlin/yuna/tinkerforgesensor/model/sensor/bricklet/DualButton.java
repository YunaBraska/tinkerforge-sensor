package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletDualButtonV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * Two tactile buttons with built-in blue LEDs
 * <b>Values</b>
 * BUTTON_PRESSED 0/1
 * BUTTON 1 10 Released
 * BUTTON 1 11 Pressed
 * BUTTON 2 20 Released
 * BUTTON 2 21 Pressed
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html">Official doku</a>
 */
public class DualButton extends Sensor<BrickletDualButtonV2> {

    private int buttonL;
    private int buttonR;

    public DualButton(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletDualButtonV2) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickletDualButtonV2> initListener() throws TimeoutException, NotConnectedException {
        device.setStateChangedCallbackConfiguration(true);
        buttonL = device.getButtonState().buttonL;
        buttonR = device.getButtonState().buttonR;
        device.addStateChangedListener((buttonL, buttonR, ledL, ledR) -> {
            if (this.buttonL != buttonL) {
                this.buttonL = buttonL;
                sendEvent(BUTTON_PRESSED, buttonL == 1 ? 0L : 1L);
                sendEvent(BUTTON, buttonL == 1 ? 10L : 11L);
            }

            if (this.buttonR != buttonR) {
                this.buttonR = buttonR;
                sendEvent(BUTTON_PRESSED, buttonR == 1 ? 0L : 1L);
                sendEvent(BUTTON, buttonR == 1 ? 20L : 21L);
            }
        });
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> value(Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> ledStatus(Integer value) {
        try {
            if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    //TODO: set button 1 && button 2 different
    @Override
    public Sensor<BrickletDualButtonV2> ledAdditional(Integer value) {
        try {
            if (value == LED_ADDITIONAL_OFF.bit) {
                device.setLEDState(3, 3);
            } else if (value == LED_ADDITIONAL_ON.bit) {
                device.setLEDState(2, 2);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setLEDState(0, 0); //FIXME: does this make sense [= invert status]???
            } else if (value == LED_ADDITIONAL_STATUS.bit) {
                device.setLEDState(1, 1);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDualButtonV2> flashLed() {
        try {
            ledAdditionalOff();
            Thread.sleep(128);
            ledAdditionalOn();
            Thread.sleep(128);
            ledAdditionalOff();
            Thread.sleep(128);
            device.setLEDState(2, 3);
            Thread.sleep(128);
            device.setLEDState(3, 2);
            Thread.sleep(128);
            ledAdditionalStatus();
        } catch (Exception ignore) {
        }
        return this;
    }
}
