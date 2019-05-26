package berlin.yuna.tinkerforgesensor.model.type;

import java.util.function.Consumer;

import static java.lang.String.format;

public abstract class AsyncRun implements Runnable {

    protected Thread thread;
    protected boolean running = false;

    protected final String name;
    protected final Consumer<Long> consumer;

    protected AsyncRun(final String name, final Consumer<Long> consumer) {
        this.name = name;
        this.consumer = consumer;
        this.start();
    }

    public synchronized void stop() {
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

    protected synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this, name);
        thread.start();
    }

    protected void sleep(final long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            System.err.println(format("Interrupted [%s]", name));
        }
    }
}
