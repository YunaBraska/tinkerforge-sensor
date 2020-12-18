package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.LIGHT_LUX;

/**
 * <li>{@link ValueType#LIGHT_LUX} [x / 100.0 = lx]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LightAmbientV2 extends SensorHandler<BrickletAmbientLightV2> {

    public LightAmbientV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> init() {
        config.put(THRESHOLD_PREFIX + LIGHT_LUX, 16);
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value));
        return setRefreshPeriod(128);
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAmbientLightV2> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> handleConnection(() -> device.setIlluminanceCallbackPeriod(milliseconds < 1 ? 1000 : milliseconds)));
        return this;
    }

}
