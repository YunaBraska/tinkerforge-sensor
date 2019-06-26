package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link IO16}</h3><br />
 * <i>16-channel digital input/output</i><br />
 *
 * <h3>Values</h3>
 * <i>input values coming soon</i><br />
 *
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16.html">Official documentation</a></li>
 * </ul>
 * <h6>Set all LEDs on</h6>
 * <code>io16.ledAdditional_setOn();</code>
 * <h6>Turn on LED 4</h6>
 * <code>io16.send(4);</code>
 * <h6>Turn off LED 12</h6>
 * <code>io16.send(-12);</code>
 */
public class IO16 extends Sensor<BrickletIO16> {

    public IO16(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletIO16) device, uid);
    }

    @Override
    protected Sensor<BrickletIO16> initListener() {
        //TODO io listener
        return this;
    }

    @Override
    public Sensor<BrickletIO16> send(final Object value) {
        try {
            Integer input = normalizeValue(value);
            if (input != null) {
                final boolean output = input > -1;
                input = output ? input : input * -1;
                final char block;
                if (input > 8) {
                    block = 'b';
                    input = input - 8;
                } else {
                    block = 'a';
                }
                device.setPortConfiguration(block, (short) getPotential(firstDigit(input)), output ? 'o' : 'i', output);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletIO16> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                device.setPortConfiguration('a', (short) 255, 'o', true);
                device.setPortConfiguration('b', (short) 255, 'o', true);
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                device.setPortConfiguration('a', (short) 255, 'i', false);
                device.setPortConfiguration('b', (short) 255, 'i', false);
            } else {
                send(value - 2);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_ADDITIONAL_OFF;
        return this;
    }

    @Override
    public Sensor<BrickletIO16> flashLed() {
        try {
            ledAdditional_setOff();
            for (int i = 1; i < 33; i++) {
                this.send(i < 17 ? i : (i - 16) * -1);
                Thread.sleep(32);
            }
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16> refreshPeriod(final int milliseconds) {
        return this;
    }

    //TODO move to Utils
    private int getPotential(final int n) {
        if (n == 1) {
            return 1;
        }
        int result = 1;
        for (int i = 1; i < n; i++) {
            result *= 2;
        }
        return result;
    }

    //TODO move to Utils
    private int firstDigit(final int n) {
        int first = n;
        while (first >= 10)
            first /= 10;
        return first;
    }

    private Integer normalizeValue(final Object value) {
        if (value instanceof Boolean) {
            if ((Boolean) value) {
                ledAdditional_setOn();
            } else {
                ledAdditional_setOff();
            }
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }
}
