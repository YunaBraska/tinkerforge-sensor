package berlin.yuna.tinkerforgesensor.generator;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.JFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.getClassVersions;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.LINE_SEPARATOR;
import static berlin.yuna.tinkerforgesensor.model.JFile.DIR_PROJECT;
import static berlin.yuna.tinkerforgesensor.model.JFile.DIR_README;
import static berlin.yuna.tinkerforgesensor.model.JFile.JAVA_EXTENSION;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_FILE_VERSIONS;
import static berlin.yuna.tinkerforgesensor.model.JFile.PROJECT_URL;
import static java.lang.String.format;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class GeneratorReadmeDoc {

    public static void generate(final List<JFile> jFileList) {
        try {
            deleteDir(DIR_README.toPath());
            final List<JFile> jFiles = new ArrayList<>(jFileList);
            final List<JFile> jFilesComments = jFiles.stream().filter(JFile::hasComments).collect(toList());
            final Set<String> packageGroups = jFilesComments.stream().map(file -> file.getClazz().getPackage().getName()).collect(toSet());
            final StringBuilder navigation = createNavigation(jFilesComments, packageGroups);

            createIndex(navigation, packageGroups, jFilesComments);

            while (!jFilesComments.isEmpty()) {
                final List<JFile> classVersions = getClassVersions(jFilesComments.listIterator().next(), jFiles);
                GeneratorReadmeDocHelper.generate(navigation, jFiles, classVersions.get(0));

                //Generate readme.md only for new versions
                for (JFile classVersion : classVersions) {
                    jFilesComments.remove(classVersion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void deleteDir(final Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private static void createIndex(final StringBuilder navigation, final Set<String> packageGroups, final List<JFile> jFiles) {
        createStartPage(navigation);
        for (String packageGroup : packageGroups) {
            final StringBuilder result = new StringBuilder();
            final List<JFile> packageFiles = jFiles.stream().filter(file -> file.getClazz().getPackage().getName().equals(packageGroup)).collect(toList());
            final File packageGroupPath = packageFiles.get(0).getReadmePackagePath();

            result.append(LINE_SEPARATOR);
            result.append("## ");
            result.append(packageGroup);
            result.append(LINE_SEPARATOR);
            result.append(navigation);
            result.append(LINE_SEPARATOR);

            while (!packageFiles.isEmpty()) {
                final List<JFile> classVersions = getClassVersions(packageFiles.listIterator().next(), jFiles);
                final JFile jFile = classVersions.get(0);

                result.append("* [").append(jFile.getBasicName()).append("]");
                result.append("(").append(jFile.getReadmeFileUrl().toString()).append(")");

                result.append(" ([source]");
                result.append("(").append(jFile.getRelativeMavenUrl().toString()).append("))");

                result.append(prepareClassVersions(classVersions));

                //Generate readme.md only for new versions
                for (JFile classVersion : classVersions) {
                    packageFiles.remove(classVersion);
                }
            }

            result.append("---").append(LINE_SEPARATOR);

            final File targetFile = new File(DIR_PROJECT, packageGroupPath.toString());
            try {
                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                    System.out.println("Created [" + targetFile + "]");
                    Files.write(targetFile.toPath(), result.toString().getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("failed to write index file for [" + targetFile + "]", e);
            }
        }
    }

    public static StringBuilder prepareClassVersions(final List<JFile> versions) {
        final List<JFile> classVersions = new ArrayList<>(versions);
        final StringBuilder result = new StringBuilder();
        result.append(" *(");
        Collections.reverse(classVersions);
        for (JFile classVersion : classVersions) {
            final Matcher match = PATTERN_FILE_VERSIONS.matcher(classVersion.getSimpleName());
            final String linkDesc = match.find() ? match.group(0) : "V1";
            result.append("[").append(linkDesc.replace("_", ""));
//            result.append("]").append("(").append(classVersion.getReadmeFileUrl().toString()).append(")").append(", ");
            result.append("]").append("(").append(classVersion.getRelativeMavenUrl().toString()).append(")").append(", ");
        }
        result.deleteCharAt(result.length() - 2);
        result.deleteCharAt(result.length() - 1);
        result.append(")*");
        result.append(LINE_SEPARATOR);
        return result;
    }

    private static void createStartPage(final StringBuilder navigation) {
        final File targetFile = new File(DIR_README, "README.md");
        final StringBuilder result = new StringBuilder();
        result.append(LINE_SEPARATOR);
        result.append("## ReadmeDoc").append(LINE_SEPARATOR);
        result.append(navigation);

        final JFile stack = JFile.getJFile(jFile -> jFile.getClazz() == Stack.class);
        final String stackLink = format("Connecting to [%s](%s) ([source](%s))", stack.getSimpleName(), stack.getReadmeFileUrl(), stack.getRelativeMavenUrl());

        result.append("Connecting to ").append(stackLink).append(LINE_SEPARATOR);
        result.append("```java").append(LINE_SEPARATOR);
        result.append("Stack stack = new Stack(\"host\", 4223, \"optionalPassword\");").append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        result.append("// Get/Define RGB_Button;").append(LINE_SEPARATOR);
        result.append("Sensor button = stack.sensors().buttonRGB();").append(LINE_SEPARATOR);
        result.append("// Get/Define value sound intensity;").append(LINE_SEPARATOR);
        result.append("Long soundIntensity = stack.values().soundIntensity();").append(LINE_SEPARATOR);
        result.append("```").append(LINE_SEPARATOR);

        result.append("Disconnecting ").append(stackLink).append(LINE_SEPARATOR);
        result.append("```java").append(LINE_SEPARATOR);
        result.append("Stack stack = new Stack(\"host\", 4223, \"optionalPassword\");").append(LINE_SEPARATOR);
        result.append("stack.disconnect()").append(LINE_SEPARATOR);
        result.append("```").append(LINE_SEPARATOR);

        result.append("Auto closeable stack ").append(stackLink).append(LINE_SEPARATOR);
        result.append("```java").append(LINE_SEPARATOR);
        result.append("try (Stack stack = new Stack(\"host\", 4223, \"optionalPassword\")) {").append(LINE_SEPARATOR);
        result.append("  // Get/Define RGB_Button;").append(LINE_SEPARATOR);
        result.append("  Sensor button = stack.sensors().buttonRGB();").append(LINE_SEPARATOR);
        result.append("}").append(LINE_SEPARATOR);
        result.append("```").append(LINE_SEPARATOR);

        result.append("Event handling with ").append(stackLink).append(LINE_SEPARATOR);
        result.append("```java").append(LINE_SEPARATOR);
        result.append("Stack stack = new Stack(\"host\", 4223, \"optionalPassword\");").append(LINE_SEPARATOR);
        result.append("//Add listener for [SensorEvent] from all sensors").append(LINE_SEPARATOR);
        result.append("stack.sensorEventConsumerList.add(ths::onSensorEvent);").append(LINE_SEPARATOR);
        result.append("```").append(LINE_SEPARATOR);

        try {
            if (!targetFile.exists() && DIR_README.mkdirs()) {
                System.out.println("Created [" + targetFile + "]");
                Files.write(targetFile.toPath(), result.toString().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to write index file for [" + targetFile + "]", e);
        }
    }

    private static StringBuilder createNavigation(final List<JFile> jFiles, final Set<String> packageGroups) {
        try {
            final StringBuilder result = new StringBuilder();
            result.append("###### Navigation").append(LINE_SEPARATOR);
            result.append("* ").append("[⌂ Start]").append("(").append(new URL(PROJECT_URL + "readmeDoc/README.md")).append(")");
            for (String packageGroup : packageGroups) {
                final JFile jFile = jFiles.stream().filter(file -> file.getClazz().getPackage().getName().equals(packageGroup)).collect(toList()).get(0);
                result.append(" · [").append(jFile.getPath().getParent().getFileName().toString().replace(JAVA_EXTENSION, "")).append("]");
                result.append("(").append(jFile.getReadmePackageUrl().toString()).append(")");
            }
            result.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("---").append(LINE_SEPARATOR);
            return result;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}