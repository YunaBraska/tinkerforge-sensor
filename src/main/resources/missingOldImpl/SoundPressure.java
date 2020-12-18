package berlin.yuna.tinkerforgesensor.model.missing;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.BrickletSoundPressureLevel;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.SensorUtils.ledStatusConfig;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_DECIBEL;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_INTENSITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.SOUND_SPECTRUM;
import static java.lang.String.format;

/**
 * <h3>{@link SoundPressure}</h3><br>
 * <i>Measures Sound Pressure Level in dB(A/B/C/D/Z)</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#SOUND_DECIBEL} [x / 10 = db]</li>
 * <li>{@link ValueType#SOUND_INTENSITY} [x / 100 = db]</li>
 * <li>{@link ValueType#SOUND_SPECTRUM} [x = x[]]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc//Hardware/Bricklets/Sound_Pressure_Level.html">Official documentation</a></li>
 * </ul>
 * <h6>Get sound spectrum</h6>
 * <code>sensor.values().listSoundSpectrum();</code>
 * <h6>Get sound spectrum with FFT index 20</h6>
 * <code>sensor.values().listSoundSpectrum(20);</code>
 * <h6>Get sound spectrum list</h6>
 * <code>sensor.values().listSoundSpectrum_List();</code>
 * <h6>Setting FFT to size of 256</h6>
 * <i>Allowed: 128, 256, 512, 1024</i>
 * <code>sensor.send(256)</code>
 * <h6>Setting weighting</h6>
 * <i>Allowed: A, B, C, D, Z</i>
 * <code>sensor.send("A")</code>
 * <h6>Setting FFT and weighting</h6>
 * <code>sensor.end("A", 256)</code>
 */
public class SoundPressure extends Sensor<BrickletSoundPressureLevel> {

    private int weighing;
    private int fftSize;

    public SoundPressure(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletSoundPressureLevel) device, uid);
    }

    @Override
    protected Sensor<BrickletSoundPressureLevel> initListener() {
        device.addDecibelListener(value -> sendEvent(SOUND_DECIBEL, (long) value));
        device.addSpectrumListener(values -> {
            //cause of nullPointer somehow
            if (values != null && values.length > 1) {
                sendEvent(SOUND_SPECTRUM, intToLongList(values), true);

                final int[] intensity = Arrays.copyOfRange(values, 1, values.length / 3);
                sendEvent(SOUND_INTENSITY, (long) Arrays.stream(intensity).mapToLong(i -> i).summaryStatistics().getAverage());
            }
        });
        refreshPeriod(1);
        send(256, 4);
        if (getwWighing() != 4 || getFftSize() != 256) {
            throw new IllegalStateException(format("Self test error weighting [%s] fftSize [%s]", getwWighing(), getFftSize()));
        }
        return this;
    }

    public int[] getSoundIntensity() {
        return getValueList(SOUND_INTENSITY, -1).stream().mapToInt(Number::intValue).toArray();
    }

    public int[] getSoundSpectrum() {
        return getValueList(SOUND_SPECTRUM, -1).stream().mapToInt(Number::intValue).toArray();
    }

    public int getwWighing() {
        return weighing;
    }

    public int getFftSize() {
        return fftSize;
    }

    public void set(final int fftSize, final int weighting) {
        setFftSize(fftSize);
        setWeighting(weighting);
    }

    public void setWeighting(final int weighing) {
        this.weighing = weighing;
    }

    public void setFftSize(final int fftSize) {
        this.fftSize = fftSize;
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> send(final Object... values) {
        if (values.length > 1 && values[1] instanceof Number) {
            final int value = ((Number) values[1]).intValue();
            if (value == 4) {
                send('Z');
            } else if (value == 0 || value == 1 || value == 2 || value == 3) {
                send((char) value);
            }
            send(values[0]);
        } else {
            Arrays.stream(values).forEach(this::send);
        }
        return this;
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> send(final Object value) {
        int fftSize = this.fftSize;
        int weighing = this.weighing;
        if (value instanceof Character) {
            switch ((Character) value) {
                case 'A':
                    weighing = 0;
                    break;
                case 'B':
                    weighing = 1;
                    break;
                case 'C':
                    weighing = 2;
                    break;
                case 'D':
                    weighing = 3;
                    break;
                case 'Z':
                    weighing = 4;
                    break;
            }
        } else if (value instanceof String) {
            send(((String) value).charAt(0));
        } else if (value instanceof Number) {
            fftSize = ((Number) value).intValue();
        }
        updateConfiguration(fftSize, weighing);

        return this;
    }

    private void updateConfiguration(final int fftSize, final int weighing) {
        try {
            if (this.fftSize != fftSize || this.weighing != weighing) {
                this.fftSize = fftSize;
                this.weighing = weighing;
                device.setConfiguration(fftSize, weighing);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> setLedStatus(final Integer value) {
        return ledStatusConfig(this, value, result ->  device.setStatusLEDConfig((short) result.intValue()));
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletSoundPressureLevel> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
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
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private List<Number> intToLongList(final int[] values) {
        return Arrays.stream(values).mapToLong(value -> value).boxed().collect(Collectors.toList());
    }
}
