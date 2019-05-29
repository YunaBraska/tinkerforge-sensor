package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.util.Iterator;
import java.util.LinkedHashMap;

import static java.lang.String.format;

public class ConnectionAndPrintValues_Example extends Helper {

    private static Stack stack;

    public static Stack connect() {
        try {
            stack = new Stack("localhost", 4223, true);
            stack.sensorEventConsumerList.add(ConnectionAndPrintValues_Example::printAllValues);
            while (stack.isConnecting()){
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
        final LinkedHashMap<ValueType, Long> values = new LinkedHashMap<>();
        for (ValueType valueType : ValueType.values()) {
            final Long value = stack.values().get(valueType, null);
            if (value != null) {
                values.put(valueType, value);
            }
        }
        final StringBuilder lineHead = new StringBuilder();
        final StringBuilder lineValue = new StringBuilder();
        for (ValueType valueType : values.keySet()) {
            lineHead.append(format("%" + (valueType.toString().length() + 1) + "s |", valueType));
        }

        final Iterator<ValueType> typeIterator = values.keySet().iterator();
        for (Long value : values.values()) {
            lineValue.append(format("%" + (typeIterator.next().toString().length() + 1) + "s |", value));
        }
        lineHead.append(format("%9s |", "Sensors"));
        lineValue.append(format("%9s |", stack.sensors().size()));

        System.out.println("\n" + lineHead.toString() + "\n" + lineValue.toString());
    }
}
