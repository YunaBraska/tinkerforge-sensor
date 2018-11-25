package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.AIR_PRESSURE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALTITUDE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ENVIRONMENT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;

public class SoundIntensity extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
        BrickletSoundIntensity device = (BrickletSoundIntensity) sensor.device;
        registration.sensitivity(50, SOUND_INTENSITY);

        device.addIntensityListener(value -> registration.sendEvent(consumerList, SOUND_INTENSITY, sensor, (long) value));

        device.setIntensityCallbackPeriod(period);
    }
}
