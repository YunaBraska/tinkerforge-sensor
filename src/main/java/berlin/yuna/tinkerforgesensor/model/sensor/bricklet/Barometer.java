package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * Measures air pressure and altitude changes
 * <b>Values</b>
 * <br />AIR_PRESSURE[mbar] = n / 1000.0
 * <br />ALTITUDE[m] = n / 1000.0
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.html">Official doku</a>
 */
public class Barometer extends Sensor<BrickletBarometer> {

    public Barometer(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletBarometer) device, uid, false);
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
    public Sensor<BrickletBarometer> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletBarometer> ledAdditional(final Integer value) {
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
}
