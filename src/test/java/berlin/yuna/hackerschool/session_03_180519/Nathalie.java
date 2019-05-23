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
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES

    static int soundMax = 1;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        Sensor io16 = sensorList.getIO16();
        long decibel = sensorList.getValueSoundIntensity() + 1;

        if (decibel > soundMax) {
            soundMax = (int) decibel;
        }

        if (sensorList.getValueTemperature() > 2880) {
            io16.value(17);
        } else if (sensorList.getValueTemperature() < 2880) {
            io16.value(-17);
        }


        if (timePassed(250)) {
            sensorList.getDisplaySegment().value(decibel / 10);
        }

        if (timePassed(50)) {
            int ledAnzahl = (int) (decibel / ((soundMax / 18) +1));

            for (int led = 1; led < ledAnzahl; led++) {
                io16.value(led);
            }


            for (int led = ledAnzahl; led < 16; led++) {
                io16.value(-led);
            }


        }


    }
}
