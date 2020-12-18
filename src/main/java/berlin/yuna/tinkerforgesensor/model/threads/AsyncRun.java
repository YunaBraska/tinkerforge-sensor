package berlin.yuna.tinkerforgesensor.model.threads;

import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class AsyncRun implements Runnable {

    protected Thread thread;
    protected boolean running = false;

    protected final String name;
    protected final Consumer<Long> consumer;

    protected AsyncRun(final String name, final Consumer<Long> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public synchronized void stopAsync() {
        final Object result = TimeoutExecutor.execute(1000, () -> {
            if (running) {
                running = false;
                thread.join();
            }
            return !running;
        });
        if (result instanceof Throwable) {
            thread.interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isNotRunning() {
        return !running;
    }

    protected synchronized void startAsync() {
        if (running)
            return;
        running = true;
        thread = new Thread(this, name);
        thread.start();
    }

    protected void sleep(final long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException ignored) {
            System.err.printf("Interrupted [%s]%n", name);
        }
    }
}
