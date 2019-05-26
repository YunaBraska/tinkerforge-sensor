package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE;

/**
 * Measures distance up to 150cm with infrared light
 * <b>Values</b>
 * <br />DISTANCE[cm] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_IR.html">Official doku</a>
 */
public class DistanceIR extends Sensor<BrickletDistanceIR> {

    public DistanceIR(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletDistanceIR) device, uid, false);
    }

    @Override
    protected Sensor<BrickletDistanceIR> initListener() {
        try {
            device.addDistanceListener(value -> sendEvent(DISTANCE, (long) value));
            device.setDistanceCallbackPeriod(CALLBACK_PERIOD);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletDistanceIR> value(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceIR> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceIR> ledAdditional(final Integer value) {
        return this;
    }
}
