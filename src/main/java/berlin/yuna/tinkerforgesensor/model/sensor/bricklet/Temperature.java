package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * Measures ambient temperature with 0.5°C accuracy
 * <b>Values</b>
 * TEMPERATURE[°C] = n / 100.0
 */
public class Temperature extends Sensor<BrickletTemperature> {

    public Temperature(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletTemperature) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletTemperature> initListener() {
        try {
            device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
            device.setTemperatureCallbackPeriod(CALLBACK_PERIOD * 8);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> value(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> ledAdditional(final Integer value) {
        return this;
    }
}
