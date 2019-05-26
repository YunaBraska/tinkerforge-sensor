package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author Maria && Sofia
 */
public class MariaSofia extends Helper {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    //START FUNCTION
    public static void main(final String[] args) {
        final SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    static boolean running = false;
    static boolean switchbeep = false;


    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {


        //Bewegungsmelder

        //if (type.isMotionDetected() == true && ){
            //sensorList.getIO16().ledAdditionalOn();
            //for(int i = 0; i < 16; i++) {
                //value.(i);

           // }

    //    }

        //Frequenzbutton
        if (type.isButtonPressed() && value == 1) {
            switchbeep = !switchbeep;


        }
        if (switchbeep && timePassed(10) && sensorList.getValueRotary() > 0) {
            sensorList.getSpeaker().value(50, sensorList.getValueRotary() * 500);

        }



        //Licht
        if (switchbeep == false && type.isSoundIntensity() && value > 1100) {
            sensorList.getIO16().ledAdditionalOn();
        }
        if (type.isSoundIntensity() && value > 1100 && !running) {
            running = true;


            async("girlYouCanDuIt", run -> {
                sensorList.getDisplaySegment().value("GIRL");
                sleep(1000);
                sensorList.getDisplaySegment().value("YOU");
                sleep(1000);
                sensorList.getDisplaySegment().value("CAN");
                sleep(1000);
                sensorList.getDisplaySegment().value("DO");
                sleep(1000);
                sensorList.getDisplaySegment().value("IT");
                sleep(1000);
                //sensorList.getIO16().value();
                running = false;
            });
        } else if (type.isSoundIntensity() && value < 1100) {
            sensorList.getIO16().ledAdditionalOff();

            //sensorList.getIO16().value();
        }


    }

}




