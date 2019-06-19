package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;

public class IO16_Loop_Example extends Helper{

    private static Stack stack;
    private static boolean ledReverse;

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();

        while (!stack.sensors().iO16().isPresent()) {
            sleep(10);
        }

        loop(IO16_Loop_Example::knightRider_example, 10);
        loop(IO16_Loop_Example::loopOver16IO_example, 10);
    }

    private static boolean loopOver16IO_example() {
        final Sensor io16 = stack.sensors().iO16();

        io16.setLedAdditional_Off();
        for (int i = 1; i < 17; i++) {
            io16.send(i);
            sleep(32);
        }
        return true;
    }


    private static boolean knightRider_example() {
        ledReverse = !ledReverse;
        for (int i = 1; i < 9; i++) {
            final int index = ledReverse ? 9 - i : i;
            //Side turn all off
            stack.sensors().iO16().setLedAdditional_Off();
            //Side A
            stack.sensors().iO16().send(index);
            //Side B
            stack.sensors().iO16().send(index + 8);
            sleep(32);
        }
        return true;
    }
}
