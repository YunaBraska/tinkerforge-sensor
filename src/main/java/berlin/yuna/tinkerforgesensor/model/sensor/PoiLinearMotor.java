package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletIO16V2;
import com.tinkerforge.BrickletMotorizedLinearPoti;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.PERCENTAGE;
import static com.tinkerforge.BrickletMotorizedLinearPoti.DRIVE_MODE_SMOOTH;

/**
 * <h3>{@link PoiLinearMotor}</h3><br>
 * <i>Motorized Linear Potentiometer</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#PERCENTAGE} [x = %]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motorized_Linear_Poti.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting position in % (0-100)</h6>
 * <code>sensor.values().percentage();</code>
 * <h6>Set position</h6>
 * <code>sensor.send(69);</code>
 * <h6>Hold position</h6>
 * <code>sensor.send(true);</code>
 */
public class PoiLinearMotor extends Sensor<BrickletMotorizedLinearPoti> {

    private int position = 0;
    private boolean holdPosition = false;

    public PoiLinearMotor(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletMotorizedLinearPoti) device, uid);
    }

    @Override
    protected Sensor<BrickletMotorizedLinearPoti> initListener() {
        device.addPositionListener(value -> sendEvent(PERCENTAGE, value, true));
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletMotorizedLinearPoti> send(final Object value) {
        try {
            if (value instanceof Boolean) {
                final Boolean holdPosition = (Boolean) value;
                if (this.holdPosition != holdPosition) {
                    this.holdPosition = holdPosition;
                    device.setMotorPosition(position, DRIVE_MODE_SMOOTH, this.holdPosition);
                }
            } else if (value instanceof Number) {
                final int position = ((Number) value).intValue();
                if (this.position != position) {
                    this.position = position;
                    device.setMotorPosition(position, DRIVE_MODE_SMOOTH, holdPosition);
                }
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotorizedLinearPoti> setLedStatus(final Integer value) {
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
    public Sensor<BrickletMotorizedLinearPoti> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletMotorizedLinearPoti> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotorizedLinearPoti> flashLed() {
        try {
            send(50);
            Thread.sleep(256);
            send(100);
            Thread.sleep(256);
            send(0);
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletMotorizedLinearPoti> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setPositionCallbackConfiguration(4, false, 'x', 0, 0);
            } else {
                device.setPositionCallbackConfiguration(milliseconds, true, 'x', 0, 0);
            }
            position = device.getPosition();
            sendEvent(PERCENTAGE, (long) position, true);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
