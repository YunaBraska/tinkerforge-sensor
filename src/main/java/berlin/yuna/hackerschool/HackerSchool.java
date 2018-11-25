package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.BrickletLCD20x4;

import static berlin.yuna.hackerschool.Example.printAllValues;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();

    public void startup() {
        //CODE HERE
        console(readFile("start.txt"));
        loop("printValues", run -> printAllValues(sensorList));
    }

    private void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType) {
        //CODE HERE
        if (valueType.is(BUTTON_PRESSED)) {
            loopEnd("Button_0", "Button_1", "Button_2", "Button_3");
            Sensor display = sensorList.first(BrickletLCD20x4.class);
            display.value("${clear}");
            display.ledAdditionalOn();
            if (value == 0) {
                Button_0();
            } else if (value == 1) {
                display.value("${space}" + dateTime() + "${space}");
            } else if (value == 2) {

            } else if (value == 3) {

            }
        }
    }

    void Button_0() {
        loop("Button_0", run -> {
            Sensor display = sensorList.first(BrickletLCD20x4.class);
            Double temperature = sensorList.valueDecimal(TEMPERATURE);

            display.ledAdditionalOn();
            display.value("${0}Ho ho ho ${space}");
            display.value("${1}Mary Christmas ${space}");
            display.value("${2}Temperature ${space}" + roundUp(temperature / 100) + " °C");
        });
    }

    public void shutdown() {
        console(readFile("stop.txt"));
    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}