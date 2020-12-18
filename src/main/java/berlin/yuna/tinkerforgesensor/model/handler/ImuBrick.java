package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ImuBrick extends SensorHandler<BrickIMU> {


    public ImuBrick(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickIMU> send(Object value) {
        return this;
    }

    public SensorHandler<BrickIMU> init() {
        config.put(THRESHOLD_PREFIX + ValueType.ACCELERATION_X, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ACCELERATION_Y, 128);
        config.put(THRESHOLD_PREFIX + ValueType.ACCELERATION_Z, 128);
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
        device.addAllDataListener((accX, accY, accZ, magX, magY, magZ, angX, angY, angZ, temperature) ->
                {
                    sendEvent(ValueType.ACCELERATION_X, accX);
                    sendEvent(ValueType.ACCELERATION_Y, accY);
                    sendEvent(ValueType.ACCELERATION_Z, accZ);
                    sendEvent(ValueType.MAGNETIC_X, magX);
                    sendEvent(ValueType.MAGNETIC_Y, magY);
                    sendEvent(ValueType.MAGNETIC_Z, magZ);
                    sendEvent(ValueType.ANGULAR_VELOCITY_X, angX);
                    sendEvent(ValueType.ANGULAR_VELOCITY_Y, angY);
                    sendEvent(ValueType.ANGULAR_VELOCITY_Z, angZ);
                }
        );
        device.addOrientationListener((heading, roll, pitch) -> {
                    sendEvent(ValueType.ORIENTATION_HEADING, heading);
                    sendEvent(ValueType.ORIENTATION_ROLL, roll);
                    sendEvent(ValueType.ORIENTATION_PITCH, pitch);
                }
        );
        return setRefreshPeriod(256);
    }

    public SensorHandler<BrickIMU> initConfig() {
        RunThrowable.handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.isStatusLEDEnabled() ? 1 : 0);
            config.put(CONFIG_INFO_LED_STATUS, device.areLedsOn() ? 1 : 0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickIMU> runTest() {
        return super.animateStatuesLed();
    }

    @Override
    public SensorHandler<BrickIMU> setStatusLedHandler(final int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_LED_STATUS, 1, device::enableStatusLED);
        } else if (value == LED_OFF.bit) {
            applyOnNewValue(CONFIG_LED_STATUS, 0, device::disableStatusLED);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickIMU> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, device::ledsOn);
        } else if (value == LED_OFF.bit) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, device::ledsOff);
        }
        return this;
    }

    public SensorHandler<BrickIMU> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            device.setAllDataPeriod(milliseconds < 1 ? 0 : milliseconds);
            device.setOrientationPeriod(milliseconds < 1 ? 0 : milliseconds);
        });
        return this;
    }
}
