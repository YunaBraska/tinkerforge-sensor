package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;

public class AllStatusLed_Loop_Example extends Helper {

    private static SensorList<Sensor> sensorList;
    private static boolean ledReverse;

    public static void main(final String[] args) {
        final SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;

        loop(AllStatusLed_Loop_Example::knightRider_example, 15);
    }


    private static boolean knightRider_example() {
        ledReverse = !ledReverse;

        for (Sensor sensor : sensorList) {
            if (sensor.hasLedStatus()) {
                sensor.ledStatus(ledReverse ? LED_STATUS_ON.bit : LED_STATUS_OFF.bit);
                sensor.ledAdditional(ledReverse ? LED_ADDITIONAL_ON.bit : LED_ADDITIONAL_OFF.bit);
                sleep(128);
            }
        }
        return true;
    }
}
