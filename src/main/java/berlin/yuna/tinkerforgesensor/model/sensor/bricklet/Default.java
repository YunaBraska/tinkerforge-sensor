package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;

import static java.lang.String.format;

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
}
