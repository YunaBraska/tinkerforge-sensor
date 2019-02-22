package berlin.yuna.tinkerforgesensor.model.driver.brick;

import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
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

public class IMU extends Sensor<BrickIMU> {

    /**
     * Full fledged AHRS with 9 degrees of freedom
     */
    public IMU(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickIMU) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickIMU> initListener() throws TimeoutException, NotConnectedException {
        device.setAllDataPeriod(CALLBACK_PERIOD);
        device.setOrientationPeriod(CALLBACK_PERIOD);
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
                    //sendEvent( TEMPERATURE, (long) temperature);
                    sendEvent(ValueType.IMU, 1L);
                }
        );
        device.addOrientationListener((heading, roll, pitch) -> {
                    sendEvent(ORIENTATION_HEADING, (long) heading);
                    sendEvent(ORIENTATION_ROLL, (long) roll);
                    sendEvent(ORIENTATION_PITCH, (long) pitch);
                    sendEvent(ValueType.IMU, 1L);
                }
        );
        return this;
    }

    @Override
    public Sensor<BrickIMU> value(Object value) {
        return this;
    }

    @Override
    public Sensor<BrickIMU> ledStatus(Integer value) {
        try {
            if (value == LED_STATUS_ON.bit) {
                device.enableStatusLED();
            } else if (value == LED_STATUS_OFF.bit) {
                device.disableStatusLED();
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickIMU> ledAdditional(Integer value) {
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                device.enableStatusLED();
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                device.disableStatusLED();
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
