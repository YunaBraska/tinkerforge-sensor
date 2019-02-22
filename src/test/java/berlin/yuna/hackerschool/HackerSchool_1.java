package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.example.ConnectionAndPrintValues_Example;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;
import static java.lang.String.format;

public class HackerSchool_1 extends TinkerForgeUtil {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    //Start method initializer
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> drWeatherStation(event.sensor, event.value, event.valueType));
    }

    private static void drWeatherStation(final Sensor sensor, final Long value, final ValueType type) {
        if (type.containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] value [%s]", sensor.name, type, value));
        }

        Sensor display = sensorList.getDisplayLcd20x4();

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