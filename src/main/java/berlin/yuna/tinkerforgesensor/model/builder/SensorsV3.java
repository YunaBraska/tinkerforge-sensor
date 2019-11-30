package berlin.yuna.tinkerforgesensor.model.builder;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Default;
import berlin.yuna.tinkerforgesensor.model.sensor.LightAmbientV3;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import java.lang.Class;
import java.lang.RuntimeException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Autogenerated with [GeneratorSensors:generate]
 */
public class SensorsV3 extends CopyOnWriteArrayList<Sensor> {
    public SensorsV3() {
    }

    public SensorsV3(final Collection<? extends Sensor> collection) {
        super(collection);
    }

    public SensorsV3(final Sensor[] toCopyIn) {
        super(toCopyIn);
    }

    public LightAmbientV3 lightAmbient() {
        return lightAmbient(0);
    }

    public LightAmbientV3 lightAmbient(final int number) {
        return (LightAmbientV3) getSensor(number, LightAmbientV3.class);
    }

    private synchronized List<Sensor> getSensor(final Class... sensorClasses) {
        return stream().filter(sensor -> sensor.compare().is(sensorClasses)).sorted(java.util.Comparator.comparingInt(Sensor::port)).collect(java.util.stream.Collectors.toList());
    }

    private synchronized Sensor getSensor(final int number, final Class... sensorClasses) {
        final List<Sensor> sensors = getSensor(sensorClasses);
        return number < sensors.size() ? sensors.get(number) : getDefaultSensor(sensorClasses[0]);
    }

    private Sensor getDefaultSensor(final Class sensorClass) {
        try {
            return new Default(sensorClass);
        } catch (NetworkConnectionException e) {
            throw new RuntimeException("Default device should not run into an exception", e);
        }
    }
}
