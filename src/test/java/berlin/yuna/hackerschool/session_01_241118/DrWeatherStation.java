package berlin.yuna.hackerschool.session_01_241118;

import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;
import static java.lang.String.format;

/**
 * @author yourNames
 */
public class DrWeatherStation extends Helper {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    //START FUNCTION
    public static void main(final String[] args) {
        final SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] value [%s]", sensor.name, type, value));
        }

        final Sensor display = sensorList.getDisplayLcd20x4();

        display.ledAdditionalOn();
        if (type.isButtonPressed()) {
            if (value == 11) {
                display.value("${clear}");
                display.value((sensorList.valueDecimal(LIGHT_LUX) / 100) + " LX");
            } else if (value == 21) {
                display.value("${clear}");
                display.value((sensorList.valueDecimal(TEMPERATURE) / 100) - 5 + " °C");
            } else if (value == 31) {
                display.value("${clear}");
                display.value(dateTime());
            }
        }

        if (sensorList.valueDecimal(LIGHT_LUX) > 30000) {
            display.value("${clear}");
            display.value("This is the end of our Project we hope you enjoyed it");
        }

    }


}