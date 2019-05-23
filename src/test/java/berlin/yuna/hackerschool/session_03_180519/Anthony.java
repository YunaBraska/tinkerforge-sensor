package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
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
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static SensorList<Sensor> sensorList = new SensorList<>();
    private static int counter = 0;
    private static int programm = 0;
    private static boolean programIsRunning = false;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        Sensor Knopf1 = sensorList.getButtonRGB(0);
        Sensor Knopf2 = sensorList.getButtonRGB(1);
        Sensor Knopf3 = sensorList.getButtonRGB(2);
        Sensor display = sensorList.getDisplayLcd20x4();

        if (sensorList.getButtonRGB().isPresent()) {
            if (programm == 0 && timePassed(200)) {
                Knopf1.value(Color.GREEN);
                Knopf2.value(Color.RED);
                Knopf3.value(Color.BLUE);
                sensorList.getDualButton().ledAdditionalOff();
                display.value("${clear}");
                display.value("Grün = Messstation  Rot = Distance Game Blau = Abstands Sensor");
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
                Knopf1.value(Color.RED);
                Knopf2.value(Color.RED);
                Knopf3.value(Color.RED);
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

        long distace = sensorList.getValueDistance();
        if (programm == 3 && timePassed(distace)) {

            Knopf1.value(Color.BLACK);
            Knopf2.value(Color.BLACK);
            Knopf3.value(Color.BLACK);
            display.value("${clear}");
            sensorList.getSpeaker().value(distace / 2);
        }


        if (programm == 2 && timePassed(10)) {


            if (distace > 250 && distace < 300) {
                Knopf3.value(Color.GREEN);

            } else if (distace > 500 && distace < 550) {
                Knopf2.value(Color.GREEN);

            } else if (distace > 400 && distace < 450) {
                Knopf1.value(Color.GREEN);
                display.value("${clear}");
                sensorList.getDisplayLcd20x4().value("Du hast gewonnen!");
            }
        }


        if (programm == 1) {
            Luftqualität(sensor, value);
            DisplayLuftdruck();
            DisplayTemperatur(sensor, value);
        }

    }


    static void Luftqualität(final Sensor sensor, final Long value) {

        Sensor Knopf2 = sensorList.getButtonRGB(1);
        Sensor Knopf1 = sensorList.getButtonRGB(0);
        if (sensorList.getButtonRGB().isPresent()) {

            int airQuality = sensorList.getValueAirPressure().intValue();


            if (sensor.is(Knopf1) && value == 1) {
                if (airQuality < 1050000) {
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Die Luftqualität istschlecht! Bitte     ein Fenster  öffnen.");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                    sensorList.getSpeaker().value(300);
                    sleep(500);
                    sensorList.getSpeaker().value(300);
                    sleep(500);
                    sensorList.getSpeaker().value(300);
                    loop("Blinken", run -> BlinkenKnopf1());

                } else {
                    Knopf1.value(Color.GREEN);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Die Luftqualität istakzeptabel.");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();

                }
            }
        }
    }

    static void DisplayLuftdruck() {

        Sensor Knopf2 = sensorList.getButtonRGB(1);
        Sensor Knopf1 = sensorList.getButtonRGB(0);
        Sensor Knopf3 = sensorList.getButtonRGB(2);
        if (sensorList.getButtonRGB().isPresent()) {


            if (Knopf3.value(BUTTON_PRESSED) == 1) {

                Knopf3.value(Color.CYAN);
                int luftdruck = sensorList.getValueAirPressure().intValue();
                sensorList.getDisplayLcd20x4().value("${clear}");
                sensorList.getDisplayLcd20x4().value("Der Luftdruck       beträgt " + (luftdruck / 1000000) + "BAR, das entspricht " + (luftdruck / 1000) + " mbar.");
                sensorList.getDisplayLcd20x4().ledAdditionalOn();
            }
        }
    }

    static void DisplayTemperatur(final Sensor sensor, final Long value) {
        Sensor Knopf2 = sensorList.getButtonRGB(1);
        if (sensorList.getButtonRGB().isPresent()) {

            if (sensor.is(Knopf2) && value == 1) {
                int temperatur = sensorList.getValueTemperature().intValue();

                if (temperatur > 2000) {
                    Knopf2.value(Color.GREEN);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Die Temperatur ist  Gut! Sie beträgt " + (temperatur / 100) + "°C");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                } else {
                    Knopf2.value(Color.RED);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Bitte Temperatur erhöhen, die Temperatur beträgt:" + (temperatur / 100) + " °C ");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                }
            }
        }
    }

    static void BlinkenKnopf1() {
        Sensor Knopf1 = sensorList.getButtonRGB(0);

        Knopf1.value(Color.RED);
        sleep(500);
        Knopf1.value(Color.BLACK);
    }
}










