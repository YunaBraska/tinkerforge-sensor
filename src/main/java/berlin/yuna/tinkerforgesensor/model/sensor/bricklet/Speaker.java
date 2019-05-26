package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BEEP_ACTIVE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * Creates beep with configurable frequency
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html">Official doku</a>
 * <b>Technical help</b>
 * <br /><a href="https://morsecode.scphillips.com/translator.html">Morse generator</a>
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

    /**
     * @param values <br /> Beep = duration
     *               <br /> Beep = duration, frequency
     *               <br /> Beep = duration, frequency, waitTime
     *               <br /> Beep = duration, frequency, waitBoolean
     *               <br /> Morse = "... --- ..."
     *               <br /> Morse = "... --- ...", frequency
     *               <br /> Morse = "... --- ...", frequency, waitBoolean
     *               <br /> Frequency device limits = [min 585 - max 7100]
     * @return {@link Sensor}
     */
    @Override
    public Sensor<BrickletPiezoSpeaker> value(final Object... values) {
        if (values != null) {
            int duration = getDuration(values);
            int wait = getWait(values, duration);
            int frequency = getFrequency(values);

            if (values.length > 0 && values[0] instanceof String) {
                morse(values[0]);
            } else {
                beep(duration, frequency, wait);
            }
        }
        return this;
    }

    /**
     * @param value <br /> Beep time = n
     *              <br /> Morse = "... --- ..."
     *              <br /> Frequency = number with prefix "f" [min 585 - max 7100]
     * @return {@link Sensor}
     */
    public Sensor<BrickletPiezoSpeaker> value(final Object value) {
        return value(value, frequency);
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
//        try {
        //FIXME: Broken sensor
//            device.calibrate();
        for (int i = 585; i < 2000; i++) {
//            value("f" + i);
            value(i, i, false);
        }
        value(0, 2000, false);
        value("...");
//        } catch (TimeoutException | NotConnectedException ignored) {
//            sendEvent(DEVICE_TIMEOUT, 404L);
//        }
        return this;
    }

    private void morse(Object value) {
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
            int frequency_tmp = ((Number) values[1]).intValue();
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
