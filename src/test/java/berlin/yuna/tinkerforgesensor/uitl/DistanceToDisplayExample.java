package berlin.yuna.tinkerforgesensor.uitl;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.threads.Color;

import java.io.IOException;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_ON;
import static java.lang.System.out;

public class DistanceToDisplayExample {

    private static Stack stack = new Stack().connect();
    private static boolean ledToggle;

    public static void main(final String[] args) throws IOException {
        stack.addListener(DistanceToDisplayExample::onEvent);
        out.println("Press key to exit"); System.in.read();
    }


    private static void onEvent(final SensorEvent event) {
        if (event.isType().distance()) {
            stack.get().displaySegment().send(event.getValue());
            stack.get().buttonRGB().sendColor(Color.RAINBOW[(int) ((event.getValue() - 200) / 2.17)]);
        } else if (event.isType().buttonPressed()) {
            ledToggle = !ledToggle;
            stack.getSensors(Sensor::hasStatusLed).forEach(sensor -> sensor.setStatusLed(ledToggle ? LED_ON : LED_OFF));
        }
    }
}
