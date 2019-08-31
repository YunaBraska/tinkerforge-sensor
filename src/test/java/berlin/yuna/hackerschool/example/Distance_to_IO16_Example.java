package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class Distance_to_IO16_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.getValue(), event.getValueType()));
    }

    //VARIABLES
    private static long distanceMax = 1500;

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isDistance()) {

            //Get Sensor and Value
            final Sensor io16 = stack.sensors().iO16();
            final long distance = value + 1;
            final int ledCount = (int) (distance / ((distanceMax / 16) + 1));

            if (distance > distanceMax) {
                distanceMax = distance;
            }

            //Switch LEDs on
            for (int led = 1; led < ledCount; led++) {
                io16.send(led);
            }

            //Switch other LEDs off
            for (int led = ledCount; led < 16; led++) {
                io16.send(-led);
            }


        }
    }
}
