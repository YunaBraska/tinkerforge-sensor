package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class Uhrsula extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static TinkerForge tinkerForge;
    private static int counter = 0;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (timePassed(1000)) {
            if (tinkerForge.values().lightLux() > 2000) {
                counter = counter + 1;
            }
            if (tinkerForge.values().lightLux() < 2000) {
                counter = counter - 1;
            }
            tinkerForge.sensors().displaySegment().send(counter);
        }
    }
}
