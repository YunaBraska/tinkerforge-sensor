package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class SensorList<T extends Sensor> extends CopyOnWriteArrayList<T> {

    public List<Sensor> sort() {
        return stream().sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public List<Sensor> sort(Predicate<? super T> predicate) {
        return stream().filter(predicate).sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public Sensor first(final Class<? extends Device> sensorType) {
        List<Sensor> sensor = sensor(sensorType);
        return sensor.isEmpty() ? getDummy() : sensor.get(0);
    }

    public Sensor second(final Class<? extends Device> sensorType) {
        return sensor(sensorType).get(1);
    }

    public Sensor third(final Class<? extends Device> sensorType) {
        return sensor(sensorType).get(2);
    }

    public Sensor fourth(final Class<? extends Device> sensorType) {
        return sensor(sensorType).get(3);
    }

    public Sensor sensor(final Class<? extends Device> sensorType, final int index) {
        return sensor(sensorType).get(index);
    }

    public List<Sensor> sensor(final Class<? extends Device> sensorType) {
        return stream().filter(item -> sensorType.isInstance(item.device)).sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public Long value(final ValueType sensorValueType) {
        return value(sensorValueType, 0L);
    }

    public Long value(final ValueType sensorValueType, final Long fallback) {
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                return sensorValues.getLast();
            }

        }
        return fallback;
    }

    public HashMap<Sensor, Long> values(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.getLast());
            }

        }
        return result;
    }

    public HashMap<Sensor, Long> min(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.stream().mapToLong(value -> value).summaryStatistics().getMin());
            }

        }
        return result;
    }

    public HashMap<Sensor, Long> max(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.stream().mapToLong(value -> value).summaryStatistics().getMax());
            }

        }
        return result;
    }

    public HashMap<Sensor, Long> average(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, new Double(sensorValues.stream().mapToLong(value -> value).summaryStatistics().getAverage()).longValue());
            }

        }
        return result;
    }

    public Sensor getDummy() {
        return first(DummyDevice.class);
    }
}
