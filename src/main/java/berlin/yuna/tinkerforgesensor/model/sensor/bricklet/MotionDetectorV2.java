package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletMotionDetectorV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED;

/**
 * Passive infrared (PIR) motion sensor, 12m range with 120° angle
 * <b>Values</b>
 * MOTION_DETECTED = 0/1
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motion_Detector_V2.html">Official doku</a>
 */
public class MotionDetectorV2 extends Sensor<BrickletMotionDetectorV2> {

    public MotionDetectorV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletMotionDetectorV2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletMotionDetectorV2> initListener() {
        try {
            device.addMotionDetectedListener(() -> sendEvent(MOTION_DETECTED, 1L));
            device.addDetectionCycleEndedListener(() -> sendEvent(MOTION_DETECTED, 0L));
            device.setSensitivity(100);
            ledStatus(LED_STATUS.bit);
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> value(final Object value) {
        try {
            if (value instanceof Number) {
                int input = ((Number) value).intValue();
                if (input == 0) {
                    device.setIndicator(0, 0, 0);
                } else if (input == 1) {
                    device.setIndicator(255, 255, 255);
                } else if (input == 2) {
                    device.setIndicator(255, 0, 0);
                } else if (input == 3) {
                    device.setIndicator(0, 255, 0);
                } else if (input == 4) {
                    device.setIndicator(0, 0, 255);
                } else if (input == 5) {
                    device.setIndicator(255, 255, 0);
                } else if (input == 6) {
                    device.setIndicator(0, 255, 255);
                } else if (input == 7) {
                    device.setIndicator(255, 0, 255);
                }
            }
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> ledAdditional(final Integer value) {
        value(value);
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> flashLed() {
        try {
            this.ledAdditionalOn();
            this.ledStatus(LED_STATUS_HEARTBEAT.bit);
            for (int i = 0; i < 8; i++) {
                value(i);
                Thread.sleep(128);
            }
            this.ledAdditionalOff();
            this.ledStatus(LED_STATUS.bit);
        } catch (Exception ignore) {
        }
        return this;
    }
}
