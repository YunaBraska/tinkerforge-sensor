package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import java.util.Random;

/**
 * @author Nathalie
 */
public class Nathalie extends Helper {

    private static Stack stack;

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(Nathalie::onSensorEvent);
    }

    //VARIABLES
    private static long soundMax = 1;
    private static final int rustle = 300;

    //CODE FUNCTION
    private static void onSensorEvent(final SensorEvent event) {
        //Get Sensor and Value
        final Sensor io16 = stack.sensors().iO16();
        long decibel = stack.sensors().sound(0).values().soundIntensity() + 1;

        //remove rustle
        if (decibel > rustle) {
            decibel = decibel - 300;
        } else {
            decibel = 1;
        }

        //Dynamic max volume
        if (decibel > soundMax) {
            soundMax = decibel;
        }

        //every 250 milliseconds - for readable display
        stack.sensors().displaySegment().sendLimit(2, (decibel / 10) + "dB");

        //every 50 milliseconds
        if (timePassed(50)) {
            final int ledCount = (int) (decibel / ((soundMax / 18) + 1));

            //Switch LEDs on
            for (int led = 1; led < ledCount; led++) {
                io16.send(led);
            }

            //Switch other LEDs off
            for (int led = ledCount; led < 17; led++) {
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
