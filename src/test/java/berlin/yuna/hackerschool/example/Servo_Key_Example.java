package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class Servo_Key_Example extends Helper {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
        stack.sensors().localControl().ledAdditional_setOn();
    }

    private static boolean invert = false;

    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;

    boolean[] keys = new int[255];

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor servo = stack.sensors().servo();
//        servo.send(-1, true);

        if (type.isKeyChar()) {
            keys.
            final String key = numberToString(value);
            if(key == 'W'){

            }
        }

//        if (sensor.compare().isRotary()) {
//            if (type.isButtonPressed() && value == 1) {
//                invert = !invert;
//                servo.send(0, true, invert);
//                servo.send(1, true, invert);
//                servo.send(2, true, invert);
//            } else if (type.isRotary()) {
//                servo.send(-1, value * 500);
//            }
//        }
    }
}
