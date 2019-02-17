package com.tinkerforge;

public class TinkerforgeThread extends Thread {

    boolean stop = false;

    TinkerforgeThread(final String name) {
        super(name + "_overwrite");
    }

    public boolean isStop() {
        return stop;
    }

    public boolean setStop(boolean stop) {
        System.out.println("Stop [" + stop + "] [" + getName() + "]");
        this.stop = stop;
        return stop;
    }
}
