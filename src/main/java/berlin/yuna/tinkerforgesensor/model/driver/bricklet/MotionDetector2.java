package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.BrickletMotionDetectorV2;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED_ON;

public class MotionDetector2 extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList) {
        BrickletMotionDetectorV2 device = (BrickletMotionDetectorV2) sensor.device;
        registration.sensitivity(100, MOTION_DETECTED_ON, MOTION_DETECTED_OFF);

        device.addMotionDetectedListener(() -> registration.sendEvent(consumerList, MOTION_DETECTED_ON, sensor, 1L));
        device.addDetectionCycleEndedListener(() -> registration.sendEvent(consumerList, MOTION_DETECTED_OFF, sensor, 0L));

        //TODO: set sensitivity
        try {
            device.setSensitivity(100);
        } catch (TimeoutException | NotConnectedException ignored) {
        }

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i ==  LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                },
                i -> {
                    if (i == LED_ADDITIONAL_ON.bit) {device.setIndicator(255, 255, 255);}
                    else if (i == LED_ADDITIONAL_OFF.bit) {device.setIndicator(0, 0, 0);}
                    else if (i == 11) {device.setIndicator(255, 0, 0);}
                    else if (i == 12) {device.setIndicator(0, 255, 0);}
                    else if (i == 13) {device.setIndicator(0, 0, 255);}
                    else if (i == 14) {device.setIndicator(255, 255, 0);}
                    else if (i == 15) {device.setIndicator(0, 255, 255);}
                    else if (i == 16) {device.setIndicator(255, 0, 255);}
                    else if (i == LED_STATUS.bit) {device.setIndicator(0, 0, 0);}

                }, ignore -> { }));
    }
}
