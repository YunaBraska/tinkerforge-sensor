package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletTemperatureV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * <h3>{@link TemperatureV2}</h3><br />
 * <i>Measures ambient temperature with 0.2°C accuracy</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Temperature_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting temperature examples</h6>
 * <code>
 * stack.values().temperature();
 * stack.values().temperature_Avg();
 * stack.values().temperature_Min();
 * stack.values().temperature_Max();
 * stack.values().temperature_Sum();
 * </code>
 */
public class TemperatureV2 extends Sensor<BrickletTemperatureV2> {

    public TemperatureV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletTemperatureV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletTemperatureV2> initListener() {
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
        refreshPeriod(-1);
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

    @Override
    public Sensor<BrickletTemperatureV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setTemperatureCallbackConfiguration(1000, false, 'x', 0, 0);
            } else {
                device.setTemperatureCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(TEMPERATURE, (long) device.getTemperature());
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
