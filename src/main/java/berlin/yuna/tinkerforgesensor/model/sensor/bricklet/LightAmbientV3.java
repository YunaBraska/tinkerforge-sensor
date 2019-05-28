package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletAmbientLightV3;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;

/**
 * Measures ambient light up to 64000lux
 * LIGHT_LUX[lx] = n / 100.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light.html_V3">Official doku</a>
 */
public class LightAmbientV3 extends Sensor<BrickletAmbientLightV3> {

    public LightAmbientV3(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAmbientLightV3) device, uid, true);
    }

    @Override
    protected Sensor<BrickletAmbientLightV3> initListener() {
        try {
            device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value));
            device.setIlluminanceCallbackConfiguration(CALLBACK_PERIOD, false, 'x', 0, 0);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV3> ledStatus(final Integer value) {
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
    public Sensor<BrickletAmbientLightV3> ledAdditional(final Integer value) {
        return this;
    }
}
