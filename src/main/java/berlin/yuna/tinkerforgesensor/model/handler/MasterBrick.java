package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.Device;

import java.util.concurrent.atomic.AtomicBoolean;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE_USB;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MasterBrick extends SensorHandler<BrickMaster> {


    public MasterBrick(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickMaster> send(Object value) {
        return this;
    }

    public SensorHandler<BrickMaster> init() {
        config.put(THRESHOLD_PREFIX + CURRENT, 128);
        config.put(THRESHOLD_PREFIX + VOLTAGE_USB, 128);
        config.put(THRESHOLD_PREFIX + VOLTAGE, 128);
        device.addStackCurrentListener(value -> sendEvent(CURRENT, value));
        device.addStackVoltageListener(value -> sendEvent(VOLTAGE, value));
        device.addUSBVoltageListener(value -> sendEvent(VOLTAGE_USB, value));
        return setRefreshPeriod(1000);
    }

    public SensorHandler<BrickMaster> initConfig() {
        handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.isStatusLEDEnabled() ? 1 : 0);
            config.put(CONFIG_INFO_LED_STATUS, isWifi2Present() && device.isWifi2StatusLEDEnabled() ? 1 : 0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickMaster> runTest() {
        return super.animateStatuesLed();
    }

    @Override
    public SensorHandler<BrickMaster> setStatusLedHandler(final int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_LED_STATUS, 1, device::enableStatusLED);
        } else if (value == LED_OFF.bit) {
            applyOnNewValue(CONFIG_LED_STATUS, 0, device::disableStatusLED);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickMaster> triggerFunctionA(int value) {
        if (isWifi2Present()) {
            if (isLedOn(value)) {
                applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, device::enableWifi2StatusLED);
            } else if (value == LED_OFF.bit) {
                applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, device::disableWifi2StatusLED);
            }
        }
        return this;
    }

    public SensorHandler<BrickMaster> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> {
            if (milliseconds < 1) {
                device.setStackCurrentCallbackPeriod(0);
                device.setStackVoltageCallbackPeriod(0);
                device.setUSBVoltageCallbackPeriod(0);
            } else {
                device.setStackCurrentCallbackPeriod(milliseconds);
                device.setStackVoltageCallbackPeriod(milliseconds);
                device.setUSBVoltageCallbackPeriod(milliseconds);
            }
        });
        return this;
    }

    private boolean isWifi2Present() {
        final AtomicBoolean result = new AtomicBoolean(false);
        handleConnection(() -> result.set(device.isWifi2Present()));
        return result.get();
    }
}
