package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.JFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.getClassVersions;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.LINE_SEPARATOR;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_COMMENT;
import static berlin.yuna.tinkerforgesensor.model.JFile.PATTERN_LINK;

public class GeneratorReadmeDocHelper {

    public static void generate(final StringBuilder navigation, final Set<String> packageGroups, final List<JFile> jFileList, final JFile jfile) {
        try {
            final List<JFile> jFiles = new ArrayList<>(jFileList);
            final StringBuilder result = new StringBuilder();
            result.append(LINE_SEPARATOR);
            result.append("## ");
            result.append(jfile.getClazz().getPackage().getName());
            result.append(".");
            result.append(jfile.getSimpleName());
            result.append(LINE_SEPARATOR);
            result.append(navigation);
            result.append(LINE_SEPARATOR).append("---").append(LINE_SEPARATOR);

            //Write all class versions on top
            result.append("###### ");
            for (JFile classVersion : getClassVersions(jfile, jFiles)) {
                result.append(classVersion.getSimpleName()).append(" · ");
            }
            result.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("---").append(LINE_SEPARATOR);

            final String content = new String(Files.readAllBytes(jfile.getPath()));
            final Matcher matcher = PATTERN_COMMENT.matcher(content);
            while (matcher.find()) {
                final String block = matcher.group(0).replaceFirst("^/\\**" + LINE_SEPARATOR, "").replace(LINE_SEPARATOR + " */", "").replaceAll("(^|" + LINE_SEPARATOR + ")\\s*\\*", LINE_SEPARATOR);
                result.append(parseNodes(Jsoup.parse(block).select("body").get(0), LINE_SEPARATOR, jFiles, jfile));
            }
            result.append(LINE_SEPARATOR).append("--- ").append(LINE_SEPARATOR);

            System.out.println("Created [" + jfile.getReadmeFilePath() + "]");
            Files.write(jfile.getReadmeFilePath().toPath(), result.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder parseNodes(final Node root, final String parentSeparator, final List<JFile> jFileList, final JFile jfile) {
        final StringBuilder result = new StringBuilder();
        for (Node node : root.childNodes()) {
            if (node instanceof Element) {
                result.append(transformHtmlElements(node, jFileList, jfile, parentSeparator));
            } else if (node instanceof TextNode) {
                result.append(resolveLinks(jFileList, jfile, ((TextNode) node).getWholeText()));
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

    private static Class<?> searchClass(final JFile jfile, final String className) {
        try {
            //Case of own class
            if (className.equals(jfile.getSimpleName()) || className.equals("this")) {
                return jfile.getClazz();
            }
            //Case of import
            final String classImport = regex("(import (static)*)(.*" + className + ");", new String(Files.readAllBytes(jfile.getPath())), 3);
            if (classImport != null) {
                return Class.forName(classImport);
            }
            //Case of same or java.lang package
            return Class.forName(jfile.getClazz().getPackage().getName() + "." + className);
        } catch (ClassNotFoundException | IOException ex) {
            try {
                return Class.forName("java.lang." + className);
            } catch (ClassNotFoundException exc) {
                return jfile.getClazz();
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
    private static String transformHtmlElements(final Node node, final List<JFile> jFileList, final JFile jfile, final String parentSeparator) {
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
                result.append("> ").append(parseNodes(node, parentSeparator + "> ", jFileList, jfile));
                result.append(parentSeparator);
                break;
            case "body":
                result.append(parseNodes(node, parentSeparator, jFileList, jfile));
                result.append(parentSeparator);
                break;
            case "br":
                result.append(parentSeparator);
                break;
            case "caption":
                result.append(parseNodes(node, parentSeparator, jFileList, jfile));
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
                result.append(parseNodes(node, parentSeparator, jFileList, jfile));
                break;
            case "dd":
                result.append(parentSeparator).append("    ").append(parseNodes(node, parentSeparator + "    ", jFileList, jfile)).append(parentSeparator);
                break;
            case "dt":
                result.append(parentSeparator).append("**").append(parseNodes(node, parentSeparator, jFileList, jfile)).append("**").append(parentSeparator);
                break;
            case "em":
                result.append("*").append(parseNodes(node, parentSeparator, jFileList, jfile)).append("*");
                break;
            case "FONT":
                break;
            case "h1":
                result.append(LINE_SEPARATOR).append("# ").append(parseNodes(node, parentSeparator, jFileList, jfile)).append(LINE_SEPARATOR);
                break;
            case "h2":
                result.append(LINE_SEPARATOR).append("## ").append(parseNodes(node, parentSeparator, jFileList, jfile)).append(LINE_SEPARATOR);
                break;
            case "h3":
                result.append(LINE_SEPARATOR).append("### ").append(parseNodes(node, parentSeparator, jFileList, jfile)).append(LINE_SEPARATOR);
                break;
            case "h4":
                result.append(LINE_SEPARATOR).append("#### ").append(parseNodes(node, parentSeparator, jFileList, jfile)).append(LINE_SEPARATOR);
                break;
            case "h5":
                result.append(LINE_SEPARATOR).append("##### ").append(parseNodes(node, parentSeparator, jFileList, jfile)).append(LINE_SEPARATOR);
                break;
            case "h6":
                result.append(LINE_SEPARATOR).append("###### ").append(parseNodes(node, parentSeparator, jFileList, jfile)).append(LINE_SEPARATOR);
                break;
            case "small":
                result.append(LINE_SEPARATOR).append("###### ").append(parseNodes(node, parentSeparator, jFileList, jfile));
                break;
            case "hr":
                result.append(LINE_SEPARATOR).append("--- ").append(LINE_SEPARATOR);
                break;
            case "strong":
            case "b":
                result.append("**").append(parseNodes(node, parentSeparator, jFileList, jfile)).append("**");
                break;
            case "i":
            case "sub":
                result.append("*").append(parseNodes(node, parentSeparator, jFileList, jfile)).append("*");
                break;
            case "img":
                result.append("![").append(element.text()).append("]");
                result.append("(").append(element.attr("src")).append(")");
                break;
            case "li":
                result.append(LINE_SEPARATOR).append("* ").append(parseNodes(node, LINE_SEPARATOR + "* ", jFileList, jfile));
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
                result.append(parseNodes(node, parentSeparator, jFileList, jfile));
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

    private static String resolveLinks(final List<JFile> jFileList, final JFile jfile, final String inputString) {
        final StringBuilder result = new StringBuilder();
        String text = inputString;
        final Matcher linkMatch = PATTERN_LINK.matcher(text);
        while (linkMatch.find()) {
            final String[] link = linkMatch.group(2).split("#");
            final Class linkedClass = searchClass(jfile, link[0].trim());


            final StringBuilder linkText = new StringBuilder();
            final Optional<JFile> linkedClassSource = jFileList.stream().filter(file -> file.getClazz() == linkedClass).findFirst();
            if (linkedClassSource.isPresent()) {
                if (linkedClassSource.get().hasComments()) {
                    final String linkDesc = link.length > 1 ? link[1].trim() : linkedClass.getSimpleName();
                    linkText.append("[").append(linkDesc).append("]");
                    linkText.append("(").append(linkedClassSource.get().getReadmeFileUrl().toString()).append(")");

                    linkText.append(" ([source]");
                    linkText.append("(").append(linkedClassSource.get().getRelativeMavenUrl().toString()).append("))");
                } else {
                    final String linkDesc = link.length > 1 ? link[1].trim() + " (" + linkedClass.getSimpleName() + ")" : linkedClass.getSimpleName();
                    linkText.append("[").append(linkDesc).append("]").append("(").append(linkedClassSource.get().getRelativeMavenUrl().toString()).append(")");
                }
            } else {
                linkText.append(link.length > 1 ? link[1].trim() + " (" + linkedClass.getSimpleName() + ")" : linkedClass.getSimpleName());
            }

            text = text.replaceFirst(PATTERN_LINK.pattern(), linkText.toString());
        }

        if (!text.trim().isEmpty() && text.trim().length() > 1) {
            result.append(text);
        }
        return result.toString();
    }
}