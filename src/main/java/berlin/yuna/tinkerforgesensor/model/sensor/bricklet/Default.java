package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;

import static java.lang.String.format;

/**
 * <h3>{@link Default}</h3><br />
 * <i>Default sensor is representing a non existing but requested sensor</i><br />
 *
 * <h6>Check if the current sensor is {@link Default}</h6>
 * <code>
 * //false = (DefaultSensor) means that the current sensor not available
 * sensors.isPresent();
 * </code>
 * <h6>All methods wont do anything</h6>
 * <code>sensors.send();</code>
 */
public class Default extends Sensor<DummyDevice> {

    private Class<? extends Sensor> sensorImitate;
    public static final DummyDevice DUMMY_DEVICE = new DummyDevice();

    public Default(final Device device, final String uid) throws NetworkConnectionException {
        super(DUMMY_DEVICE, DUMMY_DEVICE.getIdentity().uid);
    }

    public Default(final Class<?> sensorOrDevice) throws NetworkConnectionException {
        super(DUMMY_DEVICE, DUMMY_DEVICE.getIdentity().uid);
        port = -20081988;
        if (Sensor.class.isAssignableFrom(sensorOrDevice)) {
            this.sensorImitate = (Class<? extends Sensor>) sensorOrDevice;
        } else if (Device.class.isAssignableFrom(sensorOrDevice)) {
            sensorImitate = SensorRegistry.getSensor(device.getClass()).newInstance(device, uid).getClass();
        } else {
            throw new RuntimeException(format("Unsupported sensor [%s]", sensorOrDevice.getSimpleName()));
        }
    }

    @Override
    public Sensor<DummyDevice> send(final Object value) {
        if (sensorImitate == BarometerV2.class) {
            //TODO
        }
        return this;
    }

    @Override
    public Sensor<DummyDevice> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    protected Sensor<DummyDevice> initListener() {
        return this;
    }

    @Override
    public Sensor<DummyDevice> refreshPeriod(final int milliseconds) {
        return this;
    }
}
