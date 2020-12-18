package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.thread.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.threads.Color;


public class Mika_Nick extends Helper {

    //VARIABLES
    private static Stack stack;

    private static boolean buttonClicked;


    private static long volumeMax = 0;

    //CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {


        final Long volume = stack.sensors().soundIntensity().values().soundIntensity();
        final Sensor io16 = stack.sensors().iO16();
        if (volume >= volumeMax) {
            volumeMax = volume;
        }
        if (event.getValueType().isButtonPressed()) {
            buttonClicked = !buttonClicked;
        }

        if (timePassed(50)) {
            if (buttonClicked == true) {
                if (timePassed(500)) {
                    stack.sensors().displaySegment().send(volume / 100 + "DB");
                }
                stack.sensors().buttonRGB().send(Color.GREEN);


                final int ledCount = (int) (volume / ((volumeMax / 18) + 1));

                //Switch LEDs on
                for (int led = 1; led < ledCount; led++) {
                    io16.send(led);
                }

                //Switch other LEDs off
                for (int led = ledCount; led < 17; led++) {
                    io16.send(-led);
                }
            } else {
                stack.sensors().displaySegment().send("    ");
                stack.sensors().iO16().ledAdditional_setOff();
                stack.sensors().buttonRGB().send(Color.RED);
                volumeMax = 0;
            }
        }
    }


    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(Mika_Nick::onSensorEvent);
    }


}

