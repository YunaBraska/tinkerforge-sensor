package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

/**
 * @author yourNames
 */
public class ApplicationStart extends Helper {

    private static Stack stack;

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    private static int program;
    private static boolean start;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {

        if (type.isRotary()) {
            start = false;
        } else if (type.isButtonPressed() && sensor.compare().isRotary()) {
            start = true;
            asyncStop("beethoven");
        }

        if (timePassed(500)) {
            final Sensor display = stack.sensors().displayLcd20x4();
            final Sensor buttonRGB_1 = stack.sensors().buttonRGB(0);
            final Sensor buttonRGB_2 = stack.sensors().buttonRGB(1);

            if (!start) {
                //Reset buttons
                stack.sensors().forEach(Sensor::ledStatusOff);
                stack.sensors().stream().filter(s -> !s.compare().isDisplayLcd20x4()).forEach(Sensor::ledAdditionalOff);
            }
            if (!start && stack.values().rotary() == 1) {
                program = 1;
                display.send("${clear}");
                display.send("1. Programm: Wetterstation!");
                display.ledAdditionalOn();
                buttonRGB_1.ledStatusOn();
                buttonRGB_2.ledStatusOn();
                stack.sensors().lightAmbient().ledStatusOn();
                stack.sensors().airQuality().ledStatusOn();
            } else if (!start && stack.values().rotary() == 2) {
                program = 2;
                display.send("${clear}");
                display.send("2. Program: Reaction Game!");
                display.ledAdditionalOn();
                buttonRGB_1.ledStatusOn();
                buttonRGB_2.ledStatusOn();
            } else if (!start && stack.values().rotary() == 3) {
                program = 3;
                display.send("${clear}");
                display.send("3. Program: Uhrsula!");
                display.ledAdditionalOn();
                stack.sensors().lightAmbient().ledStatusOn();
            } else if (!start && stack.values().rotary() == 4) {
                program = 4;
                display.send("${clear}");
                display.send("4. Program: Beethoven!");
                display.ledAdditionalOn();
            }
        }

        if (start && program == 1) {
            WeatherStation.stack = stack;
            WeatherStation.onSensorEvent(sensor, value, type);
        } else if (start && program == 2) {
            ReactionGame.stack = stack;
            ReactionGame.onSensorEvent(sensor, value, type);
        } else if (start && program == 3) {
            Uhrsula.stack = stack;
            Uhrsula.onSensorEvent(sensor, value, type);
        } else if (start && program == 4) {
            Beethoven.stack = stack;
            Beethoven.onSensorEvent(sensor, value, type);
        }
    }
}