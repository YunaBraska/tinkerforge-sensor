package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;

import static java.lang.String.format;

public class ConnectionAndPrintValues_Example extends Helper {

    private static Stack stack;

    public static Stack connect() {
        try {
            stack = new Stack("localhost", 4223, true);
            stack.sensorEventConsumerList.add(ConnectionAndPrintValues_Example::printAllValues);
            while (stack.isConnecting()) {
                sleep(128);
            }
            return stack;
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printAllValues(final SensorEvent sensorEvent) {
        if (sensorEvent.valueType.containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] send [%s]", sensorEvent.sensor.name, sensorEvent.valueType, sensorEvent.value));
        } else if (!timePassed(256)) {
            return;
        }
        System.out.println(stack.valuesToString());
    }
}
