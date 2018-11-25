package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.Comparator.comparingInt;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;

public class SensorList<T extends Sensor> extends CopyOnWriteArrayList<T> {

    private final ReentrantLock lock = new ReentrantLock();
    private List<Consumer<SensorEvent>> sensorEventConsumerList;

    public synchronized List<Sensor> sort() {
        return stream().sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public synchronized List<Sensor> sort(Predicate<? super T> predicate) {
        return stream().filter(predicate).sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public synchronized Sensor first(final Class<? extends Device> sensorType) {
        List<Sensor> sensor = sensor(sensorType);
        return sensor.isEmpty() ? getDefault(sensorType) : sensor.get(0);
    }

    public synchronized Sensor second(final Class<? extends Device> sensorType) {
        return sensor(sensorType).get(1);
    }

    public synchronized Sensor third(final Class<? extends Device> sensorType) {
        return sensor(sensorType).get(2);
    }

    public synchronized Sensor fourth(final Class<? extends Device> sensorType) {
        return sensor(sensorType).get(3);
    }

    public synchronized Sensor sensor(final Class<? extends Device> sensorType, final int index) {
        return sensor(sensorType).get(index);
    }

    public synchronized List<Sensor> sensor(final Class<? extends Device> sensorType) {
        waitForUnlock(10101);
        return stream().filter(item -> sensorType.isInstance(item.device)).sorted(comparingInt(Sensor::port)).collect(toList());
    }

    public Double valueDecimal(final ValueType sensorValueType) {
        return value(sensorValueType).doubleValue();
    }

    public Double valueDecimal(final ValueType sensorValueType, final Long fallback) {
        Long result = value(sensorValueType, fallback);
        return result == null ? null : result.doubleValue();
    }

    public synchronized Long value(final ValueType sensorValueType) {
        return value(sensorValueType, 0L);
    }

    public synchronized Long value(final ValueType sensorValueType, final Long fallback) {
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                return sensorValues.getLast();
            }

        }
        return fallback;
    }

    public synchronized HashMap<Sensor, Long> values(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.getLast());
            }

        }
        return result;
    }

    public synchronized HashMap<Sensor, Long> min(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.stream().mapToLong(value -> value).summaryStatistics().getMin());
            }

        }
        return result;
    }

    public synchronized HashMap<Sensor, Long> max(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, sensorValues.stream().mapToLong(value -> value).summaryStatistics().getMax());
            }

        }
        return result;
    }

    public synchronized HashMap<Sensor, Long> average(final ValueType sensorValueType) {
        HashMap<Sensor, Long> result = new HashMap<>();
        for (Sensor sensor : this) {
            RollingList<Long> sensorValues = sensor.values.get(sensorValueType);
            if (sensorValues != null && !sensorValues.isEmpty()) {
                result.put(sensor, new Double(sensorValues.stream().mapToLong(value -> value).summaryStatistics().getAverage()).longValue());
            }

        }
        return result;
    }

    public synchronized boolean isLocked() {
        return lock.isLocked();
    }

    public synchronized void lock() {
        try {
            lock(30000);
        } catch (InterruptedException ignore) {
        }
    }

    public synchronized boolean lock(long waitForUnlock) throws InterruptedException {
        return lock.tryLock(waitForUnlock, MILLISECONDS);
    }

    public void unlock() {
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    public synchronized void waitForUnlock(long waitForUnlock) {
        try {
            lock(waitForUnlock);
        } catch (IllegalMonitorStateException | InterruptedException e) {
            System.err.println(format("[ERROR] LOCK [waitForUnlock] [%s] [%s]", waitForUnlock, e.getClass().getSimpleName()));
        } finally {
            unlock();
        }
    }

    public synchronized boolean add(T t) {
        int i = indexOf(t);
        if (i > -1) {
            add(i, t);
            return true;
        }
        return super.add(t);
    }

    public void listener(final List<Consumer<SensorEvent>> sensorEventConsumerList) {
        this.sensorEventConsumerList = sensorEventConsumerList;
    }

    public Sensor getDefault() {
        return getDefault(DummyDevice.class);
    }

    private Sensor getDefault(final Class<? extends Device> sensorType) {
        DummyDevice dummyDevice = new DummyDevice();
        return new Sensor(dummyDevice, null, dummyDevice.getIdentity().uid).addListener(sensorEventConsumerList);
    }
}
