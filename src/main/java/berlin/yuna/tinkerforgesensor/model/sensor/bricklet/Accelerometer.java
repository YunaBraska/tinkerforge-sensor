package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletAccelerometer;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link Accelerometer}</h3><br />
 * <i>Measures acceleration in three axis</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#ACCELERATION_X}</li>
 * <li>{@link ValueType#ACCELERATION_Y}</li>
 * <li>{@link ValueType#ACCELERATION_Z}</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Accelerometer.html">Official documentation</a></li>
 * </ul>
 */
public class Accelerometer extends Sensor<BrickletAccelerometer> {

    public Accelerometer(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAccelerometer) device, uid, false);
    }

    @Override
    protected Sensor<BrickletAccelerometer> initListener() {
        refreshPeriod(CALLBACK_PERIOD);
        device.addAccelerationListener((x, y, z) -> {
            sendEvent(ACCELERATION_X, (long) x);
            sendEvent(ACCELERATION_Y, (long) y);
            sendEvent(ACCELERATION_Z, (long) z);
        });
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometer> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometer> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometer> ledAdditional(final Integer value) {
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                device.ledOn();
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                device.ledOff();
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometer> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setAccelerationCallbackPeriod(0);
                final BrickletAccelerometer.Acceleration acceleration = device.getAcceleration();
                sendEvent(ACCELERATION_X, (long) acceleration.x);
                sendEvent(ACCELERATION_Y, (long) acceleration.y);
                sendEvent(ACCELERATION_Z, (long) acceleration.z);
            } else {
                device.setAccelerationCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
