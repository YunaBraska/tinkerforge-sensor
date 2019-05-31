package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TILT;

/**
 * Detects inclination of Bricklet (tilt switch open/closed)
 * <b>Values</b>
 * <br />TILT[012] = closed/open/vibrating
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Tilt.html">Official documentation</a>
 */
public class Tilt extends Sensor<BrickletTilt> {

    public Tilt(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletTilt) device, uid, false);
    }

    @Override
    protected Sensor<BrickletTilt> initListener() {
        device.addTiltStateListener(value -> sendEvent(TILT, (long) value));
        refreshPeriod(69);
        return this;
    }

    @Override
    public Sensor<BrickletTilt> send(final Object value) {
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

    @Override
    public Sensor<BrickletTilt> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.disableTiltStateCallback();
                sendEvent(TEMPERATURE, (long) (device.getTiltState()));
            } else {
                device.enableTiltStateCallback();
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
