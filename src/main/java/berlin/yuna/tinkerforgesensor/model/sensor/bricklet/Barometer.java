package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link Barometer}</h3><br />
 * <i>Measures air pressure and altitude changes</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#AIR_PRESSURE} [x / 1000.0 = mbar]</li>
 * <li>{@link ValueType#ALTITUDE} [x / 1000.0 = m]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.html">Official documentation</a></li>
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
public class Barometer extends Sensor<BrickletBarometer> {

    public Barometer(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletBarometer) device, uid);
    }

    @Override
    protected Sensor<BrickletBarometer> initListener() {
        refreshPeriod(CALLBACK_PERIOD * 8);
        device.addAltitudeListener(value -> sendEvent(ALTITUDE, (long) value * 10));
        device.addAirPressureListener(value -> sendEvent(AIR_PRESSURE, (long) value));
        return this;
    }

    @Override
    public Sensor<BrickletBarometer> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometer> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometer> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometer> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setAltitudeCallbackPeriod(0);
                device.setAirPressureCallbackPeriod(0);
                sendEvent(ALTITUDE, (long) device.getAltitude() * 10);
                sendEvent(AIR_PRESSURE, (long) device.getAirPressure());
            } else {
                device.setAltitudeCallbackPeriod(milliseconds);
                device.setAirPressureCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletBarometer> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }
}
