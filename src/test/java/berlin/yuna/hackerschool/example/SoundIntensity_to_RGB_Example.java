package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class SoundIntensity_to_RGB_Example {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    private static long soundMax = 1;
    private static final int[] rainbow = Color.RAINBOW;

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final long decibel = stack.values().soundIntensity() + 1;

        //Dynamic max volume
        if (decibel > soundMax) {
            soundMax = decibel;
        }

        if (type.isSoundIntensity()) {
            final int rainbowCount = (int) (decibel / ((soundMax / rainbow.length) + 1));
            stack.sensors().buttonRGB().send(rainbow[rainbowCount]);
            stack.sensors().ledRGB().send(rainbow[rainbowCount]);
        }
    }
}
