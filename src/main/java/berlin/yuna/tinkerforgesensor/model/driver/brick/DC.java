package berlin.yuna.hackerschool.model.driver.brick;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.driver.Driver;
import berlin.yuna.hackerschool.logic.SensorRegistration;
import com.tinkerforge.BrickDC;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.hackerschool.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.hackerschool.model.type.ValueType.CURRENT;
import static berlin.yuna.hackerschool.model.type.ValueType.EMERGENCY_SHUTDOWN;
import static berlin.yuna.hackerschool.model.type.ValueType.ENERGY;
import static berlin.yuna.hackerschool.model.type.ValueType.VOLTAGE;

public class DC extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickDC device = (BrickDC) sensor.device;
        registration.sensitivity(90, ENERGY);
        device.setCurrentVelocityPeriod(period);
        device.addEmergencyShutdownListener(() -> registration.sendEvent(consumerList, EMERGENCY_SHUTDOWN, sensor, 1L));
        device.addCurrentVelocityListener(value -> registration.sendEvent(consumerList, CURRENT, sensor, (long) value));
        device.addUnderVoltageListener(value -> registration.sendEvent(consumerList, VOLTAGE, sensor, (long) value));

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i ==  LED_STATUS_ON.bit) {device.enableStatusLED();}
                    else if (i == LED_STATUS_OFF.bit) {device.disableStatusLED();}
                }, ignore -> { }, ignore -> { }));
        //                current.getAcceleration();
//                current.getChipTemperature();
//                current.getCurrentConsumption();
//                current.getCurrentVelocity();
//                current.getExternalInputVoltage();
//                current.getMinimumVoltage();
//                current.getPWMFrequency();
//                current.getVelocity();
//                current.getDriveMode();
    }
}
