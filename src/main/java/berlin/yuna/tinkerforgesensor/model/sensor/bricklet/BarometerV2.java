package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletAccelerometerV2;
import com.tinkerforge.BrickletBarometerV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_NONE;
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
        super((BrickletBarometerV2) device, uid);
    }

    @Override
    protected Sensor<BrickletBarometerV2> initListener() {
        device.addAltitudeListener(value -> sendEvent(ALTITUDE, (long) value));
        device.addAirPressureListener(value -> sendEvent(AIR_PRESSURE, (long) value));
        device.addTemperatureListener(value -> sendEvent(TEMPERATURE, (long) value));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                ledStatus = LED_STATUS_HEARTBEAT;
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                ledStatus = LED_STATUS;
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setAltitudeCallbackConfiguration(1000, true, 'x', 0, 0);
                device.setAirPressureCallbackConfiguration(1000, true, 'x', 0, 0);
                device.setTemperatureCallbackConfiguration(1000, true, 'x', 0, 0);
            } else {
                device.setAltitudeCallbackConfiguration(milliseconds, true, 'x', 0, 0);
                device.setAirPressureCallbackConfiguration(milliseconds, true, 'x', 0, 0);
                device.setTemperatureCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(ALTITUDE, (long) device.getAltitude());
            sendEvent(AIR_PRESSURE, (long) device.getAirPressure());
            sendEvent(TEMPERATURE, (long) device.getTemperature());
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletBarometerV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
