package berlin.yuna.hackerschool.model.driver.brick;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.hackerschool.model.type.ValueType.CURRENT;
import static berlin.yuna.hackerschool.model.type.ValueType.ENERGY;
import static berlin.yuna.hackerschool.model.type.ValueType.VOLTAGE;
import static berlin.yuna.hackerschool.model.type.ValueType.VOLTAGE_USB;

public class Master extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickMaster device = (BrickMaster) sensor.device;
        registration.sensitivity(1, ENERGY);
        device.setStackCurrentCallbackPeriod(period);
        device.setStackVoltageCallbackPeriod(period);
        device.setUSBVoltageCallbackPeriod(period);

        device.addStackCurrentListener(value -> registration.sendEvent(consumerList, CURRENT, sensor, (long) value));
        device.addStackVoltageListener(value -> registration.sendEvent(consumerList, VOLTAGE, sensor, (long) value));
        device.addUSBVoltageListener(value -> registration.sendEvent(consumerList, VOLTAGE_USB, sensor, (long) value));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(i -> {
                    if (i == LED_STATUS_ON.bit) {device.enableStatusLED();}
                    else if (i == LED_STATUS_OFF.bit) {device.disableStatusLED();}
                },
                i -> {
                    if(device.isWifi2Present()) {
                        if (i == LED_ADDITIONAL_ON.bit) {device.enableWifi2StatusLED();} else if (i == LED_ADDITIONAL_OFF.bit) {
                            device.disableWifi2StatusLED();
                        }
                    }
                },
                ignore -> { }));
    }
}
