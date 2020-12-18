package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletHallEffectV2;
import com.tinkerforge.Device;


/**
 * <li>{@link ValueType#MAGNETIC_X} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Y} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Z} [x = number]</li>
 * <li>{@link ValueType#MAGNET_DENSITY} [x / 10 = °]</li>
 * <li>{@link ValueType#MAGNET_COUNTER} [x = µT]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class HallEffectV2 extends SensorHandler<BrickletHallEffectV2> {

    public HallEffectV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> init() {
        config.put(THRESHOLD_PREFIX + ValueType.MAGNET_DENSITY, 16);
        device.addCounterListener(counter -> sendEvent(ValueType.MAGNET_COUNTER, counter));
        device.addMagneticFluxDensityListener(magneticFluxDensity -> sendEvent(ValueType.MAGNET_DENSITY, magneticFluxDensity));
        return setRefreshPeriod(128);
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> initConfig() {
        RunThrowable.handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletHallEffectV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            device.setCounterCallbackConfiguration(milliseconds < 1 ? 512 : milliseconds, true);
            device.setMagneticFluxDensityCallbackConfiguration(milliseconds < 1 ? 512 : milliseconds, true, 'x', 0, 0);
            sendEvent(ValueType.MAGNET_DENSITY, device.getMagneticFluxDensity());
            sendEvent(ValueType.MAGNET_COUNTER, device.getCounter(true));
        });
        return this;
    }

}
