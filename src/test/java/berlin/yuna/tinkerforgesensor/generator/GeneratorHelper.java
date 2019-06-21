package berlin.yuna.tinkerforgesensor.generator;

import berlin.yuna.tinkerforgesensor.model.JFile;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_FILE_VERSIONS;
import static java.util.stream.Collectors.toList;

public class GeneratorHelper {

    public final static String SEPARATOR = System.lineSeparator();

    public static List<JFile> getClassVersions(final JFile jFile, final List<JFile> jFileList) {
        final String packageName = jFile.getClazz().getPackage().getName();
        final String name = jFile.getBasicName();
        final List<JFile> clazzVersionList = jFileList.stream()
                .filter(file -> file.getClazz().getPackage().getName().equals(packageName))
                .filter(file -> file.getClazz().getSimpleName().startsWith(name))
                .filter(file -> file.getClazz().getSimpleName().length() < name.length() + 3).sorted(Comparator.comparing(JFile::getSimpleName)).collect(toList());
        Collections.reverse(clazzVersionList);
        return clazzVersionList;
    }

    public static String firstLetterLow(final String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }
}
