package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;

public class Humidity extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList) throws TimeoutException, NotConnectedException {
        BrickletHumidity device = (BrickletHumidity) sensor.device;
        registration.sensitivity(50, HUMIDITY);

        //START EVENTS
        registration.sendEvent(consumerList, HUMIDITY, sensor, (long) (device.getHumidity() * 10));

        device.addHumidityListener(value -> registration.sendEvent(consumerList, HUMIDITY, sensor, (long) (value * 10)));
    }
}
