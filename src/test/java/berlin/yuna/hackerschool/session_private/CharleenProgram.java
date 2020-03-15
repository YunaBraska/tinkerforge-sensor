package berlin.yuna.hackerschool.session_private;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

/**
 * @author Charleen
 */
public class CharleenProgram extends Helper {

    //VARIABLES
    private static Stack stack;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {

        stack.sensors().displaySegment().send("Love");

        if (event.getValueType().isMotionDetected() && event.getValue() == 1) {
            stack.sensors().displayLcd128x64().ledAdditional_setOn();
            stack.sensors().displayLcd128x64().send("Hello welcome :)");
            stack.sensors().speaker().send(2000, 100, 3);
            stack.sensors().motionDetector().send(1);
        }

        if (event.getValueType().isMotionDetected() && event.getValue() == 0) {
            stack.sensors().displayLcd128x64().ledAdditional_setOff();
            stack.sensors().motionDetector().send(0);
        }
    }

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(CharleenProgram::onSensorEvent);
    }
}
