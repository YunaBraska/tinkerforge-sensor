package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class AmbientLight_to_Display_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isLightLux()) {
            stack.sensors().display().sendLimit(2,value / 100 + "LX");
        }
    }
}
