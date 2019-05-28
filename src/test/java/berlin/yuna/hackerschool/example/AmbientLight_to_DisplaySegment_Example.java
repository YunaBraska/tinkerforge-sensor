package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class AmbientLight_to_DisplaySegment_Example {

    private static TinkerForge tinkerForge;

    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isLightLux()) {
            tinkerForge.sensors().displaySegment().sendLimit(2,value / 100 + "LX");
        }
    }
}
