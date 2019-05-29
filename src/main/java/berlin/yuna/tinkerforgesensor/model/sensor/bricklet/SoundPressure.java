package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletSoundPressureLevel;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
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
 * Measures Sound Pressure Level in dB(A/B/C/D/Z)
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
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    //  create new getSpectrum value method
    //  create new spectrumChunkOffset value method
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
                device.setDecibelCallbackConfiguration(0, true, 'x', 0, 0);
                device.setSpectrumCallbackConfiguration(0);
                sendEvent(SOUND_INTENSITY, (long) device.getDecibel());
            } else {
                device.setSpectrumCallbackConfiguration(milliseconds);
                device.setDecibelCallbackConfiguration(milliseconds, false, 'x', 0, 0);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
