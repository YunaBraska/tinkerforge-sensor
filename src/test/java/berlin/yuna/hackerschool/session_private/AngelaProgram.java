package berlin.yuna.hackerschool.session_private;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

/**
 * @author angela m.
 */
public class AngelaProgram extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(AngelaProgram::onSensorEvent);
    }

    //VARIABLES
    private static Stack stack;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {

        stack.sensors().displaySegment().send(2701);

        stack.sensors().iO16().send(1,-5);
        sleep(500);
        stack.sensors().iO16().send(-1,5);
        sleep(500);

    }
}
