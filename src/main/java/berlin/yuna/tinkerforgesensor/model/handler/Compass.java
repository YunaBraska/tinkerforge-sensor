package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletCompass;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNETIC_X;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNETIC_Y;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNETIC_Z;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MAGNET_HEADING;
import static com.tinkerforge.BrickletCompass.DATA_RATE_100HZ;

/**
 * <li>{@link ValueType#MAGNETIC_X} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Y} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Z} [x = number]</li>
 * <li>{@link ValueType#MAGNET_HEADING} [x / 10 = °]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Compass extends SensorHandler<BrickletCompass> {

    public Compass(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletCompass> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletCompass> init() {
        config.put(THRESHOLD_PREFIX + MAGNETIC_X, 128);
        config.put(THRESHOLD_PREFIX + MAGNETIC_Y, 128);
        config.put(THRESHOLD_PREFIX + MAGNETIC_Z, 128);
        config.put(THRESHOLD_PREFIX + MAGNET_HEADING, 128);
        device.addHeadingListener(heading -> sendEvent(MAGNET_HEADING, heading));
        device.addMagneticFluxDensityListener((x, y, z) -> {
            sendEvent(MAGNETIC_X, x);
            sendEvent(MAGNETIC_Y, y);
            sendEvent(MAGNETIC_Z, z);
        });
        return setRefreshPeriod(100);
    }

    @Override
    public SensorHandler<BrickletCompass> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletCompass> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletCompass> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletCompass> triggerFunctionA(int value) {

        return this;
    }

    @Override
    public SensorHandler<BrickletCompass> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> {
            device.setConfiguration(DATA_RATE_100HZ, true);
            if (milliseconds < 1) {
                device.setHeadingCallbackConfiguration(4, true, 'x', 0, 0);
                device.setMagneticFluxDensityCallbackConfiguration(4, true);
            } else {
                device.setHeadingCallbackConfiguration(milliseconds, true, 'x', 0, 0);
                device.setMagneticFluxDensityCallbackConfiguration(milliseconds, true);
            }
        });
        return this;
    }

}
