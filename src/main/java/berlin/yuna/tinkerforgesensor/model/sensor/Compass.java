package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletCompass;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNET_HEADING;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_X;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.MAGNETIC_Z;

/**
 * <h3>{@link Compass}</h3><br>
 * <i>3-axis compass with 0.1mG (milli Gauss) and 0.1° resolution</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#MAGNETIC_X} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Y} [x = number]</li>
 * <li>{@link ValueType#MAGNETIC_Z} [x = number]</li>
 * <li>{@link ValueType#MAGNET_HEADING} [x / 10 = °]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Compass.html#compass-bricklet">Official documentation</a></li>
 * </ul>
 * <h6>Getting magnetic x position</h6>
 * <code>sensor.values().magneticX();</code>
 * <h6>Getting position in ° (0° to 360°) (0° = north 90° = east)</h6>
 * <code>sensor.values().heading() / 10;</code>
 */
public class Compass extends Sensor<BrickletCompass> {

    public Compass(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletCompass) device, uid);
    }

    @Override
    protected Sensor<BrickletCompass> initListener() {
        device.addHeadingListener(heading -> sendEvent(MAGNET_HEADING, heading));
        device.addMagneticFluxDensityListener((x, y, z) ->
                {
                    sendEvent(MAGNETIC_X, x);
                    sendEvent(MAGNETIC_Y, y);
                    sendEvent(MAGNETIC_Z, z);
                }

        );
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletCompass> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletCompass> setLedStatus(final Integer value) {
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
    public Sensor<BrickletCompass> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletCompass> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletCompass> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setHeadingCallbackConfiguration(4, false, 'x', 0, 0);
                device.setMagneticFluxDensityCallbackConfiguration(4, false);
            } else {
                device.setHeadingCallbackConfiguration(milliseconds, true, 'x', 0, 0);
                device.setMagneticFluxDensityCallbackConfiguration(milliseconds, true);
            }

            final BrickletCompass.MagneticFluxDensity magneticFluxDensity = device.getMagneticFluxDensity();
            sendEvent(MAGNETIC_X, magneticFluxDensity.x);
            sendEvent(MAGNETIC_Y, magneticFluxDensity.y);
            sendEvent(MAGNETIC_Z, magneticFluxDensity.z);
            sendEvent(MAGNET_HEADING, device.getHeading());
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
