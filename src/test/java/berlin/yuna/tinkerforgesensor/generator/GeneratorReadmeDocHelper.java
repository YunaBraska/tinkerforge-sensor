package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.JFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.SEPARATOR;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.getClassVersions;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.LINE_SEPARATOR;
import static berlin.yuna.tinkerforgesensor.model.JFile.DIR_PROJECT;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_CODE;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_COMMENT;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_LINK;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_PARAM;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_SEE;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_SINCE;

public class GeneratorReadmeDocHelper {

    public static void generate(final StringBuilder navigation, final List<JFile> jFileList, final JFile jFile) {
        try {
            final List<JFile> jFiles = new ArrayList<>(jFileList);
            final StringBuilder result = new StringBuilder();
            result.append("## ");
            result.append(jFile.getClazz().getPackage().getName());
            result.append(".");
            result.append(jFile.getBasicName());
            result.append(LINE_SEPARATOR);
            result.append(navigation);
            result.append("---").append(LINE_SEPARATOR);

            //Write all class versions on top
            result.append("###### ");
            for (JFile classVersion : getClassVersions(jFile, jFiles)) {
                result.append(classVersion.getSimpleName()).append(" · ");
            }
            result.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("---").append(LINE_SEPARATOR);

            final String content = new String(Files.readAllBytes(jFile.getPath()));
            final Matcher matcher = PATTERN_COMMENT.matcher(content);
            while (matcher.find()) {
                final String block = matcher.group(0).replaceFirst("^/\\**" + LINE_SEPARATOR, "").replaceAll("\\*/$", "").replaceAll("(^|" + LINE_SEPARATOR + ")\\s*\\*", "");
                result.append(parseNodes(Jsoup.parse(block).select("body").get(0), LINE_SEPARATOR, jFiles, jFile));
                result.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("--- ").append(LINE_SEPARATOR);
            }

            final File targetFile = new File(DIR_PROJECT, jFile.getReadmeFilePath().toString());
            System.out.println("Created [" + targetFile + "]");
            Files.write(targetFile.toPath(), result.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseNodes(final Node root, final String parentSeparator, final List<JFile> jFileList, final JFile jFile) {
        final StringBuilder result = new StringBuilder();
        for (Node node : root.childNodes()) {
            if (node instanceof Element) {
                result.append(transformHtmlElements(node, jFileList, jFile, parentSeparator));
            } else if (node instanceof TextNode) {
                String text = resolveCode(((TextNode) node).getWholeText());
                text = resolveParam(text);
                text = resolveSince(text);
                text = resolveLinks(jFileList, jFile, text, PATTERN_LINK);
                text = resolveLinks(jFileList, jFile, text, PATTERN_SEE);
                result.append(text);
            }
        }
        return result.toString().trim();
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
        return sb.toString();
    }

    private static Class<?> searchClass(final JFile jFile, final String className) {
        try {
            //Case of own class
            if (className.equals(jFile.getSimpleName()) || className.equals("this")) {
                return jFile.getClazz();
            }
            //Case of import
            final String classImport = regex("(import (static)*)(.*" + className + ");", new String(Files.readAllBytes(jFile.getPath())), 3);
            if (classImport != null) {
                return Class.forName(classImport);
            }
            //Case of same or java.lang package
            return Class.forName(jFile.getClazz().getPackage().getName() + "." + className);
        } catch (ClassNotFoundException | IOException ex) {
            try {
                return Class.forName("java.lang." + className);
            } catch (ClassNotFoundException exc) {
                return jFile.getClazz();
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
    private static String transformHtmlElements(final Node node, final List<JFile> jFileList, final JFile jFile, final String parentSeparator) {
        final Element element = (Element) node;
        final StringBuilder result = new StringBuilder();
        switch (element.tagName().toLowerCase()) {
            case "a":
                result.append("[").append(element.text()).append("]");
                result.append("(").append(element.attr("href")).append(")");
                break;
            case "pre":
            case "span":
                result.append("`").append(parseNodes(node, parentSeparator + "> ", jFileList, jFile)).append("`");
                break;
            case "blockquote":
                result.append("> ").append(parseNodes(node, parentSeparator + "> ", jFileList, jFile));
                result.append(parentSeparator);
                break;
            case "body":
                result.append(parseNodes(node, parentSeparator, jFileList, jFile));
                result.append(parentSeparator);
                break;
            case "br":
                result.append(parentSeparator);
                break;
            case "caption":
                result.append(parseNodes(node, parentSeparator, jFileList, jFile));
                result.append(parentSeparator);
                break;
            case "CENTER":
                break;
            case "code":
                result.append(parentSeparator).append("```java").append(SEPARATOR);
                final String code = filterTextOnly(element.childNodes()).replace("; ", ";" + SEPARATOR);
                result.append(code.trim()).append(parentSeparator);
                result.append("```").append(parentSeparator);
                break;
            case "dl":
                result.append(parseNodes(node, parentSeparator, jFileList, jFile));
                break;
            case "dd":
                result.append(parentSeparator).append("    ").append(parseNodes(node, parentSeparator + "    ", jFileList, jFile)).append(parentSeparator);
                break;
            case "dt":
                result.append(parentSeparator).append("**").append(parseNodes(node, parentSeparator, jFileList, jFile)).append("**").append(parentSeparator);
                break;
            case "em":
                result.append("*").append(parseNodes(node, parentSeparator, jFileList, jFile)).append("*");
                break;
            case "FONT":
                break;
            case "h1":
                result.append(LINE_SEPARATOR).append("# ").append(parseNodes(node, parentSeparator, jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "h2":
                result.append(LINE_SEPARATOR).append("## ").append(parseNodes(node, parentSeparator, jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "h3":
                result.append(LINE_SEPARATOR).append("### ").append(parseNodes(node, parentSeparator, jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "h4":
                result.append(LINE_SEPARATOR).append("#### ").append(parseNodes(node, parentSeparator, jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "h5":
                result.append(LINE_SEPARATOR).append("##### ").append(parseNodes(node, parentSeparator, jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "h6":
                result.append(LINE_SEPARATOR).append("###### ").append(parseNodes(node, parentSeparator, jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "small":
                result.append(LINE_SEPARATOR).append("###### ").append(parseNodes(node, parentSeparator, jFileList, jFile));
                break;
            case "hr":
                result.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("--- ").append(LINE_SEPARATOR);
                break;
            case "strong":
            case "b":
                result.append("**").append(parseNodes(node, parentSeparator, jFileList, jFile)).append("**");
                break;
            case "i":
            case "sub":
                result.append("*").append(parseNodes(node, parentSeparator, jFileList, jFile)).append("*");
                break;
            case "img":
                result.append("![").append(element.text()).append("]");
                result.append("(").append(element.attr("src")).append(")");
                break;
            case "li":
                result.append("* ").append(parseNodes(node, LINE_SEPARATOR + "* ", jFileList, jFile)).append(LINE_SEPARATOR);
                break;
            case "head":
            case "html":
            case "link":
            case "menu":
            case "div":
            case "meta":
            case "noscript":
            case "ul":
            case "p":
            case "ol":
            case "script":
                result.append(parseNodes(node, parentSeparator, jFileList, jFile));
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

    private static String resolveLinks(final List<JFile> jFileList, final JFile jFile, final String inputString, final Pattern pattern) {
        String text = inputString;
        final Matcher match = pattern.matcher(text);
        while (match.find()) {
            final String[] matchGroup = match.group(1).split("#");
            final Class linkedClass = searchClass(jFile, matchGroup[0].trim());

            final StringBuilder result = new StringBuilder();
            final Optional<JFile> linkedClassSource = jFileList.stream().filter(file -> file.getClazz() == linkedClass).findFirst();
            if (linkedClassSource.isPresent()) {
                if (linkedClassSource.get().hasComments()) {
                    final String linkDesc = matchGroup.length > 1 ? matchGroup[1].trim() : linkedClass.getSimpleName();
                    result.append("[").append(linkDesc).append("]");
                    result.append("(").append(linkedClassSource.get().getReadmeFileUrl().toString()).append(")");

                    result.append(" ([source]");
                    result.append("(").append(linkedClassSource.get().getRelativeMavenUrl().toString()).append(")) ");
                } else {
                    final String linkDesc = matchGroup.length > 1 ? matchGroup[1].trim() + " (" + linkedClass.getSimpleName() + ")" : linkedClass.getSimpleName();
                    result.append("[").append(linkDesc).append("]").append("(").append(linkedClassSource.get().getRelativeMavenUrl().toString()).append(")");
                }
            } else {
                result.append(matchGroup.length > 1 ? matchGroup[1].trim() + " (" + linkedClass.getSimpleName() + ")" : linkedClass.getSimpleName());
            }

            text = text.replaceFirst(pattern.pattern(), result.toString().replace("@see", ""));
        }
        return text;
    }

    private static String resolveParam(final String inputString) {
        String text = inputString;
        final Matcher match = PATTERN_PARAM.matcher(text);
        while (match.find()) {
            final StringBuilder result = new StringBuilder();
            final String[] matchGroup = match.group(1).split(" ");

            result.append("**Parameter** ");
            if (matchGroup.length > 1) {
                result.append("*").append(matchGroup[1]).append("* ");
            }
            text = text.replaceFirst(PATTERN_PARAM.pattern(), result.toString());
        }
        return text;
    }

    private static String resolveCode(final String inputString) {
        String text = inputString;
        final Matcher match = PATTERN_CODE.matcher(text);
        while (match.find()) {
            final StringBuilder result = new StringBuilder();
            final String[] matchGroup = match.group(1).split(" ");
            if (matchGroup.length > 1) {
                result.append(" *").append(matchGroup[1]).append("*");
            }
            text = text.replaceFirst(PATTERN_CODE.pattern(), result.toString());
        }
        return text;
    }

    private static String resolveSince(final String inputString) {
        String text = inputString;
        final Matcher match = PATTERN_SINCE.matcher(text);
        while (match.find()) {
            final StringBuilder result = new StringBuilder();
            final String[] matchGroup = match.group(1).split(" ");
            if (matchGroup.length > 1) {
                result.append(" *Since ").append(matchGroup[1].trim()).append("* ");
            }
            text = text.replaceFirst(PATTERN_SINCE.pattern(), result.toString());
        }
        return text;
    }
}