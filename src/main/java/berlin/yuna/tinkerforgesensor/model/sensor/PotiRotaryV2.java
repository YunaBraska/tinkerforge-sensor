package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletRotaryPotiV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.PERCENTAGE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ROTARY;

/**
 * <h3>{@link PotiRotaryV2}</h3><br>
 * <i>300° rotary potentiometer</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#ROTARY} [x = number]</li>
 * <li>{@link ValueType#PERCENTAGE} [x = number]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Rotary_Poti_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting position in % (-150 to 150)</h6>
 * <code>values().rotary();</code>
 * <h6>Getting position in % (0-100)</h6>
 * <code>sensor.values().percentage();</code>
 */
public class PotiRotaryV2 extends Sensor<BrickletRotaryPotiV2> {

    public PotiRotaryV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletRotaryPotiV2) device, uid);
    }

    @Override
    protected Sensor<BrickletRotaryPotiV2> initListener() {
        device.addPositionListener(value -> {
            sendEvent(ROTARY, value, true);
            sendEvent(PERCENTAGE, ((value + 150) * 100) / 300, true);
        });
        refreshPeriod(1);
        return this;
    }

    public int getPosition() {
        return getValue(ROTARY, -1, -1).intValue();
    }

    public int getPercentage() {
        return getValue(PERCENTAGE, -1, -1).intValue();
    }

    @Override
    public Sensor<BrickletRotaryPotiV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletRotaryPotiV2> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                ledStatus = LED_STATUS_HEARTBEAT;
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                ledStatus = LED_STATUS;
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletRotaryPotiV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletRotaryPotiV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletRotaryPotiV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setPositionCallbackConfiguration(4, false, 'x', 0, 0);
            } else {
                device.setPositionCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            sendEvent(ROTARY, device.getPosition(), true);
            sendEvent(PERCENTAGE, ((device.getPosition() + 150) * 100) / 300, true);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
