package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletUVLightV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
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
        super((BrickletUVLightV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletUVLightV2> initListener() {
        device.addUVIListener(value -> sendEvent(LIGHT_UV, (long) value));
        device.addUVAListener(value -> sendEvent(LIGHT_UVA, (long) value));
        device.addUVBListener(value -> sendEvent(LIGHT_UVB, (long) value));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> ledStatus(final Integer value) {
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
    public Sensor<BrickletUVLightV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setUVACallbackConfiguration(0, true, 'x', 0, 0);
                device.setUVBCallbackConfiguration(0, true, 'x', 0, 0);
                device.setUVICallbackConfiguration(0, true, 'x', 0, 0);
                sendEvent(LIGHT_UV, (long) device.getUVI());
                sendEvent(LIGHT_UVA, (long) device.getUVA());
                sendEvent(LIGHT_UVB, (long) device.getUVB());
            } else {
                device.setUVACallbackConfiguration(milliseconds, false, 'x', 0, 0);
                device.setUVBCallbackConfiguration(milliseconds, false, 'x', 0, 0);
                device.setUVICallbackConfiguration(milliseconds, false, 'x', 0, 0);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
