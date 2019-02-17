package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.generator.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static java.lang.String.format;

public class Default extends Sensor<DummyDevice> {

    private Class<? extends Sensor> sensorImitate;
    public static final DummyDevice DUMMY_DEVICE = new DummyDevice();

    public Default(final Device device, final Sensor sensor, final String uid) throws NetworkConnectionException {
        super(DUMMY_DEVICE, null, DUMMY_DEVICE.getIdentity().uid);
    }

    public Default(final Class<?> sensorOrDevice) throws NetworkConnectionException {
        super(DUMMY_DEVICE, null, DUMMY_DEVICE.getIdentity().uid);
        port = -99;
        if (Sensor.class.isAssignableFrom(sensorOrDevice)) {
            this.sensorImitate = (Class<? extends Sensor>) sensorOrDevice;
        } else if (Device.class.isAssignableFrom(sensorOrDevice)) {
            sensorImitate = SensorRegistry.getSensor(device.getClass()).newInstance(device, null, uid).getClass();
        } else {
            throw new RuntimeException(format("Unsupported sensor [%s]", sensorOrDevice.getSimpleName()));
        }
    }

    @Override
    public synchronized int refreshPortE() throws TimeoutException, NotConnectedException {
        return port;
    }

    @Override
    public Sensor<DummyDevice> value(Object value) {
        if (sensorImitate == BarometerV2.class) {
            //TODO
        }
        return this;
    }

    @Override
    public Sensor<DummyDevice> ledStatus(Integer value) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> ledAdditional(Integer value) {
        return this;
    }

    @Override
    protected Sensor<DummyDevice> initListener() {
        return this;
    }
}
