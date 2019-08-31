package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

public class Servo_Key_Example extends Helper {

    private static final Stack stack = ConnectionAndPrintValues_Example.connect();

    public static void main(final String[] args) {
        stack.sensorEventConsumerList.add(Servo_Key_Example::onSensorEvent);
        stack.sensors().localControl().ledAdditional_setOn();
    }

    private static final boolean[] keys = new boolean[255];
    private static final long speed = 2000;

    private static void onSensorEvent(final SensorEvent event) {
        if (event.getValueType().containsKeyInput()) {
            keys[event.getValue().intValue()] = event.getValueType().isKeyPressed();

            if (keys[VK_LEFT]) {
                move(speed, speed);
            } else if (keys[VK_RIGHT]) {
                move(speed * -1, speed * -1);
            } else if (keys[VK_UP]) {
                move(speed * -1, speed);
            } else if (keys[VK_DOWN]) {
                move(speed, speed * -1);
            } else {
                move(0, 0);
                stack.sensors().servo().send(-1, false);
            }
        }
    }

    private static void move(final long speedLeft, final long speedRight) {
        final Sensor servo = stack.sensors().servo();
        if (speedRight != 0) {
            servo.send(0, true);
            servo.send(1, true);
            servo.send(2, true);
            servo.send(3, true);
            servo.send(0, speedLeft);
            servo.send(1, speedLeft);
            servo.send(2, speedLeft);
            servo.send(3, speedLeft);
        }

        if (speedRight != 0) {
            servo.send(4, true);
            servo.send(5, true);
            servo.send(6, true);
            servo.send(4, speedRight);
            servo.send(5, speedRight);
            servo.send(6, speedRight);
        }
    }
}
