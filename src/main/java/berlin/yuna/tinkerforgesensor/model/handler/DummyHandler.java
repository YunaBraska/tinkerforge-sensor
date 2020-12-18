package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.Device;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DummyHandler extends SensorHandler<Object> {

    public final Map<String, Number> triggers = new ConcurrentHashMap<>();

    public DummyHandler(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<Object> send(Object value) {
        triggers.put("method_send", 1);
        return this;
    }

    public SensorHandler<Object> init() {
        config.put(CONFIG_LED_STATUS, 1);
        triggers.put("method_initListener", 1);
        return this;
    }

    public SensorHandler<Object> initConfig() {
        triggers.put("method_initLedStatus", 1);
        return this;
    }

    @Override
    public SensorHandler<Object> runTest() {
        triggers.put("method_runTest", 1);
        return this;
    }

    @Override
    public SensorHandler<Object> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> triggers.put("method_setStatusLed", value));
        return this;
    }

    @Override
    public SensorHandler<Object> triggerFunctionA(int value) {
        applyOnNewValue(CONFIG_FUNCTION_A, value, () -> triggers.put("method_setSecondLed", value));
        applyOnNewValue(CONFIG_BRIGHTNESS, value, () -> triggers.put("method_setBrightness", value));
        return this;
    }

    public SensorHandler<Object> setRefreshPeriod(final int milliseconds) {
        triggers.put("method_setRefreshPeriod", milliseconds);
        return this;
    }
}
