package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * <h3>{@link Temperature}</h3><br />
 * <i>Measures ambient temperature with 0.5°C accuracy</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Temperature.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting temperature examples</h6>
 * <code>
 * sensor.values().temperature();
 * sensor.values().temperature_Avg();
 * sensor.values().temperature_Min();
 * sensor.values().temperature_Max();
 * sensor.values().temperature_Sum();
 * </code>
 */
public class Temperature extends Sensor<BrickletTemperature> {

    public Temperature(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletTemperature) device, uid);
    }

    @Override
    protected Sensor<BrickletTemperature> initListener() {
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletTemperature> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setTemperatureCallbackPeriod(1000);
            } else {
                device.setTemperatureCallbackPeriod(milliseconds);
            }
            sendEvent(TEMPERATURE, (long) (device.getTemperature()));
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
