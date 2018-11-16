package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickletHumidityV2;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

public class Humidity2 extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletHumidityV2 device = (BrickletHumidityV2) sensor.device;
        registration.sensitivity(50, HUMIDITY, TEMPERATURE);

        device.addTemperatureListener(value -> registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) value));
        device.addHumidityListener(value -> registration.sendEvent(consumerList, HUMIDITY, sensor, (long) value));

        //START EVENTS
        registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) device.getTemperature());
        registration.sendEvent(consumerList, HUMIDITY, sensor, (long) device.getHumidity());

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                },
                ignore -> { }, ignore -> { }));
    }
}
