package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.Application;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratorTest {

    @Test
    public void generate() throws IOException {
        List<Class<? extends Sensor>> sensorList = getSensorList();
        File targetDeviceProviderFile = new File(System.getProperty("user.dir"), "src/main/resources/META-INF/services/com.tinkerforge.DeviceProvider");
        File targetSourceDir = new File("src/main/java");

        GeneratorSensorRegistry.generate(sensorList).writeTo(targetSourceDir);
        GeneratorSensorList.generate(sensorList).writeTo(targetSourceDir);
        GeneratorDeviceProvider.generate(targetDeviceProviderFile);
        GeneratorEnumValueType.generate();
    }

    private List<Class<? extends Sensor>> getSensorList() {
        Reflections reflections = new Reflections(Application.class.getPackage().getName());
        List<Class<? extends Sensor>> sensorList = new ArrayList<>(reflections.getSubTypesOf(Sensor.class));
        sensorList.sort(Comparator.comparing(Class::getSimpleName));
        return sensorList;
    }

}