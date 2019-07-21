package berlin.yuna.tinkerforgesensor.model.type;

import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;

public class SensorEvent {

    private final Sensor sensor;
    private final Long value;
    private final ValueType type;

    public SensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        this.sensor = sensor;
        this.value = value;
        this.type = type;
    }

    public Sensor sensor() {
        return sensor;
    }

    public Long value() {
        return value;
    }

    public ValueType type() {
        return type;
    }
}
