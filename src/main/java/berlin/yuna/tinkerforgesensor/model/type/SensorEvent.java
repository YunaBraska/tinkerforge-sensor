package berlin.yuna.tinkerforgesensor.model.type;

import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;

public class SensorEvent {

    public final Sensor sensor;
    public final Long value;
    public final ValueType valueType;

    public SensorEvent(Sensor sensor, Long value, ValueType valueType) {
        this.sensor = sensor;
        this.value = value;
        this.valueType = valueType;
    }
}