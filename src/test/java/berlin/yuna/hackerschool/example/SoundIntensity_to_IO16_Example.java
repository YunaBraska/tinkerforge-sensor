package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class SoundIntensity_to_IO16_Example {

    private static long dynamicMaxVolume = 0;
    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isSoundIntensity()) {
            final Sensor io16 = stack.sensors().iO16();

            //Dynamic max Volume
            if (value > dynamicMaxVolume) {
                dynamicMaxVolume = value;
            } else {
                dynamicMaxVolume--;
            }

            //Turn off all lights
            io16.send(false);

            //Turn on LEDs (max 16)
            final long port = ((16 * value) / dynamicMaxVolume);
            for (int i = 0; i < port; i++) {
                io16.send(i);
            }
        }
    }
}
