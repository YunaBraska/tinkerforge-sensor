package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.model.threads.Async;
import berlin.yuna.tinkerforgesensor.model.threads.AsyncRun;
import berlin.yuna.tinkerforgesensor.model.threads.Loop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.RefreshType.EACH_SECOND;

public class ThreadUtil {

    /**
     * loop = list of subprograms
     */
    public static final Map<String, AsyncRun> loops = new ConcurrentHashMap<>();

    /**
     * @param string to check on
     * @return true if string is null or empty otherwise false
     */
    public static boolean isEmpty(final String string) {
        return string == null || string.trim().equals("");
    }

    public static AsyncRun loop(final String name) {
        return loops.get(name);
    }

    protected AsyncRun loop(final String name, final Consumer<Long> consumer) {
        return loop(name, EACH_SECOND, consumer);
    }

    protected AsyncRun loop(final String name, final RefreshType refreshType, final Consumer<Long> consumer) {
        return loop(name, refreshType.ms, consumer);
    }

    protected AsyncRun loop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        return createLoop(name, refreshMs, consumer);
    }

    public static AsyncRun createAsync(final String name, final Consumer<Long> consumer) {
        final AsyncRun prevRun = loop(name);
        if (prevRun != null && prevRun.isRunning()) {
            return loop(name);
        }

        final Async loop = new Async(name, consumer);
        loops.put(name, loop);
        return loop;
    }

    public static AsyncRun createLoop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        final AsyncRun prevRun = loop(name);
        if (prevRun != null && prevRun.isRunning()) {
            return loop(name);
        }

        final Loop loop = new Loop(name, refreshMs, consumer);
        loops.put(name, loop);
        loop.start();
        return loop;
    }

    public static boolean asyncStop(final String... names) {
        boolean success = true;
        for (String name : names) {
            final AsyncRun loop = loops.get(name);
            if (loop != null) {
                loop.stopAsync();
                loops.remove(name);
            } else {
                success = false;
            }
        }
        return success;
    }

    public static void sleep(final long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean waitFor(final int timeoutMs, final BooleanSupplier actual) {
        return waitFor(timeoutMs, actual, null);
    }

    public static <X extends Throwable> boolean waitFor(final int timeoutMs, final BooleanSupplier actual, final Supplier<? extends X> throwable) throws X {
        long finalTime = System.currentTimeMillis() + timeoutMs;
        while (!actual.getAsBoolean()) {
            sleep(24);
            if (System.currentTimeMillis() >= finalTime) {
                if (throwable != null) {
                    throw throwable.get();
                }
                return false;
            }
        }
        return true;
    }

    public enum RefreshType {
        EACH_SECOND(1000), CUSTOM_INTERVAL(0), POST_PROCESS(-1000);

        public final long ms;

        RefreshType(final long ms) {
            this.ms = ms;
        }
    }
}
