package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

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
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Sound_Intensity.html">Official documentation</a>
 */
public class SoundIntensity extends Sensor<BrickletSoundIntensity> {

    public SoundIntensity(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletSoundIntensity) device, uid, true);
    }

    @Override
    protected Sensor<BrickletSoundIntensity> initListener() {
        device.addIntensityListener(value -> sendEvent(SOUND_INTENSITY, (long) value));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setIntensityCallbackPeriod(0);
                sendEvent(SOUND_INTENSITY, (long) device.getIntensity());
            } else {
                device.setIntensityCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
