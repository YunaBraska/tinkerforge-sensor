package berlin.yuna.tinkerforgesensor.examples;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.threads.Color;

import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.sleep;

public class ChristMas {

    final static Stack stack = new Stack();
    static Sensor speaker;
    static Sensor button;

    final static int speed = 10;
    final static int pause_s = 5 * speed;
    final static int pause_M = 10 * speed;
    final static int duration_s = 10 * speed;
    final static int duration_M = 20 * speed;
    final static int duration_L = 30 * speed;

    public static void main(final String[] args) {
        stack.connect();

        speaker = stack.get().speaker();
        button = stack.get().buttonRGB();
        stack.getSensors().forEach(Sensor::setStatusLedOff);

        sendTimes(3, 1500, pause_M, duration_M);
        sleep(256);
        sendTimes(3, 1500, pause_M, duration_M);
        sleep(256);
        sendTimes(1, 1500, pause_M, duration_M);
        sendTimes(1, 1600, pause_M, duration_M);
        sendTimes(1, 1500, pause_M, duration_L);
        sleep(64);
        sendTimes(1, 1500, pause_s, duration_s);
        sendTimes(1, 1500, pause_M, duration_L);

    }

    private static void sendTimes(final int times, final int frequency, final int pause, final int duration) {
        for (int i = 0; i < times; i++) {
            setColor(duration);
            speaker.sendSound(duration, frequency, true);
            button.sendColor(Color.BLACK);
            sleep(pause);
        }
    }

    private static void setColor(int duration) {
        switch(duration){
            case duration_s:
                button.sendColor(Color.GREEN);
                break;
            case duration_M:
                button.sendColor(Color.YELLOW);
                break;
            case duration_L:
                button.sendColor(Color.RED);
                break;

        }
    }
}
