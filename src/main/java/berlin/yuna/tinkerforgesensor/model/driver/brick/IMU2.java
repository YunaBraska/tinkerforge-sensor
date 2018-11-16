package berlin.yuna.tinkerforgesensor.model.driver.brick;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickIMUV2;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CALIBRATION;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EULER_ANGLE_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EULER_ANGLE_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EULER_ANGLE_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.GRAVITY_VECTOR_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.GRAVITY_VECTOR_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.GRAVITY_VECTOR_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.IMU;
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
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

public class IMU2 extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickIMUV2 device = (BrickIMUV2) sensor.device;
        registration.sensitivity(1, IMU);
        device.setAllDataPeriod(period);
        device.setOrientationPeriod(period);
        device.addAllDataListener((acceleration, magneticField, angularVelocity, eulerAngle, quaternion, linearAcceleration, gravityVector, temperature, calibrationStatus) ->
                {
                    registration.sendEvent(consumerList, ACCELERATION_X, sensor, (long) acceleration[0]);
                    registration.sendEvent(consumerList, ACCELERATION_Y, sensor, (long) acceleration[1]);
                    registration.sendEvent(consumerList, ACCELERATION_Z, sensor, (long) acceleration[2]);
                    registration.sendEvent(consumerList, MAGNETIC_X, sensor, (long) magneticField[0]);
                    registration.sendEvent(consumerList, MAGNETIC_Y, sensor, (long) magneticField[1]);
                    registration.sendEvent(consumerList, MAGNETIC_Z, sensor, (long) magneticField[2]);
                    registration.sendEvent(consumerList, ANGULAR_VELOCITY_X, sensor, (long) angularVelocity[0]);
                    registration.sendEvent(consumerList, ANGULAR_VELOCITY_Y, sensor, (long) angularVelocity[1]);
                    registration.sendEvent(consumerList, ANGULAR_VELOCITY_Z, sensor, (long) angularVelocity[2]);
                    registration.sendEvent(consumerList, EULER_ANGLE_X, sensor, (long) eulerAngle[0]);
                    registration.sendEvent(consumerList, EULER_ANGLE_Y, sensor, (long) eulerAngle[1]);
                    registration.sendEvent(consumerList, EULER_ANGLE_Z, sensor, (long) eulerAngle[2]);
                    registration.sendEvent(consumerList, QUATERNION_W, sensor, (long) quaternion[0]);
                    registration.sendEvent(consumerList, QUATERNION_X, sensor, (long) quaternion[1]);
                    registration.sendEvent(consumerList, QUATERNION_Y, sensor, (long) quaternion[2]);
                    registration.sendEvent(consumerList, QUATERNION_Z, sensor, (long) quaternion[3]);
                    registration.sendEvent(consumerList, LINEAR_ACCELERATION_X, sensor, (long) linearAcceleration[0]);
                    registration.sendEvent(consumerList, LINEAR_ACCELERATION_Y, sensor, (long) linearAcceleration[1]);
                    registration.sendEvent(consumerList, LINEAR_ACCELERATION_Z, sensor, (long) linearAcceleration[2]);
                    registration.sendEvent(consumerList, GRAVITY_VECTOR_X, sensor, (long) gravityVector[0]);
                    registration.sendEvent(consumerList, GRAVITY_VECTOR_Y, sensor, (long) gravityVector[1]);
                    registration.sendEvent(consumerList, GRAVITY_VECTOR_Z, sensor, (long) gravityVector[2]);
                    registration.sendEvent(consumerList, CALIBRATION, sensor, (long) calibrationStatus);
                    registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) temperature);
                    registration.sendEvent(consumerList, IMU, sensor, 2L);
                }
        );
        device.addOrientationListener((heading, roll, pitch) -> {
                    registration.sendEvent(consumerList, ORIENTATION_HEADING, sensor, (long) heading);
                    registration.sendEvent(consumerList, ORIENTATION_ROLL, sensor, (long) roll);
                    registration.sendEvent(consumerList, ORIENTATION_PITCH, sensor, (long) pitch);
                    registration.sendEvent(consumerList, IMU, sensor, 2L);
                }
        );

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {device.enableStatusLED();}
                    else if (i == LED_STATUS_OFF.bit) {device.disableStatusLED();}
                },
                i -> {
                    if (i == LED_ADDITIONAL_ON.bit) {device.enableStatusLED();}
                    else if (i == LED_ADDITIONAL_OFF.bit) {device.disableStatusLED();}
                },
                ignore -> { }));
    }
}
