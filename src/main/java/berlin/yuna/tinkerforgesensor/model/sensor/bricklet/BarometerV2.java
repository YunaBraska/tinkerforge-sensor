package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletBarometerV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

/**
 * <h3>{@link BarometerV2}</h3><br />
 * <i>Measures air pressure and altitude changes</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#AIR_PRESSURE} [x / 1000.0 = mbar]</li>
 * <li>{@link ValueType#ALTITUDE} [x / 1000.0 = m]</li>
 * <li>{@link ValueType#TEMPERATURE} [x / 100.0 = °C]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.htm">Official documentation</a></li>
 * </ul>
 * <h6>Getting air pressure examples</h6>
 * <code>
 * stack.values().airPressure();
 * stack.values().airPressure_Avg();
 * stack.values().airPressure_Min();
 * stack.values().airPressure_Max();
 * stack.values().airPressure_Sum();
 * </code>
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
