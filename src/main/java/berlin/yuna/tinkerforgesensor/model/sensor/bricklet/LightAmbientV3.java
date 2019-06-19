package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletAmbientLightV3;
import com.tinkerforge.BrickletHumidityV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;

/**
 * <h3>{@link LightAmbientV3}</h3><br />
 * <i>Measures ambient light up to 64000lux</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#LIGHT_LUX} [x / 100.0 = lx]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light_V3.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting lightLux examples</h6>
 * <code>
 * stack.values().lightLux();
 * stack.values().lightLux_Avg();
 * stack.values().lightLux_Min();
 * stack.values().lightLux_Max();
 * stack.values().lightLux_Sum();
 * </code>
 */
public class LightAmbientV3 extends Sensor<BrickletAmbientLightV3> {

    public LightAmbientV3(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAmbientLightV3) device, uid);
    }

    @Override
    protected Sensor<BrickletAmbientLightV3> initListener() {
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> setLedStatus(final Integer value) {
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
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setIlluminanceCallbackConfiguration(1000, false, 'x', 0, 0);
            } else {
                device.setIlluminanceCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(LIGHT_LUX, device.getIlluminance());
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
