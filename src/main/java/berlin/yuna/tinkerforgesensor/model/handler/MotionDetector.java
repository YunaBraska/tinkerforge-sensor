package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.Device;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MotionDetector extends SensorHandler<BrickletMotionDetector> {

    public MotionDetector(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletMotionDetector> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetector> init() {
        device.addMotionDetectedListener(() -> sendEvent(ValueType.MOTION_DETECTED, 1L));
        device.addDetectionCycleEndedListener(() -> sendEvent(ValueType.MOTION_DETECTED, 0L));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletMotionDetector> initConfig() {
        RunThrowable.handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetector> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetector> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig((short) value));
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetector> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetector> setRefreshPeriod(final int milliseconds) {
        return this;
    }

}
