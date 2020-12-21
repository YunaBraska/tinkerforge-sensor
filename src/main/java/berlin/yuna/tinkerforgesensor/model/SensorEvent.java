package berlin.yuna.tinkerforgesensor.model;


import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.helper.IsSensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.helper.ContainsType;
import berlin.yuna.tinkerforgesensor.model.helper.IsType;

import java.util.List;

import static java.util.Collections.singletonList;

public class SensorEvent {

    private final Sensor sensor;
    private final List<Number> values;
    private final ValueType valueType;

    public SensorEvent(final Sensor sensor, final Number value, final ValueType valueType) {
        this(sensor, singletonList(value), valueType);
    }

    public SensorEvent(final Sensor sensor, final List<Number> values, final ValueType valueType) {
        this.sensor = sensor;
        this.values = values;
        this.valueType = valueType;
    }

    public IsType isType() {
        return valueType.is();
    }

    public ContainsType containsValueType() {
        return valueType.contains();
    }

    public IsSensor isSensor() {
        return sensor.is();
    }

    public Sensor getSensor() {
        return sensor;
    }

    public List<Number> getValues() {
        return values;
    }

    public ValueType getType() {
        return valueType;
    }

    public Long getValue() {
        return !values.isEmpty() ? values.get(0).longValue() : 0L;
    }

    public static String toKey(final Number number) {
        return String.valueOf((char) number.intValue());
    }

    @Override
    public String toString() {
        return "SensorEvent{" +
                "sensor=" + sensor +
                ", values=" + values +
                ", valueType=" + valueType +
                '}';
    }
}
