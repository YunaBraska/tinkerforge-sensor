package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletMotionDetectorV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED;

/**
 * <h3>{@link MotionDetectorV2}</h3><br />
 * <i>Passive infrared (PIR) motion sensor, 12m range with 120° angle</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#MOTION_DETECTED} [0/1 cycleOff/detect]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motion_Detector_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting motion detected</h6>
 * <code>values().motionDetected();</code>
 */
public class MotionDetectorV2 extends Sensor<BrickletMotionDetectorV2> {

    public MotionDetectorV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletMotionDetectorV2) device, uid);
    }

    @Override
    protected Sensor<BrickletMotionDetectorV2> initListener() {
        device.addMotionDetectedListener(() -> sendEvent(MOTION_DETECTED, 1L));
        device.addDetectionCycleEndedListener(() -> sendEvent(MOTION_DETECTED, 0L));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> send(final Object value) {
        try {
            if (value instanceof Number) {
                final int input = ((Number) value).intValue();
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
        } catch (TinkerforgeException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                ledStatus = LED_STATUS_HEARTBEAT;
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                ledStatus = LED_STATUS;
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> ledAdditional(final Integer value) {
        send(value);
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> flashLed() {
        try {
            this.ledAdditional_setOn();
            this.setLedStatus(LED_STATUS_HEARTBEAT);
            for (int i = 0; i < 8; i++) {
                send(i);
                Thread.sleep(128);
            }
            this.ledAdditional_setOff();
            this.setLedStatus(LED_STATUS);
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotionDetectorV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setSensitivity(100);
            } else {
                device.setSensitivity((milliseconds / 10) + 1);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
