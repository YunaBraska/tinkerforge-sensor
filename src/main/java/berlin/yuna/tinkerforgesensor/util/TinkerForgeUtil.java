package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.model.Loop;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import com.tinkerforge.DummyDevice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.requireNonNull;

public class TinkerForgeUtil {

    /**
     * loop = list of subprograms
     */
    public static final ConcurrentHashMap<String, Loop> loops = new ConcurrentHashMap<>();

    /**
     * default date formatter
     */
    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    /**
     * @param string to check on
     * @return true if string is null or empty otherwise false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    /**
     * @param resourceFile file name or file path - starting from resource folder
     * @return returns file content as {@link StandardCharsets#UTF_8} string
     */
    protected String readFile(final String resourceFile) {
        try {
            return new String(readAllBytes(Paths.get(requireNonNull(this.getClass().getClassLoader().getResource(resourceFile)).toURI())), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Synonym for System.out.println
     *
     * @param print message for console
     */
    protected static void console(final String print) {
        System.out.println(dateString() + print);
    }

    /**
     * Synonym for System.out.println with {@link java.util.Formatter#format(Locale, String, Object...)}
     *
     * @param print message for console
     */
    protected static void console(final Object... print) {
        System.out.println(dateString() + format(print[0].toString(), copyOfRange(print, 1, print.length)));
    }

    /**
     * Synonym for System.err.println
     *
     * @param print message for console
     */
    protected static void error(final String print) {
        System.err.println(dateString() + print);
    }

    /**
     * Synonym for System.err.println with {@link java.util.Formatter#format(Locale, String, Object...)}
     *
     * @param print message for console
     */
    protected static void error(final Object... print) {
        System.err.println(dateString() + format(print[0].toString(), copyOfRange(print, 1, print.length)));
    }

    protected static Loop loop(final String name) {
        return loops.get(name);
    }

    protected Loop loop(final String name, final Consumer<Long> consumer) {
        return loop(name, EACH_SECOND, consumer);
    }

    protected Loop loop(final String name, final RefreshType refreshType, final Consumer<Long> consumer) {
        return loop(name, refreshType.ms, consumer);
    }

    protected Loop loop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        return createLoop(name, refreshMs, consumer);
    }

    public static Loop createLoop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        if (loops.containsKey(name)) {
            loopEnd(name);
        }
        Loop loop = new Loop(name, refreshMs, consumer);
        loops.put(name, loop);
        return loop;
    }

    public static boolean loopEnd(final String name) {
        Loop loop = loops.get(name);
        if (loop != null) {
            loop.stop();
            loops.remove(name);
            return true;
        }
        return false;
    }

    protected static void sleep(final long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static boolean isPresent(final Sensor sensor) {
        return !(sensor.device instanceof DummyDevice);
    }

    protected static double roundUp(double value) {
        return roundUp(value, 2);
    }

    protected static double roundUp(double value, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, decimals);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    protected static String letterUp(final String chr, int number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(chr);
        }
        return stringBuilder.toString();
    }

    private static String dateString() {
        return dateFormat.format(new Date()) + " ";
    }

    public enum RefreshType {
        EACH_SECOND(1000), CUSTOM_INTERVAL(0), POST_PROCESS(-1000);

        public final long ms;

        RefreshType(long ms) {
            this.ms = ms;
        }
    }
}
