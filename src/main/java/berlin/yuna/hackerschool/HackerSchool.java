package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.BrickletLCD20x4;

import static berlin.yuna.hackerschool.Example.printAllValues;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();

    public void startup() {
        //CODE HERE
        console(readFile("start.txt"));
        loop("printValues", run -> printAllValues(sensorList));
    }

    private void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType) {
        //CODE HERE
        Sensor display = sensorList.first(BrickletLCD20x4.class);

        if (valueType.is(BUTTON_PRESSED)) {

        }
    }

    public void shutdown() {
        console(readFile("stop.txt"));
    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}