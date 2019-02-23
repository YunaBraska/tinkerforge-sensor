package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TILT;

/**
 * Detects inclination of Bricklet (tilt switch open/closed)
 * <b>Values</b>
 * <br />TILT[012] = closed/open/vibrating
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Tilt.html">Official doku</a>
 */
public class Tilt extends Sensor<BrickletTilt> {

    public Tilt(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletTilt) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletTilt> initListener() {
        try {
            device.addTiltStateListener(value -> sendEvent(TILT, (long) value));
            device.enableTiltStateCallback();
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletTilt> value(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletTilt> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTilt> ledAdditional(final Integer value) {
        return this;
    }
}
