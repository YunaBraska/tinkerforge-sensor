package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Default;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.DummyDevice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

/**
 * <h3>{@link SensorList} extends {@link CopyOnWriteArrayList}</h3><br />
 * <i>List of sensors and filter functions</i><br />
 */
public class SensorList<T extends Sensor> extends CopyOnWriteArrayList<T> {

    public synchronized List<Sensor> filter(final Predicate<? super T> predicate) {
        return stream().filter(predicate).collect(toList());
    }

    public synchronized Sensor getSensor(final int number, final Class<?>... sensorOrDevices) {
        final List<Sensor> sensors = getSensor(sensorOrDevices);
        return sensors.size() > number ? sensors.get(number) : getDefault(sensorOrDevices[0]);
    }

    public synchronized List<Sensor> getSensor(final Class<?>... sensorOrDevices) {
        return stream().filter(sensor -> sensor.compare().is(sensorOrDevices)).sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public Double valueDecimal(final ValueType sensorValueType) {
        return value(sensorValueType).doubleValue();
    }

    public Double valueDecimal(final ValueType sensorValueType, final Long fallback) {
        final Long result = value(sensorValueType, fallback);
        return result == null ? null : result.doubleValue();
    }

    public synchronized Long value(final ValueType valueType, final Sensor sensor, final Long fallback) {
        final Long result = values(valueType).get(sensor);
        return result == null ? fallback : result;
    }

    public synchronized Long value(final ValueType valueType) {
        return value(valueType, 0L);
    }

    public synchronized Long value(final ValueType valueType, final Sensor sensor) {
        return value(valueType, sensor, null);
    }

    public synchronized Long value(final ValueType valueType, final Long fallback) {
        for (Sensor sensor : this) {
            final RollingList<Long> sensorValues = (RollingList<Long>) sensor.valueMap().get(valueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                return sensorValues.getLast();
            }

        }
        return fallback;
    }

    public synchronized HashMap<Sensor, Long> values(final ValueType valueType) {
        final HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            final RollingList<Long> sensorValues = (RollingList<Long>) sensor.valueMap().get(valueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.getLast());
            }
        }
        return result;
    }

    public synchronized HashMap<Sensor, Long> min(final ValueType valueType) {
        final HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            final RollingList<Long> sensorValues = (RollingList<Long>) sensor.valueMap().get(valueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.stream().mapToLong(value -> value).summaryStatistics().getMin());
            }

        }
        return result;
    }

    public synchronized HashMap<Sensor, Long> max(final ValueType valueType) {
        final HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            final RollingList<Long> sensorValues = (RollingList<Long>) sensor.valueMap().get(valueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.stream().mapToLong(value -> value).summaryStatistics().getMax());
            }

        }
        return result;
    }

    public synchronized HashMap<Sensor, Long> average(final ValueType valueType) {
        final HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            final RollingList<Long> sensorValues = (RollingList<Long>) sensor.valueMap().get(valueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor,
                        new Double(sensorValues.stream().mapToLong(value -> value).summaryStatistics().getAverage()).longValue());
            }

        }
        return result;
    }

    public synchronized boolean add(final T t) {
        final int i = indexOf(t);
        if (i > -1) {
            add(i, t);
            return true;
        }
        return super.add(t);
    }

    public Sensor getDefault() {
        return getDefault(DummyDevice.class);
    }

    public synchronized List<T> copyList() {
        return new ArrayList<>(this);
    }

    /**
     * <h3>linkParent</h3>
     * Relink set parent for all sensors connected to this parent
     * @param parent sensor to link as paren
     */
    public synchronized void linkParent(final Sensor parent) {
        forEach(sensor -> sensor.linkParent(parent));
        this.sort(Comparator.comparingInt(Sensor::port));
    }

    /**
     * <h3>relinkParents</h3>
     * Relink all sensor parents
     */
    public synchronized void relinkParents() {
        final List<T> parentList = copyList();
        for (Sensor sensor : this) {
            for (Sensor parent : parentList) {
                sensor.linkParent(parent);
            }
        }
        this.sort(Comparator.comparingInt(Sensor::port));
    }

    public Sensor getDefault(final Class<?> sensorOrDevice) {
        try {
            return new Default(sensorOrDevice);
        } catch (NetworkConnectionException e) {
            throw new RuntimeException("Default device should not run into an exception", e);
        }
    }
}
