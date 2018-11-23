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
        console(readFile("start.txt"));
    }

    private void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType) {
        //CODE HERE
    }

    public void shutdown() {
        console(readFile("stop.txt"));
    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        if (sensorEvent.valueType.parent == DEVICE_STATUS) {
            console("[%10s] [%25s] [%s]", sensorEvent.value, sensorEvent.valueType, sensorEvent.sensor.name);
        }
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}