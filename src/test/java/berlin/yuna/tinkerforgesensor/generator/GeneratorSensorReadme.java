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
import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.NL;

public class GeneratorSensorReadme {

    public static final Pattern PATTERN_COMMENT = Pattern.compile("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/");

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

                result.append(NL).append("## ").append(sensorClass.getSimpleName()).append(NL);
                final Matcher matcher = PATTERN_COMMENT.matcher(content);
                while (matcher.find()) {
                    final String block = matcher.group(0).replaceFirst("^/\\**\n", "").replace("\n */", "");
                    result.append(parseNodes(Jsoup.parse(block).select("body").get(0).childNodes(), NL, sensorClass));
                }

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

    //FIXME: add table logic
    private static StringBuilder parseNodes(final List<Node> nodeList, final String nl, final Class clazz) {
        final StringBuilder result = new StringBuilder();
        for (Node node : nodeList) {
            if (node instanceof Element) {
                final Element element = (Element) node;

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
                        result.append("> ").append(parseNodes(node.childNodes(), nl + "> ", clazz));
                        result.append(nl);
                        break;
                    case "body":
                        result.append(parseNodes(node.childNodes(), nl, clazz));
                        result.append(nl);
                        break;
                    case "br":
                        result.append(nl);
                        break;
                    case "caption":
                        result.append(parseNodes(node.childNodes(), nl, clazz));
                        result.append(nl);
                        break;
                    case "CENTER":
                        break;
                    case "code":
                        result.append("```java").append(nl);
                        result.append(element.text()).append(nl);
                        result.append("```").append(nl);
                        break;
                    case "dl":
                        result.append(parseNodes(node.childNodes(), nl, clazz));
                        break;
                    case "dd":
                        result.append(nl).append("    ").append(parseNodes(node.childNodes(), nl + "    ", clazz)).append(nl);
                        break;
                    case "dt":
                        result.append(nl).append("**").append(parseNodes(node.childNodes(), nl, clazz)).append("**").append(nl);
                        break;
                    case "em":
                        result.append("*").append(parseNodes(node.childNodes(), nl, clazz)).append("*");
                        break;
                    case "FONT":
                        break;
                    case "h1":
                        result.append(NL).append("# ").append(parseNodes(node.childNodes(), nl, clazz)).append(NL);
                        break;
                    case "h2":
                        result.append(NL).append("## ").append(parseNodes(node.childNodes(), nl, clazz)).append(NL);
                        break;
                    case "h3":
                        result.append(NL).append("### ").append(parseNodes(node.childNodes(), nl, clazz)).append(NL);
                        break;
                    case "h4":
                        result.append(NL).append("#### ").append(parseNodes(node.childNodes(), nl, clazz)).append(NL);
                        break;
                    case "h5":
                        result.append(NL).append("##### ").append(parseNodes(node.childNodes(), nl, clazz)).append(NL);
                        break;
                    case "h6":
                        result.append(NL).append("###### ").append(parseNodes(node.childNodes(), nl, clazz)).append(NL);
                        break;
                    case "small":
                        result.append(NL).append("###### ").append(parseNodes(node.childNodes(), nl, clazz));
                        break;
                    case "hr":
                        result.append(NL).append("--- ").append(NL);
                        break;
                    case "strong":
                    case "b":
                        result.append("**").append(parseNodes(node.childNodes(), nl, clazz)).append("**");
                        break;
                    case "i":
                    case "sub":
                        result.append("*").append(parseNodes(node.childNodes(), nl, clazz)).append("*");
                        break;
                    case "img":
                        result.append("![").append(element.text()).append("]");
                        result.append("(").append(element.attr("src")).append(")");
                        break;
                    case "li":
                        result.append(NL).append("* ").append(parseNodes(node.childNodes(), NL + "* ", clazz));
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
                        result.append(parseNodes(node.childNodes(), nl, clazz));
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
            } else if (node instanceof TextNode) {
                String text = ((TextNode) node).text();

                final Matcher linkMatch = Pattern.compile("(\\{@link)(.*)(})").matcher(text);
                while (linkMatch.find()) {
                    final String[] link = linkMatch.group(2).split("#");
                    final String className = link[0].trim();
                    final Class linkedClass = searchClass(className, clazz);
                    result.append("[").append(linkedClass.getSimpleName()).append(link.length > 1 ? "#" + link[1].trim() : "").append("]");
                    result.append("(").append(new File("src/main/java", linkedClass.getTypeName().replace(".", File.separator) + ".java").toString()).append(")");
                }

                text = text.replaceAll("(\\{@link)(.*)(})", "").replaceFirst("^\\s*\\*", "").trim();
                text = text.replaceAll("(\\{@link)(.*)(})", "").replaceFirst("^\\s*\\*", "").trim();
                if (!text.isEmpty()) {
                    result.append(text);
                }
            }
        }
        return result;
    }

    private static Class<?> searchClass(final String className, final Class linkedClass) {
        try {
            return className.equals(linkedClass.getSimpleName()) || className.equals("this") ? linkedClass : Class.forName(className);
        } catch (ClassNotFoundException e) {
            try {
                final File classFile = new File(System.getProperty("user.dir"), new File("src/main/java", linkedClass.getTypeName().replace(".", File.separator)).toString() + ".java");
                String classRef = find("(import (static)*)(.*" + className + ");", new String(Files.readAllBytes(classFile.toPath())), 3);
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

    private static String find(final String pattern, final String content, final int group) {
        final Matcher matcher = Pattern.compile(pattern).matcher(content);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

}