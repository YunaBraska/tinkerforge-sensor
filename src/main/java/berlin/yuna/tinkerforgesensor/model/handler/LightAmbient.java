package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_LUX;

/**
 * <li>{@link ValueType#LIGHT_LUX} [x / 100.0 = lx]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightAmbient extends SensorHandler<BrickletAmbientLight> {

    public LightAmbient(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletAmbientLight> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLight> init() {
        config.put(THRESHOLD_PREFIX + LIGHT_LUX, 16);
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value * 10));
        return setRefreshPeriod(128);
    }

    @Override
    public SensorHandler<BrickletAmbientLight> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLight> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLight> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLight> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLight> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setIlluminanceCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds));
        return this;
    }

}
