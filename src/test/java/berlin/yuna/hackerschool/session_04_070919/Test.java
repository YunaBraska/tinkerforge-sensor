package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;


public class Test extends Helper {

    //VARIABLES
    private static Stack stack;

    static void onStart() {
    }

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {
        stack.sensors().displaySegment().sendLimit(2, stack.sensors().distanceUS().values().distance() / 100);

        if (event.getValueType().isPercentage()) {
            final Long volume = stack.sensors().potiRotary().values().rotary();
            stack.sensors().localAudio().send(2, volume);
            stack.sensors().localAudio().send(1, volume);
        }

        if (event.getValueType().isButton()) {
            if (event.getValues().get(0).intValue() == 1) {
                stack.sensors().localAudio().send(2, "sounds/insturment2.wav");
            } else if (event.getValues().get(1).intValue() == 1) {
                stack.sensors().localAudio().send(1, "sounds/akkustikgitarre.wav");
            }
        }
    }

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(Test::onSensorEvent);
        onStart();
    }


}
