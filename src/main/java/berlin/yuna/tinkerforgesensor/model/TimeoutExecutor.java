package berlin.yuna.tinkerforgesensor.model;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TimeoutExecutor {

    public static Object execute(final long timeoutMs, final Callable<Object> method) {
        ExecutorService executor = Executors.newCachedThreadPool();

        Future<Object> future = executor.submit(method);
        try {
            return future.get(timeoutMs, MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException exception) {
            return exception;
        } finally {
            future.cancel(true);
        }
    }
}
