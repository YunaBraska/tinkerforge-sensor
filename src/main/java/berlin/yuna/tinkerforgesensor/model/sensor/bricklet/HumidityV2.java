package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
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
 * Measures relative humidity
 * <b>Values</b>
 * HUMIDITY[%RH] = n / 100.0
 * TEMPERATURE[°C] = n / 100.0
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity_V2.html">Official doku</a>
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
