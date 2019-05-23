package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class Beethoven extends Helper {

    //START FUNCTION
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static SensorList<Sensor> sensorList = new SensorList<>();
    private static boolean isRunning = false;
    private static long orientation = 0L;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isOrientationRoll() && type.containsOrientationRoll()) {
            orientation = value;
        }

        if (!isRunning) {
            isRunning = true;
            async("beethoven", beethoven -> isRunning = beethovenMelody());
        }
    }

    private static boolean beethovenMelody() {
        for (int i = 600; i < 1200; i++) {
            sensorList.getSpeaker().value(i, i, false);
        }
        sensorList.getSpeaker().value(500, 2590 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 2590 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 700 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 2590 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 700 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 586 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 650 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 850 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1200 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1400 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1800 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 2000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 587 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 587 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 800 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 800 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(555, 587 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1200 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1200 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 675 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 675 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(555, 586 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 586 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 650 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 850 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1050 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1250 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1450 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1650 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1850 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 2050 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 2250 + sensorList.getValueOrientationRoll(), true);
        return false;
    }
}
