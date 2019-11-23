package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author Maria && Sofia
 */
public class MariaSofia extends Helper {

    private static Stack stack;

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    //VARIABLES
    static boolean running = false;
    static boolean switchbeep = false;


    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {


        //Bewegungsmelder

        //if (type.isMotionDetected() == true && ){
            //stack.sensors().iO16().ledAdditional_setOn();
            //for(int i = 0; i < 16; i++) {
                //send.(i);

           // }

    //    }

        //Frequenzbutton
        if (type.isButtonPressed()) {
            switchbeep = !switchbeep;


        }
        if (switchbeep && timePassed(10) && stack.values().rotary() > 0) {
            stack.sensors().speaker().send(50, stack.values().rotary() * 500);
        }



        //Licht
        if (switchbeep == false && type.isSoundIntensity() && value > 1100) {
            stack.sensors().iO16().ledAdditional_setOn();
        }
        if (type.isSoundIntensity() && value > 1100 && !running) {
            running = true;


            async("girlYouCanDuIt", run -> {
                stack.sensors().displaySegment().send("GIRL");
                sleep(1000);
                stack.sensors().displaySegment().send("YOU");
                sleep(1000);
                stack.sensors().displaySegment().send("CAN");
                sleep(1000);
                stack.sensors().displaySegment().send("DO");
                sleep(1000);
                stack.sensors().displaySegment().send("IT");
                sleep(1000);
                running = false;
            });
        } else if (type.isSoundIntensity() && value < 1100) {
            stack.sensors().iO16().ledAdditional_setOff();

            //tinkerForge.sensors().iO16().send();
        }


    }

}




