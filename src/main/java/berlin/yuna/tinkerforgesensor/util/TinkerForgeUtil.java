package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Async;
import berlin.yuna.tinkerforgesensor.model.type.Loop;
import berlin.yuna.tinkerforgesensor.model.type.AsyncRun;
import com.tinkerforge.DummyDevice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;

public class TinkerForgeUtil {

    /**
     * loop = list of subprograms
     */
    public static final ConcurrentHashMap<String, AsyncRun> loops = new ConcurrentHashMap<>();

    /**
     * @param string to check on
     * @return true if string is null or empty otherwise false
     */
    public static boolean isEmpty(String string) {
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
        AsyncRun prevRun = loop(name);
        if (prevRun != null && prevRun.isRunning()) {
            return loop(name);
        }

        Async loop = new Async(name, consumer);
        loops.put(name, loop);
        return loop;
    }

    public static AsyncRun createLoop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        AsyncRun prevRun = loop(name);
        if (prevRun != null && prevRun.isRunning()) {
            return loop(name);
        }

        Loop loop = new Loop(name, refreshMs, consumer);
        loops.put(name, loop);
        return loop;
    }

    public static boolean asyncStop(final String... names) {
        boolean success = true;
        for (String name : names) {
            AsyncRun loop = loops.get(name);
            if (loop != null) {
                loop.stop();
                loops.remove(name);
            } else {
                success = false;
            }
        }
        return success;
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

    public enum RefreshType {
        EACH_SECOND(1000), CUSTOM_INTERVAL(0), POST_PROCESS(-1000);

        public final long ms;

        RefreshType(long ms) {
            this.ms = ms;
        }
    }
}
