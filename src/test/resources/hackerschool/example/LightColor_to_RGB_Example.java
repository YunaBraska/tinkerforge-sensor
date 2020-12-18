package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

public class LightColor_to_RGB_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (type.isColor()) {
            sensor.ledAdditional_setOn();
            stack.sensors().buttonRGB().send(true);
            stack.sensors().ledRGB().send(true);
            stack.sensors().buttonRGB().send(value);
            stack.sensors().ledRGB().send(value);
        }
    }
}
