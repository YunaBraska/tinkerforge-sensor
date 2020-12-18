package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickIMUV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ImuBrickV2 extends SensorHandler<BrickIMUV2> {


    public ImuBrickV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickIMUV2> send(Object value) {
        return this;
    }

    public SensorHandler<BrickIMUV2> init() {
        config.put(THRESHOLD_PREFIX + ValueType.EULER_ANGLE_X, 16);
        config.put(THRESHOLD_PREFIX + ValueType.EULER_ANGLE_Y, 16);
        config.put(THRESHOLD_PREFIX + ValueType.EULER_ANGLE_Z, 16);
        config.put(THRESHOLD_PREFIX + ValueType.QUATERNION_W, 128);
        config.put(THRESHOLD_PREFIX + ValueType.QUATERNION_X, 128);
        config.put(THRESHOLD_PREFIX + ValueType.QUATERNION_Y, 128);
        config.put(THRESHOLD_PREFIX + ValueType.QUATERNION_Z, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ACCELERATION_X, 8);
        config.put(THRESHOLD_PREFIX + ValueType.ACCELERATION_Y, 8);
        config.put(THRESHOLD_PREFIX + ValueType.ACCELERATION_Z, 8);
        config.put(THRESHOLD_PREFIX + ValueType.LINEAR_ACCELERATION_X, 128);
        config.put(THRESHOLD_PREFIX + ValueType.LINEAR_ACCELERATION_Y, 128);
        config.put(THRESHOLD_PREFIX + ValueType.LINEAR_ACCELERATION_Z, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ANGULAR_VELOCITY_X, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ANGULAR_VELOCITY_Y, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ANGULAR_VELOCITY_Z, 128);
        config.put(THRESHOLD_PREFIX + ValueType.MAGNETIC_X, 128);
        config.put(THRESHOLD_PREFIX + ValueType.MAGNETIC_Y, 128);
        config.put(THRESHOLD_PREFIX + ValueType.MAGNETIC_Z, 128);
        config.put(THRESHOLD_PREFIX + ValueType.MAGNET_HEADING, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ORIENTATION_PITCH, 24);
        config.put(THRESHOLD_PREFIX + ValueType.ORIENTATION_ROLL, 24);
        config.put(THRESHOLD_PREFIX + ValueType.ORIENTATION_HEADING, 16);
        config.put(THRESHOLD_PREFIX + ValueType.CALIBRATION, 16);
        config.put(THRESHOLD_PREFIX + ValueType.GRAVITY_VECTOR_X, 16);
        config.put(THRESHOLD_PREFIX + ValueType.GRAVITY_VECTOR_Y, 16);
        config.put(THRESHOLD_PREFIX + ValueType.GRAVITY_VECTOR_Z, 16);
        device.addAllDataListener((acceleration, magneticField, angularVelocity, eulerAngle, quaternion, linearAcceleration, gravityVector, temperature, calibrationStatus) ->
                {
                    sendEvent(ValueType.ACCELERATION_X, acceleration[0]);
                    sendEvent(ValueType.ACCELERATION_Y, acceleration[1]);
                    sendEvent(ValueType.ACCELERATION_Z, acceleration[2]);
                    sendEvent(ValueType.MAGNETIC_X, magneticField[0]);
                    sendEvent(ValueType.MAGNETIC_Y, magneticField[1]);
                    sendEvent(ValueType.MAGNETIC_Z, magneticField[2]);
                    sendEvent(ValueType.ANGULAR_VELOCITY_X, angularVelocity[0]);
                    sendEvent(ValueType.ANGULAR_VELOCITY_Y, angularVelocity[1]);
                    sendEvent(ValueType.ANGULAR_VELOCITY_Z, angularVelocity[2]);
                    sendEvent(ValueType.EULER_ANGLE_X, eulerAngle[0]);
                    sendEvent(ValueType.EULER_ANGLE_Y, eulerAngle[1]);
                    sendEvent(ValueType.EULER_ANGLE_Z, eulerAngle[2]);
                    sendEvent(ValueType.QUATERNION_W, quaternion[0]);
                    sendEvent(ValueType.QUATERNION_X, quaternion[1]);
                    sendEvent(ValueType.QUATERNION_Y, quaternion[2]);
                    sendEvent(ValueType.QUATERNION_Z, quaternion[3]);
                    sendEvent(ValueType.LINEAR_ACCELERATION_X, linearAcceleration[0]);
                    sendEvent(ValueType.LINEAR_ACCELERATION_Y, linearAcceleration[1]);
                    sendEvent(ValueType.LINEAR_ACCELERATION_Z, linearAcceleration[2]);
                    sendEvent(ValueType.GRAVITY_VECTOR_X, gravityVector[0]);
                    sendEvent(ValueType.GRAVITY_VECTOR_Y, gravityVector[1]);
                    sendEvent(ValueType.GRAVITY_VECTOR_Z, gravityVector[2]);
                    sendEvent(ValueType.CALIBRATION, calibrationStatus);
                }
        );
        device.addOrientationListener((heading, roll, pitch) -> {
                    sendEvent(ValueType.ORIENTATION_HEADING, heading);
                    sendEvent(ValueType.ORIENTATION_ROLL, roll);
                    sendEvent(ValueType.ORIENTATION_PITCH, pitch);
                }
        );
        return setRefreshPeriod(512);
    }

    public SensorHandler<BrickIMUV2> initConfig() {
        RunThrowable.handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.isStatusLEDEnabled() ? 1 : 0);
            config.put(CONFIG_INFO_LED_STATUS, device.areLedsOn() ? 1 : 0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickIMUV2> runTest() {
        return super.animateStatuesLed();
    }

    @Override
    public SensorHandler<BrickIMUV2> setStatusLedHandler(final int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_LED_STATUS, 1, device::enableStatusLED);
        } else if (value == LedStatusType.LED_OFF.bit) {
            applyOnNewValue(CONFIG_LED_STATUS, 0, device::disableStatusLED);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickIMUV2> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, device::ledsOn);
        } else if (value == LedStatusType.LED_OFF.bit) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, device::ledsOff);
        }
        return this;
    }

    public SensorHandler<BrickIMUV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            device.setAllDataPeriod(milliseconds < 1 ? 0 : milliseconds);
            device.setOrientationPeriod(milliseconds < 1 ? 0 : milliseconds);
        });
        return this;
    }
}
