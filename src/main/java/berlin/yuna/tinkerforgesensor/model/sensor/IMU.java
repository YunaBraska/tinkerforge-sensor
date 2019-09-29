package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ORIENTATION_HEADING;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ORIENTATION_PITCH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ORIENTATION_ROLL;

/**
 * <h3>{@link IMU}</h3><br>
 * <i>Full fledged AHRS with 9 degrees of freedom</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#ACCELERATION_X} [x - number]</li>
 * <li>{@link ValueType#ACCELERATION_Y} [x = number]</li>
 * <li>{@link ValueType#ACCELERATION_Z} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_X} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Y} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Y} [x = number]</li>
 * <li>{@link ValueType#ANGULAR_VELOCITY_X} [x = number]</li>
 * <li>{@link ValueType#ANGULAR_VELOCITY_Y} [x = number]</li>
 * <li>{@link ValueType#ANGULAR_VELOCITY_Z} [x = number]</li>
 * <li>{@link ValueType#ORIENTATION_HEADING} [x = number]</li>
 * <li>{@link ValueType#ORIENTATION_ROLL} [x = number]</li>
 * <li>{@link ValueType#ORIENTATION_PITCH} [x = number]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricks/IMU_Brick.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting acceleration x example</h6>
 * <code>sensor.values().accelerationX();</code>
 */
public class IMU extends Sensor<BrickIMU> {

    /**
     * Full fledged AHRS with 9 degrees of freedom
     */
    public IMU(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickIMU) device, uid);
    }

    @Override
    protected Sensor<BrickIMU> initListener() {
        device.addAllDataListener((accX, accY, accZ, magX, magY, magZ, angX, angY, angZ, temperature) ->
                {
                    sendEvent(ACCELERATION_X, (long) accX);
                    sendEvent(ACCELERATION_Y, (long) accY);
                    sendEvent(ACCELERATION_Z, (long) accZ);
                    sendEvent(MAGNETIC_X, (long) magX);
                    sendEvent(MAGNETIC_Y, (long) magY);
                    sendEvent(MAGNETIC_Z, (long) magZ);
                    sendEvent(ANGULAR_VELOCITY_X, (long) angX);
                    sendEvent(ANGULAR_VELOCITY_Y, (long) angY);
                    sendEvent(ANGULAR_VELOCITY_Z, (long) angZ);
                }
        );
        device.addOrientationListener((heading, roll, pitch) -> {
                    sendEvent(ORIENTATION_HEADING, (long) heading);
                    sendEvent(ORIENTATION_ROLL, (long) roll);
                    sendEvent(ORIENTATION_PITCH, (long) pitch);
                }
        );
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickIMU> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickIMU> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.enableStatusLED();
            } else if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.disableStatusLED();
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickIMU> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                ledAdditional = LED_ADDITIONAL_ON;
                device.ledsOn();
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                ledAdditional = LED_ADDITIONAL_OFF;
                device.ledsOff();
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickIMU> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setAllDataPeriod(0);
                device.setOrientationPeriod(0);
            } else {
                device.setAllDataPeriod(milliseconds);
                device.setOrientationPeriod(milliseconds);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickIMU> initLedConfig() {
        try {
            ledStatus = device.isStatusLEDEnabled() ? LED_STATUS_ON : LED_ADDITIONAL_OFF;
            ledAdditional = device.areLedsOn() ? LED_ADDITIONAL_ON : LED_ADDITIONAL_OFF;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
