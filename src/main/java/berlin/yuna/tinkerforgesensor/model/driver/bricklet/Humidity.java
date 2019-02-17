package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.generator.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;

/**
 * Measures relative humidity
 * <b>Values</b>
 * HUMIDITY[%RH] = n / 100.0
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity.html">Official doku</a>
 */
public class Humidity extends Sensor<BrickletHumidity> {

    public Humidity(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletHumidity) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletHumidity> initListener() {
        try {
            device.addHumidityListener(value -> sendEvent(HUMIDITY, (long) value * 10));
            device.setHumidityCallbackPeriod(CALLBACK_PERIOD * 8);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletHumidity> value(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletHumidity> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletHumidity> ledAdditional(final Integer value) {
        return this;
    }
}
