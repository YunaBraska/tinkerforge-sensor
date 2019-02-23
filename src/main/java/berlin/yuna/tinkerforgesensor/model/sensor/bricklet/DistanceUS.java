package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE;

/**
 * Measures distance between 2cm and 400cm with ultrasound
 * <b>Values</b>
 * <br />DISTANCE[cm] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_US.html">Official doku</a>
 */
public class DistanceUS extends Sensor<BrickletDistanceUS> {

    public DistanceUS(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletDistanceUS) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletDistanceUS> initListener() {
        try {
            device.addDistanceListener(value -> sendEvent(DISTANCE, (long) value * 10));
            device.setDistanceCallbackPeriod(CALLBACK_PERIOD);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> value(final Object value) {
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
}
