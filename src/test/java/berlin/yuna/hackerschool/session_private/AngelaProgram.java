package berlin.yuna.hackerschool.session_private;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.AsyncRun;
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
    private static AsyncRun program_01;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {

        if (program_01 == null) {
            program_01 = loop("Police LED", run -> {
                stack.sensors().iO16().send(1, -5);
                stack.sensors().dualButton().send(-1, 2);
                stack.sensors().speaker().send(400, 2000);
                sleep(500);
                stack.sensors().iO16().send(-1, 5);
                stack.sensors().dualButton().send(1, -2);
                stack.sensors().speaker().send(400, 1000);
                sleep(500);
            });
        }

    }
}
