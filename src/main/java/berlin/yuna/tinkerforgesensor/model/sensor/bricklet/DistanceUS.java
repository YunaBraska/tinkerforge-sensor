package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE;

/**
 * Measures distance between 2cm and 400cm with ultrasound
 * <b>Values</b>
 * <br />DISTANCE[cm] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_US.html">Official doku</a>
 */
public class DistanceUS extends Sensor<BrickletDistanceUS> {

    public DistanceUS(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletDistanceUS) device, uid, false);
    }

    @Override
    protected Sensor<BrickletDistanceUS> initListener() {
        device.addDistanceListener(value -> sendEvent(DISTANCE, (long) value * 10));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setDistanceCallbackPeriod(0);
                sendEvent(ALTITUDE, (long) device.getDistanceValue() * 10);
            } else {
                device.setDistanceCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
