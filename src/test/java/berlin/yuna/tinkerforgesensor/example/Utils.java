package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;

public class Utils {

    private static SensorList<Sensor> sensorList;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;

        while (sensorList.isEmpty()) {
            sleep(10);
        }

        loop(Utils::knightRider_example, 5);
    }

    private static boolean ledReverse;

    private static boolean knightRider_example() {
        ledReverse = !ledReverse;
        List<Sensor> list = new ArrayList<>(sensorList);
        if (ledReverse) {
            Collections.reverse(list);
        }

        for (Sensor sensor : list) {
            if (sensor.hasLedStatus() && sensor.isBrick()) {
                sensor.ledStatus(ledReverse? LED_STATUS_ON.bit : LED_STATUS_OFF.bit);
                sleep(128);
            }
        }
        return true;
    }

    static void loop(final Supplier supplier, final int times) {
        for (int i = 0; i < times; i++) {
            supplier.get();
        }
    }

    static void sleep(final long sleepMs) {
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
