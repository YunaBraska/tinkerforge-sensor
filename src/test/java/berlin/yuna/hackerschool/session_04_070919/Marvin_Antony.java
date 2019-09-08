package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static java.lang.String.format;


public class Marvin_Antony extends Helper {

    //VARIABLES
    private static Stack stackRemote;
    private static Stack stackRobot;

    static void onStart() {
        loop("PoliceLED", run -> {
            stackRobot.sensors().buttonDual().send(false, true);
            stackRobot.sensors().speaker().send(700, 600);
            sleep(700);
            stackRobot.sensors().buttonDual().send(true, false);
            stackRobot.sensors().speaker().send(700, 630);
            sleep(700);
        });
    }

    static long left = 0;
    static long right = 0;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {
        if (event.getValueType().isRotary()) {
            if (event.getValue() > 0) {
                right = event.getValue() * 5;
            } else if (event.getValue() < 0) {
                left = event.getValue() * 5;
            }

            final Long percentage = stackRemote.values().percentage();
            move((-100 + right) * percentage, (100 + left) * percentage);
        }

        if (event.getValueType().isPercentage()) {
            final Long percentage = event.getValue();
            move((-100 + right) * percentage, (100 + left) * percentage);
        }
    }

    //START FUNCTION
    public static void main(final String[] args) {
        stackRemote = ConnectionAndPrintValues_Example.connect();
        stackRemote.sensorEventConsumerList.add(Marvin_Antony::onSensorEvent);

        try {
            stackRobot = new Stack("100.100.0.2", 4223, true);
            stackRobot.sensorEventConsumerList.add(Marvin_Antony::printAllValues);
            onStart();
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printAllValues(final SensorEvent sensorEvent) {
        if (sensorEvent.getValueType().containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] send [%s]", sensorEvent.sensor().name, sensorEvent.getValueType(), sensorEvent.getValue()));
        } else if (!timePassed(256)) {
            return;
        }
        System.out.println(stackRobot.valuesToString());
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
