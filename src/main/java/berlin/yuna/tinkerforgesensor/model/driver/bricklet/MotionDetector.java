package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED_ON;

public class MotionDetector extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletMotionDetector device = (BrickletMotionDetector) sensor.device;
        registration.sensitivity(100, MOTION_DETECTED_ON, MOTION_DETECTED_OFF);

        device.addMotionDetectedListener(() -> registration.sendEvent(consumerList, MOTION_DETECTED_ON, sensor, 1L));
        device.addDetectionCycleEndedListener(() -> registration.sendEvent(consumerList, MOTION_DETECTED_OFF, sensor, 0L));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {device.setStatusLEDConfig((short) LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);}
                },
                ignore -> { }, ignore -> { }));
    }
}
