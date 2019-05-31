package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED;

/**
 * <h3>{@link MotionDetector}</h3><br />
 * <i>Passive infrared (PIR) motion sensor, 7m range with 100° angle</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#MOTION_DETECTED} [0/1 cycleOff/detect]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motion_Detector.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting motion detected example</h6>
 * <code>stack.values().motionDetected();</code>
 */
public class MotionDetector extends Sensor<BrickletMotionDetector> {

    public MotionDetector(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletMotionDetector) device, uid, true);
    }

    @Override
    protected Sensor<BrickletMotionDetector> initListener() {
        device.addMotionDetectedListener(() -> sendEvent(MOTION_DETECTED, 1L));
        device.addDetectionCycleEndedListener(() -> sendEvent(MOTION_DETECTED, 0L));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetector> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetector> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetector> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetector> refreshPeriod(final int milliseconds) {
        return this;
    }
}
