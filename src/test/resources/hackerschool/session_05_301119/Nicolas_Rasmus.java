package berlin.yuna.hackerschool.session_05_301119;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.thread.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.threads.Color;

import java.util.Random;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;

/**
 * Code Link: http://hs.yuna.berlin/
 * Sensor Doku Link: https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md
 */
public class Nicolas_Rasmus extends Helper {

    //VARIABLES
    Stack stack;
    boolean ton2 = false;
    private final boolean[] keys = new boolean[255];
    private final long speed = 2000;


    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {


        //EXAMPLE CODEif (event.getValueType().isButtonPressed()) {if (event.getValueType().isButtonPressed()) {


        //int myNumber = new Random().nextInt(99);
        if (event.getValueType().isButtonPressed()) {
            //stack.sensors().forEach(sensor -> sensor.send(myNumber));
            //stack.sensors().display().send("HALLO");

            int farbeJetzt = Color.RAINBOW[new Random().nextInt(Color.RAINBOW.length)];
            stack.sensors().buttonRGB().send(farbeJetzt);
        }

        if (timePassed(500)) {


            if (ton2) {
                stack.sensors().speaker().send(500, 1000);
                stack.sensors().buttonDual(1).send(-1, 2);
                stack.sensors().buttonRGB().send(Color.BLUE);
                stack.sensors().buttonDual().send(-1, 2);


            } else {
                stack.sensors().speaker().send(500, 700);
                stack.sensors().buttonDual(1).send(1, -2);
                stack.sensors().buttonRGB().send(Color.RED);
                stack.sensors().buttonDual().send(1, -2);
            }

            ton2 = !ton2;
        }

        if (event.getValueType().containsKeyInput()) {
            keys[event.getValue().intValue()] = event.getValueType().isKeyPressed();

            if (keys[VK_A]) {
                move(speed, speed);
            } else if (keys[VK_D]) {
                move(speed * -1, speed * -1);
            } else if (keys[VK_W]) {
                move(speed * -1, speed);
            } else if (keys[VK_S]) {
                move(speed, speed * -1);
            } else {
                move(0, 0);
                stack.sensors().servo().send(-1, false);
            }
        }
        //stack.sensors().servo().send();
    }


    private void move(final long speedLeft, final long speedRight) {
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

    //START FUNCTION
    public static void main(final String[] args) {
        Nicolas_Rasmus template = new Nicolas_Rasmus();
        template.stack = ConnectionAndPrintValues_Example.connect();
        template.stack.consumers.add(template::onSensorEvent);
        template.stack.sensors().localControl().ledAdditional_setOn();
    }
}
