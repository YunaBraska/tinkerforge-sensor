package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_INTENSITY;

/**
 * <li>{@link ValueType#SOUND_INTENSITY} [x / 100 = db]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class SoundIntensity extends SensorHandler<BrickletSoundIntensity> {

    public SoundIntensity(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> init() {
        config.put(THRESHOLD_PREFIX + SOUND_INTENSITY, 32);
        device.addIntensityListener(value -> sendEvent(SOUND_INTENSITY, value * 10));
        return setRefreshPeriod(512);
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletSoundIntensity> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setIntensityCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds));
        return this;
    }

}
