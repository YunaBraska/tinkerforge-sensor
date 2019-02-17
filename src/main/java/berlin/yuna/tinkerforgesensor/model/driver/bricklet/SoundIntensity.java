package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;

/**
 * Measures sound intensity
 * <b>Values</b>
 * SOUND_DECIBEL[db] = n / 10.0
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Sound_Intensity.html">Official doku</a>
 */
public class SoundIntensity extends Sensor<BrickletSoundIntensity> {

    public SoundIntensity(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletSoundIntensity) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickletSoundIntensity> initListener() {
        try {
            device.addIntensityListener(value -> sendEvent(SOUND_INTENSITY, (long) value));
            device.setIntensityCallbackPeriod(3);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> value(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> ledAdditional(Integer value) {
        return this;
    }
}
