package berlin.yuna.tinkerforgesensor.model;

public class Beep {

    final int durationMs;
    final int frequency;
    final int volume;
    final boolean wait;

    public Beep(int duration, int frequency, int volume, final boolean wait) {
        this.durationMs = duration;
        this.frequency = frequency;
        this.volume = volume;
        this.wait = wait;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getVolume() {
        return volume;
    }

    public boolean getWait() {
        return wait;
    }
}
