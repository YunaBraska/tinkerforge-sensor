package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletUVLightV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UV;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UVA;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UVB;

/**
 * <h3>{@link LightUvV2}</h3><br />
 * <i>Measures UV-A, UV-B and UV index</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#LIGHT_UV} [x / 10.0 = index]</li>
 * <li>{@link ValueType#LIGHT_UVA} [x / 10.0 = mW/m²]</li>
 * <li>{@link ValueType#LIGHT_UVB} [x / 10.0 = mW/m²]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light.html_V2">Official documentation</a></li>
 * </ul>
 * <h6>Getting lightUv examples</h6>
 * <code>
 * stack.values().lightUv();
 * stack.values().lightUv_Avg();
 * stack.values().lightUv_Min();
 * stack.values().lightUv_Max();
 * stack.values().lightUv_Sum();
 * </code>
 */
public class LightUvV2 extends Sensor<BrickletUVLightV2> {

    public LightUvV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletUVLightV2) device, uid);
    }

    @Override
    protected Sensor<BrickletUVLightV2> initListener() {
        device.addUVIListener(value -> sendEvent(LIGHT_UV, (long) value));
        device.addUVAListener(value -> sendEvent(LIGHT_UVA, (long) value));
        device.addUVBListener(value -> sendEvent(LIGHT_UVB, (long) value));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickletUVLightV2> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setUVACallbackConfiguration(1000, false, 'x', 0, 0);
                device.setUVBCallbackConfiguration(1000, false, 'x', 0, 0);
                device.setUVICallbackConfiguration(1000, false, 'x', 0, 0);
            } else {
                device.setUVACallbackConfiguration(milliseconds, true, 'x', 0, 0);
                device.setUVBCallbackConfiguration(milliseconds, true, 'x', 0, 0);
                device.setUVICallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(LIGHT_UV, (long) device.getUVI());
            sendEvent(LIGHT_UVA, (long) device.getUVA());
            sendEvent(LIGHT_UVB, (long) device.getUVB());
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
