package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.Connection;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;


public class Marvin_Antony extends Helper {

    //VARIABLES
    private static Stack stack;

    static boolean sirene = false;
    static long distance = 300;

    static void onStart() {
        loop("PoliceLED", run -> {
            stack.sensors().iO16().send(4, 5, 6);
            sleep(150);
            stack.sensors().iO16().send(-4, -5, -6);
            sleep(150);
            stack.sensors().iO16().send(4, 5, 6);
            sleep(150);

            stack.sensors().iO16().send(12, 13, 11, -4, -5, -6);
            sleep(150);
            stack.sensors().iO16().send(-11, -12, -13);
            sleep(150);
            stack.sensors().iO16().send(11, 12, 13);
            sleep(150);
            stack.sensors().iO16().send(-11, -12, -13);
        });

        loop("Blinker", run -> {
            if (stack.values().rotary() < 0) {
                stack.sensors().iO16().send(9, 10);
                sleep(425);
                stack.sensors().iO16().send(-9, -10);
                sleep(425);
            } else if (stack.values().rotary() > 0) {
                stack.sensors().iO16().send(7, 8);
                sleep(425);
                stack.sensors().iO16().send(-7, -8);
                sleep(425);
            }
        });

        loop("distance", run -> {
            distance = stack.values().distance();
            if (distance < 200) {
                stack.sensors().iO16().send(14, 15, 16);
            } else if (distance > 200) {
                stack.sensors().iO16().send(-14, -15, -16);
            }
            if (distance < 150) {
                move(0, 0);
            }

        });


        loop("Sirene", run -> {
            if (sirene == true) {
                stack.sensors().speaker().send(550, 600);
                sleep(550);
                stack.sensors().speaker().send(550, 745);
                sleep(550);

            }
        });
    }

    static long left = 0;
    static long right = 0;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {

        if (distance > 0 && distance < 200 && timePassed(distance)) {
            stack.sensors().speaker().send(100, 9000);
        }

        if (event.getValueType().isRotary()) {
            if (event.getValue() > 0) {
                right = event.getValue() * 10;
            } else if (event.getValue() < 0) {
                left = event.getValue() * 10;
            }

            final Long percentage = stack.values().percentage();
            move((-100 + right) * percentage, (100 + left) * percentage);
        }

        if (event.getValueType().isPercentage()) {
            final Long percentage = event.getValue();
            move((-100 + right) * percentage, (100 + left) * percentage);
        }

        if (event.getValueType().isButtonPressed()) {
            sirene = !sirene;
        }

    }

    //START FUNCTION
    public static void main(final String[] args) throws Exception {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(Marvin_Antony::onSensorEvent);
        stack.addStack(new Connection("192.168.3.5", 4223, true));
        onStart();
    }

    private static void move(final long speedLeft, final long speedRight) {
        final Sensor servo = stack.sensors().servo();
        if (speedLeft == 0 && speedRight == 0) {
            servo.send(-1, false);
        } else {

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


}
