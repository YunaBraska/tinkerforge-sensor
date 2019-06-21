package berlin.yuna.tinkerforgesensor.model;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class JFile {

    private final Path path;
    private final Path relativePath;
    private final Path relativeMavenPath;
    private final Class clazz;
    private final Class superClass;
    private final Class genericClass;
    private final boolean hasComments;

    public static final String PROJECT_URL = "https://github.com/YunaBraska/tinkerforge-sensor/blob/master/";
    public static final String DIR_REL_MAVEN = "src/main/java";
    public static final File DIR_PROJECT = new File(System.getProperty("user.dir"));
    public static final File DIR_MAVEN_PROJECT = new File(DIR_PROJECT, DIR_REL_MAVEN);
    public static final Pattern PATTERN_COMMENT = Pattern.compile("(?s)\\/\\*.*?\\*\\/");
    public static final Pattern PATTERN_LINK = Pattern.compile("\\{@link(.*?)}");
    public static final Pattern PATTERN_SEE = Pattern.compile("(\\@see\\s(\\w|#)*\\s?)");
    public static final Pattern PATTERN_CODE = Pattern.compile("\\{@code(.*?)}");
    public static final Pattern PATTERN_SINCE = Pattern.compile("(\\@since\\s(\\w|\\.)*\\s?)");
    public static final Pattern PATTERN_PARAM = Pattern.compile("(\\@param\\s\\w*\\s?)");
    public static final Pattern PATTERN_FILE_VERSIONS = Pattern.compile("(_*V\\d*?)$");
    //    public static final Pattern PATTERN_COMMENT = Pattern.compile("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/");
    public static final String JAVA_EXTENSION = ".java";
    public static final File DIR_README = new File(DIR_PROJECT, "readmeDoc");

    public JFile(final Path path) {
        this.path = path;

        try {
            this.hasComments = PATTERN_COMMENT.matcher(new String(Files.readAllBytes(path))).find();
            relativePath = Paths.get(path.toString().substring(path.toString().indexOf(DIR_REL_MAVEN) + DIR_REL_MAVEN.length() + 1));
            relativeMavenPath = Paths.get(path.toString().substring(path.toString().indexOf(DIR_REL_MAVEN)));
            final String packageName = relativePath.getParent().toString().replace(File.separator, ".");
            clazz = Class.forName(packageName + "." + path.getFileName().toString().replace(JAVA_EXTENSION, ""));

            final Type genericSuperclass = clazz.getGenericSuperclass();
            final String typeName = genericSuperclass.getTypeName();
            superClass = typeName.contains("<") ? Class.forName(typeName.substring(0, typeName.indexOf("<"))) : null;

            final String genericClassName = clazz.getGenericSuperclass() instanceof ParameterizedTypeImpl ? ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0].getTypeName() : "";
            genericClass = genericClassName.length() > 2 ? Class.forName(genericClassName) : null;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to read file [" + path.toString() + "]", e);
        }

//        try {
//            Class.forName(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
//        } catch (ClassNotFoundException ignored) {
//
//        }
    }

    public File getReadmeFilePath() {
        return new File(DIR_README.getName(), getRelativePath().toString().replace(JAVA_EXTENSION, ".md"));
    }

    public URL getReadmeFileUrl() {
        return getUrl(getReadmeFilePath());
    }

    public File getReadmePackagePath() {
        final File targetReadme = new File(DIR_README.getName(), getRelativePath().getParent().toString());
        return new File(targetReadme, "README.md");
    }

    public URL getReadmePackageUrl() {
        return getUrl(getReadmePackagePath());
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public Package getPackage() {
        return clazz.getPackage();
    }

    public Path getPath() {
        return path;
    }

    public Path getRelativePath() {
        return relativePath;
    }

    public Path getRelativeMavenPath() {
        return relativeMavenPath;
    }

    public URL getRelativeMavenUrl() {
        return getUrl(getRelativeMavenPath().toFile());
    }

    public Class getClazz() {
        return clazz;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public Class getGenericClass() {
        return genericClass;
    }

    public boolean hasComments() {
        return hasComments;
    }

    public String getBasicName() {
        final String className = getSimpleName();
        final Matcher match = PATTERN_FILE_VERSIONS.matcher(className);
        return match.find() ? className.substring(0, className.length() - match.group(1).length()) : className;
    }

    public static List<JFile> getProjectJavaFiles() throws IOException {
        return Files.walk(DIR_MAVEN_PROJECT.toPath())
                .filter(Files::isRegularFile)
                .filter(file -> file.getFileName().toString().endsWith(JAVA_EXTENSION))
                .map(JFile::new).sorted(comparing(JFile::getSimpleName)).collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JFile jFile = (JFile) o;
        return Objects.equals(clazz, jFile.clazz);
    }

    private URL getUrl(final File path) {
        try {
            return new URL(PROJECT_URL + path.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("unable to build url from [" + PROJECT_URL + path.toString() + "]", e);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz);
    }

    @Override
    public String toString() {
        return "ProjectJavaFile{" +
                "clazz=" + clazz.getName() +
                ", hasComments=" + hasComments +
                '}';
    }
}
