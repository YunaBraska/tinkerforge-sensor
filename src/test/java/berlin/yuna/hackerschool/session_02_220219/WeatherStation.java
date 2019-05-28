package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;

/**
 * @author yourNames
 */
public class WeatherStation extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static TinkerForge tinkerForge;
    private static int counter = 0;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        if (tinkerForge.sensors().buttonRGB().isPresent()) {
            final Sensor Knopf1 = tinkerForge.sensors().buttonRGB(0);
            final Sensor Knopf2 = tinkerForge.sensors().buttonRGB(1);
            final int light = tinkerForge.values().lightLux().intValue();
            final int airQuality = tinkerForge.values().airPressure().intValue();


            if (sensor.is(Knopf1) && value == 1) {
                counter = counter + 1;
            }

            if (counter == 2) {
                tinkerForge.sensors().displayLcd20x4().send("Display: OFF, zum   Einschalten beliebige  Taste betätigen.");
                tinkerForge.sensors().displayLcd20x4().ledAdditionalOff();
                counter = 0;
            } else if (Knopf1.send(BUTTON_PRESSED) == 1 && Knopf2.send(BUTTON_PRESSED) == 1) {
                if (airQuality < 1050000) {
                    Knopf2.send(Color.RED);
                    Knopf1.send(Color.RED);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Die Luftqualität istschlecht! Bite      ein Fenster  öffnen.");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                } else {
                    Knopf2.send(Color.GREEN);
                    Knopf1.send(Color.GREEN);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Die Luftqualität istakzeptabel.");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();

                }

            } else if (sensor.is(Knopf1) && value == 1) {
                if (light < 4000) {
                    tinkerForge.sensors().buttonRGB().send(Color.RED);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Es ist sehr dunkel! \nMach doch Licht an ${space}");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                } else {
                    tinkerForge.sensors().buttonRGB().send(new Color(0, light / 1500, 0));
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Es ist Hell! ${space}");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                }
            } else if (sensor.is(Knopf2) && value == 1) {
                final int temperatur = tinkerForge.values().temperature().intValue();

                if (temperatur > 2000) {
                    Knopf2.send(Color.GREEN);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Die Temperatur ist  Gut! Sie beträgt " + (temperatur / 100) + "°C");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                } else {
                    Knopf2.send(Color.RED);
                    tinkerForge.sensors().displayLcd20x4().send("${clear}");
                    tinkerForge.sensors().displayLcd20x4().send("Bitte Temperatur erhöhen, man könnte sich erkälten denn die Temperatur beträgt" + (temperatur / 100) + " °C ");
                    tinkerForge.sensors().displayLcd20x4().ledAdditionalOn();
                }
            }
        }
    }
}
