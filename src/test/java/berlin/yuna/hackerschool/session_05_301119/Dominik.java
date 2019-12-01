package berlin.yuna.hackerschool.session_05_301119;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

/**
 * Code Link: http://hs.yuna.berlin/
 * Sensor Doku Link: https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md
 */
public class Dominik extends Helper {

    //VARIABLES
    Stack stack;
    private static boolean ButtonPressed;
    private  static  int Schieberegler;
    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {
        if (event.getValueType().isButtonPressed()){
            ButtonPressed = !ButtonPressed;
        }
        if (ButtonPressed == false){
            stack.sensors().buttonRGB().send(Color.RED);
            stack.sensors().displaySegment().send(8888);
            stack.sensors().displaySegment(1).send(8888);
            stack.sensors().poiLinearMotor().send(0);
            stack.sensors().poiLinearMotor().send(true);
        }
        if (ButtonPressed == true){
            if (timePassed(400)){
                stack.sensors().displaySegment().send((stack.values().soundIntensity() / 100) + "DB");
            }
            if (event.getValueType().isPercentage() && event.getValue() == 100){
                stack.sensors().poiLinearMotor().send(0);
            }
            if (event.getValueType().isPercentage()){
                Schieberegler = event.getValue().intValue();
            }
            stack.sensors().displaySegment(1).send(Schieberegler);
            stack.sensors().buttonRGB().send(Color.GREEN);
            stack.sensors().poiLinearMotor().send(false);
            if (timePassed(400)){
                stack.sensors().displaySegment().send((stack.values().soundIntensity()) / 100 + "DB");



            }

        }

    }


    //START FUNCTION
    public static void main(final String[] args) {
        Dominik template = new Dominik();
        template.stack = ConnectionAndPrintValues_Example.connect();
        template.stack.consumers.add(template::onSensorEvent);
    }
}
