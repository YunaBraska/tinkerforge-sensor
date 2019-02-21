package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class SoundIntensity_to_IO16_Example {

    private static long dynamicMaxVolume = 0;
    private static SensorList<Sensor> sensorList;

    public static void main(String[] args) {
        SensorListener sensorListener = Connection_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isSoundIntensity()) {
            Sensor io16 = sensorList.getIO16();

            //Dynamic max Volume
            if (value > dynamicMaxVolume) {
                dynamicMaxVolume = value;
            } else {
                dynamicMaxVolume--;
            }

            //Turn off all lights
            io16.value(false);

            //Turn on LEDs (max 16)
            long port = ((16 * value) / dynamicMaxVolume);
            for (int i = 0; i < port; i++) {
                io16.value(i);
            }
        }
    }
}
