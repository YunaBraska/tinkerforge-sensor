package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickDC;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.CURRENT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.EMERGENCY_SHUTDOWN;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DcBrick extends SensorHandler<BrickDC> {

    public DcBrick(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickDC> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickDC> init() {
        device.addEmergencyShutdownListener(() -> sendEvent(EMERGENCY_SHUTDOWN, 1L));
        device.addCurrentVelocityListener(value -> sendEvent(CURRENT, value));
        device.addUnderVoltageListener(value -> sendEvent(VOLTAGE, value));
        return setRefreshPeriod(64);
    }

    @Override
    public SensorHandler<BrickDC> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.isStatusLEDEnabled() ? LedStatusType.LED_ON.bit : LedStatusType.LED_OFF.bit));
        return this;
    }

    @Override
    public SensorHandler<BrickDC> runTest() {
        super.animateStatuesLed();
        return this;
    }

    @Override
    public SensorHandler<BrickDC> setStatusLedHandler(final int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_LED_STATUS, 1, device::enableStatusLED);
        } else if (value == LedStatusType.LED_OFF.bit) {
            applyOnNewValue(CONFIG_LED_STATUS, 0, device::disableStatusLED);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickDC> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickDC> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> device.setCurrentVelocityPeriod(milliseconds < 1 ? 0 : milliseconds));
        return this;
    }

}
