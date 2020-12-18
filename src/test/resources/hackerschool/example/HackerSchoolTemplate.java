package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.thread.SensorEvent;

public abstract class HackerSchoolTemplate extends Helper {

    protected final Stack stack;

    public HackerSchoolTemplate() {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(this::onSensorEvent);
        onStart();
    }

    public abstract void onStart();

    public abstract void onSensorEvent(final SensorEvent event);
}
