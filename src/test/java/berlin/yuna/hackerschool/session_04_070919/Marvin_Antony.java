package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.logic.Stacks;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example.printAllValues;
import static java.lang.String.format;


public class Marvin_Antony extends Helper {

    //VARIABLES
    private static Stack stackRemote;
    private static Stack stackRobot;

    static boolean sirene = false;
    static long distance = 300;

    static void onStart() {
        loop("PoliceLED", run -> {
            stackRobot.sensors().iO16().send(4, 5, 6);
            sleep(150);
            stackRobot.sensors().iO16().send(-4, -5, -6);
            sleep(150);
            stackRobot.sensors().iO16().send(4, 5, 6);
            sleep(150);

            stackRobot.sensors().iO16().send(12, 13, 11, -4, -5, -6);
            sleep(150);
            stackRobot.sensors().iO16().send(-11, -12, -13);
            sleep(150);
            stackRobot.sensors().iO16().send(11, 12, 13);
            sleep(150);
            stackRobot.sensors().iO16().send(-11, -12, -13);
        });

        loop("Blinker", run -> {
            if (stackRemote.values().rotary() < 0) {
                stackRobot.sensors().iO16().send(9, 10);
                sleep(425);
                stackRobot.sensors().iO16().send(-9, -10);
                sleep(425);
            } else if (stackRemote.values().rotary() > 0) {
                stackRobot.sensors().iO16().send(7, 8);
                sleep(425);
                stackRobot.sensors().iO16().send(-7, -8);
                sleep(425);
            }
        });

        loop("distance", run -> {
            distance = stackRobot.values().distance();
            if (distance < 200) {
                stackRobot.sensors().iO16().send(14, 15, 16);
            } else if (distance > 200) {
                stackRobot.sensors().iO16().send(-14, -15, -16);
            }
            if (distance < 150) {
                move(0, 0);
            }

        });


        loop("Sirene", run -> {
            if (sirene == true) {
                //stackRobot.sensors().speaker().send(1000, 600);

                stackRobot.sensors().speaker().send(550, 600);
                sleep(550);
                stackRobot.sensors().speaker().send(550, 745);
                sleep(550);

            }
        });
    }

    static long left = 0;
    static long right = 0;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {

        if (distance < 200 && timePassed(distance)) {
            stackRobot.sensors().speaker().send(100, 9000);

        }

        if (event.getValueType().isRotary()) {
            if (event.getValue() > 0) {
                right = event.getValue() * 10;
            } else if (event.getValue() < 0) {
                left = event.getValue() * 10;
            }

            final Long percentage = stackRemote.values().percentage();
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
    public static void main(final String[] args) throws NetworkConnectionException {
//        stackRemote = Stacks.connect("192.168.3.5", 4223).orElse(new Stack());
        stackRobot = Stacks.connect("localhost", 4223).orElse(new Stack());
        Stacks.addConsumer(Marvin_Antony::onSensorEvent);
        printAllValues();
    }

    private static void move(final long speedLeft, final long speedRight) {
        if (stackRobot == null) {
            return;
        }
        final Sensor servo = stackRobot.sensors().servo();
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
