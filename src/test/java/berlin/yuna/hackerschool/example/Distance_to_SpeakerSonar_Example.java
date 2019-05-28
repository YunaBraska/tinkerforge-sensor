package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;

public class Distance_to_SpeakerSonar_Example extends Helper {

    private static TinkerForge tinkerForge;

    public static void main(final String[] args) {
        tinkerForge = ConnectionAndPrintValues_Example.connect();
        sonar();

    }

    private static void sonar() {
        while (true) {
            final Long distance = tinkerForge.values().distance();
            if (distance > 1 && distance < 250 && timePassed(32)) {
                tinkerForge.sensors().speaker().send(32);
            }
            if (distance > 1 && timePassed(distance + 200)) {
                tinkerForge.sensors().speaker().send(distance / 8);
            }
        }

    }
}
