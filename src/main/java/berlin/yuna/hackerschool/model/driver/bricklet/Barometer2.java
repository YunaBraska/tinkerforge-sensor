package berlin.yuna.hackerschool.model.driver.bricklet;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickletBarometerV2;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.hackerschool.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.hackerschool.model.type.ValueType.ALTITUDE;
import static berlin.yuna.hackerschool.model.type.ValueType.ENVIRONMENT;
import static berlin.yuna.hackerschool.model.type.ValueType.TEMPERATURE;

public class Barometer2 extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletBarometerV2 device = (BrickletBarometerV2) sensor.device;
        registration.sensitivity(50, ENVIRONMENT);

        device.addAltitudeListener(value -> registration.sendEvent(consumerList, ALTITUDE, sensor, (long) value));
        device.addAirPressureListener(value -> registration.sendEvent(consumerList, AIR_PRESSURE, sensor, (long) value));
        device.addTemperatureListener(value -> registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) value));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                }, ignore -> { }, ignore -> { }));
    }
}
