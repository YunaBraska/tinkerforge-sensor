package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class Beethoven extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor(), event.value(), event.type()));
    }

    //VARIABLES
    public static Stack stack;
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
            stack.sensors().speaker().send(i, i, false);
        }
        stack.sensors().speaker().send(500, 2590 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 600 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 2590 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 600 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 700 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 600 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 2590 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 600 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 700 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 586 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 650 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 850 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1000 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1200 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1400 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1600 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1800 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 2000 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 587 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 587 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1000 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1000 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 800 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 800 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(555, 587 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1200 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1200 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 675 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 675 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1000 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(350, 1000 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(555, 586 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 586 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 650 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 850 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 1050 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 1250 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 1450 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 1650 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 1850 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 2050 + stack.values().orientationRoll(), true);
        stack.sensors().speaker().send(200, 2250 + stack.values().orientationRoll(), true);
        return false;
    }
}
