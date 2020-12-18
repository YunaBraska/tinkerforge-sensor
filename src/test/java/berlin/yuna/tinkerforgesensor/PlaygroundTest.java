package berlin.yuna.tinkerforgesensor;

import berlin.yuna.tinkerforgesensor.model.threads.Color;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorEvent;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_UNKNOWN;
import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.createLoop;

//TODO Police siren
//TODO Alarm Motion sensor
//TODO Kasse Beep on Dark
//TODO Daylight counter
//TODO Temperature LED
//TODO Daylight LED
//TODO RGB Button change color on press
//TODO Reaction Game RGB Button
//TODO Night-Rider effect
@Tag("UnitTest")
class PlaygroundTest {

    final Stack stack = new Stack();
    boolean ledStatusToggle = true;

    private void listen(final SensorEvent event) {
        if (event.getValueType().equals(DEVICE_UNKNOWN)) {
            System.err.println(event);
        } else {
            System.out.println(event);
        }

        if (event.isValueType().buttonPressed() && event.isSensor().buttonRGB()) {
            ledStatusToggle = !ledStatusToggle;
            if (ledStatusToggle) {
                stack.get().ledRGBList().forEach(button -> button.setColor(Color.GREEN));
                stack.get().buttonRGBList().forEach(button -> button.setColor(Color.GREEN));
                stack.get().buttonDualList().forEach(button -> button.setLedState(0, 1));
                stack.get().buttonDualList().forEach(button -> button.setLedState(1, 0));
                stack.get().speakerList().forEach(speaker -> speaker.sendSound(128, 1000));
            } else {
                stack.get().ledRGBList().forEach(button -> button.setColor(Color.RED));
                stack.get().buttonRGBList().forEach(button -> button.setColor(Color.RED));
                stack.get().buttonDualList().forEach(button -> button.setLedState(0, 0));
                stack.get().buttonDualList().forEach(button -> button.setLedState(1, 1));
                stack.get().speakerList().forEach(speaker -> speaker.sendSound(128, 2000));
            }

            stack.getSensors(Sensor::hasStatusLed).forEach(sensor -> {
                if (ledStatusToggle) {
                    sensor.setStatusLedOn();
                } else {
                    sensor.setStatusLedOff();
                }
            });

            stack.getSensors().forEach(sensor -> {
                if (ledStatusToggle) {
                    sensor.handler().triggerFunctionA(1);
                } else {
                    sensor.handler().triggerFunctionA(0);
                }
            });
        }

        if (event.isValueType().colorRgb()) {
            stack.get().buttonRGBList().forEach(button -> button.setColor(event.getValue()));
        }
    }

    @Test
    @Disabled
    void playgroundTest() throws InterruptedException {
        stack.addListener(this::listen).connect();
        AtomicInteger i = new AtomicInteger();
//        final String text = "    I LOVE CHRIS    ";
        final String text = "    1234567890    1.2.3.4.5.6.7.8.9.0    ";
        createLoop("test", 1000, aLong -> {

            stack.get().displaySegmentList().forEach(sensor -> {

                if (i.get() + 4 > text.length()) {
                    i.set(0);
                }
//                sensor.send(LocalDateTime.now());
//                sensor.send(text.substring(i.get(), i.getAndIncrement() + 4));
                sensor.send(text.substring(i.getAndIncrement()));
            });

        });

        while (true) {
            Thread.sleep(1000);
        }
    }
}