package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletSoundPressureLevel;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM_CHUNK;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM_LENGTH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM_OFFSET;

/**
 * <h3>{@link SoundPressure}</h3><br />
 * <i>Measures Sound Pressure Level in dB(A/B/C/D/Z)</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#SOUND_INTENSITY} [x / 10 = db]</li>
 * <li>{@link ValueType#SOUND_SPECTRUM_OFFSET}</li>
 * <li>{@link ValueType#SOUND_SPECTRUM_LENGTH}</li>
 * <li>{@link ValueType#SOUND_SPECTRUM} [x = x[]]</li>
 * <li>{@link ValueType#SOUND_SPECTRUM_CHUNK} [x = x[]]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc//Hardware/Bricklets/Sound_Pressure_Level.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting sound spectrum examples</h6>
 * <code>stack.values().listSoundSpectrum();</code>
 * <code>stack.values().listSoundSpectrumChunk();</code>
 */
public class SoundPressure extends Sensor<BrickletSoundPressureLevel> {

    public SoundPressure(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletSoundPressureLevel) device, uid, true);
    }

    @Override
    protected Sensor<BrickletSoundPressureLevel> initListener() {
        device.addDecibelListener(value -> sendEvent(SOUND_INTENSITY, (long) value));
        device.addSpectrumListener(value -> sendSpectrum(SOUND_SPECTRUM, value));
        device.addSpectrumLowLevelListener((spectrumLength, spectrumChunkOffset, spectrumChunkData) -> {
            sendSpectrum(SOUND_SPECTRUM_CHUNK, spectrumChunkData);
            send(SOUND_SPECTRUM_OFFSET, spectrumChunkOffset);
            send(SOUND_SPECTRUM_LENGTH, spectrumLength);
        });
        refreshPeriod(1);
        return this;
    }

    private void sendSpectrum(final ValueType valueType, final int[] values) {
        valueMap().put(valueType, new RollingList<>(Arrays.stream(values).mapToLong(value -> value).boxed().collect(Collectors.toList())));
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_OFF.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setDecibelCallbackConfiguration(1000, false, 'x', 0, 0);
                device.setSpectrumCallbackConfiguration(0);
            } else {
                device.setSpectrumCallbackConfiguration(milliseconds);
                device.setDecibelCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(SOUND_INTENSITY, (long) device.getDecibel());
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
