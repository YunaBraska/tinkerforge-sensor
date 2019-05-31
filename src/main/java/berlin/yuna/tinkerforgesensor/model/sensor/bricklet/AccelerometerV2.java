package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletAccelerometerV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
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
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Accelerometer_V2.html">Official documentation</a></li>
 * </ul>
 */
public class AccelerometerV2 extends Sensor<BrickletAccelerometerV2> {

    public AccelerometerV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAccelerometerV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletAccelerometerV2> initListener() {
        refreshPeriod(-1);
        device.addAccelerationListener((x, y, z) -> {
            sendEvent(ACCELERATION_X, (long) x);
            sendEvent(ACCELERATION_Y, (long) y);
            sendEvent(ACCELERATION_Z, (long) z);
        });
        return this;


    }

    @Override
    public Sensor<BrickletAccelerometerV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometerV2> ledStatus(final Integer value) {
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
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometerV2> ledAdditional(final Integer value) {
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                device.setInfoLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                device.setInfoLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_ADDITIONAL_HEARTBEAT.bit) {
                device.setInfoLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletAccelerometerV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setAccelerationCallbackConfiguration(1000, true);
                final BrickletAccelerometerV2.Acceleration acceleration = device.getAcceleration();
                sendEvent(ACCELERATION_X, (long) acceleration.x);
                sendEvent(ACCELERATION_Y, (long) acceleration.y);
                sendEvent(ACCELERATION_Z, (long) acceleration.z);
            } else {
                device.setAccelerationCallbackConfiguration(milliseconds, false);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
