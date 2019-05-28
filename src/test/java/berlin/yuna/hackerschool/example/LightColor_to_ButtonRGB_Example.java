package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class LightColor_to_ButtonRGB_Example {

    private static TinkerForge tinkerForge;

    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isColor()) {
            sensor.ledAdditionalOn();
            tinkerForge.sensors().buttonRGB().send(value);
        }
    }
}
