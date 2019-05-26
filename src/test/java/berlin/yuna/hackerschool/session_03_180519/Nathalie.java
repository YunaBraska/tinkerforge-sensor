package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

/**
 * @author Nathalie
 */
public class Nathalie extends Helper {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    //START FUNCTION
    public static void main(final String[] args) {
        final SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES

    private static long soundMax = 1;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        //Get Sensor and Value
        final Sensor io16 = sensorList.getIO16();
        final long decibel = sensorList.getValueSoundIntensity() + 1;

        //Dynamic max volume
        if (decibel > soundMax) {
            soundMax = decibel;
        }

        //every 250 milliseconds - for readable display
        if (timePassed(250)) {
            sensorList.getDisplaySegment().value((decibel / 10) + "dB");
        }

        //every 50 milliseconds
        if (timePassed(50)) {
            final int ledAnzahl = (int) (decibel / ((soundMax / 18) + 1));

            //Switch LEDs on
            for (int led = 1; led < ledAnzahl; led++) {
                io16.value(led);
            }

            //Switch other LEDs off
            for (int led = ledAnzahl; led < 16; led++) {
                io16.value(-led);
            }
        }

        //Fan temperature
        if (sensorList.getValueTemperature() > 2880) {
            io16.value(17);
        } else if (sensorList.getValueTemperature() < 2880) {
            io16.value(-17);
        }
    }
}
