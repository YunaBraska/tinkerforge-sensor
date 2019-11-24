package berlin.yuna.hackerschool.session_private;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.Connection;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Loop;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;


public class Roboter_Marvin extends Helper {

    //VARIABLES
    private Stack stack;

    long turn = 0;
    long speed = 0;
    long turnBefore = 0;
    long speedBefore = 0;

    Loop carMove = new Loop(128, this::carMoveApp);
    Loop carBlinker = new Loop(64, this::carBlinkerApp);
    Loop carStatusLed = new Loop(64, this::carStatusLed);
    Loop carDistance = new Loop(32, this::carDistanceApp);
    Loop carPoliceLed = new Loop(128, this::carPoliceLedApp);
    Loop carPoliceSiren = new Loop(256, this::carPoliceSirenApp);

    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {
        if (carStatusLed.isNotRunning()) {
            //Car status Led effect
            carStatusLed.start();
            //Car distance detection
            carDistance.start();
            //Car blinker automation
            carBlinker.start();
            //Car movement
            carMove.start();
        }

        //Car speed
        if (event.getValueType().isPercentage()) {
            final Long percentage = event.getValue();
            speed = (percentage == 0 ? 0 : percentage - 50) * 64;
        }

        //Car turn
        if (event.getValueType().isRotary()) {
            turn = event.getValue() * 100;
        }

        //Start police siren
        if (event.getValueType().isButtonPressed()) {
            carPoliceSiren.start(!carPoliceSiren.isRunning());
            carPoliceLed.start(carPoliceSiren.isRunning());
        }

        //Reconnect
        if (event.getValueType().isDeviceConnected() || event.getValueType().containsDeviceReconnected()) {
            stack.sensors().forEach(sensor -> {
                sensor.ledStatus_setOff();
                sensor.ledAdditional_setOff();
                speed = 0;
                turn = 0;
                stack.sensors().iO16().send(4, 5, 6, 11, 12, 13, 14);
            });
        }
    }

    private void carMoveApp(final long refreshMs) {
        if (speed != speedBefore || turn != turnBefore) {
            speedBefore = speed;
            turnBefore = turn;
            final long left;
            final long right;
            if (turn == 0) {
                left = speed;
                right = speed;
            } else if (turn < 0) {
                left = speed;
                right = ((speed + turn / 2));
            } else {
                left = ((speed - turn / 2));
                right = speed;
            }
            stack.sensors().display(0).ledAdditional_setOn().send(left);
            stack.sensors().display(1).ledAdditional_setOn().send(right);
            move(left, right);

            if (speed == 0) {
                stack.sensors().iO16().send(14);
            } else {
                stack.sensors().iO16().send(-14);
            }
        }
    }

    private void carBlinkerApp(final long refrehMs) {
        if (turn < 0) {
            stack.sensors().iO16().send(9, 10);
            sleep(425);
            stack.sensors().iO16().send(-9, -10);
            sleep(425);
        } else if (turn > 0) {
            stack.sensors().iO16().send(7, 8);
            sleep(425);
            stack.sensors().iO16().send(-7, -8);
            sleep(425);
        }
    }

    private void carDistanceApp(final long refreshMs) {
        final Long distance = stack.values().distance();
        if (timePassed(distance) && distance > 1 && distance < 300) {
            if (carPoliceSiren.isRunning()) {
                carPoliceSiren.stop();
            }
            stack.sensors().speaker().send(distance / 8, 7000 - (distance * 10));
            if (distance < 150) {
                move(0, 0);
            }
        }
    }

    private void carPoliceSirenApp(final long refreshMs) {
        stack.sensors().speaker().send(550, 600);
        sleep(550);
        stack.sensors().speaker().send(550, 745);
        sleep(550);
    }

    private void carPoliceLedApp(final long refreshMs) {
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
    }

    boolean ledReverse;

    private void carStatusLed(final long refreshMs) {
        ledReverse = !ledReverse;

        for (Sensor sensor : stack.sensors()) {
            if (sensor.isBrick() && sensor.hasLedStatus()) {
                sensor.setLedStatus(ledReverse ? LED_STATUS_ON : LED_STATUS_OFF);
                sensor.ledAdditional(ledReverse ? LED_ADDITIONAL_ON : LED_ADDITIONAL_OFF);
                sleep(128);
            }
        }
    }

    //START FUNCTION
    public static void main(final String[] args) throws NetworkConnectionException {
        final Roboter_Marvin app = new Roboter_Marvin();
        app.stack = ConnectionAndPrintValues_Example.connect();
        app.stack.consumers.add(app::onSensorEvent);
        app.stack.addStack(new Connection("192.168.3.5", 4223, true));

    }

    private void move(final long speedLeft, final long speedRight) {
        final Sensor servo = stack.sensors().servo();
        if (speedLeft == 0 && speedRight == 0) {
            servo.send(-1, false);
        } else {
            servo.send(0, true, true);
            servo.send(1, true, true);
            servo.send(2, true, true);
            servo.send(3, true, true);
            servo.send(4, true);
            servo.send(5, true);
            servo.send(6, true);

            //LEFT
            servo.send(0, speedLeft);
            servo.send(1, speedLeft);
            servo.send(2, speedLeft);
            servo.send(3, speedLeft);

            //RIGHT
            servo.send(4, speedRight);
            servo.send(5, speedRight);
            servo.send(6, speedRight);
        }
    }
}
