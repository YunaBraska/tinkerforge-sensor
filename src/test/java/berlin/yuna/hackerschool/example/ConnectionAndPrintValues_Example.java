package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static java.lang.String.format;

public class ConnectionAndPrintValues_Example extends Helper {

    private static Stack stack;

    public static Stack connect() {
        return connect("localhost", 4223);
    }

    public static Stack connect(final String host, final int port) {
        try {
            stack = new Stack(host, port, true);
            stack.sensorEventConsumerList.add(ConnectionAndPrintValues_Example::printAllValues);
            while (stack.isConnecting()) {
                sleep(128);
            }
            return stack;
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printAllValues(final SensorEvent sensorEvent) {
        printAllValues(stack, sensorEvent);
    }

    public static void printAllValues(final Stack stack, final SensorEvent sensorEvent) {
        if (sensorEvent.getValueType().containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] send [%s]", sensorEvent.sensor().name, sensorEvent.getValueType(), sensorEvent.getValue()));
        } else if (!timePassed(256)) {
            return;
        }
        System.out.println(stack.valuesToString());
    }
}
