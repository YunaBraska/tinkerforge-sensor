package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static com.tinkerforge.BrickletRGBLEDButton.BUTTON_STATE_PRESSED;

public class ButtonRGB extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletRGBLEDButton device = (BrickletRGBLEDButton) sensor.device;
        registration.sensitivity(100, BUTTON);
        device.addButtonStateChangedListener(value -> {
            registration.sendEvent(consumerList, BUTTON, sensor, (long) value);
            registration.sendEvent(consumerList, (value == BUTTON_STATE_PRESSED) ? BUTTON_PRESSED : BUTTON_RELEASED, sensor, (long) value);
        });
//            current.getChipTemperature()
//            current.getColor()
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                }, ignored -> { }, customValue -> {
                    if (customValue instanceof Number) {
                        Color color = new Color(((Number) customValue).intValue());
                        device.setColor(color.getRed(), color.getGreen(), color.getBlue());
                    } else {
                        device.setColor(0, 0, 0);
                    }
                }));

        sensor.hasStatusLed = true;
    }
}
