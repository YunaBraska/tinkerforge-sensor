package berlin.yuna.tinkerforgesensor.model.driver.brick;

import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.generator.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.VOLTAGE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.VOLTAGE_USB;

/**
 * Basis to build stacks and has 4 Bricklet ports
 */
public class Master extends Sensor<BrickMaster> {

    public Master(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickMaster) device, parent, uid, true);
    }

    @Override
    protected Sensor<BrickMaster> initListener() throws TimeoutException, NotConnectedException {
        device.addStackCurrentListener(value -> sendEvent(CURRENT, (long) value));
        device.addStackVoltageListener(value -> sendEvent(VOLTAGE, (long) value));
        device.addUSBVoltageListener(value -> sendEvent(VOLTAGE_USB, (long) value));
        device.setStackCurrentCallbackPeriod(CALLBACK_PERIOD * 8);
        device.setStackVoltageCallbackPeriod(CALLBACK_PERIOD * 8);
        device.setUSBVoltageCallbackPeriod(CALLBACK_PERIOD * 8);
        return this;
    }

    @Override
    public Sensor<BrickMaster> value(Object value) {
        return this;
    }

    @Override
    public Sensor<BrickMaster> ledStatus(Integer value) {
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
    public Sensor<BrickMaster> ledAdditional(Integer value) {
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
}
