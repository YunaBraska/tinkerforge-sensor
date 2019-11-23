package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.AudioCmd;

import java.net.URL;

import static berlin.yuna.tinkerforgesensor.model.AudioCmd.PAUSE;
import static berlin.yuna.tinkerforgesensor.model.AudioCmd.PLAY;
import static berlin.yuna.tinkerforgesensor.model.AudioCmd.REPLAY;
import static berlin.yuna.tinkerforgesensor.model.AudioCmd.STOP;

public class Check_LocalAudio extends Helper {

    private static Stack stack;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.clear();

        final URL siren = Check_LocalAudio.class.getClassLoader().getResource("sounds/siren.mp3");
        final URL cheer = Check_LocalAudio.class.getClassLoader().getResource("sounds/cheer.wav");
        final URL sonar = Check_LocalAudio.class.getClassLoader().getResource("sounds/sonar.wav");
        final URL sonarLong = Check_LocalAudio.class.getClassLoader().getResource("sounds/sonarLong.wav");

        console("Play mp3 sound file should be ignored");
        stack.sensors().localAudio().send(siren);
        sleep(2000);

        console("Play sound file");
        stack.sensors().localAudio().send(cheer);
        sleep(2000);

        console("Pause sound file");
        stack.sensors().localAudio().send(PAUSE);
        sleep(2000);

        console("Resume sound file");
        stack.sensors().localAudio().send(PLAY);
        sleep(2000);

        console("Stop and Play sound file should move cursor to start");
        stack.sensors().localAudio().send(STOP);
        sleep(1000);
        stack.sensors().localAudio().send(PLAY);
        sleep(2000);

        console("Replay sound file");
        stack.sensors().localAudio().send(REPLAY);
        sleep(2000);

        console("Play sound file again should stop current stream");
        stack.sensors().localAudio().send(cheer);
        sleep(2000);

        //playerId, (String) file, (int) volume, (boolean) mute
        console("Play volume 10 (full parameterised)");
        stack.sensors().localAudio().send(1, cheer, 10, false, REPLAY);
        sleep(2000);

        console("Play volume 20 (full parameterised)");
        stack.sensors().localAudio().send(1, cheer, 30, false, PLAY);
        sleep(2000);

        console("Play mute (full parameterised)");
        stack.sensors().localAudio().send(1, cheer, 20, true, REPLAY);
        sleep(2000);

        console("unmute");
        stack.sensors().localAudio().send(false);
        sleep(2000);

        console("Play multiple sounds on different player");
        for (int i = 0; i < 10; i++) {
            stack.sensors().localAudio().send(i, cheer);
            sleep(500);
        }
        sleep(2000);
        sleep(5000);

        console("Play two sound files");
        stack.sensors().localAudio().send(2, sonarLong, 10, false, PLAY);
        sleep(1000);
        stack.sensors().localAudio().send(1, sonar, 10, false, PLAY);
        sleep(1000);
        stack.sensors().localAudio().send(0, sonar, 10, false, PLAY);
        sleep(10000);
        stack.close();
    }
}
