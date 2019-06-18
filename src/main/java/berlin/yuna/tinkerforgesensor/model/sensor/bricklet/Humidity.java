package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;

/**
 * <h3>{@link Humidity}</h3><br />
 * <i>Measures relative humidity</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#HUMIDITY} [x / 100.0 = %RH]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting humidity examples</h6>
 * <code>
 * stack.values().humidity();
 * stack.values().humidity_Avg();
 * stack.values().humidity_Min();
 * stack.values().humidity_Max();
 * stack.values().humidity_Sum();
 * </code>
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
                device.setHumidityCallbackPeriod(1000);
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
