package berlin.yuna.tinkerforgesensor.model.driver.brick;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ACCELERATION_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ANGULAR_VELOCITY_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Z;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;


public class IMU extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickIMU device = (BrickIMU) sensor.device;
        registration.sensitivity(1, ValueType.IMU);
        device.setAllDataPeriod(period);
        device.addAllDataListener((accX, accY, accZ, magX, magY, magZ, angX, angY, angZ, temperature) ->
                {
                    registration.sendEvent(consumerList, ACCELERATION_X, sensor, (long) accX);
                    registration.sendEvent(consumerList, ACCELERATION_Y, sensor, (long) accY);
                    registration.sendEvent(consumerList, ACCELERATION_Z, sensor, (long) accZ);
                    registration.sendEvent(consumerList, MAGNETIC_X, sensor, (long) magX);
                    registration.sendEvent(consumerList, MAGNETIC_Y, sensor, (long) magY);
                    registration.sendEvent(consumerList, MAGNETIC_Z, sensor, (long) magZ);
                    registration.sendEvent(consumerList, ANGULAR_VELOCITY_X, sensor, (long) angX);
                    registration.sendEvent(consumerList, ANGULAR_VELOCITY_Y, sensor, (long) angY);
                    registration.sendEvent(consumerList, ANGULAR_VELOCITY_Z, sensor, (long) angZ);
                    //registration.sendEvent(consumerList, TEMPERATURE, sensor, (long) temperature);
                    registration.sendEvent(consumerList, ValueType.IMU, sensor, 1L);
                }
        );

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i == LED_STATUS_ON.bit) {device.enableStatusLED();}
                    else if (i == LED_STATUS_OFF.bit) {device.disableStatusLED();}
                    },
                i -> {
                    if (i == LED_ADDITIONAL_ON.bit) {device.enableStatusLED();}
                    else if (i == LED_ADDITIONAL_OFF.bit) {device.disableStatusLED();}
                },
                ignore -> { }));
    }
}
