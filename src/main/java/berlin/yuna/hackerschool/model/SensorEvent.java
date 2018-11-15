package berlin.yuna.hackerschool.model;

import berlin.yuna.hackerschool.model.type.ValueType;
import com.tinkerforge.Device;

public class SensorEvent {

    public final Sensor sensor;
    public final Class<? extends Device> sensorType;
    public final Long value;
    public final ValueType sensorValueType;

    public SensorEvent(Sensor sensor, Long value, ValueType sensorValueType) {
        this.sensor = sensor;
        this.value = value;
        this.sensorValueType = sensorValueType;
        if (sensor != null) {
            this.sensorType = sensor.device.getClass();
        } else {
            sensorType = null;
        }
    }
}
