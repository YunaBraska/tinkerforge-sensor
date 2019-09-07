package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;


public class Template02 extends Helper {

    //VARIABLES
    private static Stack stack;

    static void onStart(){
        loop("PoliceLED", run -> {
            stack.sensors().buttonDual().send(-1, 2);
        });
    }

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {


    }

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(Template02::onSensorEvent);
        onStart();
    }


}
