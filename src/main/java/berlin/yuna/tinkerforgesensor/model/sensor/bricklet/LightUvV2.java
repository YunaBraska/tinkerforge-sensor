package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
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
 * Measures UV-A, UV-B and UV index
 * <b>Values</b>
 * LIGHT_UV[index] = n / 10.0
 * LIGHT_UVA[mW/m²] = n / 10.0
 * LIGHT_UVB[mW/m²] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light_V2.html">Official documentation</a>
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
