package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TILT;

/**
 * <h3>{@link Tilt}</h3><br>
 * <i>Detects inclination of Bricklet (tilt switch open/closed)</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#TILT} [0/1/2 = closed/open/vibrating]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Tilt.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting tilt examples</h6>
 * <code>sensor.values().tilt();</code>
 */
public class Tilt extends Sensor<BrickletTilt> {

    public Tilt(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletTilt) device, uid);
    }

    @Override
    protected Sensor<BrickletTilt> initListener() {
        device.addTiltStateListener(value -> sendEvent(TILT, (long) value));
        refreshPeriod(69);
        return this;
    }

    public long getTilt(){
        return getValue(TILT, -1, -1).intValue();
    }

    @Override
    public Sensor<BrickletTilt> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletTilt> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTilt> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTilt> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletTilt> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.disableTiltStateCallback();
                sendEvent(TILT, (long) (device.getTiltState()));
            } else {
                device.enableTiltStateCallback();
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
