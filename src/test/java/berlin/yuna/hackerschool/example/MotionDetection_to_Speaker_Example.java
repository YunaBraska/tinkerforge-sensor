package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class MotionDetection_to_Speaker_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isMotionDetected() && value.intValue() == 1) {
            sensor.ledAdditional_setOn();
            stack.sensors().speaker().send(1000);
        } else if(type.isMotionDetected() && value.intValue() == 0){
            sensor.ledAdditional_setOff();
        }
    }
}
