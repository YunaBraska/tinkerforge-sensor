package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletDistanceIRV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE;

/**
 * Measures distance up to 150cm with infrared light
 * <b>Values</b>
 * <br />DISTANCE[cm] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_IR_V2.html">Official doku</a>
 */
public class DistanceIRV2 extends Sensor<BrickletDistanceIRV2> {

    public DistanceIRV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletDistanceIRV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletDistanceIRV2> initListener() {
        device.addDistanceListener(value -> sendEvent(DISTANCE, (long) value));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletDistanceIRV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceIRV2> ledStatus(final Integer value) {
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
    public Sensor<BrickletDistanceIRV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletDistanceIRV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setDistanceCallbackConfiguration(0, true, 'x', 0, 0);
                sendEvent(ALTITUDE, (long) device.getDistance());
            } else {
                device.setDistanceCallbackConfiguration(milliseconds, false, 'x', 0, 0);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
