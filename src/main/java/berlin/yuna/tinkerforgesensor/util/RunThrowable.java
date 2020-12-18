package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.exception.ConnectionException;
import com.tinkerforge.TinkerforgeException;

@FunctionalInterface
public interface RunThrowable extends Runnable {

    @Override
    default void run() {
        try {
            runThrows();
        } catch (final TinkerforgeException th) {
            //TODO: remove sensor from stack on TinkerForgeExceptions
        } catch (final Throwable th) {
            throw new ConnectionException("Unexpected error", th);

        }
    }

    /**
     * Performs this operation on the given argument.
     *
     * @throws Throwable when the underlying apply throws
     */
    void runThrows() throws Throwable;

    static void handleConnection(final RunThrowable function) {
        function.run();
    }
}