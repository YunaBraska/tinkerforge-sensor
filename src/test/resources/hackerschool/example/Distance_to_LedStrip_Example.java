package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.threads.Color;

import static berlin.yuna.tinkerforgesensor.model.thread.LedChipType.CHIP_TYPE_WS2812;

public class Distance_to_LedStrip_Example extends Helper {

    private static Stack stack;
    //Config
    private static final int startLed = 36;
    private static final int maxLed = 60;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.getValue(), event.getValueType()));
        while (!stack.sensors().ledStrip().isPresent()) {
            sleep(128);
        }
        stack.sensors().ledStrip().refreshLimit(60);
        stack.sensors().ledStrip().send(150, CHIP_TYPE_WS2812);
    }

    //VARIABLES
    private static long currentMax = 1;
    private static int lastLedIndex;

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isDistance() && timePassed(8)) {
            //Get Sensor and Value
            final Sensor ledStrip = stack.sensors().ledStrip();
            final long distance = value + 1;
            final int ledIndex = (int) (distance / ((currentMax / maxLed) + 1)) + startLed;

            if (distance > currentMax) {
                currentMax = distance;
            }

            if (ledIndex != lastLedIndex) {
                lastLedIndex = ledIndex;
                ledStrip.send(false, -1, Color.BLACK);
                ledStrip.send(
                        ledIndex - 3, Color.BLUE,
                        ledIndex - 2, Color.BLUE,
                        ledIndex - 1, Color.YELLOW,
                        ledIndex, Color.WHITE,
                        ledIndex + 1, Color.YELLOW,
                        ledIndex + 2, Color.BLUE,
                        ledIndex + 3, Color.BLUE
                );
            }
        }
    }
}
