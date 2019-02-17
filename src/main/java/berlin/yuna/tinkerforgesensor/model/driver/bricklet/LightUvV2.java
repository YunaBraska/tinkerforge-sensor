package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletUVLightV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.generator.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UV;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UVA;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UVB;

/**
 * Measures UV-A, UV-B and UV index
 * <b>Values</b>
 * LIGHT_UV[index] = n / 10.0
 * LIGHT_UVA[mW/m²] = n / 10.0
 * LIGHT_UVB[mW/m²] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light_V2.html">Official doku</a>
 */
public class LightUvV2 extends Sensor<BrickletUVLightV2> {

    public LightUvV2(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletUVLightV2) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickletUVLightV2> initListener() {
        try {
            device.addUVIListener(value -> sendEvent(LIGHT_UV, (long) value));
            device.addUVAListener(value -> sendEvent(LIGHT_UVA, (long) value));
            device.addUVBListener(value -> sendEvent(LIGHT_UVB, (long) value));
            device.setUVACallbackConfiguration(CALLBACK_PERIOD, false, 'x', 0, 0);
            device.setUVBCallbackConfiguration(CALLBACK_PERIOD, false, 'x', 0, 0);
            device.setUVICallbackConfiguration(CALLBACK_PERIOD, false, 'x', 0, 0);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletUVLightV2> value(final Object value) {
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
}
