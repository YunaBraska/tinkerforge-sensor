package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class Distance_to_RGBButton_Example {

    private static SensorList<Sensor> sensorList;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isDistance()) {
            if (value < 500) {
                sensorList.getButtonRGB().value(Color.RED);
            } else if (value < 1000) {
                sensorList.getButtonRGB().value(Color.BLUE);
            } else {
                sensorList.getButtonRGB().value(Color.GREEN);
            }
        }
    }
}
