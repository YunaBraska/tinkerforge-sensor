package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.Color.convertToHighContrast;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link ButtonRGB}</h3><br />
 * <i>Push button with built-in RGB LED</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BUTTON_PRESSED} [1] = Pressed</li>
 * <li>{@link ValueType#BUTTON_RELEASED} [0] = Released</li>
 * <li>{@link ValueType#BUTTON} [0/1] = Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/RGB_LED_Button.htm">Official documentation</a></li>
 * </ul>
 * <h6>Getting button pressed</h6>
 * <code>sensor.values().button(0);</code>
 * <code>sensor.values().buttonPressed();</code>
 */
public class ButtonRGB extends Sensor<BrickletRGBLEDButton> {

    boolean highContrast = false;

    public ButtonRGB(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletRGBLEDButton) device, uid);
    }

    @Override
    protected Sensor<BrickletRGBLEDButton> initListener() {
        device.addButtonStateChangedListener(value -> {
            sendEvent((value == 1 ? BUTTON_RELEASED : BUTTON_PRESSED), (value == 1 ? 0L : 1L), true);
            sendEvent(BUTTON, (value == 1 ? 0L : 1L), true);
        });
        return this;
    }

    /**
     * <h3>Send</h3>
     * <h6>Set LED color</h6>
     * <code>
     * sensor.send(Color.MAGENTA);
     * sensor.send(new Color(255, 128, 64));
     * sensor.send(12367);
     * </code>
     * <h6>Set auto contrast on=true off=false</h6>
     * <code>
     * sensor.send(true);
     * </code>
     */
    @Override
    public Sensor<BrickletRGBLEDButton> send(final Object value) {
        try {
            if (value instanceof Boolean) {
                highContrast = (Boolean) value;
            } else if (value instanceof Color) {
                return send(((Color) value).getRGB());
            } else if (value instanceof java.awt.Color) {
                return send(((java.awt.Color) value).getRGB());
            } else if (value instanceof Number) {
                Color color = new Color(((Number) value).intValue());
                color = highContrast ? convertToHighContrast(color) : color;
                device.setColor(color.getRed(), color.getGreen(), color.getBlue());
            }
        } catch (TinkerforgeException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> setLedStatus(final Integer value) {
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
    public Sensor<BrickletRGBLEDButton> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        if (value == LED_ADDITIONAL_ON.bit) {
            ledAdditional = LED_ADDITIONAL_ON;
            send(true);
        } else if (value == LED_ADDITIONAL_OFF.bit) {
            ledAdditional = LED_ADDITIONAL_OFF;
            send(false);
        }
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> flashLed() {
        super.flashLed();
        try {
            for (int color : Color.RAINBOW) {
                send(color);
                Thread.sleep(1);
            }
            send(Color.BLACK);
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> refreshPeriod(final int milliseconds) {
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_ADDITIONAL_ON;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
