package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletRotaryEncoderV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ROTARY;

/**
 * <h3>{@link RotaryV2}</h3><br />
 * <i>360° rotary encoder with push-button</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#ROTARY} [x = number]</li>
 * <li>{@link ValueType#BUTTON_PRESSED} [0/1] = Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Rotary_Encoder_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting rotary number example</h6>
 * <code>stack.values().rotary();</code>
 */
public class RotaryV2 extends Sensor<BrickletRotaryEncoderV2> {

    public RotaryV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletRotaryEncoderV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletRotaryEncoderV2> initListener() {
        device.addCountListener(value -> sendEvent(ROTARY, (long) value));
        device.addPressedListener(() -> sendEvent(BUTTON_PRESSED, 1L));
        device.addReleasedListener(() -> sendEvent(BUTTON_PRESSED, 0L));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletRotaryEncoderV2> send(final Object value) {
        try {
            if (value instanceof Boolean) {
                device.getCount((Boolean) value);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletRotaryEncoderV2> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig(LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig(LED_STATUS.bit);
            } else if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig(LED_STATUS_OFF.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletRotaryEncoderV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletRotaryEncoderV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setCountCallbackConfiguration(4, false, 'x', 0, 0);
            } else {
                device.setCountCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(ROTARY, (long) device.getCount(false));
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
