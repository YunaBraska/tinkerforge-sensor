package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE;

public class Distance_to_SpeakerSonar_Example extends Helper {

    private static SensorList<Sensor> sensorList;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sonar();

    }

    private static void sonar() {
        while (true) {
            Long distance = sensorList.value(DISTANCE);
            if (distance > 1 && distance < 250 && timePassed(32)) {
                sensorList.getSpeaker().value(32);
            }
            if (distance > 1 && timePassed(distance + 200)) {
                sensorList.getSpeaker().value(distance / 8);
            }
        }

    }
}
