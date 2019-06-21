package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.JFile;
import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import com.tinkerforge.DummyDevice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.SEPARATOR;
import static berlin.yuna.tinkerforgesensor.model.JFile.DIR_PROJECT;
import static java.util.Comparator.comparing;

class GeneratorDeviceProvider {
    static void generate() throws IOException {
        final StringBuffer sbf = new StringBuffer();
        SensorRegistry.getDeviceAvailableDevices().stream().filter(device -> !device.equals(DummyDevice.class)).sorted(comparing(Class::getSimpleName)).forEach(device -> sbf.append(device.getCanonicalName()).append("Provider").append(SEPARATOR));
        Files.write(new File(DIR_PROJECT, "src/main/resources/META-INF/services/com.tinkerforge.DeviceProvider").toPath(), sbf.toString().getBytes());
    }
}