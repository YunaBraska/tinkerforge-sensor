package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletIO16V2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * 16-channel digital input/output
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16_V2.html">Official doku</a>
 */
public class IO16V2 extends Sensor<BrickletIO16V2> {

    public IO16V2(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletIO16V2) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickletIO16V2> initListener() {
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
    public Sensor<BrickletIO16V2> value(final Object value) {
        try {
            Integer input = normalizeValue(value);
            if (input != null) {
                boolean output = input > -1;
                input = output ? input : input * -1;
                if (output) {
                    device.setConfiguration(input, 'o', true);
                } else {
                    device.setConfiguration(input, 'i', false);
                }
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16V2> ledStatus(final Integer value) {
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
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    /**
     * <br /> {@link LedStatusType#LED_ADDITIONAL_ON} / {@link LedStatusType#LED_ADDITIONAL_OFF} = all LEDs on/off
     * <br /> [0] = all LEDs off
     * <br /> [1] = all LEDs on
     * <br /> [2 ... 18] turn on 16 LED ports
     * <br /> [2 ... -18] turn off 16 LED ports
     * Todo: [1000] = 3V output
     * Todo: [2000] = 5V output
     */
    @Override
    public Sensor<BrickletIO16V2> ledAdditional(Integer value) {
        if (value == LED_ADDITIONAL_ON.bit) {
            for (int i = 0; i < 17; i++) {
                value(i);
            }
        } else if (value == LED_ADDITIONAL_OFF.bit) {
            for (int i = 0; i < 17; i++) {
                value(i * -1);
            }
        } else {
            value(value - 2);
        }
        return this;
    }

    @Override
    protected Sensor<BrickletIO16V2> flashLed() {
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
