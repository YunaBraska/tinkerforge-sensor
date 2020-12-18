package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletAccelerometer;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Accelerometer extends SensorHandler<BrickletAccelerometer> {

    public Accelerometer(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletAccelerometer> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometer> init() {
        config.put(THRESHOLD_PREFIX + ACCELERATION_X, 128);
        config.put(THRESHOLD_PREFIX + ACCELERATION_Y, 128);
        config.put(THRESHOLD_PREFIX + ACCELERATION_Z, 128);
        device.addAccelerationListener((x, y, z) -> {
            sendEvent(ACCELERATION_X, x);
            sendEvent(ACCELERATION_Y, y);
            sendEvent(ACCELERATION_Z, z);
        });
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickletAccelerometer> initConfig() {
        handleConnection(() -> config.put(CONFIG_INFO_LED_STATUS, device.isLEDOn() ? 1 : 0));
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometer> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometer> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometer> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, device::ledOn);
        } else if (value == LED_OFF.bit) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, device::ledOn);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometer> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setAccelerationCallbackPeriod(milliseconds < 1 ? 0 : milliseconds));
        return this;
    }

}
