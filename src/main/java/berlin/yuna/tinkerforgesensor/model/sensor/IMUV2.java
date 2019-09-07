package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickIMUV2;
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
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CALIBRATION;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EULER_ANGLE_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EULER_ANGLE_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EULER_ANGLE_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.GRAVITY_VECTOR_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.GRAVITY_VECTOR_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.GRAVITY_VECTOR_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LINEAR_ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LINEAR_ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LINEAR_ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ORIENTATION_HEADING;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ORIENTATION_PITCH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ORIENTATION_ROLL;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.QUATERNION_W;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.QUATERNION_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.QUATERNION_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.QUATERNION_Z;

/**
 * <h3>{@link IMUV2}</h3><br />
 * <i>Full fledged AHRS with 9 degrees of freedom</i><br />
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
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricks/IMU_V2_Brick.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting acceleration x example</h6>
 * <code>sensor.values().accelerationX();</code>
 */
public class IMUV2 extends Sensor<BrickIMUV2> {

    public IMUV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickIMUV2) device, uid);
    }

    @Override
    protected Sensor<BrickIMUV2> initListener() {
        device.addAllDataListener((acceleration, magneticField, angularVelocity, eulerAngle, quaternion, linearAcceleration, gravityVector, temperature, calibrationStatus) ->
                {
                    sendEvent(ACCELERATION_X, (long) acceleration[0]);
                    sendEvent(ACCELERATION_Y, (long) acceleration[1]);
                    sendEvent(ACCELERATION_Z, (long) acceleration[2]);
                    sendEvent(MAGNETIC_X, (long) magneticField[0]);
                    sendEvent(MAGNETIC_Y, (long) magneticField[1]);
                    sendEvent(MAGNETIC_Z, (long) magneticField[2]);
                    sendEvent(ANGULAR_VELOCITY_X, (long) angularVelocity[0]);
                    sendEvent(ANGULAR_VELOCITY_Y, (long) angularVelocity[1]);
                    sendEvent(ANGULAR_VELOCITY_Z, (long) angularVelocity[2]);
                    sendEvent(EULER_ANGLE_X, (long) eulerAngle[0]);
                    sendEvent(EULER_ANGLE_Y, (long) eulerAngle[1]);
                    sendEvent(EULER_ANGLE_Z, (long) eulerAngle[2]);
                    sendEvent(QUATERNION_W, (long) quaternion[0]);
                    sendEvent(QUATERNION_X, (long) quaternion[1]);
                    sendEvent(QUATERNION_Y, (long) quaternion[2]);
                    sendEvent(QUATERNION_Z, (long) quaternion[3]);
                    sendEvent(LINEAR_ACCELERATION_X, (long) linearAcceleration[0]);
                    sendEvent(LINEAR_ACCELERATION_Y, (long) linearAcceleration[1]);
                    sendEvent(LINEAR_ACCELERATION_Z, (long) linearAcceleration[2]);
                    sendEvent(GRAVITY_VECTOR_X, (long) gravityVector[0]);
                    sendEvent(GRAVITY_VECTOR_Y, (long) gravityVector[1]);
                    sendEvent(GRAVITY_VECTOR_Z, (long) gravityVector[2]);
                    sendEvent(CALIBRATION, (long) calibrationStatus);
                    //sendEventUnchecked( TEMPERATURE, (long) temperature);
                    sendEvent(ValueType.IMU, 2L);
                }
        );
        device.addOrientationListener((heading, roll, pitch) -> {
                    sendEvent(ORIENTATION_HEADING, (long) heading);
                    sendEvent(ORIENTATION_ROLL, (long) roll);
                    sendEvent(ORIENTATION_PITCH, (long) pitch);
                    sendEvent(ValueType.IMU, 2L);
                }
        );
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickIMUV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickIMUV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickIMUV2> ledAdditional(final Integer value) {
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
    public Sensor<BrickIMUV2> refreshPeriod(final int milliseconds) {
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
    public Sensor<BrickIMUV2> initLedConfig() {
        try {
            ledStatus = device.isStatusLEDEnabled() ? LED_STATUS_ON : LED_ADDITIONAL_OFF;
            ledAdditional = device.areLedsOn() ? LED_ADDITIONAL_ON : LED_ADDITIONAL_OFF;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
