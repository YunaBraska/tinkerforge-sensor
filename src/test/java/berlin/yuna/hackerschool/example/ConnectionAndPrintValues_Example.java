package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.logic.Stacks;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.Loop;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static java.lang.String.format;

public class ConnectionAndPrintValues_Example extends Helper {

    private static Stack stack;

    public static Stack connect() {
        try {
            stack = Stacks.connect("localhost", 4223).orElse(new Stack());
            printAllValues();
            while (stack.isConnecting()) {
                sleep(128);
            }
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
        return stack;
    }

    public static Stack connectSingle() {
        try {
            stack = new Stack("localhost", 4223, true);
            stack.consumers.add(ConnectionAndPrintValues_Example::printAllValues);
            while (stack.isConnecting()) {
                sleep(128);
            }
            return stack;
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printAllValues(final SensorEvent sensorEvent) {
        if (sensorEvent.getValueType().containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] send [%s]", sensorEvent.sensor().name, sensorEvent.getValueType(), sensorEvent.getValue()));
        } else if (!timePassed(256)) {
            return;
        }
        System.out.println(Stacks.valuesToString());
    }

    public static void printAllValues() {
        new Loop(256, run -> System.out.println(Stacks.valuesToString()));
    }
}
