package berlin.yuna.hackerschool.a_program;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorList;
import berlin.yuna.hackerschool.model.type.LedStatusType;
import berlin.yuna.hackerschool.model.type.ValueType;
import berlin.yuna.hackerschool.util.TinkerforgeUtil;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletSegmentDisplay4x7;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_CUSTOM;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.hackerschool.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.hackerschool.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_B;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_G;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_R;
import static berlin.yuna.hackerschool.model.type.ValueType.HUMIDITY;
import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT_LUX;
import static berlin.yuna.hackerschool.model.type.ValueType.MOTION_DETECTED_OFF;
import static berlin.yuna.hackerschool.model.type.ValueType.MOTION_DETECTED_ON;
import static berlin.yuna.hackerschool.model.type.ValueType.TEMPERATURE;
import static java.lang.String.format;
import static java.util.Collections.reverse;

public class Example extends TinkerforgeUtil {

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
                sensor.led(status == LED_STATUS_ON ? LED_ADDITIONAL_ON : LED_ADDITIONAL_OFF);
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

    public static void displayWeather(SensorList<Sensor> sensorList) {
        Sensor display = sensorList.first(BrickletLCD20x4.class);
        double illumination = ((double) sensorList.value(LIGHT_LUX, 1L)) / 100.0; //lx
        double humidity = ((double) sensorList.value(HUMIDITY, 1L)) / 100.0; //%
        double airPressure = ((double) sensorList.value(AIR_PRESSURE, 1L)) / 1000.0; //mb
        double temperature = ((double) sensorList.value(TEMPERATURE, 1L)) / 100.0; //°C

        String text = format("Illumina${space} %s lx\n", roundUp(illumination, 2));
        text += format("Humidity${space}${space}${space} %s %%\n", roundUp(humidity, 2));
        text += format("AirPress${space} %s mb\n", roundUp(airPressure, 2));
        text += format("Temperature${space} %s °C\n", roundUp(temperature, 2));

        display.led(LED_ADDITIONAL_ON);
        display.value(text);
    }
}
