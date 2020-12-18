package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

/**
 * @author yourNames
 */
public class Uhrsula extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    //VARIABLES
    public static Stack stack;
    private static int counter = 0;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (timePassed(1000)) {
            if (stack.values().lightLux() > 2000) {
                counter = counter + 1;
            }
            if (stack.values().lightLux() < 2000) {
                counter = counter - 1;
            }
            stack.sensors().displaySegment().send(counter);
        }
    }
}
