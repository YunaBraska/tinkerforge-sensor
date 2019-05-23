package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
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
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static SensorList<Sensor> sensorList = new SensorList<>();
    private static int counter = 0;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

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
}
