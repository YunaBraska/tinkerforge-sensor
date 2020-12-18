package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.util.RunThrowable;
import com.tinkerforge.BrickletUVLight;
import com.tinkerforge.Device;

/**
 * <li>{@link ValueType#LIGHT_UV} [x / 10.0 = index]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightUv extends SensorHandler<BrickletUVLight> {

    public LightUv(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletUVLight> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLight> init() {
        config.put(THRESHOLD_PREFIX + ValueType.LIGHT_UV, 8);
        device.addUVLightListener(value -> sendEvent(ValueType.LIGHT_UV, value));
        return setRefreshPeriod(256);
    }

    @Override
    public SensorHandler<BrickletUVLight> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLight> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLight> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLight> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletUVLight> setRefreshPeriod(final int milliseconds) {
        RunThrowable.handleConnection(() -> device.setUVLightCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds));
        return this;
    }

}
