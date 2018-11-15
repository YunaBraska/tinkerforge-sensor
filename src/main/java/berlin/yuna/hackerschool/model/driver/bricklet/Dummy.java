package berlin.yuna.hackerschool.model.driver.bricklet;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.DummyDevice;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.ValueType.DUMMY;
import static java.lang.String.format;

public class Dummy extends Driver {

    public static final Random RANDOM = new Random();
    private static int registeredDummies = 0;

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        DummyDevice device = (DummyDevice) sensor.device;
        if (registeredDummies > 2) {
            throw new RuntimeException(format("Its not allowed to create more than [%s] [%s]", registeredDummies, device.getClass().getSimpleName()));
        }

        registeredDummies++;
        registration.sensitivity(100, DUMMY);

        createThread(1000, run -> registration.sendEvent(consumerList, DUMMY, sensor, RANDOM.nextLong()));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                statusLed -> console("[%s] StatusLed [%s] ", Dummy.class.getSimpleName(), statusLed),
                additionalLed -> console("[%s] AdditionalLed [%s] ", Dummy.class.getSimpleName(), additionalLed),
                value -> console("[%s] Value [%s] ", Dummy.class.getSimpleName(), value)
        ));
    }
}
