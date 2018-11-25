package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletSegmentDisplay4x7;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_CUSTOM;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_B;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_G;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.COLOR_R;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MOTION_DETECTED_ON;
import static java.lang.String.format;
import static java.util.Collections.reverse;

public class Example extends TinkerForgeUtil {

    private static LedStatusType status = LED_STATUS_ON;

    public static void animateStatusLEDs(final SensorList<Sensor> sensorList) {
        status = status == LED_STATUS_ON ? LED_STATUS_OFF : LED_STATUS_ON;
        List<Sensor> sortedList = sensorList.sort(sensor -> sensor.hasStatusLed);
        if (status == LED_STATUS_OFF) {
            reverse(sortedList);
        }
        for (Sensor sensor : sortedList) {
            if (sensor.hasStatusLed && sensor.isBrick) {
                sensor.led(status);
                sleep(128);
            }
        }
    }

    public static void refreshBrickPorts(final SensorList<Sensor> sensorList, final long milliSeconds) {
        sensorList.forEach(Sensor::refreshPort);
        sleep(milliSeconds);
    }

    private void brightnessOnAtMotion(final Sensor sensor, final ValueType valueType, final SensorList<Sensor> sensorList) {
        Sensor display = sensorList.first(BrickletSegmentDisplay4x7.class);
        if (valueType.is(MOTION_DETECTED_ON)) {
            sensor.ledStatusOn();
            display.ledBrightness(7);
        } else if (valueType.is(MOTION_DETECTED_OFF)) {
            sensor.ledStatusOff();
            display.ledBrightness(0);
        }
    }

//    private final SensorList<Sensor> sensorList = new SensorList<>();
//
//    private void onStart(){
//        console(readFile("hello.txt"));
//        loop(EACH_SECOND, run -> showNumberOfLoops());
//        loop(EACH_SECOND, run -> displayMoveMessage());
//    }
//
//    private void showNumberOfLoops() {
//        console("[%s] Running programs", loopList.size());
//    }
//
//    private void onSensorEvent(final Sensor currentSensor, final ValueType valueType){
//        Sensor display = sensorList.first(BrickletSegmentDisplay4x7.class);
//
//        if (valueType.is(MOTION_DETECTED_ON)) {
//
//            console("Motion detected");
//            currentSensor.ledStatusOn();
//            display.ledBrightness(7);
//
//        } else if (valueType.is(MOTION_DETECTED_OFF)) {
//
//            console("No motion detected");
//            currentSensor.ledStatusOff();
//            display.ledBrightness(0);
//
//        }
//    }
//
//    public void displayMoveMessage() {
//        onStart();
//        onSensorEvent();
//    }

    //TODO: stop character to prevent StringIndexOutOfBoundsException: String index out of range: 70
    public static void displayMoveMessage(final SensorList<Sensor> sensorList, final long milliSeconds) {
        Sensor display = sensorList.first(BrickletLCD20x4.class);
        if (isPresent(display)) {
            display.led(LED_ADDITIONAL_ON);
            int displaySize = 20;
            String msg = "-= Welcome to HackerSchool =-";
            msg = letterUp(" ", displaySize) + msg;
            for (int i = 0; i < msg.length() + displaySize; i++) {
                int cutSize = i + displaySize;
                String text = "                    \n";
                text += (msg + letterUp(" ", displaySize)).substring(i, cutSize) + "\n";
                text += "                    \n";
                text += "                    \n";
                display.value(text);
                sleep(milliSeconds);
            }
            throw new RuntimeException("Finished moving text");
        }
    }

    public static void displayAlphabet(final SensorList<Sensor> sensorList, final long speedMs) {
        Sensor display = sensorList.first(BrickletSegmentDisplay4x7.class);
        if (display.isPresent()) {
            for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                display.value(Character.toString(alphabet));
                sleep(speedMs);
            }
        }
    }

    public static void displayTimeoutMessage(final SensorList<Sensor> sensorList, final long timeoutMs) {
        try {
            Sensor sensor = sensorList.first(BrickletLCD20x4.class);
            if (isPresent(sensor)) {
                BrickletLCD20x4 device = (BrickletLCD20x4) sensor.device;
                device.setDefaultText((short) 0, "|                  |");
                device.setDefaultText((short) 1, "|   HackerSchool   |");
                device.setDefaultText((short) 2, "|  Ready to serve  |");
                device.setDefaultText((short) 3, "|                  |");
                device.setDefaultTextCounter((int) timeoutMs);
            }
            sleep(timeoutMs - 1000);
        } catch (Exception e) {
            error("[%s] [%s] [%s]", Example.class.getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public static void mapColorToRgbBtton(Sensor colorSensor, Sensor rgbButton) {
        if (colorSensor.is(BrickletColor.class)) {
            colorSensor.ledStatusOn();
            float r = colorSensor.value(COLOR_R).floatValue() / 1000;
            float g = colorSensor.value(COLOR_G).floatValue() / 1000;
            float b = colorSensor.value(COLOR_B).floatValue() / 1000;
            rgbButton.value(new Color(r, g, b).getRGB());
        }
    }

    public static void RgbButtonChangeColorOnPress(Sensor sensor, ValueType valueType) {
        if (valueType.is(BUTTON_PRESSED)) {
            Random random = new Random();
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            sensor.led(LED_CUSTOM, (long) new Color(r, g, b).getRGB());
        }
    }

    public static void printAllValues(final SensorList<Sensor> sensorList) {
        LinkedHashMap<ValueType, Long> values = new LinkedHashMap<>();
        for (ValueType valueType : ValueType.values()) {
            Long value = sensorList.value(valueType, null);
            if (value != null) {
                values.put(valueType, value);
            }
        }
        StringBuilder lineHead = new StringBuilder();
        StringBuilder lineValue = new StringBuilder();
        for (ValueType valueType : values.keySet()) {
            lineHead.append(format("%15s |", valueType));
        }
        for (Long value : values.values()) {
            lineValue.append(format("%15s |", value));
        }
        console("\n" + lineHead.toString() + "\n" + lineValue.toString());
    }
}
