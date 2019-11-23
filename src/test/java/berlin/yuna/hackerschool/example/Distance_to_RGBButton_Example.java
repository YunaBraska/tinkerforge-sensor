package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class Distance_to_RGBButton_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.getValue(), event.getValueType()));
    }

    private static long maxDistance = 0;

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isDistance()) {
            if(value > maxDistance){
                maxDistance = value;
            }

            if (value < ((maxDistance * 25) / 100)) {
                stack.sensors().buttonRGB().send(Color.RED);
            } else if (value < ((maxDistance * 75) / 100)) {
                stack.sensors().buttonRGB().send(Color.BLUE);
            } else {
                stack.sensors().buttonRGB().send(Color.GREEN);
            }
        }
    }
}
