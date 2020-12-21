package berlin.yuna.tinkerforgesensor.util;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
    @Override
    default void run() {
        try {
            tryRun();
        } catch (final Throwable t) {
            throwUnchecked(t);
        }
    }

    private static <E extends RuntimeException> void throwUnchecked(Throwable t) {
        throw (E) t;
    }

    void tryRun() throws Throwable;
}