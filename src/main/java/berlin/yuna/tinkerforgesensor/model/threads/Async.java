package berlin.yuna.tinkerforgesensor.model.threads;

import java.util.function.Consumer;

public class Async extends AsyncRun {

    public Async(final String name, final Consumer<Long> consumer) {
        super(name, consumer);
        this.startAsync();
    }

    @Override
    public void run() {
        consumer.accept(System.currentTimeMillis());
        running = false;
    }

    @Override
    public String toString() {
        return "Async{" +
                "running=" + running +
                ", name='" + name + '\'' +
                '}';
    }
}
