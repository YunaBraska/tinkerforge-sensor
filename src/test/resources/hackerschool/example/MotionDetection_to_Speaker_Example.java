package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

public class MotionDetection_to_Speaker_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
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
