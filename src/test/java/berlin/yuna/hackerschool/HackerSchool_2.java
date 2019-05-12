package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.example.ConnectionAndPrintValues_Example;
import berlin.yuna.tinkerforgesensor.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.util.Random;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;

public class HackerSchool_2 extends Helper {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    private static int counter = 0;


    //Start method initializer
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }


    private static int program;
    private static boolean start;

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        if (type.isRotary()) {
            start = false;
        } else if (type.isButtonPressed() && sensor.is(sensorList.getRotary())) {
            start = true;
        }

        if (timePassed(500)) {
            Sensor display = sensorList.getDisplayLcd20x4();
            Sensor buttonRGB_1 = sensorList.getButtonRGB(0);
            Sensor buttonRGB_2 = sensorList.getButtonRGB(1);

            if (!start) {
                //Reset buttons
                sensorList.forEach(Sensor::ledStatusOff);
                sensorList.stream().filter(s -> !s.is(display)).forEach(Sensor::ledAdditionalOff);
            }
            if (!start && sensorList.getValueRotary() == 1) {
                program = 1;
                display.value("${clear}");
                display.value("1. Programm: Wetterstation!");
                display.ledAdditionalOn();
                buttonRGB_1.ledStatusOn();
                buttonRGB_2.ledStatusOn();
                sensorList.getLightAmbient().ledStatusOn();
                sensorList.getAirQuality().ledStatusOn();
            } else if (!start && sensorList.getValueRotary() == 2) {
                program = 2;
                display.value("${clear}");
                display.value("2. Program: Reaction Game!");
                display.ledAdditionalOn();
                buttonRGB_1.ledStatusOn();
                buttonRGB_2.ledStatusOn();
            } else if (!start && sensorList.getValueRotary() == 3) {
                program = 3;
                display.value("${clear}");
                display.value("3. Program: Uhrsula!");
                display.ledAdditionalOn();
                sensorList.getLightAmbient().ledStatusOn();
            } else if (!start && sensorList.getValueRotary() == 4) {
                program = 4;
                display.value("${clear}");
                display.value("4. Program: Beethoven!");
                display.ledAdditionalOn();
            }
        }


        if (start && program == 1) {
            program_1_wetterstation(sensor, value, type);
        } else if (start && program == 2) {
            program_reactionGame(sensor, value, type);
        } else if (start && program == 3) {
            program_uhrsula(sensor, value, type);
        } else if (start && program == 4) {
            program_beethoven(sensor, value, type);
        }

    }


    private static void program_1_wetterstation(final Sensor sensor, final Long value, final ValueType type) {

        if (sensorList.getButtonRGB().isPresent()) {
            Sensor Knopf1 = sensorList.getButtonRGB(0);
            Sensor Knopf2 = sensorList.getButtonRGB(1);
            int light = sensorList.getValueLightLux().intValue();
            int airQuality = sensorList.getValueAirPressure().intValue();


            if (sensor.is(Knopf1) && value == 1) {
                counter = counter + 1;
            }

            if (counter == 2) {
                sensorList.getDisplayLcd20x4().value("Display: OFF, zum   Einschalten beliebige  Taste betätigen.");
                sensorList.getDisplayLcd20x4().ledAdditionalOff();
                counter = 0;
            } else if (Knopf1.value(BUTTON_PRESSED) == 1 && Knopf2.value(BUTTON_PRESSED) == 1) {
                if (airQuality < 1050000) {
                    Knopf2.value(Color.RED);
                    Knopf1.value(Color.RED);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Die Luftqualität istschlecht! Bite      ein Fenster  öffnen.");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                } else {
                    Knopf2.value(Color.GREEN);
                    Knopf1.value(Color.GREEN);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Die Luftqualität istakzeptabel.");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();

                }

            } else if (sensor.is(Knopf1) && value == 1) {
                if (light < 4000) {
                    sensorList.getButtonRGB().value(Color.RED);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Es ist sehr dunkel! \nMach doch Licht an ${space}");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                } else {
                    sensorList.getButtonRGB().value(new Color(0, light / 1500, 0));
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Es ist Hell! ${space}");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                }
            } else if (sensor.is(Knopf2) && value == 1) {
                int temperatur = sensorList.getValueTemperature().intValue();

                if (temperatur > 2000) {
                    Knopf2.value(Color.GREEN);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Die Temperatur ist  Gut! Sie beträgt " + (temperatur / 100) + "°C");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                } else {
                    Knopf2.value(Color.RED);
                    sensorList.getDisplayLcd20x4().value("${clear}");
                    sensorList.getDisplayLcd20x4().value("Bitte Temperatur erhöhen, man könnte sich erkälten denn die Temperatur beträgt" + (temperatur / 100) + " °C ");
                    sensorList.getDisplayLcd20x4().ledAdditionalOn();
                }
            }
        }
    }

    private static int score = -1;
    private static boolean reactionNow = false;

    private static void program_reactionGame(final Sensor sensor, final Long value, final ValueType type) {
        Sensor displayLcd = sensorList.getDisplayLcd20x4();
        Sensor displaySegment = sensorList.getDisplaySegment();
        Sensor buttonRGB = sensorList.getButtonRGB();
        Sensor speaker = sensorList.getSpeaker();

        //SCORE VIEW
        if (score > -1) {
            displaySegment.value("P" + score);
            displayLcd.value("Score: " + score + " ${space}");
        }

        //GAME END EVENTS
        if (score == 0) {
            score = -1;
            speaker.value(778, 5000, false);
            displayLcd.value("${1}${space}Game Over! ${space}");
        } else if (score == 10) {
            score = -1;
            displayLcd.value("${1}${space}You Win!${space}");
            loop("reaction_win", run -> {
                speaker.flashLed();
                buttonRGB.flashLed();
            });
        }

        //TIME BASED EVENTS
        if (timePassed("reactionGame", 50)) {
            //random yellow
            if (timePassed("yellow", 1500 + new Random().nextInt(9000)) && !reactionNow && score > -1) {
                sensorList.getButtonRGB().value(Color.YELLOW);
                reactionNow = true;
                displayLcd.value("${1}Do it now! ${space}");
            } else if (reactionNow && timePassed("yellow", 600 + new Random().nextInt(1000))) {
                //SCORE DOWN - didnt make it in time
                reactionNow = false;
                speaker.value(200, 5000, false);
                buttonRGB.value(Color.RED);
                displayLcd.value("${1}Missed it!${space}");
                score = score - 1;
            }
        }

        //BUTTON EVENTS
        if (type.isButtonPressed() && value == 1L) {
            if (sensor.is(sensorList.getRotary())) {
                //RESET GAME
                score = -1;
                buttonRGB.ledStatusOn();
                async("3-2-1-GO", run -> reactionGameStart());
            } else if (reactionNow) {
                //SCORE UP - made it in time
                reactionNow = false;
                score = score + 1;
                speaker.value(200, 2000, false);
                displayLcd.value("${1}Got it!${space}");
                buttonRGB.value(Color.GREEN);
            } else if (!reactionNow) {
                //SCORE DOWN - accident press
                score = score - 1;
                buttonRGB.value(Color.RED);
                speaker.value(200, 5000, false);
                displayLcd.value("${1} Too early! ${space}");
            }
        }

    }

    private static void reactionGameStart() {
        Sensor displaySegment = sensorList.getDisplaySegment();
        Sensor buttonRGB = sensorList.getButtonRGB();
        Sensor displayLcd = sensorList.getDisplayLcd20x4();
        Sensor speaker = sensorList.getSpeaker();

        if (score == -1) {
            score = -2;
            loopStop("reaction_win");
            displayLcd.value("${clear}");
            buttonRGB.ledStatusOn();
            displayLcd.ledStatusOn();
            displayLcd.ledAdditionalOn();
            displaySegment.value(3);
            displayLcd.value("${space} 3 ${space}");
            buttonRGB.value(Color.RED);
            speaker.value(200, 3000, false);
            sleep(1024);

            displaySegment.value(2);
            displayLcd.value("${space} 2 ${space}");
            buttonRGB.value(Color.YELLOW);
            speaker.value(200, 3000, false);
            sleep(1024);

            displaySegment.value(1);
            displayLcd.value("${space} 1 ${space}");
            buttonRGB.value(Color.GREEN);
            speaker.value(200, 3000, false);
            sleep(1024);
            displaySegment.value("-GO-");
            displayLcd.value("${space} -GO- ${space}");
            speaker.value(512, 2000, false);
            sleep(512);
            score = 5;
        }
    }

    public static int counter2 = 0;

    private static void program_uhrsula(final Sensor sensor, final Long value, final ValueType type) {
        if (timePassed(1000)) {
            if (sensorList.getValueLightLux() > 2000) {
                counter2 = counter2 + 1;
            }
            if (sensorList.getValueLightLux() < 2000) {
                counter2 = counter2 - 1;
            }
            sensorList.getDisplaySegment().value(counter2);
        }
    }


    public static int blink = 0;
    public static boolean isRunning = false;
    public static long orientation = 0L;

    private static void program_beethoven(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isOrientationRoll() && type.containsOrientationRoll()) {
            orientation = value;
        }

        if (blink == 1 && !isRunning) {
            isRunning = true;
            loop("beethoven", beethoven -> isRunning = beethoven());
        }

        if (type.isButtonPressed() && value == 1) {
            blink = 1;
        } else if (type.isDistance()) {
            blink = 0;
        }
    }

    private static boolean beethoven() {
        for (int i = 600; i < 1200; i++) {
            sensorList.getSpeaker().value(i, i, false);
        }
        sensorList.getSpeaker().value(500, 2590 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 2590 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 700 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 2590 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 700 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 586 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 650 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 850 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1200 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1400 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1600 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1800 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 2000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 587 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 587 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 800 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 800 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(555, 587 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1200 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1200 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 675 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 675 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(350, 1000 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(555, 586 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 586 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 650 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 850 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1050 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1250 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1450 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1650 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 1850 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 2050 + sensorList.getValueOrientationRoll(), true);
        sensorList.getSpeaker().value(200, 2250 + sensorList.getValueOrientationRoll(), true);
        loopStop("beethoven");
        return false;
    }


}