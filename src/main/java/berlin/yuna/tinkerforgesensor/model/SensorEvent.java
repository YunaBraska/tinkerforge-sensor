package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.Device;

public class SensorEvent {

    public final Sensor sensor;
    public final Class<? extends Device> sensorType;
    public final Long value;
    public final ValueType valueType;

    public SensorEvent(Sensor sensor, Long value, ValueType valueType) {
        this.sensor = sensor;
        this.value = value;
        this.valueType = valueType;
        if (sensor != null) {
            this.sensorType = sensor.device.getClass();
        } else {
            sensorType = null;
        }
    }
}
