package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

public class Temperature extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletTemperature device = (BrickletTemperature) sensor.device;
        registration.sensitivity(100, TEMPERATURE);

        device.addTemperatureListener(value -> registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) value));

        device.setTemperatureCallbackPeriod(period);
    }
}
