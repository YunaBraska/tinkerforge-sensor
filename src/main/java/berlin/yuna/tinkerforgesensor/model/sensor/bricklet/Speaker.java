package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * Creates beep with configurable frequency
 * <br /><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html">Official doku</a>
 * <b>Technical help</b>
 * <br /><a href="https://morsecode.scphillips.com/translator.html">Morse generator</a>
 */
public class Speaker extends Sensor<BrickletPiezoSpeaker> {

    private int frequency = 2000;

    public Speaker(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletPiezoSpeaker) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletPiezoSpeaker> initListener() {
        return this;
    }

    /**
     * @param value <br /> Beep time = n
     *              <br /> Morse = "... --- ..."
     *              <br /> Frequency = number with prefix "f" [min 585 - max 7100]
     * @return {@link Sensor}
     */
    @Override
    public Sensor<BrickletPiezoSpeaker> value(final Object value) {
        try {
            if (value instanceof String) {
                String morse = (String) value;
                if (morse.startsWith("f") || morse.startsWith("F")) {
                    int frequency_tmp = Integer.valueOf(morse.substring(1));
                    this.frequency = frequency_tmp < 7101 && frequency_tmp > 585 ? frequency_tmp : frequency;
                } else {
                    device.morseCode(morse, frequency);
                }
            } else if (value instanceof Number) {
                device.beep(((Number) value).longValue(), frequency);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
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
    protected Sensor<BrickletPiezoSpeaker> flashLed() {
//        try {
        //FIXME: Broken sensor
//            device.calibrate();
        for (int i = 585; i < 2000; i++) {
            value("f" + i);
            value(i);
        }
        value("f2000");
        value("...");
//        } catch (TimeoutException | NotConnectedException ignored) {
//            sendEvent(DEVICE_TIMEOUT, 404L);
//        }
        return this;
    }
}
