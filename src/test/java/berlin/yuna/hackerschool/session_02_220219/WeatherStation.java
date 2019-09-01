package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class WeatherStation extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    //VARIABLES
    public static Stack stack;
    private static int counter = 0;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        if (stack.sensors().buttonRGB().isPresent()) {
            final Sensor Knopf1 = stack.sensors().buttonRGB(0);
            final Sensor Knopf2 = stack.sensors().buttonRGB(1);
            final int light = stack.values().lightLux().intValue();
            final int airQuality = stack.values().airPressure().intValue();


            if (sensor.compare().is(Knopf1) && value == 1) {
                counter = counter + 1;
            }

            if (counter == 2) {
                stack.sensors().displayLcd20x4().send("Display: OFF, zum   Einschalten beliebige  Taste betätigen.");
                stack.sensors().displayLcd20x4().ledAdditional_setOff();
                counter = 0;
            } else if (Knopf1.values().buttonPressed() == 1 && Knopf2.values().buttonPressed() == 1) {
                if (airQuality < 1050000) {
                    Knopf2.send(Color.RED);
                    Knopf1.send(Color.RED);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Die Luftqualität istschlecht! Bite      ein Fenster  öffnen.");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                } else {
                    Knopf2.send(Color.GREEN);
                    Knopf1.send(Color.GREEN);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Die Luftqualität istakzeptabel.");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();

                }

            } else if (sensor.compare().is(Knopf1) && value == 1) {
                if (light < 4000) {
                    stack.sensors().buttonRGB().send(Color.RED);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Es ist sehr dunkel! \nMach doch Licht an ${space}");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                } else {
                    stack.sensors().buttonRGB().send(new Color(0, light / 1500, 0));
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Es ist Hell! ${space}");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                }
            } else if (sensor.compare().is(Knopf2) && value == 1) {
                final int temperatur = stack.values().temperature().intValue();

                if (temperatur > 2000) {
                    Knopf2.send(Color.GREEN);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Die Temperatur ist  Gut! Sie beträgt " + (temperatur / 100) + "°C");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                } else {
                    Knopf2.send(Color.RED);
                    stack.sensors().displayLcd20x4().send("${clear}");
                    stack.sensors().displayLcd20x4().send("Bitte Temperatur erhöhen, man könnte sich erkälten denn die Temperatur beträgt" + (temperatur / 100) + " °C ");
                    stack.sensors().displayLcd20x4().ledAdditional_setOn();
                }
            }
        }
    }
}
