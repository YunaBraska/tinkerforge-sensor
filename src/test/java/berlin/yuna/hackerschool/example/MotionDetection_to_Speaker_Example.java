package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class MotionDetection_to_Speaker_Example {

    private static SensorList<Sensor> sensorList;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isMotionDetected() && value.intValue() == 1) {
            sensor.ledAdditionalOn();
            sensorList.getSpeaker().value(1000);
        } else if(type.isMotionDetected() && value.intValue() == 0){
            sensor.ledAdditionalOff();
        }
    }
}
