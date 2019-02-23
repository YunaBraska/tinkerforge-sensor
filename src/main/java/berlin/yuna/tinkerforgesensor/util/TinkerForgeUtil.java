package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.model.type.Loop;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import com.tinkerforge.DummyDevice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;
import static java.lang.String.format;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.requireNonNull;

public class TinkerForgeUtil {

    /**
     * loop = list of subprograms
     */
    public static final ConcurrentHashMap<String, Loop> loops = new ConcurrentHashMap<>();

    /**
     * @param string to check on
     * @return true if string is null or empty otherwise false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
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

    public static boolean loopEnd(final String... names) {
        boolean success = true;
        for (String name : names) {
            Loop loop = loops.get(name);
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
