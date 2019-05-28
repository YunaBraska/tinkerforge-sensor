package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class Beethoven extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static TinkerForge tinkerForge;
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
            tinkerForge.sensors().speaker().send(i, i, false);
        }
        tinkerForge.sensors().speaker().send(500, 2590 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 600 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 2590 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 600 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 700 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 600 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 2590 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 600 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 700 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 586 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 650 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 850 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1000 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1200 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1400 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1600 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1800 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 2000 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 587 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 587 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1000 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1000 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 800 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 800 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(555, 587 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1200 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1200 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 675 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 675 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1000 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(350, 1000 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(555, 586 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 586 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 650 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 850 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 1050 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 1250 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 1450 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 1650 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 1850 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 2050 + tinkerForge.values().orientationRoll(), true);
        tinkerForge.sensors().speaker().send(200, 2250 + tinkerForge.values().orientationRoll(), true);
        return false;
    }
}
