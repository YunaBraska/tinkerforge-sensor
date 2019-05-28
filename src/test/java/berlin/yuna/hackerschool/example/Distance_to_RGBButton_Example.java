package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class Distance_to_RGBButton_Example {

    private static TinkerForge tinkerForge;

    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    private static long maxDistance = 0;

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isDistance()) {
            if(value > maxDistance){
                maxDistance = value;
            }

            if (value < ((maxDistance * 25) / 100)) {
                tinkerForge.sensors().buttonRGB().send(Color.RED);
            } else if (value < ((maxDistance * 75) / 100)) {
                tinkerForge.sensors().buttonRGB().send(Color.BLUE);
            } else {
                tinkerForge.sensors().buttonRGB().send(Color.GREEN);
            }
        }
    }
}
