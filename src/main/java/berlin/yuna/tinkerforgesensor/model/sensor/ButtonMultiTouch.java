package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletMultiTouchV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.List;
import java.util.stream.IntStream;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_TOUCH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static java.util.stream.Collectors.toList;

/**
 * <h3>{@link ButtonMultiTouch}</h3>
 * <i>Two tactile buttons with built-in blue LEDs</i>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BUTTON_PRESSED} [1] = Pressed</li>
 * <li>{@link ValueType#BUTTON_RELEASED} [0] = Released</li>
 * <li>{@link ValueType#BUTTON_TOUCH} [0/1,...] = 13x Button Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Multi_Touch_V2.html#multi-touch-v2-bricklet">Official documentation</a></li>
 * </ul>
 * <h6>Getting button state from second button (0=Released, 1= pressed)</h6>
 * <code>sensor.values().buttonTouch(1);</code>
 */
public class ButtonMultiTouch extends Sensor<BrickletMultiTouchV2> {

    public ButtonMultiTouch(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletMultiTouchV2) device, uid);
    }

    @Override
    protected Sensor<BrickletMultiTouchV2> initListener() {
        device.addTouchStateListener(this::sendTouchEvent);
        refreshPeriod(69);
        return this;
    }

    @Override
    public Sensor<BrickletMultiTouchV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletMultiTouchV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickletMultiTouchV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletMultiTouchV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletMultiTouchV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setTouchStateCallbackConfiguration(256, false);
            } else {
                device.setTouchStateCallbackConfiguration(milliseconds, true);
            }

            sendTouchEvent(device.getTouchState());
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private void sendTouchEvent(final boolean[] bools) {
        if (bools != null) {
            final List<Number> states = IntStream.range(0, bools.length).mapToObj(idx -> bools[idx]).map(bool -> bool ? 1 : 0).collect(toList());
            final boolean pressed = states.stream().anyMatch(state -> state.intValue() == 1);

            sendEvent((pressed ? BUTTON_PRESSED : BUTTON_RELEASED), (pressed ? 1 : 0), true);
            sendEvent(BUTTON_TOUCH, states, true);
        }
    }
}