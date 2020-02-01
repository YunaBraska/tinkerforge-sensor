package berlin.yuna.hackerschool.session_private;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static berlin.yuna.tinkerforgesensor.model.type.LedChipType.CHIP_TYPE_WS2812;

public class FloorLedStrip extends Helper {

    //VARIABLES
    private Stack stack;
    private static final int startLed = 1;
    private static final int maxLed = 30;
    private static long currentMax = 1;
    private static int lastLedIndex;
    private static boolean animation;


    //CODE FUNCTION
    void onSensorEvent(final SensorEvent event) {
        if (timePassed(1000)) {
            stack.sensors().ledStrip().send('C', 150, CHIP_TYPE_WS2812);
        } else if (event.getValueType().isDistance() && timePassed(8)) {

            //Get Sensor and Value
            final Sensor ledStrip = stack.sensors().ledStrip();
            final long distance = event.getValue() + 1;
            if (distance > currentMax) {
                currentMax = distance;
            }
            final int ledIndex = (int) (distance / ((currentMax / maxLed) + 1)) + startLed;


            if (!animation && ledIndex != lastLedIndex) {
                animation = true;
                final int startLed = lastLedIndex;
                lastLedIndex = ledIndex;
                async("ledAnimation", run -> {
                    int currLed = startLed;
                    while (currLed != ledIndex) {
                        //TODO: speed
                        //TODO: brightness from environment
                        sleep(60);
                        ledStrip.send(false, -1, Color.BLACK);
                        ledStrip.send(
                                currLed - 3, Color.WHITE,
                                currLed - 2, Color.WHITE,
                                currLed - 1, Color.WHITE,
                                currLed, Color.WHITE,
                                currLed + 1, Color.WHITE,
                                currLed + 2, Color.WHITE,
                                currLed + 3, Color.WHITE
                        );
                        if (currLed < ledIndex) {
                            currLed++;
                        } else {
                            currLed--;
                        }
                    }
                    animation = false;
                });
            }
        }

    }

    //START FUNCTION
    public static void main(final String[] args) {
        final FloorLedStrip template = new FloorLedStrip();
        template.stack = ConnectionAndPrintValues_Example.connect();
        template.stack.consumers.add(template::onSensorEvent);
    }
}
