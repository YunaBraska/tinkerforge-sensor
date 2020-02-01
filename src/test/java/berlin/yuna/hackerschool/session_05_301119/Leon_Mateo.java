package berlin.yuna.hackerschool.session_05_301119;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.Loop;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import java.util.Random;

/**
 * Code Link: http://hs.yuna.berlin/
 * Sensor Doku Link: https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md
 */
public class Leon_Mateo extends Helper {

    //VARIABLES
    Stack stack;
    int positionplayer = 1;
    int playerX = 1;
    int gegner1Y = 10;
    int gegner1X = 10;

    int gegner2Y = 10;
    int gegner2X = 10;
    int an = 1;

    Loop fpsApp = new Loop(128, this::fps);

    private void fps(final long refreshMs) {
        if(timePassed(512)){
            //stack.sensors().displayLcd128x64().send(true);
        }

        String spaces = "";
        for(int x = 0; x < positionplayer; x++){
            spaces = spaces + " ";
        }

        stack.sensors().displayLcd128x64().send(spaces + "8${s}");
    }


    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {

//             <-------------- SPIELER ------------------->

        if (timePassed(512)) {

            if (positionplayer > 19 && stack.values().cursorMoveX() < -90) {
                positionplayer = 1;
            }

            if (positionplayer < 0 && stack.values().cursorMoveX() > 90) {
                positionplayer = 20;
            }

            if (stack.values().cursorMoveX() < -90) {
                stack.sensors().speaker().send(200, 1000, 10);
                positionplayer++;
            }

            if (stack.values().cursorMoveX() > 90) {
                stack.sensors().speaker().send(200, 1000, 10);
                positionplayer--;
            }

            if (timePassed(0)) {
                console(positionplayer);
            }


        }

//             <-------------- GEGNER 1 ------------------->

        if (timePassed(128) && gegner1Y > 0) {
            stack.sensors().displayLcd128x64().send("*", gegner1X, gegner1Y);
            gegner1Y--;
        }


        if (gegner1Y == 0 && gegner1X == positionplayer) {
            positionplayer ++;
            fpsApp.stop();
            stack.sensors().displayLcd128x64().send(true);
            stack.sensors().displayLcd128x64().send("GAME OVER", true);
            stack.sensors().speaker().send(1000, 1000, 1000);
            sleep(1000);
            stack.sensors().displayLcd128x64().send(true);
            stack.sensors().displayLcd128x64().send("GAME OVER", true);
            System.exit(0);

        }
        if (gegner1Y == 0) {
            stack.sensors().displayLcd128x64().send(true);
            gegner1Y = 10;
            gegner1X = new Random().nextInt(20);
        }

//             <-------------- GEGNER 2 ------------------->

        if (timePassed(128) && gegner2Y > 0) {
            stack.sensors().displayLcd128x64().send("*", gegner2X, gegner2Y);
            gegner2Y--;
        }


        if (gegner2Y == 0 && gegner2X == positionplayer) {
            fpsApp.stop();
            positionplayer ++;
            stack.sensors().displayLcd128x64().send(true);
            stack.sensors().displayLcd128x64().send("GAME OVER", true);
            stack.sensors().speaker().send(1000, 1000, 1000);
            sleep(1000);
            stack.sensors().displayLcd128x64().send(true);
            stack.sensors().displayLcd128x64().send("GAME OVER", true);
            System.exit(0);

        }
        if (gegner2Y == 0) {
            stack.sensors().displayLcd128x64().send(true);
            gegner2Y = 10;
            gegner2X = new Random().nextInt(20);
        }
    }


    //START FUNCTION
    public static void main(final String[] args) {
        Leon_Mateo template = new Leon_Mateo();
        template.stack = ConnectionAndPrintValues_Example.connect();
        template.stack.consumers.add(template::onSensorEvent);
        template.stack.sensors().display().ledAdditional_setOn();
        template.stack.sensors().display().send("P");
        template.fpsApp.start();
    }
}
