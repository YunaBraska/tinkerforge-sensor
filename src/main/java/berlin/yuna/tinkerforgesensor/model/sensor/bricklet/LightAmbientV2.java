package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;

/**
 * Measures ambient light up to 64000lux
 * LIGHT_LUX[lx] = n / 100.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light.html_V2">Official documentation</a>
 */
public class LightAmbientV2 extends Sensor<BrickletAmbientLightV2> {

    public LightAmbientV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAmbientLightV2) device, uid, false);
    }

    @Override
    protected Sensor<BrickletAmbientLightV2> initListener() {
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setIlluminanceCallbackPeriod(0);
                sendEvent(LIGHT_LUX, device.getIlluminance() * 10);
            } else {
                device.setIlluminanceCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
