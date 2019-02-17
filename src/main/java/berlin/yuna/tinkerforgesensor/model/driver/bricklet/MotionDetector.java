package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED;

/**
 * Passive infrared (PIR) motion sensor, 7m range with 100° angle
 * <b>Values</b>
 * MOTION_DETECTED = 0/1
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motion_Detector_V2.html">Official doku</a>
 */
public class MotionDetector extends Sensor<BrickletMotionDetector> {

    public MotionDetector(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletMotionDetector) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickletMotionDetector> initListener() {
        device.addMotionDetectedListener(() -> sendEvent(MOTION_DETECTED, 1L));
        device.addDetectionCycleEndedListener(() -> sendEvent(MOTION_DETECTED, 0L));
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetector> value(Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetector> ledStatus(Integer value) {
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
    public Sensor<BrickletMotionDetector> ledAdditional(Integer value) {
        return this;
    }
}
