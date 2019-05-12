package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * 16-channel digital input/output
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16.html">Official doku</a>
 */
public class IO16 extends Sensor<BrickletIO16> {

    public IO16(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletIO16) device, parent, uid, false);
    }

    @Override
    protected Sensor<BrickletIO16> initListener() {
        //TODO io listener
        return this;
    }

    /**
     * <br /> [true/false] = all LEDs on/off
     * <br /> [0] = nothing
     * <br /> [1 ... 17] turn on 16 LED ports
     * <br /> [1 ... -17] turn off 16 LED ports
     * Todo: [1000] = 3V output
     * Todo: [2000] = 5V output
     */
    @Override
    public Sensor<BrickletIO16> value(final Object value) {
        try {
            Integer input = normalizeValue(value);
            if (input != null) {
                boolean output = input > -1;
                input = output ? input : input * -1;
                char block;
                if (input > 8) {
                    block = 'b';
                    input = input - 8;
                } else {
                    block = 'a';
                }
                device.setPortConfiguration(block, (short) getPotential(firstDigit(input)), output ? 'o' : 'i', output);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16> ledStatus(final Integer value) {
        return this;
    }

    /**
     * <br /> {@link berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType#LED_ADDITIONAL_ON} / {@link berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType#LED_ADDITIONAL_OFF} = all LEDs on/off
     * <br /> [0] = all LEDs off
     * <br /> [1] = all LEDs on
     * <br /> [2 ... 18] turn on 16 LED ports
     * <br /> [2 ... -18] turn off 16 LED ports
     * Todo: [1000] = 3V output
     * Todo: [2000] = 5V output
     */
    @Override
    public Sensor<BrickletIO16> ledAdditional(Integer value) {
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                device.setPortConfiguration('a', (short) 255, 'o', true);
                device.setPortConfiguration('b', (short) 255, 'o', true);
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                device.setPortConfiguration('a', (short) 255, 'i', false);
                device.setPortConfiguration('b', (short) 255, 'i', false);
            } else {
                value(value - 2);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16> flashLed() {
        try {
            ledAdditionalOff();
            for (int i = 1; i < 33; i++) {
                this.value(i < 17 ? i : (i - 16) * -1);
                Thread.sleep(32);
            }
        } catch (Exception ignore) {
        }
        return this;
    }

    //TODO move to Utils
    private int getPotential(int n) {
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
                ledAdditionalOn();
            } else {
                ledAdditionalOff();
            }
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }
}
