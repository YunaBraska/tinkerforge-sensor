package berlin.yuna.hackerschool.model.driver.bricklet;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT;
import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT_LUX;

public class LightAmbient2 extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletAmbientLightV2 device = (BrickletAmbientLightV2) sensor.device;
        registration.sensitivity(50, LIGHT);

        device.addIlluminanceListener(value -> registration.sendEvent(consumerList, LIGHT_LUX, sensor, value));
        device.setIlluminanceCallbackPeriod(period);
    }
}
