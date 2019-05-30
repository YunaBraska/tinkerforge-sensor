package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.generator.builder.GeneratorCompare;
import berlin.yuna.tinkerforgesensor.generator.builder.GeneratorSensors;
import berlin.yuna.tinkerforgesensor.generator.builder.GeneratorValues;
import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import com.squareup.javapoet.JavaFile;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Character.toUpperCase;

public class GeneratorTest {

    /**
     * Every method parameter with this suffix will be replaced with an 'dot array'
     * Example: "String[] stringArray_DotArray" will be replaced with "String... stringArray"
     */
    public static final String DOT_ARRAY = "_DotArray";
    public static final String NL = System.lineSeparator();

    @Test
    public void generate() throws IOException {
        final List<Class<? extends Sensor>> sensorList = getSensorList();
        final File targetDeviceProviderFile = new File(System.getProperty("user.dir"), "src/main/resources/META-INF/services/com.tinkerforge.DeviceProvider");
        final File targetParentDir = new File("src/main/java");

        //Must be in order
        writeJavaFile(GeneratorEnumValueType.generate(), targetParentDir);
        writeJavaFile(GeneratorSensorRegistry.generate(sensorList), targetParentDir);
        //Deprecated...
        GeneratorDeviceProvider.generate(targetDeviceProviderFile);

        //builder
        writeJavaFile(GeneratorCompare.generate(sensorList), targetParentDir);
        writeJavaFile(GeneratorSensors.generate(sensorList), targetParentDir);
        writeJavaFile(GeneratorValues.generate(sensorList), targetParentDir);

        //README.md
        GeneratorSensorReadme.generate(sensorList);
    }

    private void writeJavaFile(final JavaFile javaFile, final File targetParentDir) throws IOException {
        //DEFAULT FILE WRITER
        javaFile.writeTo(targetParentDir);

        //VERIFY FILE IS WRITTEN
        final Path javaFilePath = Paths.get(System.getProperty("user.dir"), targetParentDir + "/" + javaFile.toJavaFileObject().toUri().toString());
        if (!Files.exists(javaFilePath)) {
            throw new RuntimeException("could not find generated JavaFile" + javaFilePath.toString());
        }

        //HANDLE DOT_ARRAY
        String javaFileContent = javaFile.toString();
        javaFileContent = javaFileContent.replaceAll("(?<dynamic>\\[\\])(?<content>.*)(?<name>" + DOT_ARRAY + ")", "...${content}");

        //IDENT
        javaFileContent = javaFileContent.replace("  ","    ");

        //SAVE CHANGES
        Files.write(javaFilePath, javaFileContent.getBytes());
    }

    private List<Class<? extends Sensor>> getSensorList() {
        final Reflections reflections = new Reflections(SensorRegistry.class.getPackage().getName());
        final List<Class<? extends Sensor>> sensorList = new ArrayList<>(reflections.getSubTypesOf(Sensor.class));
        sensorList.sort(Comparator.comparing(Class::getSimpleName));
        return sensorList;
    }

    public static String toHumanReadable(final Enum<?> anEnum, final boolean startUpperCase) {
        final char[] chars = anEnum.toString().toLowerCase().toCharArray();
        if (startUpperCase) {
            chars[0] = toUpperCase(chars[0]);
        }
        for (int i = 1; i < chars.length; i++) {
            final char prev = chars[i - 1];
            if (prev == '_') {
                chars[i] = toUpperCase(chars[i]);
            }
        }
        return new String(chars).replace("_", "");
    }

}