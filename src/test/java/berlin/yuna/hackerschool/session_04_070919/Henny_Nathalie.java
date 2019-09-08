package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;


public class Henny_Nathalie extends Helper {

    //VARIABLES
    private static Stack stack;
    private static boolean ledIstAn;


    static void onStart() {
    }

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {

        Sensor knopf01 = stack.sensors().buttonRGB(0);
        Sensor knopf02 = stack.sensors().buttonRGB(1);

        if (event.getValueType().isButtonPressed() && event.sensor().compare().is(knopf01)) {
            ledIstAn = !ledIstAn;

            if (!ledIstAn) {
                stack.sensors().buttonRGB(1).send(Color.BLACK);
            }
        }


        if (ledIstAn) {
            knopf01.send(Color.MAGENTA);
        } else {
            knopf01.send(Color.BLUE);
        }


        final long distanz = stack.values().distance() / 10;
        stack.sensors().displaySegment().send(distanz + "cm");


        if (ledIstAn) {
            if (distanz < 20) {
                if (timePassed(distanz * 10)) {
                    stack.sensors().speaker().send(100, 9000);
                }


            } else if (event.getValueType().isDistance()) {
                if (event.getValue() < 250) {
                    knopf02.send(Color.RED);
                } else {
                    knopf02.send(Color.GREEN);
                }

            }
        }

    }


    //START Funktion

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(Henny_Nathalie::onSensorEvent);
        onStart();

    }
}
