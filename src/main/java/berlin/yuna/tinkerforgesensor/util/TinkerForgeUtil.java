package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import com.tinkerforge.DummyDevice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.POST_PROCESS;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.requireNonNull;

public class TinkerForgeUtil {

    protected static CopyOnWriteArrayList<Thread> loopList = new CopyOnWriteArrayList<>();

    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    protected boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    protected String readFile(final String file) {
        try {
            return new String(readAllBytes(Paths.get(requireNonNull(this.getClass().getClassLoader().getResource(file)).toURI())), UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void console(final String print) {
        System.out.println(dateString() + print);
    }

    protected static void console(final Object... print) {
        System.out.println(dateString() + format(print[0].toString(), copyOfRange(print, 1, print.length)));
    }

    protected static void error(final String print) {
        System.err.println(dateString() + print);
    }

    protected static void error(final Object... print) {
        System.err.println(dateString() + format(print[0].toString(), copyOfRange(print, 1, print.length)));
    }

    private static String dateString() {
        return dateFormat.format(new Date()) + " ";
    }

    public enum RefreshType {
        EACH_SECOND(1000), CUSTOM_INTERVAL(0), POST_PROCESS(-1000);

        public long ms;

        RefreshType(long ms) {
            this.ms = ms;
        }
    }

    protected Thread loop(final Consumer<Long> consumer) {
        return loop(EACH_SECOND, consumer);
    }

    protected Thread loop(final RefreshType refreshType, final Consumer<Long> consumer) {
        return loop(refreshType.ms, consumer);
    }

    protected Thread loop(final long refreshMs, final Consumer<Long> consumer) {
        return createThread(refreshMs, consumer);
    }

    protected static Thread createThread(final long refreshMs, final Consumer<Long> consumer) {
        Thread thread = new Thread(() -> {
            long lastTime = System.nanoTime();
            final double numTicks = 60.0;
            double nanoSeconds = 1000000000.0 / numTicks;
            double delta = 0;
            int frames = 0;
            int ticks = 0;
            long time = System.currentTimeMillis();

            while (true) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / nanoSeconds;
                lastTime = currentTime;

                if (delta >= 1) {
                    if (refreshMs == POST_PROCESS.ms) {
                        consumer.accept(POST_PROCESS.ms);
                    }
                    ticks++;
                    delta--;
                }
                frames++;
                sleep(15);
                if (System.currentTimeMillis() - time >= EACH_SECOND.ms) {
                    consumer.accept(EACH_SECOND.ms);
                    time += EACH_SECOND.ms;
                    ticks = 0;
                    frames = 0;
                } else if (System.currentTimeMillis() - time >= refreshMs) {
                    consumer.accept(refreshMs);
                    time += refreshMs;
                    ticks = 0;
                    frames = 0;
                }
            }
        });
        loopList.add(thread);
        thread.start();
        return thread;
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
}
