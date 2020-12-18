package berlin.yuna.tinkerforgesensor.model.helper;

import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ALL;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ANGULAR_VELOCITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ANGULAR_VELOCITY_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ANGULAR_VELOCITY_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ANGULAR_VELOCITY_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP_ACTIVE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP_FINISH;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_TOUCH;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CALIBRATION;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_B;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_C;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_G;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_LUX;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_R;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_RGB;
import static berlin.yuna.tinkerforgesensor.model.ValueType.COLOR_TEMPERATURE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_CLICK_COUNT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_DRAGGED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_ENTERED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_EXITED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_INPUT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_MOVED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_MOVE_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_MOVE_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURSOR_WHEEL_MOVED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_ALREADY_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_CONNECTED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_DISCONNECTED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_RECONNECTED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_SEARCH;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_STATUS;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_UNKNOWN;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DISTANCE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DUMMY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.EMERGENCY_SHUTDOWN;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ENERGY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ENVIRONMENT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.EULER_ANGLE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.EULER_ANGLE_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.EULER_ANGLE_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.EULER_ANGLE_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.GRAVITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.GRAVITY_VECTOR_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.GRAVITY_VECTOR_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.GRAVITY_VECTOR_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.HUMAN_INPUT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.HUMIDITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.IAQ_INDEX;
import static berlin.yuna.tinkerforgesensor.model.ValueType.KEY_CHAR;
import static berlin.yuna.tinkerforgesensor.model.ValueType.KEY_INPUT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.KEY_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.KEY_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_LUX;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_UV;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_UVA;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_UVB;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LINEAR_ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LINEAR_ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LINEAR_ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNET;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNETIC_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNETIC_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNETIC_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNET_COUNTER;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNET_DENSITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNET_HEADING;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MOTION_DETECTED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MOTOR;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MOTOR_POSITION;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MOTOR_VELOCITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ORIENTATION;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ORIENTATION_HEADING;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ORIENTATION_PITCH;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ORIENTATION_ROLL;
import static berlin.yuna.tinkerforgesensor.model.ValueType.PERCENTAGE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.PING;
import static berlin.yuna.tinkerforgesensor.model.ValueType.POWER;
import static berlin.yuna.tinkerforgesensor.model.ValueType.QUATERNION;
import static berlin.yuna.tinkerforgesensor.model.ValueType.QUATERNION_W;
import static berlin.yuna.tinkerforgesensor.model.ValueType.QUATERNION_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.QUATERNION_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.QUATERNION_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ROTARY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_DECIBEL;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_INTENSITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_SPECTRUM;
import static berlin.yuna.tinkerforgesensor.model.ValueType.TEMPERATURE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.TILT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.UNDER_VOLTAGE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE_USB;

import berlin.yuna.tinkerforgesensor.model.ValueType;

@SuppressWarnings("unused")
public class ContainsValueType {
    private final ValueType valuetype;

    public ContainsValueType(final ValueType valuetype) {
        this.valuetype = valuetype;
    }

    public boolean linearAccelerationY() {
        return valuetype.contains(LINEAR_ACCELERATION_Y);
    }

    public boolean voltageUsb() {
        return valuetype.contains(VOLTAGE_USB);
    }

    public boolean emergencyShutdown() {
        return valuetype.contains(EMERGENCY_SHUTDOWN);
    }

    public boolean motor() {
        return valuetype.contains(MOTOR);
    }

    public boolean calibration() {
        return valuetype.contains(CALIBRATION);
    }

    public boolean lightUv() {
        return valuetype.contains(LIGHT_UV);
    }

    public boolean tilt() {
        return valuetype.contains(TILT);
    }

    public boolean deviceDisconnected() {
        return valuetype.contains(DEVICE_DISCONNECTED);
    }

    public boolean orientationPitch() {
        return valuetype.contains(ORIENTATION_PITCH);
    }

    public boolean cursorExited() {
        return valuetype.contains(CURSOR_EXITED);
    }

    public boolean deviceConnected() {
        return valuetype.contains(DEVICE_CONNECTED);
    }

    public boolean dummy() {
        return valuetype.contains(DUMMY);
    }

    public boolean airPressure() {
        return valuetype.contains(AIR_PRESSURE);
    }

    public boolean humanInput() {
        return valuetype.contains(HUMAN_INPUT);
    }

    public boolean eulerAngleZ() {
        return valuetype.contains(EULER_ANGLE_Z);
    }

    public boolean deviceAlreadyConnected() {
        return valuetype.contains(DEVICE_ALREADY_CONNECTED);
    }

    public boolean cursorMoveX() {
        return valuetype.contains(CURSOR_MOVE_X);
    }

    public boolean colorC() {
        return valuetype.contains(COLOR_C);
    }

    public boolean soundIntensity() {
        return valuetype.contains(SOUND_INTENSITY);
    }

    public boolean magnetCounter() {
        return valuetype.contains(MAGNET_COUNTER);
    }

    public boolean power() {
        return valuetype.contains(POWER);
    }

    public boolean motionDetected() {
        return valuetype.contains(MOTION_DETECTED);
    }

    public boolean magneticX() {
        return valuetype.contains(MAGNETIC_X);
    }

    public boolean energy() {
        return valuetype.contains(ENERGY);
    }

    public boolean orientationRoll() {
        return valuetype.contains(ORIENTATION_ROLL);
    }

    public boolean colorRgb() {
        return valuetype.contains(COLOR_RGB);
    }

    public boolean light() {
        return valuetype.contains(LIGHT);
    }

    public boolean linearAccelerationZ() {
        return valuetype.contains(LINEAR_ACCELERATION_Z);
    }

    public boolean ping() {
        return valuetype.contains(PING);
    }

    public boolean gravityVectorY() {
        return valuetype.contains(GRAVITY_VECTOR_Y);
    }

    public boolean cursorClickCount() {
        return valuetype.contains(CURSOR_CLICK_COUNT);
    }

    public boolean all() {
        return valuetype.contains(ALL);
    }

    public boolean quaternionZ() {
        return valuetype.contains(QUATERNION_Z);
    }

    public boolean cursorWheelMoved() {
        return valuetype.contains(CURSOR_WHEEL_MOVED);
    }

    public boolean colorB() {
        return valuetype.contains(COLOR_B);
    }

    public boolean percentage() {
        return valuetype.contains(PERCENTAGE);
    }

    public boolean colorG() {
        return valuetype.contains(COLOR_G);
    }

    public boolean buttonReleased() {
        return valuetype.contains(BUTTON_RELEASED);
    }

    public boolean sound() {
        return valuetype.contains(SOUND);
    }

    public boolean gravityVectorZ() {
        return valuetype.contains(GRAVITY_VECTOR_Z);
    }

    public boolean colorLux() {
        return valuetype.contains(COLOR_LUX);
    }

    public boolean cursorMoved() {
        return valuetype.contains(CURSOR_MOVED);
    }

    public boolean distance() {
        return valuetype.contains(DISTANCE);
    }

    public boolean cursorEntered() {
        return valuetype.contains(CURSOR_ENTERED);
    }

    public boolean quaternion() {
        return valuetype.contains(QUATERNION);
    }

    public boolean cursorPressed() {
        return valuetype.contains(CURSOR_PRESSED);
    }

    public boolean keyReleased() {
        return valuetype.contains(KEY_RELEASED);
    }

    public boolean quaternionW() {
        return valuetype.contains(QUATERNION_W);
    }

    public boolean orientation() {
        return valuetype.contains(ORIENTATION);
    }

    public boolean gravity() {
        return valuetype.contains(GRAVITY);
    }

    public boolean angularVelocity() {
        return valuetype.contains(ANGULAR_VELOCITY);
    }

    public boolean cursorInput() {
        return valuetype.contains(CURSOR_INPUT);
    }

    public boolean accelerationY() {
        return valuetype.contains(ACCELERATION_Y);
    }

    public boolean button() {
        return valuetype.contains(BUTTON);
    }

    public boolean gravityVectorX() {
        return valuetype.contains(GRAVITY_VECTOR_X);
    }

    public boolean environment() {
        return valuetype.contains(ENVIRONMENT);
    }

    public boolean quaternionX() {
        return valuetype.contains(QUATERNION_X);
    }

    public boolean motorPosition() {
        return valuetype.contains(MOTOR_POSITION);
    }

    public boolean deviceUnknown() {
        return valuetype.contains(DEVICE_UNKNOWN);
    }

    public boolean acceleration() {
        return valuetype.contains(ACCELERATION);
    }

    public boolean deviceReconnected() {
        return valuetype.contains(DEVICE_RECONNECTED);
    }

    public boolean underVoltage() {
        return valuetype.contains(UNDER_VOLTAGE);
    }

    public boolean soundSpectrum() {
        return valuetype.contains(SOUND_SPECTRUM);
    }

    public boolean iaqIndex() {
        return valuetype.contains(IAQ_INDEX);
    }

    public boolean buttonTouch() {
        return valuetype.contains(BUTTON_TOUCH);
    }

    public boolean lightUva() {
        return valuetype.contains(LIGHT_UVA);
    }

    public boolean colorTemperature() {
        return valuetype.contains(COLOR_TEMPERATURE);
    }

    public boolean deviceSearch() {
        return valuetype.contains(DEVICE_SEARCH);
    }

    public boolean soundDecibel() {
        return valuetype.contains(SOUND_DECIBEL);
    }

    public boolean eulerAngle() {
        return valuetype.contains(EULER_ANGLE);
    }

    public boolean current() {
        return valuetype.contains(CURRENT);
    }

    public boolean cursorMoveY() {
        return valuetype.contains(CURSOR_MOVE_Y);
    }

    public boolean lightUvb() {
        return valuetype.contains(LIGHT_UVB);
    }

    public boolean angularVelocityX() {
        return valuetype.contains(ANGULAR_VELOCITY_X);
    }

    public boolean voltage() {
        return valuetype.contains(VOLTAGE);
    }

    public boolean quaternionY() {
        return valuetype.contains(QUATERNION_Y);
    }

    public boolean altitude() {
        return valuetype.contains(ALTITUDE);
    }

    public boolean eulerAngleY() {
        return valuetype.contains(EULER_ANGLE_Y);
    }

    public boolean magnetHeading() {
        return valuetype.contains(MAGNET_HEADING);
    }

    public boolean keyChar() {
        return valuetype.contains(KEY_CHAR);
    }

    public boolean rotary() {
        return valuetype.contains(ROTARY);
    }

    public boolean magnetDensity() {
        return valuetype.contains(MAGNET_DENSITY);
    }

    public boolean eulerAngleX() {
        return valuetype.contains(EULER_ANGLE_X);
    }

    public boolean humidity() {
        return valuetype.contains(HUMIDITY);
    }

    public boolean cursorReleased() {
        return valuetype.contains(CURSOR_RELEASED);
    }

    public boolean angularVelocityY() {
        return valuetype.contains(ANGULAR_VELOCITY_Y);
    }

    public boolean deviceTimeout() {
        return valuetype.contains(DEVICE_TIMEOUT);
    }

    public boolean color() {
        return valuetype.contains(COLOR);
    }

    public boolean keyPressed() {
        return valuetype.contains(KEY_PRESSED);
    }

    public boolean linearAccelerationX() {
        return valuetype.contains(LINEAR_ACCELERATION_X);
    }

    public boolean buttonPressed() {
        return valuetype.contains(BUTTON_PRESSED);
    }

    public boolean orientationHeading() {
        return valuetype.contains(ORIENTATION_HEADING);
    }

    public boolean magneticY() {
        return valuetype.contains(MAGNETIC_Y);
    }

    public boolean motorVelocity() {
        return valuetype.contains(MOTOR_VELOCITY);
    }

    public boolean beep() {
        return valuetype.contains(BEEP);
    }

    public boolean accelerationX() {
        return valuetype.contains(ACCELERATION_X);
    }

    public boolean colorR() {
        return valuetype.contains(COLOR_R);
    }

    public boolean lightLux() {
        return valuetype.contains(LIGHT_LUX);
    }

    public boolean cursorDragged() {
        return valuetype.contains(CURSOR_DRAGGED);
    }

    public boolean keyInput() {
        return valuetype.contains(KEY_INPUT);
    }

    public boolean temperature() {
        return valuetype.contains(TEMPERATURE);
    }

    public boolean angularVelocityZ() {
        return valuetype.contains(ANGULAR_VELOCITY_Z);
    }

    public boolean beepActive() {
        return valuetype.contains(BEEP_ACTIVE);
    }

    public boolean beepFinish() {
        return valuetype.contains(BEEP_FINISH);
    }

    public boolean accelerationZ() {
        return valuetype.contains(ACCELERATION_Z);
    }

    public boolean magnet() {
        return valuetype.contains(MAGNET);
    }

    public boolean deviceStatus() {
        return valuetype.contains(DEVICE_STATUS);
    }

    public boolean magneticZ() {
        return valuetype.contains(MAGNETIC_Z);
    }
}
