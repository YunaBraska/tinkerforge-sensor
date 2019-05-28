package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;

/**
 * @author Anthony
 */
public class Anthony extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    private static TinkerForge tinkerForge;
    private static final int counter = 0;
    private static int programm = 0;
    private static boolean programIsRunning = false;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor Knopf1 = tinkerForge.sensors().buttonRGB(0);
        final Sensor Knopf2 = tinkerForge.sensors().buttonRGB(1);
        final Sensor Knopf3 = tinkerForge.sensors().buttonRGB(2);
        final Sensor display = tinkerForge.sensors().displayLcd20x4();

        if (tinkerForge.sensors().buttonRGB().isPresent()) {
            if (programm == 0 && timePassed(200)) {
                Knopf1.send(Color.GREEN);
                Knopf2.send(Color.RED);
                Knopf3.send(Color.BLUE);
                tinkerForge.sensors().dualButton().ledAdditionalOff();
                display.send("${clear}");
                display.send("Grün = Messstation  Rot = Distance Game Blau = Abstands Sensor");
                display.ledAdditionalOn();
            }


        }

        if (!programIsRunning && programm != -1) {
            if (sensor.is(Knopf1)) {
                programIsRunning = true;
                programm = 1;
            } else if (sensor.is(Knopf2)) {
                programIsRunning = true;
                programm = 2;
                Knopf1.send(Color.RED);
                Knopf2.send(Color.RED);
                Knopf3.send(Color.RED);
            } else if (sensor.is(Knopf3)) {
                programIsRunning = true;
                programm = 3;
            }
        }

        if (type.isButton() && value == 21) {
            programm = -1;
            loopStop("Blinken");
            programIsRunning = false;
            sleep(500);
            programm = 0;
        }

        final long distace = tinkerForge.values().distance();
        if (programm == 3 && timePassed(distace)) {

            Knopf1.send(Color.BLACK);
            Knopf2.send(Color.BLACK);
            Knopf3.send(Color.BLACK);
            display.send("${clear}");
            tinkerForge.sensors().speaker().send(distace / 2);
        }


        if (programm == 2 && timePassed(10)) {


            if (distace > 250 && distace < 300) {
                Knopf3.send(Color.GREEN);

            } else if (distace > 500 && distace < 550) {
                Knopf2.send(Color.GREEN);

            } else if (distace > 400 && distace < 450) {
                Knopf1.send(Color.GREEN);
                display.send("${clear}");
                tinkerForge.sensors().displayLcd20x4().send("Du hast gewonnen!");
            }
        }


        if (programm == 1) {
            airQuality(sensor, value);
            DisplayLuftdruck();
            DisplayTemperature(sensor, value);
        }

    }


    static void airQuality(final Sensor sensor, final Long value) {

        final Sensor Knopf2 = tinkerForge.sensors().buttonRGB(1);
        final Sensor Knopf1 = tinkerForge.sensors().buttonRGB(0);
        if (tinkerForge.sensors().buttonRGB().isPresent()) {

            final int airQuality = tinkerForge.values().airPressure().intValue();


            if (sensor.is(Knopf1) && value == 1) {
                if (airQuality < 1050000) {
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Die airQuality istschlecht! Bitte     ein Fenster  öffnen.");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                    tinkerForge.sensors().speaker().send(300);
                    sleep(500);
                    tinkerForge.sensors().speaker().send(300);
                    sleep(500);
                    tinkerForge.sensors().speaker().send(300);
                    loop("Blinken", run -> BlinkenKnopf1());

                } else {
                    Knopf1.send(Color.GREEN);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Die airQuality istakzeptabel.");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();

                }
            }
        }
    }

    static void DisplayLuftdruck() {

        final Sensor Knopf2 = tinkerForge.sensors().buttonRGB(1);
        final Sensor Knopf1 = tinkerForge.sensors().buttonRGB(0);
        final Sensor Knopf3 = tinkerForge.sensors().buttonRGB(2);
        if (tinkerForge.sensors().buttonRGB().isPresent()) {


            if (Knopf3.send(BUTTON_PRESSED) == 1) {

                Knopf3.send(Color.CYAN);
                final int luftdruck = tinkerForge.values().airPressure().intValue();
                tinkerForge.sensors().displayLcd20x4().send("${clear}");
                tinkerForge.sensors().displayLcd20x4().send("Der Luftdruck       beträgt " + (luftdruck / 1000000) + "BAR, das entspricht " + (luftdruck / 1000) + " mbar.");
                tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
            }
        }
    }

    static void DisplayTemperature(final Sensor sensor, final Long value) {
        final Sensor Knopf2 = tinkerForge.sensors().buttonRGB(1);
        if (tinkerForge.sensors().buttonRGB().isPresent()) {

            if (sensor.is(Knopf2) && value == 1) {
                final int temperatur = tinkerForge.values().temperature().intValue();

                if (temperatur > 2000) {
                    Knopf2.send(Color.GREEN);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Die Temperatur ist  Gut! Sie beträgt " + (temperatur / 100) + "°C");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                } else {
                    Knopf2.send(Color.RED);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Bitte Temperatur erhöhen, die Temperatur beträgt:" + (temperatur / 100) + " °C ");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                }
            }
        }
    }

    static void BlinkenKnopf1() {
        final Sensor Knopf1 = tinkerForge.sensors().buttonRGB(0);

        Knopf1.send(Color.RED);
        sleep(500);
        Knopf1.send(Color.BLACK);
    }
}










