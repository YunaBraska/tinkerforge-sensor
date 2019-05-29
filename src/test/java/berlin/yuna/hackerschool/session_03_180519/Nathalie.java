package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

/**
 * @author Nathalie
 */
public class Nathalie extends Helper {

    private static Stack stack;

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //TODO: implement limit callback per second

    //VARIABLES
    private static long soundMax = 1;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        //Get Sensor and Value
        final Sensor io16 = stack.sensors().iO16();
        final long decibel = stack.values().soundIntensity() + 1;

        //Dynamic max volume
        if (decibel > soundMax) {
            soundMax = decibel;
        }

        //every 250 milliseconds - for readable display
        if (timePassed(250)) {
            stack.sensors().displaySegment().send((decibel / 10) + "dB");
        }

        //every 50 milliseconds
        if (timePassed(50)) {
            final int ledAnzahl = (int) (decibel / ((soundMax / 18) + 1));

            //Switch LEDs on
            for (int led = 1; led < ledAnzahl; led++) {
                io16.send(led);
            }

            //Switch other LEDs off
            for (int led = ledAnzahl; led < 16; led++) {
                io16.send(-led);
            }
        }

        //Fan temperature
        if (stack.values().temperature() > 2880) {
            io16.send(17);
        } else if (stack.values().temperature() < 2880) {
            io16.send(-17);
        }
    }
}
