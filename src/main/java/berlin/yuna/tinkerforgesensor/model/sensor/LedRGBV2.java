package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import com.tinkerforge.BrickletRGBLEDV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.Color.convertToHighContrast;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link LedRGBV2}</h3><br>
 * <i>Controls one RGB LED</i><br>
 *
 * <h6>Set LED color</h6>
 * <code>
 * sensor.send(Color.MAGENTA);
 * sensor.send(new Color(255, 128, 64));
 * sensor.send(12367);
 * </code>
 * <h6>Set auto contrast on=true off=false</h6>
 * <code>
 * ledRgb.send(true);
 * </code>
 */
public class LedRGBV2 extends Sensor<BrickletRGBLEDV2> {

    boolean highContrast = false;

    public LedRGBV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletRGBLEDV2) device, uid);
    }

    @Override
    protected Sensor<BrickletRGBLEDV2> initListener() {
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDV2> send(final Object value) {
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
                device.setRGBValue(color.getRed(), color.getGreen(), color.getBlue());
            }
        } catch (TinkerforgeException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickletRGBLEDV2> ledAdditional(final Integer value) {
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
    public Sensor<BrickletRGBLEDV2> flashLed() {
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
    public Sensor<BrickletRGBLEDV2> refreshPeriod(final int milliseconds) {
        return this;
    }

    @Override
    public Sensor<BrickletRGBLEDV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
