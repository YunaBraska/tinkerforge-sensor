package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;

/**
 * <h3>{@link SoundIntensity}</h3><br>
 * <i>Measures sound intensity</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#SOUND_INTENSITY} [x / 100 = db]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Sound_Intensity.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting sound intensity</h6>
 * <code>sensor.values().soundIntensity();</code>
 */
public class SoundIntensity extends Sensor<BrickletSoundIntensity> {

    public SoundIntensity(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletSoundIntensity) device, uid);
    }

    @Override
    protected Sensor<BrickletSoundIntensity> initListener() {
        device.addIntensityListener(value -> sendEvent(SOUND_INTENSITY, (long) value  * 10));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletSoundIntensity> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setIntensityCallbackPeriod(0);
            } else {
                device.setIntensityCallbackPeriod(milliseconds);
            }
            sendEvent(SOUND_INTENSITY, (long) device.getIntensity());
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
