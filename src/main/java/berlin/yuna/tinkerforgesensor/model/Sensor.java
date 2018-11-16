package berlin.yuna.hackerschool.model;

import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import static java.lang.Character.toLowerCase;

public class Sensor extends SensorRegistration {
    public final String uid;
    public final String name;
    public final Device device;
    public final Sensor parent;
    public boolean isBrick = false;
    public boolean hasStatusLed = false;
    public int port = -1;


    public Sensor(final Device device, final Sensor parent) {
        try {
            this.uid = device.getIdentity().uid;
            this.name = device.getClass().getSimpleName();
            this.device = device;
            this.parent = parent;
            port();
        } catch (TimeoutException | NotConnectedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connected port first number represents the brick and the second the connected port
     *
     * @return port starts at 0
     */
    public synchronized int port() {
        return port > -1 ? port : refreshPort();
    }

    public synchronized int refreshPort() {
        try {
            Device.Identity identity = device.getIdentity();
            if (identity.connectedUid.equals("0")) {
                isBrick = true;
                port = 10;
                return port;
            } else if (isDigit(identity.position)) {
                isBrick = true;
                port = (getNumericValue(identity.position) + 1) * 10;
                return port;
            } else {
                isBrick = false;
                port = (((int) toLowerCase(identity.position)) - 96) + (parent == null ? 0 : parent.port());
                return port;
            }
        } catch (TimeoutException | NotConnectedException e) {
            error("[%s] [%s] [%s]", getClass().getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
            return port;
        }
    }

    public Sensor addListener(final List<Consumer<SensorEvent>> consumerList) {
        super.addListener(this, consumerList);
        return this;
    }

    public boolean isPresent() {
        return !(device instanceof DummyDevice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(uid, sensor.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "port='" + port + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean is(Class<? extends Device> deviceClass) {
        return device.getClass() == deviceClass;
    }
}
