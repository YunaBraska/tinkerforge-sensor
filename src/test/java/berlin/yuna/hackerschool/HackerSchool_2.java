package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.example.ConnectionAndPrintValues_Example;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

public class HackerSchool_2 extends TinkerForgeUtil {

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