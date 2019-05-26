package berlin.yuna.tinkerforgesensor.model.sensor.brick;

import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
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
    protected Sensor<BrickDC> initListener() throws TimeoutException, NotConnectedException {
        device.setCurrentVelocityPeriod((int) CALLBACK_PERIOD);
        device.addEmergencyShutdownListener(() -> sendEvent(EMERGENCY_SHUTDOWN, 1L));
        device.addCurrentVelocityListener(value -> sendEvent(CURRENT, (long) value));
        device.addUnderVoltageListener(value -> sendEvent(VOLTAGE, (long) value));
////                current.getChipTemperature();
////                current.getCurrentConsumption();
////                current.getCurrentVelocity();
////                current.getExternalInputVoltage();
////                current.getMinimumVoltage();
////                current.getPWMFrequency();
////                current.getVelocity();
////                current.getDriveMode();
        return this;
    }

    @Override
    public Sensor<BrickDC> value(Object value) {
        return this;
    }

    @Override
    public Sensor<BrickDC> ledStatus(Integer value) {
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
    public Sensor<BrickDC> ledAdditional(Integer value) {
        return this;
    }
}
