package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import com.tinkerforge.DummyDevice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class GeneratorDeviceProvider {
    static void generate(final File targetFile) throws IOException {
        StringBuffer sbf = new StringBuffer();
        SensorRegistry.getDeviceAvailableDevices().stream().filter(device -> !device.equals(DummyDevice.class)).forEach(device -> sbf.append(device.getCanonicalName()).append("Provider").append(System.getProperty("line.separator")));
        Files.write(targetFile.toPath(), sbf.toString().getBytes());
    }
}