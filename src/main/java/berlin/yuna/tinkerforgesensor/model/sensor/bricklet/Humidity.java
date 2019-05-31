package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;

/**
 * Measures relative humidity
 * <b>Values</b>
 * HUMIDITY[%RH] = n / 100.0
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity.html">Official documentation</a>
 */
public class Humidity extends Sensor<BrickletHumidity> {

    public Humidity(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletHumidity) device, uid, false);
    }

    @Override
    protected Sensor<BrickletHumidity> initListener() {
        device.addHumidityListener(value -> sendEvent(HUMIDITY, (long) value * 10));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletHumidity> send(final Object value) {
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

    @Override
    public Sensor<BrickletHumidity> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setHumidityCallbackPeriod(0);
                sendEvent(HUMIDITY, (long) device.getHumidity() * 10);
            } else {
                device.setHumidityCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
