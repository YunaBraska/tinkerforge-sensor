package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;

public class IO16_Loop_Example extends Helper{

    private static SensorList<Sensor> sensorList;
    private static boolean ledReverse;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;

        while (!sensorList.getIO16().isPresent()) {
            sleep(10);
        }

        loop(IO16_Loop_Example::knightRider_example, 10);
        loop(IO16_Loop_Example::loopOver16IO_example, 10);
    }

    private static boolean loopOver16IO_example() {
        Sensor io16 = sensorList.getIO16();

        io16.ledAdditionalOff();
        for (int i = 1; i < 17; i++) {
            io16.value(i);
            sleep(32);
        }
        return true;
    }


    private static boolean knightRider_example() {
        ledReverse = !ledReverse;
        for (int i = 1; i < 9; i++) {
            int index = ledReverse ? 9 - i : i;
            //Side turn all off
            sensorList.getIO16().ledAdditionalOff();
            //Side A
            sensorList.getIO16().value(index);
            //Side B
            sensorList.getIO16().value(index + 8);
            sleep(32);
        }
        return true;
    }
}
