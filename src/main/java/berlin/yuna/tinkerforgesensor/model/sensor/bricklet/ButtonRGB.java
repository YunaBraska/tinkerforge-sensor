package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.Arrays;
import java.util.Collections;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;

/**
 * Push button with built-in RGB LED
 * BUTTON_PRESSED 1/0 pressed/released
 */
public class ButtonRGB extends Sensor<BrickletRGBLEDButton> {

    boolean highContrast = false;

    public ButtonRGB(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletRGBLEDButton) device, uid, true);
    }

    @Override
    protected Sensor<BrickletRGBLEDButton> initListener() {
        device.addButtonStateChangedListener(value -> sendEvent(BUTTON_PRESSED, value == 1 ? 0L : 1L));
        return this;
    }

    /**
     * @param value <br /> [{@link Color#getRGB()}] RGB value
     *              <br /> [Number] RGB value {@link Color#getRGB()}
     *              <br /> [Boolean] activate highContrast
     * @return {@link Sensor}
     */
    @Override
    public Sensor<BrickletRGBLEDButton> value(final Object value) {
        try {
            if (value instanceof Boolean) {
                highContrast = (Boolean) value;
            }
            if (value instanceof Color) {
                return value(((Color) value).getRGB());
            }
            if (value instanceof Number) {
                Color color = new Color(((Number) value).intValue());
                color = highContrast ? calculateHighContrast(color) : color;

                device.setColor(color.getRed(), color.getGreen(), color.getBlue());
            } else {
                device.setColor(0, 0, 0);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    private Color calculateHighContrast(final Color color) {
        Color result = color;
        int max = Collections.min(Arrays.asList(result.getRed(), result.getGreen(), result.getBlue()));
        result = new Color(result.getRed() - max, result.getGreen() - max, result.getBlue() - max);

        // +100% brightness
        float[] hsb = Color.RGBtoHSB(result.getRed(), result.getGreen(), result.getBlue(), null);
        result = new Color(Color.HSBtoRGB(hsb[0], hsb[1], 0.5f * (1f + hsb[2])));
        return result;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> ledStatus(final Integer value) {
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
        }
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> ledAdditional(final Integer value) {
        if (value == LED_ADDITIONAL_ON.bit) {
            value(true);
        } else if (value == LED_ADDITIONAL_OFF.bit) {
            value(false);
        }
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDButton> flashLed() {
        super.flashLed();
        try {
            ledAdditionalOn();
            for (int color : Arrays.asList(
                    Color.WHITE,
                    Color.RED,
                    Color.ORANGE,
                    Color.YELLOW,
                    Color.GREEN,
                    Color.CYAN,
                    Color.BLUE,
                    Color.MAGENTA,
                    Color.PINK,
                    Color.BLACK
            )) {
                value(color);
                Thread.sleep(64);
            }
            ledAdditionalOff();
        } catch (Exception ignore) {
        }
        return this;
    }
}
