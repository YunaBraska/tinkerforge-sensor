package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletAirQuality;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.HUMIDITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.IAQ_INDEX;
import static berlin.yuna.tinkerforgesensor.model.ValueType.TEMPERATURE;

/**
 * <li>{@link ValueType#IAQ_INDEX} [x = IAQ]</li>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 * <li>{@link ValueType#HUMIDITY} [x / 100.0 = %RH]</li>
 * <li>{@link ValueType#AIR_PRESSURE} [x / 1000.0 = mbar]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class AirQuality extends SensorHandler<BrickletAirQuality> {

    public AirQuality(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletAirQuality> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAirQuality> init() {
        config.put(THRESHOLD_PREFIX + IAQ_INDEX, 1);
        config.put(THRESHOLD_PREFIX + TEMPERATURE, 8);
        config.put(THRESHOLD_PREFIX + HUMIDITY, 16);
        config.put(THRESHOLD_PREFIX + AIR_PRESSURE, 32);
        device.addAllValuesListener((iaqIndex, iaqIndexAccuracy, temperature, humidity, airPressure) ->
        {
            sendEvent(IAQ_INDEX, iaqIndex);
            sendEvent(TEMPERATURE, temperature);
            sendEvent(HUMIDITY, humidity);
            sendEvent(AIR_PRESSURE, airPressure * 10);
        });
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletAirQuality> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletAirQuality> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickletAirQuality> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletAirQuality> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletAirQuality> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setAllValuesCallbackConfiguration(milliseconds < 1 ? 0 : milliseconds, true));
        return this;
    }

}
