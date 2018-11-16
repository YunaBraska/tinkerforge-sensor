package berlin.yuna.hackerschool.model.driver.bricklet;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_B;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_G;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_LUX;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_R;
import static berlin.yuna.hackerschool.model.type.ValueType.COLOR_TEMPERATURE;

public class LightColor extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletColor device = (BrickletColor) sensor.device;
        registration.sensitivity(50, COLOR);

        device.addColorListener((r, g, b, c) -> {
            registration.sendEvent(consumerList, COLOR_R, sensor, (long) r / 256);
            registration.sendEvent(consumerList, COLOR_G, sensor, (long) g / 256);
            registration.sendEvent(consumerList, COLOR_B, sensor, (long) b / 256);
            registration.sendEvent(consumerList, COLOR_LUX, sensor, (long) c / 256);
        });
        device.addColorTemperatureListener(value -> registration.sendEvent(consumerList, COLOR_TEMPERATURE, sensor, (long) value));

        device.setColorCallbackPeriod(period);
        device.setColorTemperatureCallbackPeriod(period);

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(ignore -> { },
                i -> {
                    if (i == LED_ADDITIONAL_ON.bit) {device.lightOn();}
                    else if (i == LED_ADDITIONAL_OFF.bit) {device.lightOff();}
                },
                ignore -> { }));
    }
}
