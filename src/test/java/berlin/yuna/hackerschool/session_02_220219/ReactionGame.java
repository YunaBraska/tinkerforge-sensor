package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;

import java.util.Random;

/**
 * @author yourNames
 */
public class ReactionGame extends Helper {

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor(), event.getValue(), event.getValueType()));
    }

    //VARIABLES
    public static Stack stack;
    private static int score = -1;
    private static boolean reactionNow = false;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor displayLcd = stack.sensors().displayLcd20x4();
        final Sensor displaySegment = stack.sensors().displaySegment();
        final Sensor buttonRGB = stack.sensors().buttonRGB();
        final Sensor speaker = stack.sensors().speaker();

        //SCORE VIEW
        if (score > -1) {
            displaySegment.send("P" + score);
            displayLcd.send("Score: " + score + " ${space}");
        }

        //GAME END EVENTS
        if (score == 0) {
            score = -1;
            speaker.send(778, 5000, false);
            displayLcd.send("${1}${space}Game Over! ${space}");
        } else if (score == 10) {
            score = -1;
            displayLcd.send("${1}${space}You Win!${space}");
            async("reaction_win_speaker", run -> speaker.flashLed());
            async("reaction_win_led", run -> buttonRGB.flashLed());
        }

        //TIME BASED EVENTS
        if (timePassed("reactionGame", 50)) {
            //random yellow
            if (timePassed("yellow", 1500 + new Random().nextInt(9000)) && !reactionNow && score > -1) {
                stack.sensors().buttonRGB().send(Color.YELLOW);
                reactionNow = true;
                displayLcd.send("${1}Do it now! ${space}");
            } else if (reactionNow && timePassed("yellow", 600 + new Random().nextInt(1000))) {
                //SCORE DOWN - didnt make it in time
                reactionNow = false;
                speaker.send(200, 5000, false);
                buttonRGB.send(Color.RED);
                displayLcd.send("${1}Missed it!${space}");
                score = score - 1;
            }
        }

        //BUTTON EVENTS
        if (type.isButtonPressed() && value == 1L) {
            if (sensor.compare().isRotary()) {
                //RESET GAME
                score = -1;
                buttonRGB.ledStatus_setOn();
                async("3-2-1-GO", run -> reactionGameStart());
            } else if (reactionNow) {
                //SCORE UP - made it in time
                reactionNow = false;
                score = score + 1;
                speaker.send(200, 2000, false);
                displayLcd.send("${1}Got it!${space}");
                buttonRGB.send(Color.GREEN);
            } else if (!reactionNow) {
                //SCORE DOWN - accident press
                score = score - 1;
                buttonRGB.send(Color.RED);
                speaker.send(200, 5000, false);
                displayLcd.send("${1} Too early! ${space}");
            }
        }
    }

    private static void reactionGameStart() {
        final Sensor displaySegment = stack.sensors().displaySegment();
        final Sensor buttonRGB = stack.sensors().buttonRGB();
        final Sensor displayLcd = stack.sensors().displayLcd20x4();
        final Sensor speaker = stack.sensors().speaker();

        if (score == -1) {
            score = -2;
            loopStop("reaction_win");
            displayLcd.send("${clear}");
            buttonRGB.ledStatus_setOn();
            displayLcd.ledStatus_setOn();
            displayLcd.ledAdditional_setOn();
            displaySegment.send(3);
            displayLcd.send("${space} 3 ${space}");
            buttonRGB.send(Color.RED);
            speaker.send(200, 3000, false);
            sleep(1024);

            displaySegment.send(2);
            displayLcd.send("${space} 2 ${space}");
            buttonRGB.send(Color.YELLOW);
            speaker.send(200, 3000, false);
            sleep(1024);

            displaySegment.send(1);
            displayLcd.send("${space} 1 ${space}");
            buttonRGB.send(Color.GREEN);
            speaker.send(200, 3000, false);
            sleep(1024);
            displaySegment.send("-GO-");
            displayLcd.send("${space} -GO- ${space}");
            speaker.send(512, 2000, false);
            sleep(512);
            score = 5;
        }
    }


}
