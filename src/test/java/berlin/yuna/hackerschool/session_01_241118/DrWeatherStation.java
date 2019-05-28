package berlin.yuna.hackerschool.session_01_241118;

import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;

import static java.lang.String.format;

/**
 * @author yourNames
 */
public class DrWeatherStation extends Helper {

    private static TinkerForge tinkerForge;

    //START FUNCTION
    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] send [%s]", sensor.name, type, value));
        }

        final Sensor display = tinkerForge.sensors().displayLcd20x4();

        display.ledAdditionalOn();
        if (type.isButtonPressed()) {
            if (value == 11) {
                display.send("${clear}");
                display.send((tinkerForge.values().lightLux() / 100) + " LX");
            } else if (value == 21) {
                display.send("${clear}");
                display.send((tinkerForge.values().temperature() / 100) - 5 + " °C");
            } else if (value == 31) {
                display.send("${clear}");
                display.send(dateTime());
            }
        }

        if (tinkerForge.values().lightLux() > 30000) {
            display.send("${clear}");
            display.send("This is the end of our Project we hope you enjoyed it");
        }

    }


}