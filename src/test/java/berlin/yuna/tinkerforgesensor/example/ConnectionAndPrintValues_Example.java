package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.util.Iterator;
import java.util.LinkedHashMap;

import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.timePassed;
import static java.lang.String.format;

public class ConnectionAndPrintValues_Example {

    private static SensorList<Sensor> sensorList;

    public static SensorListener connect() {
        try {
            SensorListener sensorListener = new SensorListener("localhost", 4223, true);
            sensorList = sensorListener.sensorList;
            sensorListener.sensorEventConsumerList.add(ConnectionAndPrintValues_Example::printAllValues);
            return sensorListener;
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printAllValues(final SensorEvent sensorEvent) {
        if (!timePassed(256)) {
            return;
        }
        if (sensorEvent.valueType.containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] value [%s]", sensorEvent.sensor.name, sensorEvent.valueType, sensorEvent.value));
        } else {
            LinkedHashMap<ValueType, Long> values = new LinkedHashMap<>();
            for (ValueType valueType : ValueType.values()) {
                Long value = sensorList.value(valueType, null);
                if (value != null) {
                    values.put(valueType, value);
                }
            }
            StringBuilder lineHead = new StringBuilder();
            StringBuilder lineValue = new StringBuilder();
            for (ValueType valueType : values.keySet()) {
                lineHead.append(format("%" + (valueType.toString().length() + 1) + "s |", valueType));
            }

            Iterator<ValueType> typeIterator = values.keySet().iterator();
            for (Long value : values.values()) {
                lineValue.append(format("%" + (typeIterator.next().toString().length() + 1) + "s |", value));
            }
            lineHead.append(format("%9s |", "Sensors"));
            lineValue.append(format("%9s |", sensorList.size()));

            System.out.println("\n" + lineHead.toString() + "\n" + lineValue.toString());
        }
    }
}
