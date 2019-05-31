package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;

/**
 * <h3>{@link SoundIntensity}</h3><br />
 * <i>Measures sound intensity</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#SOUND_INTENSITY} [x / 10 = db]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Sound_Intensity.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting sound intensity example</h6>
 * <code>stack.values().soundIntensity();</code>
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
