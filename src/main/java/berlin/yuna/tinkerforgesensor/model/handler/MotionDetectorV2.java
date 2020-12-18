package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletMotionDetectorV2;
import com.tinkerforge.Device;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MotionDetectorV2 extends SensorHandler<BrickletMotionDetectorV2> {

    public MotionDetectorV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> init() {
        device.addMotionDetectedListener(() -> sendEvent(ValueType.MOTION_DETECTED, 1L));
        device.addDetectionCycleEndedListener(() -> sendEvent(ValueType.MOTION_DETECTED, 0L));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> initConfig() {
        RunThrowable.handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig((short) value));
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMotionDetectorV2> setRefreshPeriod(final int sensitivity) {
        RunThrowable.handleConnection(() -> {
            device.setSensitivity(sensitivity < 1 ? 100 : (sensitivity / 10) + 1);
            device.setSensitivity((sensitivity / 10) + 1);
        });
        return this;
    }

}
