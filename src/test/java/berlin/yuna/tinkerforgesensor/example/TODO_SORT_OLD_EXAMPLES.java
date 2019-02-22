package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletSegmentDisplay4x7;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static java.util.Collections.reverse;

public class TODO_SORT_OLD_EXAMPLES extends TinkerForgeUtil {

    private static LedStatusType status = LED_STATUS_ON;

    public static void animateStatusLEDs(final SensorList<Sensor> sensorList) {
        status = status == LED_STATUS_ON ? LED_STATUS_OFF : LED_STATUS_ON;
        List<Sensor> sortedList = sensorList.sort(sensor -> sensor.hasLedStatus());
        if (status == LED_STATUS_OFF) {
            reverse(sortedList);
        }
        for (Sensor sensor : sortedList) {
            if (sensor.hasLedStatus() && sensor.isBrick()) {
                sensor.ledStatus(status.bit);
                sleep(128);
            }
        }
    }

    public static void refreshBrickPorts(final SensorList<Sensor> sensorList, final long milliSeconds) {
        sensorList.forEach(Sensor::refreshPort);
        sleep(milliSeconds);
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
            error("[%s] [%s] [%s]", TODO_SORT_OLD_EXAMPLES.class.getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public static void RgbButtonChangeColorOnPress(Sensor sensor, ValueType valueType) {
        if (valueType.is(BUTTON_PRESSED)) {
            Random random = new Random();
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            sensor.value((long) new Color(r, g, b).getRGB());
        }
    }
}
