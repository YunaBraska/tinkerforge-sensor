package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletJoystickV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURSOR_MOVE_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURSOR_MOVE_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURSOR_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURSOR_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link JoystickV2}</h3><br>
 * <i>2-axis joystick with push-button</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#CURSOR_MOVE_X} [x] = number</li>
 * <li>{@link ValueType#CURSOR_MOVE_Y} [y] = number</li>
 * <li>{@link ValueType#CURSOR_PRESSED} [1] = Pressed</li>
 * <li>{@link ValueType#CURSOR_RELEASED} [0] = Released</li>
 * <li>{@link ValueType#BUTTON_PRESSED}  [1] = Pressed</li>
 * <li>{@link ValueType#BUTTON_RELEASED} [0] = Released</li>
 * <li>{@link ValueType#BUTTON} [0/1] = Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Joystick_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting x axis</h6>
 * <code>sensor.values().mouseMoveX();</code>
 * <h6>Getting y axis</h6>
 * <code>sensor.values().mouseMoveY();</code>
 * <h6>Getting button pressed</h6>
 * <code>sensor.values().buttonPressed();</code>
 */
public class JoystickV2 extends Sensor<BrickletJoystickV2> {

    public JoystickV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletJoystickV2) device, uid);
    }

    @Override
    protected Sensor<BrickletJoystickV2> initListener() {
        device.addPositionListener((x, y) -> {
            sendEvent(CURSOR_MOVE_X, x, true);
            sendEvent(CURSOR_MOVE_Y, y, true);
        });
        device.addPressedListener(pressed -> {
            if (pressed) {
                sendEvent(CURSOR_PRESSED, 1, true);
                sendEvent(BUTTON_PRESSED, 1, true);
                sendEvent(BUTTON, 1, true);
            } else {
                sendEvent(CURSOR_RELEASED, 0, true);
                sendEvent(BUTTON_RELEASED, 0, true);
                sendEvent(BUTTON, 0, true);
            }
        });
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletJoystickV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletJoystickV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickletJoystickV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletJoystickV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletJoystickV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setPositionCallbackConfiguration(4, false);
                device.setPressedCallbackConfiguration(4, false);
            } else {
                device.setPositionCallbackConfiguration(milliseconds, true);
                device.setPressedCallbackConfiguration(milliseconds, true);
            }

            final BrickletJoystickV2.Position position = device.getPosition();
            sendEvent(CURSOR_MOVE_X, position.x, true);
            sendEvent(CURSOR_MOVE_Y, position.y, true);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
