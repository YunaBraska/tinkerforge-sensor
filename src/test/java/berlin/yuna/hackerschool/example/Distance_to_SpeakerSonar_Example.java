package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;

public class Distance_to_SpeakerSonar_Example extends Helper {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        sonar();

    }

    private static void sonar() {
        while (true) {
            final Long distance = stack.values().distance();
            if (distance > 1 && distance < 250 && timePassed(32)) {
                stack.sensors().speaker().send(32);
            }
            if (distance > 1 && timePassed(distance + 200)) {
                stack.sensors().speaker().send(distance / 8);
            }
        }

    }
}
