package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BEEP;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BEEP_ACTIVE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BEEP_FINISH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static java.util.Arrays.stream;

/**
 * <h3>{@link Speaker}</h3><br>
 * <i>Creates beep with configurable frequency</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BEEP_ACTIVE} [1 = active]</li>
 * <li>{@link ValueType#BEEP_FINISH} [0 = finish]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html">Official documentation</a></li>
 * <li><a href="https://morsecode.scphillips.com/translator.html">Morse generator</a></li>
 * </ul>
 * <h6>Send 1 second beep</h6>
 * <code>sensor.send(1000)</code>
 * <h6>Send 2000 millisecond beep with 1000 frequency (585Hz - 7100Hz)</h6>
 * <code>sensor.send(2000, 1000)</code>
 * <h6>Wait until sound is finished</h6>
 * <code>sensor.send(256, 4000, true)</code>
 */
public class Speaker extends Sensor<BrickletPiezoSpeaker> {

    private int frequency = 2000;
    private long duration = 200;
    private boolean wait = false;

    public Speaker(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletPiezoSpeaker) device, uid);
    }

    @Override
    protected Sensor<BrickletPiezoSpeaker> initListener() {
        device.addBeepFinishedListener(() -> {
            sendEvent(BEEP, 0L);
            sendEvent(BEEP_FINISH, 0L);
        });
        return this;
    }

    public int getFrequency() {
        return frequency;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isWait() {
        return wait;
    }

    public boolean isBeepActive() {
        return getValue(BEEP, -1, -1).intValue() == 1;
    }

    public Sensor<BrickletPiezoSpeaker> sendBeep(final long duration) {
        return send(duration);
    }

    public Sensor<BrickletPiezoSpeaker> sendBeep(final long duration, final int frequency) {
        return send(duration, frequency);
    }

    public Sensor<BrickletPiezoSpeaker> sendBeep(final long duration, final int frequency, final boolean waitToEnd) {
        return send(duration, frequency, waitToEnd);
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> send(final Object... values) {
        if (values != null && stream(values).anyMatch(v -> v instanceof Number)) {
            beep(
                    getDuration(0, values),
                    getFrequency(1, values),
                    getWait(values)
            );
        }
        return this;
    }

    public Sensor<BrickletPiezoSpeaker> send(final Object value) {
        return send(value, frequency);
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> flashLed() {
        for (int i = 600; i < 2000; i++) {
            send(50, i, false);
        }
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> refreshPeriod(final int milliseconds) {
        return this;
    }

    private void beep(final long duration, final int frequency, final boolean wait) {
        try {
            if (duration > 0) {
                sendEvent(BEEP, 1L);
                sendEvent(BEEP_ACTIVE, 1L);
                device.beep(duration, frequency);
                waitForEnd(wait ? duration : -1);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void waitForEnd(final long duration) {
        if (duration > 0) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getDuration(final int index, final Object... values) {
        final long result = getObject(index, Number.class, values).orElse(duration).longValue();
        duration = result;
        return result;
    }

    private int getFrequency(final int index, final Object... values) {
        int result = getObject(index, Number.class, values).orElse(frequency).intValue();
        result = (result < 585 || result > 7100) ? frequency : result;
        frequency = result;
        return result;
    }

    private boolean getWait(final Object[] values) {
        final boolean result = stream(values)
                .filter(v -> v instanceof Boolean)
                .map(v -> (Boolean) v)
                .findFirst().orElse(wait);
        wait = result;
        return result;
    }
}
