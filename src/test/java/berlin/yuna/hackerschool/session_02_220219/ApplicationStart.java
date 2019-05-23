package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class ApplicationStart extends Helper {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    //START FUNCTION
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    private static int program;
    private static boolean start;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        if (type.isRotary()) {
            start = false;
        } else if (type.isButtonPressed() && sensor.is(sensorList.getRotary())) {
            start = true;
            asyncStop("beethoven");
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
            WeatherStation.sensorList = sensorList;
            WeatherStation.onSensorEvent(sensor, value, type);
        } else if (start && program == 2) {
            ReactionGame.sensorList = sensorList;
            ReactionGame.onSensorEvent(sensor, value, type);
        } else if (start && program == 3) {
            Uhrsula.sensorList = sensorList;
            Uhrsula.onSensorEvent(sensor, value, type);
        } else if (start && program == 4) {
            Beethoven.sensorList = sensorList;
            Beethoven.onSensorEvent(sensor, value, type);
        }
    }
}