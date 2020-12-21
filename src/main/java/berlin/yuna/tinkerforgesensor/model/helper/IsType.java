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
public class IsType {
    private final ValueType valuetype;

    public IsType(final ValueType valuetype) {
        this.valuetype = valuetype;
    }

    public boolean voltageUsb() {
        return valuetype == VOLTAGE_USB;
    }

    public boolean quaternionY() {
        return valuetype == QUATERNION_Y;
    }

    public boolean cursorDragged() {
        return valuetype == CURSOR_DRAGGED;
    }

    public boolean colorB() {
        return valuetype == COLOR_B;
    }

    public boolean beepFinish() {
        return valuetype == BEEP_FINISH;
    }

    public boolean rotary() {
        return valuetype == ROTARY;
    }

    public boolean accelerationZ() {
        return valuetype == ACCELERATION_Z;
    }

    public boolean linearAccelerationY() {
        return valuetype == LINEAR_ACCELERATION_Y;
    }

    public boolean eulerAngleY() {
        return valuetype == EULER_ANGLE_Y;
    }

    public boolean environment() {
        return valuetype == ENVIRONMENT;
    }

    public boolean soundIntensity() {
        return valuetype == SOUND_INTENSITY;
    }

    public boolean magneticX() {
        return valuetype == MAGNETIC_X;
    }

    public boolean quaternionX() {
        return valuetype == QUATERNION_X;
    }

    public boolean iaqIndex() {
        return valuetype == IAQ_INDEX;
    }

    public boolean gravity() {
        return valuetype == GRAVITY;
    }

    public boolean eulerAngle() {
        return valuetype == EULER_ANGLE;
    }

    public boolean cursorPressed() {
        return valuetype == CURSOR_PRESSED;
    }

    public boolean orientation() {
        return valuetype == ORIENTATION;
    }

    public boolean motor() {
        return valuetype == MOTOR;
    }

    public boolean deviceTimeout() {
        return valuetype == DEVICE_TIMEOUT;
    }

    public boolean motionDetected() {
        return valuetype == MOTION_DETECTED;
    }

    public boolean acceleration() {
        return valuetype == ACCELERATION;
    }

    public boolean cursorClickCount() {
        return valuetype == CURSOR_CLICK_COUNT;
    }

    public boolean deviceSearch() {
        return valuetype == DEVICE_SEARCH;
    }

    public boolean keyPressed() {
        return valuetype == KEY_PRESSED;
    }

    public boolean energy() {
        return valuetype == ENERGY;
    }

    public boolean deviceAlreadyConnected() {
        return valuetype == DEVICE_ALREADY_CONNECTED;
    }

    public boolean angularVelocityY() {
        return valuetype == ANGULAR_VELOCITY_Y;
    }

    public boolean deviceConnected() {
        return valuetype == DEVICE_CONNECTED;
    }

    public boolean magnetHeading() {
        return valuetype == MAGNET_HEADING;
    }

    public boolean cursorWheelMoved() {
        return valuetype == CURSOR_WHEEL_MOVED;
    }

    public boolean soundDecibel() {
        return valuetype == SOUND_DECIBEL;
    }

    public boolean angularVelocity() {
        return valuetype == ANGULAR_VELOCITY;
    }

    public boolean cursorMoveY() {
        return valuetype == CURSOR_MOVE_Y;
    }

    public boolean keyChar() {
        return valuetype == KEY_CHAR;
    }

    public boolean linearAccelerationX() {
        return valuetype == LINEAR_ACCELERATION_X;
    }

    public boolean voltage() {
        return valuetype == VOLTAGE;
    }

    public boolean eulerAngleX() {
        return valuetype == EULER_ANGLE_X;
    }

    public boolean deviceUnknown() {
        return valuetype == DEVICE_UNKNOWN;
    }

    public boolean colorTemperature() {
        return valuetype == COLOR_TEMPERATURE;
    }

    public boolean gravityVectorX() {
        return valuetype == GRAVITY_VECTOR_X;
    }

    public boolean beep() {
        return valuetype == BEEP;
    }

    public boolean motorVelocity() {
        return valuetype == MOTOR_VELOCITY;
    }

    public boolean humidity() {
        return valuetype == HUMIDITY;
    }

    public boolean deviceStatus() {
        return valuetype == DEVICE_STATUS;
    }

    public boolean cursorReleased() {
        return valuetype == CURSOR_RELEASED;
    }

    public boolean emergencyShutdown() {
        return valuetype == EMERGENCY_SHUTDOWN;
    }

    public boolean motorPosition() {
        return valuetype == MOTOR_POSITION;
    }

    public boolean humanInput() {
        return valuetype == HUMAN_INPUT;
    }

    public boolean deviceReconnected() {
        return valuetype == DEVICE_RECONNECTED;
    }

    public boolean power() {
        return valuetype == POWER;
    }

    public boolean quaternionZ() {
        return valuetype == QUATERNION_Z;
    }

    public boolean light() {
        return valuetype == LIGHT;
    }

    public boolean magneticZ() {
        return valuetype == MAGNETIC_Z;
    }

    public boolean colorG() {
        return valuetype == COLOR_G;
    }

    public boolean accelerationY() {
        return valuetype == ACCELERATION_Y;
    }

    public boolean tilt() {
        return valuetype == TILT;
    }

    public boolean underVoltage() {
        return valuetype == UNDER_VOLTAGE;
    }

    public boolean buttonPressed() {
        return valuetype == BUTTON_PRESSED;
    }

    public boolean buttonReleased() {
        return valuetype == BUTTON_RELEASED;
    }

    public boolean ping() {
        return valuetype == PING;
    }

    public boolean percentage() {
        return valuetype == PERCENTAGE;
    }

    public boolean cursorMoved() {
        return valuetype == CURSOR_MOVED;
    }

    public boolean beepActive() {
        return valuetype == BEEP_ACTIVE;
    }

    public boolean magnetCounter() {
        return valuetype == MAGNET_COUNTER;
    }

    public boolean temperature() {
        return valuetype == TEMPERATURE;
    }

    public boolean soundSpectrum() {
        return valuetype == SOUND_SPECTRUM;
    }

    public boolean gravityVectorY() {
        return valuetype == GRAVITY_VECTOR_Y;
    }

    public boolean keyInput() {
        return valuetype == KEY_INPUT;
    }

    public boolean linearAccelerationZ() {
        return valuetype == LINEAR_ACCELERATION_Z;
    }

    public boolean angularVelocityX() {
        return valuetype == ANGULAR_VELOCITY_X;
    }

    public boolean dummy() {
        return valuetype == DUMMY;
    }

    public boolean cursorExited() {
        return valuetype == CURSOR_EXITED;
    }

    public boolean magnet() {
        return valuetype == MAGNET;
    }

    public boolean current() {
        return valuetype == CURRENT;
    }

    public boolean colorRgb() {
        return valuetype == COLOR_RGB;
    }

    public boolean sound() {
        return valuetype == SOUND;
    }

    public boolean cursorEntered() {
        return valuetype == CURSOR_ENTERED;
    }

    public boolean deviceDisconnected() {
        return valuetype == DEVICE_DISCONNECTED;
    }

    public boolean buttonTouch() {
        return valuetype == BUTTON_TOUCH;
    }

    public boolean lightUv() {
        return valuetype == LIGHT_UV;
    }

    public boolean eulerAngleZ() {
        return valuetype == EULER_ANGLE_Z;
    }

    public boolean quaternionW() {
        return valuetype == QUATERNION_W;
    }

    public boolean colorC() {
        return valuetype == COLOR_C;
    }

    public boolean orientationPitch() {
        return valuetype == ORIENTATION_PITCH;
    }

    public boolean orientationRoll() {
        return valuetype == ORIENTATION_ROLL;
    }

    public boolean magneticY() {
        return valuetype == MAGNETIC_Y;
    }

    public boolean distance() {
        return valuetype == DISTANCE;
    }

    public boolean lightUvb() {
        return valuetype == LIGHT_UVB;
    }

    public boolean colorR() {
        return valuetype == COLOR_R;
    }

    public boolean accelerationX() {
        return valuetype == ACCELERATION_X;
    }

    public boolean cursorMoveX() {
        return valuetype == CURSOR_MOVE_X;
    }

    public boolean lightUva() {
        return valuetype == LIGHT_UVA;
    }

    public boolean magnetDensity() {
        return valuetype == MAGNET_DENSITY;
    }

    public boolean button() {
        return valuetype == BUTTON;
    }

    public boolean altitude() {
        return valuetype == ALTITUDE;
    }

    public boolean keyReleased() {
        return valuetype == KEY_RELEASED;
    }

    public boolean gravityVectorZ() {
        return valuetype == GRAVITY_VECTOR_Z;
    }

    public boolean cursorInput() {
        return valuetype == CURSOR_INPUT;
    }

    public boolean colorLux() {
        return valuetype == COLOR_LUX;
    }

    public boolean quaternion() {
        return valuetype == QUATERNION;
    }

    public boolean lightLux() {
        return valuetype == LIGHT_LUX;
    }

    public boolean calibration() {
        return valuetype == CALIBRATION;
    }

    public boolean color() {
        return valuetype == COLOR;
    }

    public boolean airPressure() {
        return valuetype == AIR_PRESSURE;
    }

    public boolean all() {
        return valuetype == ALL;
    }

    public boolean orientationHeading() {
        return valuetype == ORIENTATION_HEADING;
    }

    public boolean angularVelocityZ() {
        return valuetype == ANGULAR_VELOCITY_Z;
    }
}
