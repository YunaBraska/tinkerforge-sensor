package berlin.yuna.tinkerforgesensor.model;

public class LedState {
    final int id;
    final int state;

    public LedState(int id, int state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }
}
