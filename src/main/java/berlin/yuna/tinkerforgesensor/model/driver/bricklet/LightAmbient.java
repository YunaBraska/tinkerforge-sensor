package berlin.yuna.hackerschool.model.driver.bricklet;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT;
import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT_LUX;
import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT_LUX_ANALOG;

public class LightAmbient extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletAmbientLight device = (BrickletAmbientLight) sensor.device;
        registration.sensitivity(50, LIGHT);

        device.addIlluminanceListener(value -> registration.sendEvent(consumerList, LIGHT_LUX, sensor, (long) (value * 10)));
        device.addAnalogValueListener(value -> registration.sendEvent(consumerList, LIGHT_LUX_ANALOG, sensor, (long) value));
        device.setIlluminanceCallbackPeriod(period);
        device.setAnalogValueCallbackPeriod(period);
    }
}
