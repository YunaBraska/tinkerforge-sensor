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
 * <h3>{@link IO16}</h3><br />
 * <i>16-channel digital input/output</i><br />
 *
 * <h3>Values</h3>
 * <i>input values coming soon</i><br />
 *
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Set all LEDs on</h6>
 * <code>io16.ledAdditionalOn();</code>
 * <h6>Turn on LED 4</h6>
 * <code>io16.send(4);</code>
 * <h6>Turn off LED 12</h6>
 * <code>io16.send(-12);</code>
 */
public class IO16V2 extends Sensor<BrickletIO16V2> {

    public IO16V2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletIO16V2) device, uid, true);
    }

    @Override
    protected Sensor<BrickletIO16V2> initListener() {
        //TODO io listener
        return this;
    }

    @Override
    public Sensor<BrickletIO16V2> send(final Object value) {
        try {
            Integer input = normalizeValue(value);
            if (input != null) {
                final boolean output = input > -1;
                input = output ? input : (input * -1);
                if (output) {
                    device.setConfiguration(input - 1, 'o', true);
                } else {
                    device.setConfiguration(input - 1, 'i', false);
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

    @Override
    public Sensor<BrickletIO16V2> ledAdditional(final Integer value) {
        if (value == LED_ADDITIONAL_ON.bit) {
            for (int i = 0; i < 17; i++) {
                send(i);
            }
        } else if (value == LED_ADDITIONAL_OFF.bit) {
            for (int i = 0; i < 17; i++) {
                send(i * -1);
            }
        } else {
            send(value - 2);
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16V2> flashLed() {
        try {
            ledAdditionalOff();
            for (int i = 1; i < 33; i++) {
                this.send(i < 17 ? i : (i - 16) * -1);
                Thread.sleep(32);
            }
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletIO16V2> refreshPeriod(final int milliseconds) {
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
