package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
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
 * sensor.values().humidity();
 * sensor.values().humidity_Avg();
 * sensor.values().humidity_Min();
 * sensor.values().humidity_Max();
 * sensor.values().humidity_Sum();
 * </code>
 */
public class Humidity extends Sensor<BrickletHumidity> {

    public Humidity(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletHumidity) device, uid);
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
    public Sensor<BrickletHumidity> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletHumidity> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletHumidity> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
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
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
