package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

import java.util.List;

public class Check_MultipleDisplays extends Helper {

    private static Stack stack;

    public static void main(final String[] args) throws InterruptedException {
        stack = ConnectionAndPrintValues_Example.connect();

        console("Waiting for Displays [%s]");
        Thread.sleep(1000);

        final List<Sensor> displays = stack.sensors().getDisplayList();
        for(int i = 0; i < displays.size(); i++){
            final Sensor sensor = displays.get(i);
            sensor.ledAdditional_setOn().send("D" + i + ":P" + sensor.port());
        }
        stack.close();
    }
}
