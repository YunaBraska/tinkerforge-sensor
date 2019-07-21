package berlin.yuna.tinkerforgesensor.model.type;

import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

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

    public Sensor getSensor() {
        return sensor;
    }

    public List<Number> getValues() {
        return values;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Long getValue() {
        return values.size() > 0? values.get(0).longValue() : 0L;
    }

    public static String toKey(final Number number) {
        return String.valueOf((char) number.intValue());
    }
}
