package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.model.type.Loop;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
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
import java.util.function.Supplier;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.requireNonNull;

public class Helper {

    /**
     * loop = list of subprograms
     */
    public static final ConcurrentHashMap<String, Loop> loops = new ConcurrentHashMap<>();

    /**
     * waitProcessList processes which are waiting
     */
    private static final ConcurrentHashMap<String, Long> waitProcessList = new ConcurrentHashMap<>();
    /**
     * default date formatter
     */
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static boolean timePassed(final long waitMs) {
        StackTraceElement callerTrace = Thread.currentThread().getStackTrace()[2];
        String name = callerTrace.getClassName() + ":" + callerTrace.getMethodName() + ":" + callerTrace.getLineNumber();
        return timePassed(name, waitMs);
    }

    public static boolean timePassed(final String timerName, final long waitMs) {
        Long lastTimeMs = waitProcessList.computeIfAbsent(timerName, value -> System.currentTimeMillis());
        if ((lastTimeMs + waitMs) < System.currentTimeMillis()) {
            waitProcessList.put(timerName, System.currentTimeMillis());
            return true;
        }
        return false;
    }

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
    public String readFile(final String resourceFile) {
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
    public static void console(final String print) {
        System.out.println(dateTime() + " " + print);
    }

    /**
     * Synonym for System.out.println with {@link java.util.Formatter#format(Locale, String, Object...)}
     *
     * @param print message for console
     */
    public static void console(final Object... print) {
        System.out.println(dateTime() + " " + format(print[0].toString(), copyOfRange(print, 1, print.length)));
    }

    /**
     * Synonym for System.err.println
     *
     * @param print message for console
     */
    public static void error(final String print) {
        System.err.println(dateTime() + " " + print);
    }

    /**
     * Synonym for System.err.println with {@link java.util.Formatter#format(Locale, String, Object...)}
     *
     * @param print message for console
     */
    public static void error(final Object... print) {
        System.err.println(dateTime() + " " + format(print[0].toString(), copyOfRange(print, 1, print.length)));
    }

    public static Loop loop(final String name) {
        return loops.get(name);
    }

    public Loop loop(final String name, final Consumer<Long> consumer) {
        return loop(name, EACH_SECOND, consumer);
    }

    public Loop loop(final String name, final TinkerForgeUtil.RefreshType refreshType, final Consumer<Long> consumer) {
        return loop(name, refreshType.ms, consumer);
    }

    public Loop loop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        return TinkerForgeUtil.createLoop(name, refreshMs, consumer);
    }

    public static boolean loopEnd(final String... names) {
        return TinkerForgeUtil.loopEnd(names);
    }

    public static void sleep(final long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPresent(final Sensor sensor) {
        return !(sensor.device instanceof DummyDevice);
    }

    public static double roundUp(double value) {
        return roundUp(value, 2);
    }

    public static double roundUp(double value, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, decimals);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String letterUp(final String chr, int number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(chr);
        }
        return stringBuilder.toString();
    }

    public static String date() {
        return dateFormat.format(new Date());
    }

    public static String time() {
        return timeFormat.format(new Date());
    }

    public static String dateTime() {
        return dateTimeFormat.format(new Date());
    }

    static void loop(final Supplier supplier, final int times) {
        for (int i = 0; i < times; i++) {
            supplier.get();
        }
    }
}
