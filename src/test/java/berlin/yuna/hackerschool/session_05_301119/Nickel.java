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
public class Nickel extends Helper {

    //VARIABLES
    Stack stack;
    private static boolean ButtonPressed;
    private static int Schieberegler;
    private static int Drehregler;
    private static int Lautstaerke;
    private static int Takt;

    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {
        if (event.getValueType().isButtonPressed()) {
            ButtonPressed = !ButtonPressed;
        }
        if (ButtonPressed == false) {
            stack.sensors().buttonRGB().send(Color.RED);
            stack.sensors().displaySegment().send(8888);
            stack.sensors().displaySegment(1).send(time());
            stack.sensors().poiLinearMotor().send(0);
            stack.sensors().poiLinearMotor().send(true);
        }
        //Wenn an
        if (ButtonPressed == true) {
            if (timePassed(100)) {
                stack.sensors().displaySegment(1).send(Schieberegler);
            }
            //if (event.getValueType().isPercentage() && event.getValue() == 100) {
            if (timePassed(500)) {
                stack.sensors().displaySegment().send((stack.values().soundIntensity() / 100) + "DB");
            }
            if (Schieberegler == 100) {
                stack.sensors().buttonRGB().send(Color.RAINBOW);
                stack.sensors().poiLinearMotor().send(0);
            }
            if (event.sensor().compare().is(stack.sensors().poiLinearMotor()) && event.getValueType().isPercentage()) {
                Schieberegler = event.getValue().intValue();
            }

            stack.sensors().poiLinearMotor().send(false);
            if (timePassed(400)) {
                stack.sensors().displaySegment().send((stack.values().soundIntensity()) / 100 + "DB");
                stack.sensors().buttonRGB().send(Color.GREEN);
                Takt = Drehregler;
            }
            if (event.getValueType().isSoundIntensity()) {
                if (stack.values().soundIntensity() >= 40000) {
                    stack.sensors().displaySegment().send("LAUT");
                }
            }

            if (Schieberegler > 50 && Drehregler <=0) {
                Lautstaerke = (Drehregler +150) / 10;
            }
                if (timePassed(140)){
                    if (Drehregler >=0 && Drehregler <=150) {
                        stack.sensors().speaker().send(Takt, (Schieberegler * 100), Lautstaerke);
                    }
                }
                if (event.sensor().compare().is(stack.sensors().potiRotary()) && event.getValueType().isRotary()){
                    Drehregler = event.getValue().intValue();
                }
            }
        }


    //START FUNCTION
    public static void main(final String[] args) {
        Nickel nickel = new Nickel();
        nickel.stack = ConnectionAndPrintValues_Example.connect();
        nickel.stack.consumers.add(nickel::onSensorEvent);
    }
}
