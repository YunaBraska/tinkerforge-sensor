package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Character.toUpperCase;

public class GeneratorTest {

    @Test
    public void generate() throws IOException {
        List<Class<? extends Sensor>> sensorList = getSensorList();
        File targetDeviceProviderFile = new File(System.getProperty("user.dir"), "src/main/resources/META-INF/services/com.tinkerforge.DeviceProvider");
        File targetSourceDir = new File("src/main/java");

        GeneratorEnumValueType.generate();
        GeneratorSensorRegistry.generate(new ArrayList<>(sensorList)).writeTo(targetSourceDir);
        GeneratorSensorList.generate(new ArrayList<>(sensorList)).writeTo(targetSourceDir);
        GeneratorSensorHelper.generate(new ArrayList<>(sensorList) ).writeTo(targetSourceDir);
        GeneratorDeviceProvider.generate(targetDeviceProviderFile);
    }

    private List<Class<? extends Sensor>> getSensorList() {
        Reflections reflections = new Reflections(SensorRegistry.class.getPackage().getName());
        List<Class<? extends Sensor>> sensorList = new ArrayList<>(reflections.getSubTypesOf(Sensor.class));
        sensorList.sort(Comparator.comparing(Class::getSimpleName));
        return sensorList;
    }

    static String toHumanReadable(final Enum<?> anEnum, boolean startUpperCase) {
        char[] chars = anEnum.toString().toLowerCase().toCharArray();
        if (startUpperCase) {
            chars[0] = toUpperCase(chars[0]);
        }
        for (int i = 1; i < chars.length; i++) {
            char prev = chars[i - 1];
            if (prev == '_') {
                chars[i] = toUpperCase(chars[i]);
            }
        }
        return new String(chars).replace("_", "");
    }

}