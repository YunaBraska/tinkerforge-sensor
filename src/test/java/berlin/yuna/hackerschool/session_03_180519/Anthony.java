package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author Anthony
 */
public class Anthony extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    //VARIABLES
    private static Stack stack;
    private static int program = 0;
    private static boolean programIsRunning = false;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor Knopf1 = stack.sensors().buttonRGB(0);
        final Sensor Knopf2 = stack.sensors().buttonRGB(1);
        final Sensor Knopf3 = stack.sensors().buttonRGB(2);
        final Sensor display = stack.sensors().displayLcd20x4();

        if (stack.sensors().buttonRGB().isPresent()) {
            if (program == 0 && timePassed(200)) {
                Knopf1.send(Color.GREEN);
                Knopf2.send(Color.RED);
                Knopf3.send(Color.BLUE);
                stack.sensors().dualButton().ledAdditional_setOff();
                display.send("${clear}");
                display.send("Grün = Messstation  Rot = Distance Game Blau = Abstands Sensor");
                display.ledAdditional_setOn();
            }


        }

        if (!programIsRunning && program != -1) {
            if (sensor.compare().is(Knopf1)) {
                programIsRunning = true;
                program = 1;
            } else if (sensor.compare().is(Knopf2)) {
                programIsRunning = true;
                program = 2;
                Knopf1.send(Color.RED);
                Knopf2.send(Color.RED);
                Knopf3.send(Color.RED);
            } else if (sensor.compare().is(Knopf3)) {
                programIsRunning = true;
                program = 3;
            }
        }

        if (type.isButton() && value == 21) {
            program = -1;
            loopStop("Blinken");
            programIsRunning = false;
            sleep(500);
            program = 0;
        }

        final long distace = stack.values().distance();
        if (program == 3 && timePassed(distace)) {

            Knopf1.send(Color.BLACK);
            Knopf2.send(Color.BLACK);
            Knopf3.send(Color.BLACK);
            display.send("${clear}");
            stack.sensors().speaker().send(distace / 2);
        }


        if (program == 2 && timePassed(10)) {


            if (distace > 250 && distace < 300) {
                Knopf3.send(Color.GREEN);

            } else if (distace > 500 && distace < 550) {
                Knopf2.send(Color.GREEN);

            } else if (distace > 400 && distace < 450) {
                Knopf1.send(Color.GREEN);
                display.send("${clear}");
                stack.sensors().displayLcd20x4().send("Du hast gewonnen!");
            }
        }


        if (program == 1) {
            airQuality(sensor, value);
            DisplayAirPressure();
            DisplayTemperature(sensor, value);
        }

    }


    static void airQuality(final Sensor sensor, final Long value) {
        final Sensor Knopf1 = stack.sensors().buttonRGB(0);
        if (stack.sensors().buttonRGB().isPresent()) {

            final int airQuality = stack.values().airPressure().intValue();


            if (sensor.compare().is(Knopf1) && value == 1) {
                if (airQuality < 1050000) {
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Die airQuality istschlecht! Bitte     ein Fenster  öffnen.");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                    stack.sensors().speaker().send(300);
                    sleep(500);
                    stack.sensors().speaker().send(300);
                    sleep(500);
                    stack.sensors().speaker().send(300);
                    loop("Blinken", run -> BlinkKnopf1());

                } else {
                    Knopf1.send(Color.GREEN);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Die airQuality istakzeptabel.");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();

                }
            }
        }
    }

    static void DisplayAirPressure() {
        final Sensor Knopf3 = stack.sensors().buttonRGB(2);
        if (stack.sensors().buttonRGB().isPresent()) {


            if (Knopf3.values().buttonPressed() == 1) {

                Knopf3.send(Color.CYAN);
                final int luftdruck = stack.values().airPressure().intValue();
                stack.sensors().displayLcd20x4().send("${clear}");
                stack.sensors().displayLcd20x4().send("Der Luftdruck       beträgt " + (luftdruck / 1000000) + "BAR, das entspricht " + (luftdruck / 1000) + " mbar.");
                stack.sensors().displayLcd20x4().ledAdditional_setOn();
            }
        }
    }

    static void DisplayTemperature(final Sensor sensor, final Long value) {
        final Sensor Knopf2 = stack.sensors().buttonRGB(1);
        if (stack.sensors().buttonRGB().isPresent()) {

            if (sensor.compare().is(Knopf2) && value == 1) {
                final int temperatur = stack.values().temperature().intValue();

                if (temperatur > 2000) {
                    Knopf2.send(Color.GREEN);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Die Temperatur ist  Gut! Sie beträgt " + (temperatur / 100) + "°C");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                } else {
                    Knopf2.send(Color.RED);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Bitte Temperatur erhöhen, die Temperatur beträgt:" + (temperatur / 100) + " °C ");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                }
            }
        }
    }

    static void BlinkKnopf1() {
        final Sensor Knopf1 = stack.sensors().buttonRGB(0);

        Knopf1.send(Color.RED);
        sleep(500);
        Knopf1.send(Color.BLACK);
    }
}










