package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class ApplicationStart extends Helper {

    private static TinkerForge tinkerForge;

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    private static int program;
    private static boolean start;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        if (type.isRotary()) {
            start = false;
        } else if (type.isButtonPressed() && sensor.is(tinkerForge.sensors().rotary())) {
            start = true;
            asyncStop("beethoven");
        }

        if (timePassed(500)) {
            final Sensor display = tinkerForge.sensors().displayLcd20x4();
            final Sensor buttonRGB_1 = tinkerForge.sensors().buttonRGB(0);
            final Sensor buttonRGB_2 = tinkerForge.sensors().buttonRGB(1);

            if (!start) {
                //Reset buttons
                tinkerForge.sensors().forEach(Sensor::ledStatusOff);
                tinkerForge.sensors().stream().filter(s -> !s.is(display)).forEach(Sensor::ledAdditionalOff);
            }
            if (!start && tinkerForge.values().rotary() == 1) {
                program = 1;
                display.send("${clear}");
                display.send("1. Programm: Wetterstation!");
                display.ledAdditionalOn();
                buttonRGB_1.ledStatusOn();
                buttonRGB_2.ledStatusOn();
                tinkerForge.sensors().lightAmbient().ledStatusOn();
                tinkerForge.sensors().airQuality().ledStatusOn();
            } else if (!start && tinkerForge.values().rotary() == 2) {
                program = 2;
                display.send("${clear}");
                display.send("2. Program: Reaction Game!");
                display.ledAdditionalOn();
                buttonRGB_1.ledStatusOn();
                buttonRGB_2.ledStatusOn();
            } else if (!start && tinkerForge.values().rotary() == 3) {
                program = 3;
                display.send("${clear}");
                display.send("3. Program: Uhrsula!");
                display.ledAdditionalOn();
                tinkerForge.sensors().lightAmbient().ledStatusOn();
            } else if (!start && tinkerForge.values().rotary() == 4) {
                program = 4;
                display.send("${clear}");
                display.send("4. Program: Beethoven!");
                display.ledAdditionalOn();
            }
        }

        if (start && program == 1) {
            WeatherStation.tinkerForge = tinkerForge;
            WeatherStation.onSensorEvent(sensor, value, type);
        } else if (start && program == 2) {
            ReactionGame.tinkerForge = tinkerForge;
            ReactionGame.onSensorEvent(sensor, value, type);
        } else if (start && program == 3) {
            Uhrsula.tinkerForge = tinkerForge;
            Uhrsula.onSensorEvent(sensor, value, type);
        } else if (start && program == 4) {
            Beethoven.tinkerForge = tinkerForge;
            Beethoven.onSensorEvent(sensor, value, type);
        }
    }
}