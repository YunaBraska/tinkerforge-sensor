package berlin.yuna.hackerschool.model.driver.bricklet;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickletUVLight;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT;
import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT_UV;

public class LightUv extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletUVLight device = (BrickletUVLight) sensor.device;
        registration.sensitivity(100, LIGHT);
        device.setUVLightCallbackPeriod(period);
        device.addUVLightListener(value -> registration.sendEvent(consumerList, LIGHT_UV, sensor, value));
    }
}
