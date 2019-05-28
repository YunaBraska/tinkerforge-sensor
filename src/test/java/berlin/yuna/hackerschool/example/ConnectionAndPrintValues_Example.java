package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.TinkerForge;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.util.Iterator;
import java.util.LinkedHashMap;

import static java.lang.String.format;

public class ConnectionAndPrintValues_Example extends Helper {

    private static TinkerForge tinkerForge;

    public static TinkerForge connect() {
        try {
            tinkerForge = new TinkerForge("localhost", 4223, true);
            tinkerForge.sensorEventConsumerList.add(ConnectionAndPrintValues_Example::printAllValues);
            while (tinkerForge.isConnecting()){
                sleep(128);
            }
            return tinkerForge;
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
            final Long value = tinkerForge.values().get(valueType, null);
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
        lineValue.append(format("%9s |", tinkerForge.sensors().size()));

        System.out.println("\n" + lineHead.toString() + "\n" + lineValue.toString());
    }
}
