package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletHumidityV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * <h3>{@link Humidity}</h3><br />
 * <i>Measures relative humidity</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#HUMIDITY} [x / 100.0 = %RH]</li>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity_V2.html">Official documentation</a></li>
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
public class HumidityV2 extends Sensor<BrickletHumidityV2> {

    public HumidityV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletHumidityV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletHumidityV2> initListener() {
        device.addHumidityListener(value -> sendEvent(HUMIDITY, (long) value));
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
        refreshPeriod(CALLBACK_PERIOD * 8);
        return this;
    }

    @Override
    public Sensor<BrickletHumidityV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletHumidityV2> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletHumidityV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletHumidityV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setTemperatureCallbackConfiguration(0, true, 'x', 0, 0);
                device.setHumidityCallbackConfiguration(0, true, 'x', 0, 0);
                sendEvent(HUMIDITY, (long) device.getHumidity());
                sendEvent(TEMPERATURE, (long) device.getTemperature());
            } else {
                device.setTemperatureCallbackConfiguration(milliseconds, false, 'x', 0, 0);
                device.setHumidityCallbackConfiguration(milliseconds, false, 'x', 0, 0);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
