package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickletVoltageCurrentV2;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ENERGY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.POWER;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.VOLTAGE;

public class VoltageCurrent2 extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) {
        BrickletVoltageCurrentV2 device = (BrickletVoltageCurrentV2) sensor.device;
        registration.sensitivity(50, ENERGY);

        device.addCurrentListener(value -> registration.sendEvent(consumerList, CURRENT, sensor, (long) value));
        device.addPowerListener(value -> registration.sendEvent(consumerList, POWER, sensor, (long) value));
        device.addVoltageListener(value -> registration.sendEvent(consumerList, VOLTAGE, sensor, (long) value));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i ==  LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                }, ignore -> { }, ignore -> { }));
    }
}
