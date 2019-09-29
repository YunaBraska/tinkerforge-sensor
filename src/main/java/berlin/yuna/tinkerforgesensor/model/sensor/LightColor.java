package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_B;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_C;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_G;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_LUX;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_R;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_TEMPERATURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link LightColor}</h3><br>
 * <i>Measures color (RGB send), illuminance and color temperature</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#COLOR}</li>
 * <li>{@link ValueType#COLOR_R}</li>
 * <li>{@link ValueType#COLOR_G}</li>
 * <li>{@link ValueType#COLOR_B}</li>
 * <li>{@link ValueType#COLOR_LUX}</li>
 * <li>{@link ValueType#COLOR_TEMPERATURE}</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Color.html">Official documentation</a></li>
 * </ul>
 * <h6>Turn on flash LED</h6>
 * <code>color.ledAdditional_setOn();</code>
 * <h6>Getting color examples</h6>
 * <code>
 * sensor.values().color();
 * sensor.values().color_Avg();
 * sensor.values().color_Min();
 * sensor.values().color_Max();
 * sensor.values().color_Sum();
 * </code>
 */
public class LightColor extends Sensor<BrickletColor> {

    private BrickletColor.Config config;

    public LightColor(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletColor) device, uid);
    }

    @Override
    protected Sensor<BrickletColor> initListener() {
        device.addColorListener(this::sendEvent);
        device.addIlluminanceListener(value -> sendEvent(COLOR_LUX, value * 700 / config.gain / config.integrationTime));
        device.addColorTemperatureListener(value -> sendEvent(COLOR_TEMPERATURE, (long) value));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletColor> send(final Object value) {
        //TODO: set config && accept config
        return this;
    }

    @Override
    public Sensor<BrickletColor> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletColor> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                ledAdditional = LED_ADDITIONAL_ON;
                device.lightOn();
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                ledAdditional = LED_ADDITIONAL_OFF;
                device.lightOff();
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletColor> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_ADDITIONAL_OFF;
        return this;
    }

    @Override
    public Sensor<BrickletColor> flashLed() {
        try {
            for (int i = 0; i < 7; i++) {
                if (i % 2 == 0) {
                    this.ledAdditional_setOff();
                } else {
                    this.ledAdditional_setOn();
                }
                Thread.sleep(128);
            }
            this.ledStatus_setStatus();
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletColor> refreshPeriod(final int milliseconds) {
        try {
            config = device.getConfig();

            if (milliseconds < 1) {
                device.setColorCallbackPeriod(0);
                device.setIlluminanceCallbackPeriod(0);
                device.setColorTemperatureCallbackPeriod(0);

                final BrickletColor.Color color = device.getColor();
                sendEvent(color.r, color.g, color.b, color.c);
                sendEvent(COLOR_LUX, device.getIlluminance() * 700 / config.gain / config.integrationTime);
                sendEvent(COLOR_TEMPERATURE, (long) device.getColorTemperature());

            } else {
                device.setColorCallbackPeriod(milliseconds);
                device.setIlluminanceCallbackPeriod(milliseconds);
                device.setColorTemperatureCallbackPeriod(milliseconds);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private void sendEvent(final int r, final int g, final int b, final long c) {
        sendEvent(COLOR, (long) new Color(r / 256, g / 256, b / 256).getRGB());
        sendEvent(COLOR_R, (long) r / 256);
        sendEvent(COLOR_G, (long) g / 256);
        sendEvent(COLOR_B, (long) b / 256);
        sendEvent(COLOR_C, c / 256);
    }
}