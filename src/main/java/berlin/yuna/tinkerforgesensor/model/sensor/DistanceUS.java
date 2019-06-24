package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE;

/**
 * <h3>{@link DistanceIR}</h3><br />
 * <i>Measures distance between 2cm and 400cm with ultrasound</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#DISTANCE} [x / 10.0 = cm]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_US.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting distance examples</h6>
 * <code>
 * stack.values().distance();
 * stack.values().distance();
 * stack.values().distance();
 * stack.values().distance();
 * stack.values().distance();
 * </code>
 */
public class DistanceUS extends Sensor<BrickletDistanceUS> {

    public DistanceUS(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletDistanceUS) device, uid);
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
    public Sensor<BrickletDistanceUS> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUS> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
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
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
