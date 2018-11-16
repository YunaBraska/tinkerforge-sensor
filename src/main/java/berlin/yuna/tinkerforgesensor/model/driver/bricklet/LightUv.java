package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickletUVLight;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UV;

public class LightUv extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletUVLight device = (BrickletUVLight) sensor.device;
        registration.sensitivity(100, LIGHT);
        device.setUVLightCallbackPeriod(period);
        device.addUVLightListener(value -> registration.sendEvent(consumerList, LIGHT_UV, sensor, value));
    }
}
