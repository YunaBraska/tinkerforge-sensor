package berlin.yuna.hackerschool.session_05_301119;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import java.util.Random;


public class Template extends Helper {

    //VARIABLES
    Stack stack;

    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {


        //EXAMPLE CODE
        int myNumber = new Random().nextInt(99);
        if (event.getValueType().isButtonPressed()) {
            stack.sensors().forEach(sensor -> sensor.send(myNumber));
            stack.sensors().display().send(myNumber);
        }


    }

    //START FUNCTION
    public static void main(final String[] args) {
        Template template = new Template();
        template.stack = ConnectionAndPrintValues_Example.connect();
        template.stack.consumers.add(template::onSensorEvent);
    }
}
