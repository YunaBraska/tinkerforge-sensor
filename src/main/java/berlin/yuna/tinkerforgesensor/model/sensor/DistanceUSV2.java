package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletDistanceUSV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
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
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_US_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting distance examples</h6>
 * <code>
 * stack.values().distance();
 * </code>
 */
public class DistanceUSV2 extends Sensor<BrickletDistanceUSV2> {

    public DistanceUSV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletDistanceUSV2) device, uid);
    }

    @Override
    protected Sensor<BrickletDistanceUSV2> initListener() {
        device.addDistanceListener(value -> sendEvent(DISTANCE, value * 10));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUSV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUSV2> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUSV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUSV2> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletDistanceUSV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setDistanceCallbackConfiguration(1000, false, 'x', 0, 0);
                sendEvent(DISTANCE, device.getDistance());
            } else {
                device.setDistanceCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
