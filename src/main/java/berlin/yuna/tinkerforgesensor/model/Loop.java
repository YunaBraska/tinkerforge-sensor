package berlin.yuna.tinkerforgesensor.model;

import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.POST_PROCESS;
import static java.lang.System.currentTimeMillis;

public class Loop implements Runnable {

    private Thread thread;
    private boolean running = false;

    private final String name;
    private final long refreshMs;
    private final Consumer<Long> consumer;

    public Loop(final String name, final long refreshMs, final Consumer<Long> consumer) {
        this.name = name;
        this.consumer = consumer;
        this.refreshMs = refreshMs;
        this.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double numTicks = 60.0;
        double nanoSeconds = 1000000000.0 / numTicks;
        double delta = 0;
        int frames = 0;
        int ticks = 0;
        long time = currentTimeMillis();

        while (running) {
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
            if (currentTimeMillis() - time >= refreshMs) {
                consumer.accept(refreshMs);
                time += refreshMs;
                ticks = 0;
                frames = 0;
            } else if (currentTimeMillis() - time >= EACH_SECOND.ms) {
                consumer.accept(EACH_SECOND.ms);
                time += EACH_SECOND.ms;
                ticks = 0;
                frames = 0;
            }
        }
    }

    private synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this, name);
        thread.start();
    }

    public synchronized void stop() {
        Object result = TimeoutExecutor.execute(1000, () -> {
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

    private void sleep(final long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Loop{" +
                "name='" + name + '\'' +
                '}';
    }
}
