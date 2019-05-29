package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletBarometerV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * Measures air pressure and altitude changes
 * <b>Values</b>
 * <br />AIR_PRESSURE[mbar] = n / 1000.0
 * <br />ALTITUDE[m] = n / 1000.0
 * <br />TEMPERATURE[°C] = n / 100.0
 * <br /><a https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer_V2.html">Official doku</a>
 */
public class BarometerV2 extends Sensor<BrickletBarometerV2> {

    public BarometerV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletBarometerV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletBarometerV2> initListener() {
        device.addAltitudeListener(value -> sendEvent(ALTITUDE, (long) value));
        device.addAirPressureListener(value -> sendEvent(AIR_PRESSURE, (long) value));
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig(LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig(LED_STATUS.bit);
            } else if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig(LED_STATUS_OFF.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setAltitudeCallbackConfiguration(0, true, 'x', 0, 0);
                device.setAirPressureCallbackConfiguration(0, true, 'x', 0, 0);
                device.setTemperatureCallbackConfiguration(0, true, 'x', 0, 0);

                sendEvent(ALTITUDE, (long) device.getAltitude());
                sendEvent(AIR_PRESSURE, (long) device.getAirPressure());
                sendEvent(TEMPERATURE, (long) device.getTemperature());
            } else {
                device.setAltitudeCallbackConfiguration(milliseconds, false, 'x', 0, 0);
                device.setAirPressureCallbackConfiguration(milliseconds, false, 'x', 0, 0);
                device.setTemperatureCallbackConfiguration(milliseconds, false, 'x', 0, 0);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
