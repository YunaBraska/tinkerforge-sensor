package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.util.SegmentsV2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Check_DisplaySegment extends Helper {

    private static Stack stack;

    public static void main(final String[] args) throws InterruptedException {
        stack = ConnectionAndPrintValues_Example.connect();
//        stack.sensorEventConsumerList.clear();

        console("Waiting for [%s]", DisplaySegment.class);
        while (!stack.sensors().displaySegment().isPresent()) {
            Thread.sleep(128);
            System.out.print(".");
        }
        Thread.sleep(1000);

        final List<Sensor> displays = new ArrayList<>();
        //TODO: extends sensors (listSensorXy)
        while (true) {
            final Sensor sensor = stack.sensors().displaySegment(displays.size());
            if (!sensor.isPresent()) {
                break;
            }
            displays.add(sensor);
        }

        console("Found [%s] count [%s]", DisplaySegment.class, displays.size());
        while (true) {
            console("Display all available symbols");
            displays.forEach(display -> display.send("1.2.:3.‘4."));
            sleep(1000);
            displays.forEach(display -> display.send("1.2.:3.‘4."));
            final List<Character> list = Arrays.stream(SegmentsV2.getSymbols()).map(SegmentsV2.PairSmV2::getKey).collect(Collectors.toList());
            for (Character key : list) {
                displays.forEach(display -> display.send(key));
                sleep(256);
            }
            sleep(1000);

            console("Display HH:mm with blinking dots");
            displays.forEach(display -> display.send(ofPattern("HH:mm")));
            for (int i = 0; i < 18; i++) {
                sleep(128);
                displays.forEach(display -> display.send(LocalDateTime.now()));
            }

            console("Display dd:MM");
            displays.forEach(display -> display.send(ofPattern("dd:MM")));
            for (int i = 0; i < 18; i++) {
                sleep(128);
                displays.forEach(display -> display.send(LocalDateTime.now()));
            }

            console("Set brightness");
            displays.forEach(display -> display.send("8888"));
            for (int i = 0; i < 12; i++) {
                final int index = i;
                displays.forEach(display -> display.setLedAdditional(index));
                sleep(256);
            }

            sleep(1000);
            console("Display numbers");
            sleep(256);
            displays.forEach(display -> display.send(1));
            sleep(256);
            displays.forEach(display -> display.send(12));
            sleep(256);
            displays.forEach(display -> display.send(123));
            sleep(256);
            displays.forEach(display -> display.send(1234));

            sleep(1000);
            console("Display text");
            sleep(256);
            displays.forEach(display -> display.send("A"));
            sleep(256);
            displays.forEach(display -> display.send("AB"));
            sleep(256);
            displays.forEach(display -> display.send("ABC"));
            sleep(256);
            displays.forEach(display -> display.send("ABCD"));
            sleep(256);

            sleep(1000);
            console("Display double dots");
            displays.forEach(display -> display.send("dd:dd"));
        }
    }
}
