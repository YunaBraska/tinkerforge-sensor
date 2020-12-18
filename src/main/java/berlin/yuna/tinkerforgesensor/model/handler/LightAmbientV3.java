package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletAmbientLightV3;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_LUX;

/**
 * <li>{@link ValueType#LIGHT_LUX} [x / 100.0 = lx]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightAmbientV3 extends SensorHandler<BrickletAmbientLightV3> {

    public LightAmbientV3(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> init() {
        config.put(THRESHOLD_PREFIX + LIGHT_LUX, 8);
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value));
        return setRefreshPeriod(16);
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV3> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setIlluminanceCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0));
        return this;
    }

}
