package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletTemperatureV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * Measures ambient temperature with 0.2°C accuracy
 * <b>Values</b>
 * TEMPERATURE[°C] = n / 100.0
 */
public class TemperatureV2 extends Sensor<BrickletTemperatureV2> {

    public TemperatureV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletTemperatureV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletTemperatureV2> initListener() {
        try {
            device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
            device.setTemperatureCallbackConfiguration(CALLBACK_PERIOD * 8, false, 'x', 0, 0);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletTemperatureV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperatureV2> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperatureV2> ledAdditional(final Integer value) {
        return this;
    }
}
