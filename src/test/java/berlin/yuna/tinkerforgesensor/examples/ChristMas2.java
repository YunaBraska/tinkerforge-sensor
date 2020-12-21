package berlin.yuna.tinkerforgesensor.examples;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.threads.Color;

import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.sleep;

public class ChristMas2 {

    final static Stack stack = new Stack();
    static Sensor speaker;
    static Sensor button;

    final static int speed = 10;
    final static int pause_s = 5 * speed;
    final static int pause_M = 10 * speed;
    final static int duration_s = 10 * speed;
    final static int duration_M = 20 * speed;
    final static int duration_L = 30 * speed;

    final static int C = 500;
    final static int D = 722;
    final static int E = 944;
    final static int F = 1166;
    final static int G = 1388;
    final static int A = 1610;
    final static int B = 1832;

    //C && D unsichtbar unten, dann kommt sichtbar E F G A (B/H)
    //Zeilen = Schwarz und Weiß
    public static void main(final String[] args) {
        stack.connect();

        speaker = stack.get().speaker();
        button = stack.get().buttonRGB();
        stack.getSensors().forEach(Sensor::setStatusLedOff);


        sendTimes(3, F, pause_M, duration_M);
        sleep(4 * pause_M);
        sendTimes(3, F, pause_M, duration_M);
        sleep(4 * pause_M);
        sendTimes(1, F, pause_M, duration_M);
        sendTimes(1, A, pause_M, duration_M);
        sendTimes(1, D, pause_M, duration_M);
        sendTimes(1, E, pause_M, duration_M);
        sendTimes(1, F, pause_M, duration_M);
        sleep(4 * pause_M);
        sendTimes(4, G, pause_M, duration_M);
        sleep(4 * pause_M);
        sendTimes(1, G, pause_M, duration_M);
        sendTimes(4, F, pause_M, duration_M);
        sleep(4 * pause_M);
        sendTimes(1, F, pause_M, duration_M);
        sendTimes(2, E, pause_M, duration_M);
        sendTimes(1, F, pause_M, duration_M);
        sleep(4 * pause_M);
        sendTimes(1, E, pause_M, duration_M);
        sendTimes(1, A, pause_M, duration_M);
//
//        for (int i = 500; i < 2000; i = i + (2000 / 9)) {
//            System.out.println("Ton: " + i );
//            speaker.sendSound(1000, i);
////            sleep(1000);
//        }

//        sendTimes(3, 1500, pause_M, duration_M);
//        sleep(256);
//        sendTimes(3, 1500, pause_M, duration_M);
//        sleep(256);
//        sendTimes(1, 1500, pause_M, duration_M);
//        sendTimes(1, 1600, pause_M, duration_M);
//        sendTimes(1, 1500, pause_M, duration_L);
//        sleep(64);
//        sendTimes(1, 1500, pause_s, duration_s);
//        sendTimes(1, 1500, pause_M, duration_L);

    }

    private static void sendTimes(final int times, final int frequency, final int pause, final int duration) {
        for (int i = 0; i < times; i++) {
            setColor(duration);
            speaker.sendSound(duration, frequency, true);
            button.sendColor(Color.BLACK);
            if (i + i < times) {
                sleep(pause);
            }
        }
    }

    private static void setColor(int duration) {
        switch (duration) {
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
