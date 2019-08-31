package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

/**
 * Rotary will change all servo positions
 * Rotary press will invert servo[0,1,2] position
 */
public class Servo_Rotary_Example extends Helper {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    private static boolean invert = false;

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor servo = stack.sensors().servo();
        servo.send(-1, true);

        if (sensor.compare().isRotary()) {
            if (type.isButtonPressed() && value == 1) {
                invert = !invert;
                servo.send(0, true, invert);
                servo.send(1, true, invert);
                servo.send(2, true, invert);
            } else if (type.isRotary()) {
                servo.send(-1, value * 500);
            }
        }
    }
}
