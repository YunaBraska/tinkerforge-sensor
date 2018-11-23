package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_STATUS;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();

    public void startup() {
        //CODE HERE
        console(readFile("start.txt"));
    }

    private void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType) {
        //CODE HERE
    }

    public void shutdown() {
        //CODE HERE
        console(readFile("stop.txt"));
    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}