package berlin.yuna.tinkerforgesensor.model.sensor.brick;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import com.tinkerforge.BrickDC;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.EMERGENCY_SHUTDOWN;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.VOLTAGE;

public class DC extends Sensor<BrickDC> {

    public DC(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickDC) device, uid, true);
    }

    @Override
    protected Sensor<BrickDC> initListener() {
        device.addEmergencyShutdownListener(() -> sendEvent(EMERGENCY_SHUTDOWN, 1L));
        device.addCurrentVelocityListener(value -> sendEvent(CURRENT, (long) value));
        device.addUnderVoltageListener(value -> sendEvent(VOLTAGE, (long) value));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickDC> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickDC> ledStatus(final Integer value) {
        try {
            if (value == LED_STATUS_ON.bit) {
                device.enableStatusLED();
            } else if (value == LED_STATUS_OFF.bit) {
                device.disableStatusLED();
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickDC> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickDC> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setCurrentVelocityPeriod(0);
                sendEvent(CURRENT, (long) device.getVelocity());
            } else {
                device.setCurrentVelocityPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
