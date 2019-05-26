package berlin.yuna.hackerschool.session_02_220219;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
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
        final SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    public static SensorList<Sensor> sensorList = new SensorList<>();
    private static int score = -1;
    private static boolean reactionNow = false;

    //CODE FUNCTION
    static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor displayLcd = sensorList.getDisplayLcd20x4();
        final Sensor displaySegment = sensorList.getDisplaySegment();
        final Sensor buttonRGB = sensorList.getButtonRGB();
        final Sensor speaker = sensorList.getSpeaker();

        //SCORE VIEW
        if (score > -1) {
            displaySegment.value("P" + score);
            displayLcd.value("Score: " + score + " ${space}");
        }

        //GAME END EVENTS
        if (score == 0) {
            score = -1;
            speaker.value(778, 5000, false);
            displayLcd.value("${1}${space}Game Over! ${space}");
        } else if (score == 10) {
            score = -1;
            displayLcd.value("${1}${space}You Win!${space}");
            async("reaction_win_speaker", run -> speaker.flashLed());
            async("reaction_win_led", run -> buttonRGB.flashLed());
        }

        //TIME BASED EVENTS
        if (timePassed("reactionGame", 50)) {
            //random yellow
            if (timePassed("yellow", 1500 + new Random().nextInt(9000)) && !reactionNow && score > -1) {
                sensorList.getButtonRGB().value(Color.YELLOW);
                reactionNow = true;
                displayLcd.value("${1}Do it now! ${space}");
            } else if (reactionNow && timePassed("yellow", 600 + new Random().nextInt(1000))) {
                //SCORE DOWN - didnt make it in time
                reactionNow = false;
                speaker.value(200, 5000, false);
                buttonRGB.value(Color.RED);
                displayLcd.value("${1}Missed it!${space}");
                score = score - 1;
            }
        }

        //BUTTON EVENTS
        if (type.isButtonPressed() && value == 1L) {
            if (sensor.is(sensorList.getRotary())) {
                //RESET GAME
                score = -1;
                buttonRGB.ledStatusOn();
                async("3-2-1-GO", run -> reactionGameStart());
            } else if (reactionNow) {
                //SCORE UP - made it in time
                reactionNow = false;
                score = score + 1;
                speaker.value(200, 2000, false);
                displayLcd.value("${1}Got it!${space}");
                buttonRGB.value(Color.GREEN);
            } else if (!reactionNow) {
                //SCORE DOWN - accident press
                score = score - 1;
                buttonRGB.value(Color.RED);
                speaker.value(200, 5000, false);
                displayLcd.value("${1} Too early! ${space}");
            }
        }
    }

    private static void reactionGameStart() {
        final Sensor displaySegment = sensorList.getDisplaySegment();
        final Sensor buttonRGB = sensorList.getButtonRGB();
        final Sensor displayLcd = sensorList.getDisplayLcd20x4();
        final Sensor speaker = sensorList.getSpeaker();

        if (score == -1) {
            score = -2;
            loopStop("reaction_win");
            displayLcd.value("${clear}");
            buttonRGB.ledStatusOn();
            displayLcd.ledStatusOn();
            displayLcd.ledAdditionalOn();
            displaySegment.value(3);
            displayLcd.value("${space} 3 ${space}");
            buttonRGB.value(Color.RED);
            speaker.value(200, 3000, false);
            sleep(1024);

            displaySegment.value(2);
            displayLcd.value("${space} 2 ${space}");
            buttonRGB.value(Color.YELLOW);
            speaker.value(200, 3000, false);
            sleep(1024);

            displaySegment.value(1);
            displayLcd.value("${space} 1 ${space}");
            buttonRGB.value(Color.GREEN);
            speaker.value(200, 3000, false);
            sleep(1024);
            displaySegment.value("-GO-");
            displayLcd.value("${space} -GO- ${space}");
            speaker.value(512, 2000, false);
            sleep(512);
            score = 5;
        }
    }


}
