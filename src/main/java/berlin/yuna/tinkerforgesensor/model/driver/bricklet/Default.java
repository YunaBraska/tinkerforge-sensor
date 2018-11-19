package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DUMMY;
import static java.lang.String.format;

public class Default extends Driver {

    private static final Random RANDOM = new Random();
    public static final String name = Default.class.getSimpleName() + "_" + UUID.randomUUID().toString();

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        loopEnd(name);

        registration.sensitivity(100, DUMMY);

        createLoop(name, 1000, run -> registration.sendEvent(consumerList, DUMMY, sensor, RANDOM.nextLong()));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                statusLed -> console("[%s] StatusLed [%s] ", Default.class.getSimpleName(), statusLed),
                additionalLed -> console("[%s] AdditionalLed [%s] ", Default.class.getSimpleName(), additionalLed),
                value -> console("[%s] Value [%s] ", Default.class.getSimpleName(), value)
        ));
    }
}
