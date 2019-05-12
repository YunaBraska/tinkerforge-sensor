package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;

import static berlin.yuna.tinkerforgesensor.example.Helper.loop;
import static berlin.yuna.tinkerforgesensor.example.Helper.sleep;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;

public class AllStatusLed_Loop_Example {

    private static SensorList<Sensor> sensorList;
    private static boolean ledReverse;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;

        loop(AllStatusLed_Loop_Example::knightRider_example, 5);
    }


    private static boolean knightRider_example() {
        ledReverse = !ledReverse;

        for (Sensor sensor : sensorList) {
            if (sensor.hasLedStatus() && sensor.isBrick()) {
                sensor.ledStatus(ledReverse ? LED_STATUS_ON.bit : LED_STATUS_OFF.bit);
                sensor.ledAdditional(ledReverse ? LED_ADDITIONAL_ON.bit : LED_ADDITIONAL_OFF.bit);
                sleep(128);
            }
        }
        return true;
    }
}
