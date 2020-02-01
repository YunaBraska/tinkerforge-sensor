package berlin.yuna.hackerschool.session_05_301119;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import java.util.Random;

/**
 * Code Link: http://hs.yuna.berlin/
 * Sensor Doku Link: https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md
 */
public class Junis_Johnny extends Helper {

    //VARIABLES
    Stack stack;

    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {

        //EXAMPLE CODE
        if (timePassed(100)) {
            stack.sensors().display().ledAdditional_setOn();
            stack.sensors().display().send("" + stack.values().percentage() + "      ");
        }
        if (event.getValueType().isPercentage() && event.getValue() == 100) {
            stack.sensors().display().send("max", true);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 99) {
            stack.sensors().display().send(true);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 1) {
            stack.sensors().display().send(true);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 0) {
            stack.sensors().display().send("min", true);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 3) {
            stack.sensors().iO16().send(-1);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 50) {
            stack.sensors().iO16().send(1);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 6) {
            stack.sensors().iO16().send(-2);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 53) {
            stack.sensors().iO16().send(2);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 9) {
            stack.sensors().iO16().send(-3);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 56) {
            stack.sensors().iO16().send(3);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 12) {
            stack.sensors().iO16().send(-4);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 59) {
            stack.sensors().iO16().send(4);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 15) {
            stack.sensors().iO16().send(-5);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 62) {
            stack.sensors().iO16().send(5);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 18) {
            stack.sensors().iO16().send(-6);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 65) {
            stack.sensors().iO16().send(6);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 21) {
            stack.sensors().iO16().send(-7);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 68) {
            stack.sensors().iO16().send(7);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 24) {
            stack.sensors().iO16().send(-8);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 71) {
            stack.sensors().iO16().send(8);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 27) {
            stack.sensors().iO16().send(-9);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 74) {
            stack.sensors().iO16().send(9);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 30) {
            stack.sensors().iO16().send(-10);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 77) {
            stack.sensors().iO16().send(10);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 33) {
            stack.sensors().iO16().send(-11);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 80) {
            stack.sensors().iO16().send(11);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 36) {
            stack.sensors().iO16().send(-12);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 83) {
            stack.sensors().iO16().send(12);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 39) {
            stack.sensors().iO16().send(-13);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 86) {
            stack.sensors().iO16().send(13);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 42) {
            stack.sensors().iO16().send(-14);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 89) {
            stack.sensors().iO16().send(14);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 45) {
            stack.sensors().iO16().send(-15);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 92) {
            stack.sensors().iO16().send(15);
        }
        if (event.getValueType().isPercentage() && event.getValue() < 48) {
            stack.sensors().iO16().send(-16);
        }
        if (event.getValueType().isPercentage() && event.getValue() == 95) {
            stack.sensors().iO16().send(16);
        }

    }

    //START FUNCTION
    public static void main(final String[] args) {
        Junis_Johnny template = new Junis_Johnny();
        template.stack = ConnectionAndPrintValues_Example.connect();
        template.stack.consumers.add(template::onSensorEvent);
    }
}
