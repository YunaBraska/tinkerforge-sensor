package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BEEP_ACTIVE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link Speaker}</h3><br />
 * <i>Creates beep with configurable frequency</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BEEP_ACTIVE} [1 = active]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html">Official documentation</a></li>
 * <li><a href="https://morsecode.scphillips.com/translator.html">Morse generator</a></li>
 * </ul>
 * <h6>Send 1 second beep</h6>
 * <code>speaker.send(1000)</code>
 * <h6>Send 2 second beep with frequency (min 585 - max 7100)</h6>
 * <code>speaker.send(1000, 2000)</code>
 * <h6>Send morse</h6>
 * <code>speaker.send("... --- ...")</code>
 * <h6>Send morse with frequency (min 585 - max 7100)</h6>
 * <code>speaker.send("... --- ...", 3000)</code>
 * <h6>Wait until sound is finished</h6>
 * <code>speaker.send(256, 4000, true)</code>
 */
public class Speaker extends Sensor<BrickletPiezoSpeaker> {

    private int frequency = 2000;
    private int duration = 200;
    private int wait = duration;

    public Speaker(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletPiezoSpeaker) device, uid, false);
    }

    @Override
    protected Sensor<BrickletPiezoSpeaker> initListener() {
        device.addBeepFinishedListener(() -> sendEvent(BEEP_ACTIVE, 0L));
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> send(final Object... values) {
        if (values != null) {
            final int duration = getDuration(values);
            final int wait = getWait(values, duration);
            final int frequency = getFrequency(values);

            if (values.length > 0 && values[0] instanceof String) {
                morse(values[0]);
            } else {
                beep(duration, frequency, wait);
            }
        }
        return this;
    }

    public Sensor<BrickletPiezoSpeaker> send(final Object value) {
        return send(value, frequency);
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletPiezoSpeaker> flashLed() {
        for (int i = 585; i < 2000; i++) {
            send(i, i, false);
        }
        send(0, 2000, false);
        send("...");
        return this;
    }


    @Override
    public Sensor<BrickletPiezoSpeaker> refreshPeriod(final int milliseconds) {
        return this;
    }

    private void morse(final Object value) {
        try {
            sendEvent(BEEP_ACTIVE, 1L);
            device.morseCode((String) value, frequency);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void beep(final int duration, final int frequency, final int wait) {
        try {
            if (duration > 0) {
                sendEvent(BEEP_ACTIVE, 1L);
                device.beep(duration, frequency);
                waitForEnd(wait);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void waitForEnd(final int wait) {
        if (wait > 0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getDuration(final Object[] values) {
        int result = this.duration;
        if (values.length > 0 && values[0] instanceof Number) {
            result = ((Number) values[0]).intValue();
        }
        this.duration = result;
        return result;
    }

    private int getFrequency(final Object[] values) {
        int result = this.frequency;
        if (values.length > 1 && values[1] instanceof Number) {
            final int frequency_tmp = ((Number) values[1]).intValue();
            result = frequency_tmp < 7101 && frequency_tmp > 585 ? frequency_tmp : frequency;
        }
        this.frequency = result;
        return result;
    }

    private int getWait(final Object[] values, final int duration) {
        int result = this.wait;
        if (values.length > 2 && values[2] instanceof Number) {
            result = ((Number) values[2]).intValue();
        }
        if (values.length > 2 && values[2] instanceof Boolean) {
            result = (Boolean) values[2] ? duration : -1;
        }
        if (values.length > 1 && values[1] instanceof Boolean) {
            result = (Boolean) values[1] ? duration : -1;
        }
        this.wait = result;
        return wait;
    }
}
