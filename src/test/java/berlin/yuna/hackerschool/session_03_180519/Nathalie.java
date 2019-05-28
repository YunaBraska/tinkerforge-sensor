package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.util.Random;

/**
 * @author Nathalie
 */
public class Nathalie extends Helper {

    private static TinkerForge tinkerForge;

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //TODO: implement limit callback per second

    //VARIABLES
    private static long soundMax = 1;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        final int[] colors = new int[]{Color.WHITE,
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.BLUE,
                Color.MAGENTA,
                Color.PINK,
                Color.BLACK};

        //TODO: check refresh Limit
        final int color = colors[new Random().nextInt(colors.length)];
        tinkerForge.sensors().buttonRGB(1).sendLimit(10, color);
        tinkerForge.sensors().buttonRGB(0).sendLimit(1, color);
        tinkerForge.sensors().displaySegment().sendLimit(2, tinkerForge.sensors().accelerometer().send(ValueType.ACCELERATION_Y));

        //Get Sensor and Value
        final Sensor io16 = tinkerForge.sensors().iO16();
        final long decibel = tinkerForge.values().soundIntensity() + 1;

        //Dynamic max volume
        if (decibel > soundMax) {
            soundMax = decibel;
        }

        //every 250 milliseconds - for readable display
        if (timePassed(250)) {
            //tinkerForge.sensors().displaySegment().send((decibel / 10) + "dB");
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
        if (tinkerForge.values().temperature() > 2880) {
            io16.send(17);
        } else if (tinkerForge.values().temperature() < 2880) {
            io16.send(-17);
        }
    }
}
