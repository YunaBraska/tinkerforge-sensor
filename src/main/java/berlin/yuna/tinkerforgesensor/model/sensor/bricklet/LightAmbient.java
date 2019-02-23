package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;

/**
 * Measures ambient light up to 900lux
 * LIGHT_LUX[lx] = n / 100.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light.html">Official doku</a>
 */
public class LightAmbient extends Sensor<BrickletAmbientLight> {

    public LightAmbient(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletAmbientLight) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletAmbientLight> initListener() {
        try {
            device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, (long) (value * 10)));
            device.setIlluminanceCallbackPeriod(CALLBACK_PERIOD);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> value(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> ledAdditional(final Integer value) {
        return this;
    }
}
