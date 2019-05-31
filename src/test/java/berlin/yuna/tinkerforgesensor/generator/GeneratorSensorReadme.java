package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.getSensorVersions;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.LINE_SEPARATOR;

public class GeneratorSensorReadme {

    public static final Pattern PATTERN_COMMENT = Pattern.compile("(?s)\\/\\*.*?\\*\\/");
    //        public static final Pattern PATTERN_COMMENT = Pattern.compile("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/");
    public static final Pattern LINK_PATTERN = Pattern.compile("(\\{@link)(.*)(})");

    public static void generate(final List<Class<? extends Sensor>> sensors) {
        final List<Class<? extends Sensor>> sensorList = new ArrayList<>(sensors);

        final File projectDir = new File(System.getProperty("user.dir"));
        final StringBuilder result = new StringBuilder();
        while (!sensorList.isEmpty()) {
            try {
                final Class<? extends Sensor> sensor = sensorList.iterator().next();
                final List<Class<? extends Sensor>> sensorVersions = getSensorVersions(sensor, sensorList);

                final File sourceFile = new File(new File(projectDir, "src/main/java"), sensor.getTypeName().replace(".", File.separator) + ".java");
                final Class<?> sensorClass = Class.forName(sensor.getTypeName());
                final String content = new String(Files.readAllBytes(sourceFile.toPath()));

                final Matcher matcher = PATTERN_COMMENT.matcher(content);
                while (matcher.find()) {
                    final String block = matcher.group(0).replaceFirst("^/\\**" + LINE_SEPARATOR, "").replace(LINE_SEPARATOR + " */", "").replaceAll("(^|" + LINE_SEPARATOR + ")\\s*\\*", LINE_SEPARATOR);
                    result.append(parseNodes(Jsoup.parse(block).select("body").get(0), LINE_SEPARATOR, sensorClass));
                }
                result.append(LINE_SEPARATOR).append("---").append(LINE_SEPARATOR);

                //REMOVE SENSORS VARIANTS FROM LIST
                for (Class<? extends Sensor> sensorVersion : sensorVersions) {
                    sensorList.remove(sensorVersion);
                }

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(new File(projectDir, "SENSOR_README.md").toPath(), result.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder parseNodes(final Node root, final String parentSeparator, final Class baseClass) {
        final StringBuilder result = new StringBuilder();
        for (Node node : root.childNodes()) {
            if (node instanceof Element) {
                result.append(transformHtmlElements(node, baseClass, parentSeparator));
            } else if (node instanceof TextNode) {
                result.append(resolveLinks(baseClass, ((TextNode) node).getWholeText()));
            }
        }
        return result;
    }

    private static String filterTextOnly(final List<Node> nodes) {
        final StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            if (node.childNodes().isEmpty()) {
                sb.append(((TextNode) node).getWholeText());
            } else {
                sb.append(filterTextOnly(node.childNodes()));
            }
        }
        return sb.toString().trim().replaceAll(LINE_SEPARATOR + "\\s*", LINE_SEPARATOR);
    }

    private static Class<?> searchClass(final String className, final Class linkedClass) {
        try {
            return className.equals(linkedClass.getSimpleName()) || className.equals("this") ? linkedClass : Class.forName(className);
        } catch (ClassNotFoundException e) {
            try {
                final File classFile = new File(System.getProperty("user.dir"), new File("src/main/java", linkedClass.getTypeName().replace(".", File.separator)).toString() + ".java");
                String classRef = regex("(import (static)*)(.*" + className + ");", new String(Files.readAllBytes(classFile.toPath())), 3);
                classRef = classRef != null ? classRef : linkedClass.getPackage().getName() + "." + className;
                return Class.forName(classRef);
            } catch (ClassNotFoundException | IOException ex) {
                try {
                    return Class.forName("java.lang." + className);
                } catch (ClassNotFoundException exc) {
                    return linkedClass;
                }
            }
        }
    }

    private static String regex(final String pattern, final String content, final int group) {
        final Matcher matcher = Pattern.compile(pattern).matcher(content);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

    //FIXME: add table logic
    private static String transformHtmlElements(final Node node, final Class baseClass, final String parentSeparator) {
        final Element element = (Element) node;
        final StringBuilder result = new StringBuilder();
        switch (element.tagName().toLowerCase()) {
            case "a":
                result.append("[").append(element.text()).append("]");
                result.append("(").append(element.attr("href")).append(")");
                break;
            case "p":
            case "pre":
            case "span":
                result.append("`").append(element.text()).append("`");
                break;
            case "blockquote":
                result.append("> ").append(parseNodes(node, parentSeparator + "> ", baseClass));
                result.append(parentSeparator);
                break;
            case "body":
                result.append(parseNodes(node, parentSeparator, baseClass));
                result.append(parentSeparator);
                break;
            case "br":
                result.append(parentSeparator);
                break;
            case "caption":
                result.append(parseNodes(node, parentSeparator, baseClass));
                result.append(parentSeparator);
                break;
            case "CENTER":
                break;
            case "code":
                result.append("```java").append(parentSeparator);
                result.append(filterTextOnly(element.childNodes())).append(parentSeparator);
                result.append("```").append(parentSeparator);
                break;
            case "dl":
                result.append(parseNodes(node, parentSeparator, baseClass));
                break;
            case "dd":
                result.append(parentSeparator).append("    ").append(parseNodes(node, parentSeparator + "    ", baseClass)).append(parentSeparator);
                break;
            case "dt":
                result.append(parentSeparator).append("**").append(parseNodes(node, parentSeparator, baseClass)).append("**").append(parentSeparator);
                break;
            case "em":
                result.append("*").append(parseNodes(node, parentSeparator, baseClass)).append("*");
                break;
            case "FONT":
                break;
            case "h1":
                result.append(LINE_SEPARATOR).append("# ").append(parseNodes(node, parentSeparator, baseClass)).append(LINE_SEPARATOR);
                break;
            case "h2":
                result.append(LINE_SEPARATOR).append("## ").append(parseNodes(node, parentSeparator, baseClass)).append(LINE_SEPARATOR);
                break;
            case "h3":
                result.append(LINE_SEPARATOR).append("### ").append(parseNodes(node, parentSeparator, baseClass)).append(LINE_SEPARATOR);
                break;
            case "h4":
                result.append(LINE_SEPARATOR).append("#### ").append(parseNodes(node, parentSeparator, baseClass)).append(LINE_SEPARATOR);
                break;
            case "h5":
                result.append(LINE_SEPARATOR).append("##### ").append(parseNodes(node, parentSeparator, baseClass)).append(LINE_SEPARATOR);
                break;
            case "h6":
                result.append(LINE_SEPARATOR).append("###### ").append(parseNodes(node, parentSeparator, baseClass)).append(LINE_SEPARATOR);
                break;
            case "small":
                result.append(LINE_SEPARATOR).append("###### ").append(parseNodes(node, parentSeparator, baseClass));
                break;
            case "hr":
                result.append(LINE_SEPARATOR).append("--- ").append(LINE_SEPARATOR);
                break;
            case "strong":
            case "b":
                result.append("**").append(parseNodes(node, parentSeparator, baseClass)).append("**");
                break;
            case "i":
            case "sub":
                result.append("*").append(parseNodes(node, parentSeparator, baseClass)).append("*");
                break;
            case "img":
                result.append("![").append(element.text()).append("]");
                result.append("(").append(element.attr("src")).append(")");
                break;
            case "li":
                result.append(LINE_SEPARATOR).append("* ").append(parseNodes(node, LINE_SEPARATOR + "* ", baseClass));
                break;
            case "head":
            case "html":
            case "link":
            case "menu":
            case "div":
            case "meta":
            case "noscript":
            case "ul":
            case "ol":
            case "script":
                result.append(parseNodes(node, parentSeparator, baseClass));
                break;
            case "TABLE":
                break;
            case "TBODY":
                break;
            case "TD":
                break;
            case "TH":
                break;
            case "TITLE":
                break;
            case "TR":
                break;
            case "TT":
                break;
            case "VAR":
                break;
        }
        return result.toString();
    }

    private static String resolveLinks(final Class baseClass, final String inputString) {
        final StringBuilder result = new StringBuilder();
        String text = inputString;
        final Matcher linkMatch = LINK_PATTERN.matcher(text);
        while (linkMatch.find()) {
            final String[] link = linkMatch.group(2).split("#");
            final Class linkedClass = searchClass(link[0].trim(), baseClass);
            final String linkDesc = linkedClass.getSimpleName() + (link.length > 1 ? "#" + link[1].trim() : "");
            final String linkTarget = new File("src/main/java", linkedClass.getTypeName().replace(".", File.separator) + ".java").toString();
            text = text.replaceFirst(LINK_PATTERN.pattern(), "[" + linkDesc + "]" + "(" + linkTarget + ")");
        }

        if (!text.trim().isEmpty() && text.trim().length() > 1) {
            result.append(text);
        }
        return result.toString();
    }

}