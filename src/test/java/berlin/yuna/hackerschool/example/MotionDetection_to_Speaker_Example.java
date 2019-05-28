package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class MotionDetection_to_Speaker_Example {

    private static TinkerForge tinkerForge;

    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isMotionDetected() && value.intValue() == 1) {
            sensor.ledAdditionalOn();
            tinkerForge.sensors().speaker().send(1000);
        } else if(type.isMotionDetected() && value.intValue() == 0){
            sensor.ledAdditionalOff();
        }
    }
}
