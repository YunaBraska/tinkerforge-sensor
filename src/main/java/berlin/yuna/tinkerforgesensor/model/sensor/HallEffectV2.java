package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletHallEffectV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNET_COUNTER;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNET_DENSITY;

/**
 * <h3>{@link HallEffectV2}</h3><br />
 * <i>Measures magnetic flux density between -7mT and +7mT</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#MAGNETIC_X} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Y} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Z} [x = number]</li>
 * <li>{@link ValueType#MAGNET_DENSITY} [x / 10 = °]</li>
 * <li>{@link ValueType#MAGNET_COUNTER} [x = µT]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Hall_Effect_V2.html#hall-effect-v2-bricklet">Official documentation</a></li>
 * </ul>
 * <h6>Getting magnetic density (µT)</h6>
 * <code>sensor.values().magneticDensity();</code>
 * <h6>Getting counter</h6>
 * <code>sensor.values().magneticCounter();</code>
 */
public class HallEffectV2 extends Sensor<BrickletHallEffectV2> {

    //TODO: configurable
    public static final int HIGH_THRESHOLD = 3000;
    public static final int LOW_THRESHOLD = -3000;
    public static final int DEBOUNCE = 10000;

    public HallEffectV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletHallEffectV2) device, uid);
    }

    @Override
    protected Sensor<BrickletHallEffectV2> initListener() {
        device.addCounterListener(counter -> sendEvent(MAGNET_COUNTER, counter));
        device.addMagneticFluxDensityListener(magneticFluxDensity -> sendEvent(MAGNET_DENSITY, magneticFluxDensity));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletHallEffectV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletHallEffectV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickletHallEffectV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletHallEffectV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletHallEffectV2> refreshPeriod(final int milliseconds) {
        try {
            device.setCounterConfig(HIGH_THRESHOLD, LOW_THRESHOLD, DEBOUNCE);

            if (milliseconds < 1) {
                device.setCounterCallbackConfiguration(256, false);
                device.setMagneticFluxDensityCallbackConfiguration(256, false, 'x', 0, 0);
            } else {
                device.setCounterCallbackConfiguration(milliseconds, true);
                device.setMagneticFluxDensityCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }

            sendEvent(MAGNET_DENSITY, device.getMagneticFluxDensity());
            sendEvent(MAGNET_COUNTER, device.getCounter(true));
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
