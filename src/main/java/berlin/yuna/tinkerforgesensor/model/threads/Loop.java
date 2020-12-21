package berlin.yuna.tinkerforgesensor.model.threads;

import java.util.UUID;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.RefreshType.EACH_SECOND;
import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.RefreshType.POST_PROCESS;
import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.sleep;
import static java.lang.System.currentTimeMillis;

public class Loop extends AsyncRun {

    private final long refreshMs;

    public Loop(final Consumer<Long> program) {
        this("Loop " + UUID.randomUUID().toString(), 512, program);
    }

    public Loop(final long refreshMs, final Consumer<Long> program) {
        this("Loop " + UUID.randomUUID().toString(), refreshMs, program);
    }

    public Loop(final String name, final long refreshMs, final Consumer<Long> program) {
        super(name, program);
        this.refreshMs = refreshMs;
    }

    public boolean isRunning() {
        return super.running;
    }

    public boolean isNotRunning() {
        return !super.running;
    }

    public Loop start(final boolean start) {
        if (start && isNotRunning()) {
            start();
        } else if (!start && isRunning()) {
            stop();
        }
        return this;
    }

    public Loop start() {
        super.startAsync();
        return this;
    }

    public Loop stop() {
        super.stopAsync();
        return this;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double numTicks = 60.0;
        final double nanoSeconds = 1000000000.0 / numTicks;
        double delta = 0;
        int frames = 0;
        int ticks = 0;
        long time = currentTimeMillis();

        while (running) {
            final long currentTime = System.nanoTime();
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

    @Override
    public String toString() {
        return "Loop{" +
                "name='" + name + '\'' +
                '}';
    }
}
