package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletAccelerometerV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.ACCELERATION_Z;
import static com.tinkerforge.BrickletAccelerometerV2.DATA_RATE_100HZ;
import static com.tinkerforge.BrickletAccelerometerV2.FULL_SCALE_4G;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class AccelerometerV2 extends SensorHandler<BrickletAccelerometerV2> {

    public AccelerometerV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> init() {
        config.put(THRESHOLD_PREFIX + ACCELERATION_X, 128);
        config.put(THRESHOLD_PREFIX + ACCELERATION_Y, 128);
        config.put(THRESHOLD_PREFIX + ACCELERATION_Z, 128);
        device.addAccelerationListener((x, y, z) -> {
            sendEvent(ACCELERATION_X, x);
            sendEvent(ACCELERATION_Y, y);
            sendEvent(ACCELERATION_Z, z);
        });
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> initConfig() {
        handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_LED_INFO, device.getInfoLEDConfig() == 0 ? 0 : 1);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_LED_INFO, 1, () -> device.setInfoLEDConfig(1));
        } else if (value == LED_OFF.bit) {
            applyOnNewValue(CONFIG_LED_INFO, 0, () -> device.setInfoLEDConfig(0));
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletAccelerometerV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> {
            device.setConfiguration(DATA_RATE_100HZ, FULL_SCALE_4G);
            device.setAccelerationCallbackConfiguration(milliseconds < 1 ? 0 : milliseconds, true);
        });
        return this;
    }

}
