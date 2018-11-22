package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.BrickletAirQuality;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ENVIRONMENT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.HUMIDITY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.IAQ_INDEX;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

public class AirQuality extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) {
        BrickletAirQuality device = (BrickletAirQuality) sensor.device;
        registration.sensitivity(100, ENVIRONMENT);

        device.addAllValuesListener((iaqIndex, iaqIndexAccuracy, temperature, humidity, airPressure) ->
        {
            registration.sendEvent(consumerList, IAQ_INDEX, sensor, (long) iaqIndex);
            registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) temperature);
            registration.sendEvent(consumerList, HUMIDITY, sensor, (long) humidity);
            registration.sendEvent(consumerList, AIR_PRESSURE, sensor, (long) airPressure * 10);
        });

        sensor.hasStatusLed = true;

        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {
                        device.setStatusLEDConfig(LED_STATUS_ON.bit);
                    } else if (i == LED_STATUS_HEARTBEAT.bit) {
                        device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);
                    } else if (i == LED_STATUS.bit) {
                        device.setStatusLEDConfig(LED_STATUS.bit);
                    } else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                }, ignore -> { }, ignore -> { }));

        try {
            device.setAllValuesCallbackConfiguration(period, true);
        } catch (TimeoutException | NotConnectedException ignore) {
        }
    }
}
