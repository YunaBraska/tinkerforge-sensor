package berlin.yuna.tinkerforgesensor.model.sensor.brick;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.VOLTAGE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.VOLTAGE_USB;

/**
 * Basis to build stacks and has 4 Bricklet ports
 */
public class Master extends Sensor<BrickMaster> {

    public Master(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickMaster) device, uid, true);
    }

    @Override
    protected Sensor<BrickMaster> initListener() {
        device.addStackCurrentListener(value -> sendEvent(CURRENT, (long) value));
        device.addStackVoltageListener(value -> sendEvent(VOLTAGE, (long) value));
        device.addUSBVoltageListener(value -> sendEvent(VOLTAGE_USB, (long) value));
        refreshPeriod(1000);
        return this;
    }

    @Override
    public Sensor<BrickMaster> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickMaster> ledStatus(final Integer value) {
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
    public Sensor<BrickMaster> ledAdditional(final Integer value) {
        try {
            if (device.isWifi2Present()) {
                if (value == LED_ADDITIONAL_ON.bit) {
                    device.enableWifi2StatusLED();
                } else if (value == LED_ADDITIONAL_OFF.bit) {
                    device.disableWifi2StatusLED();
                }
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickMaster> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setStackCurrentCallbackPeriod(0);
                device.setStackVoltageCallbackPeriod(0);
                device.setUSBVoltageCallbackPeriod(0);
                sendEvent(CURRENT, (long) device.getStackCurrent());
                sendEvent(VOLTAGE, (long) device.getStackVoltage());
                sendEvent(VOLTAGE_USB, (long) device.getUSBVoltage());
            } else {
                device.setStackCurrentCallbackPeriod(milliseconds);
                device.setStackVoltageCallbackPeriod(milliseconds);
                device.setUSBVoltageCallbackPeriod(milliseconds);
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
