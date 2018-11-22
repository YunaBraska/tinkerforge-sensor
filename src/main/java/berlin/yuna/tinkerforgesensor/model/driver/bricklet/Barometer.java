package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ENVIRONMENT;

public class Barometer extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletBarometer device = (BrickletBarometer) sensor.device;
        registration.sensitivity(50, ENVIRONMENT);

        device.addAltitudeListener(value -> registration.sendEvent(consumerList, ALTITUDE, sensor, (long) value * 10));
        device.addAirPressureListener(value -> registration.sendEvent(consumerList, AIR_PRESSURE, sensor, (long) value));

        device.setAirPressureCallbackPeriod(period);
        device.setAltitudeCallbackPeriod(period);
    }
}
