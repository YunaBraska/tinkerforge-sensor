package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletUVLightV2;
import com.tinkerforge.Device;

/**
 * <li>{@link ValueType#LIGHT_UV} [x / 10.0 = index]</li>
 * <li>{@link ValueType#LIGHT_UVA} [x / 10.0 = mW/m²]</li>
 * <li>{@link ValueType#LIGHT_UVB} [x / 10.0 = mW/m²]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightUvV2 extends SensorHandler<BrickletUVLightV2> {

    public LightUvV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletUVLightV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLightV2> init() {
        config.put(THRESHOLD_PREFIX + ValueType.LIGHT_UV, 8);
        config.put(THRESHOLD_PREFIX + ValueType.LIGHT_UVA, 32);
        config.put(THRESHOLD_PREFIX + ValueType.LIGHT_UVB, 32);
        device.addUVIListener(value -> sendEvent(ValueType.LIGHT_UV, value));
        device.addUVAListener(value -> sendEvent(ValueType.LIGHT_UVA, value));
        device.addUVBListener(value -> sendEvent(ValueType.LIGHT_UVB, value));
        return setRefreshPeriod(32);
    }

    @Override
    public SensorHandler<BrickletUVLightV2> initConfig() {
        RunThrowable.handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLightV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLightV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLightV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLightV2> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> {
            device.setUVACallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
            device.setUVBCallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
            device.setUVICallbackConfiguration(milliseconds < 1 ? 1000 : milliseconds, true, 'x', 0, 0);
        });
        return this;
    }

}
