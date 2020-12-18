package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static berlin.yuna.tinkerforgesensor.generator.JFile.DIR_PROJECT;
import static berlin.yuna.tinkerforgesensor.generator.JFile.DIR_REL_MAVEN;
import static java.lang.Character.toUpperCase;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Tag("UnitTest")
class GeneratorTest {

    /**
     * Every method parameter with this suffix will be replaced with an 'dot array'
     * Example: "String[] stringArray_DotArray" will be replaced with "String... stringArray"
     */
    public static final String DOT_ARRAY = "_DotArray";
    public static final String LINE_SEPARATOR = System.lineSeparator();

    @Test
    void generate() throws IOException {
        final List<JFile> jFiles = JFile.getProjectJavaFiles();
        final List<JFile> handlers = jFiles.stream().filter(c -> c.getSuperClass() == SensorHandler.class).collect(toList());

        writeJavaFile(GeneratorIsSensor.generate(handlers));
        writeJavaFile(GeneratorGetSensor.generate(handlers));
        writeJavaFile(GeneratorIsValueType.generate());
        writeJavaFile(GeneratorContainsValueType.generate());

        //Deprecated...
        GeneratorDeviceProvider.generate();
    }

    private void writeJavaFile(final JavaFile javaFile) throws IOException {
        //DEFAULT FILE WRITER
        javaFile.writeTo(new File(DIR_REL_MAVEN));

        //VERIFY FILE IS WRITTEN
        final Path javaFilePath = new File(new File(DIR_PROJECT, DIR_REL_MAVEN), javaFile.toJavaFileObject().toUri().toString()).toPath();
        if (!Files.exists(javaFilePath)) {
            throw new RuntimeException("could not find generated JavaFile" + javaFilePath.toString());
        }

        //HANDLE DOT_ARRAY
        String javaFileContent = javaFile.toString().replace("import java.lang.SuppressWarnings;\n", "");
        javaFileContent = javaFileContent.replaceAll("(?<dynamic>\\[\\])(?<content>.*)(?<name>" + DOT_ARRAY + ")", "...${content}");

        //IDENT
        javaFileContent = javaFileContent.replace("  ", "    ");

        //SAVE CHANGES
        System.out.println(format("Generated [%s]", javaFilePath));
        Files.write(javaFilePath, javaFileContent.getBytes());
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