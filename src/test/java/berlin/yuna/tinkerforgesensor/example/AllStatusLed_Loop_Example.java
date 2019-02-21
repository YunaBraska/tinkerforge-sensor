package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;

import static berlin.yuna.tinkerforgesensor.example.Utils.loop;
import static berlin.yuna.tinkerforgesensor.example.Utils.sleep;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;

public class AllStatusLed_Loop_Example {

    private static SensorList<Sensor> sensorList;
    private static boolean ledReverse;

    public static void main(String[] args) {
        SensorListener sensorListener = Connection_Example.connect();
        sensorList = sensorListener.sensorList;

        loop(AllStatusLed_Loop_Example::knightRider_example, 5);
    }


    private static boolean knightRider_example() {
        ledReverse = !ledReverse;

        for (Sensor sensor : sensorList) {
            if (sensor.hasLedStatus() && sensor.isBrick()) {
                sensor.ledStatus(ledReverse ? LED_STATUS_ON.bit : LED_STATUS_OFF.bit);
                sleep(128);
            }
        }
        return true;
    }
}