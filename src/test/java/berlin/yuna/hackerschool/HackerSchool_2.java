package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.example.ConnectionAndPrintValues_Example;
import berlin.yuna.tinkerforgesensor.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.AirQuality;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class HackerSchool_2 extends Helper {

    public static SensorList<Sensor> sensorList = new SensorList<>();

    //Start method initializer
    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        //Code Here
    }


}