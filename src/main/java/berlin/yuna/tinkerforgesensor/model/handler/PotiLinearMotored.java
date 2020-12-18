package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletMotorizedLinearPoti;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.PERCENTAGE;
import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.sleep;
import static com.tinkerforge.BrickletMotorizedLinearPoti.DRIVE_MODE_SMOOTH;

/**
 * <li>{@link ValueType#PERCENTAGE} [x = %]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PotiLinearMotored extends SensorHandler<BrickletMotorizedLinearPoti> {

    public PotiLinearMotored(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> send(final Object value) {
        if (value instanceof Boolean) {
            final int holdPosition = ((boolean) value) ? 1 : 0;
            applyOnNewValue(CONFIG_POSITION_HOLD, holdPosition, () -> device.setMotorPosition(getConfig(CONFIG_POSITION).intValue(), DRIVE_MODE_SMOOTH, holdPosition == 1));
        } else if (value instanceof Number) {
            final int position = ((Number) value).intValue();
            applyOnNewValue(CONFIG_POSITION, position, () -> device.setMotorPosition(position, DRIVE_MODE_SMOOTH, getConfig(CONFIG_POSITION_HOLD).intValue() == 1));
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> init() {
        device.addPositionListener(value -> sendEvent(PERCENTAGE, value));
        return setRefreshPeriod(1);
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> initConfig() {
        handleConnection(() -> {
            final BrickletMotorizedLinearPoti.MotorPosition motorPosition = device.getMotorPosition();
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_POSITION, device.getPosition());
            config.put(CONFIG_POSITION_HOLD, motorPosition.holdPosition ? 1 : 0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> runTest() {
        int before = config.get(CONFIG_POSITION).intValue();
        int beforeHold = config.get(CONFIG_POSITION_HOLD).intValue();
        super.animateStatuesLed();
        send(50);
        sleep(256);
        send(100);
        sleep(256);
        send(0);
        sleep(256);
        send(before);
        send(beforeHold == 1);
        return this;
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletMotorizedLinearPoti> setRefreshPeriod(final int milliseconds) {
        handleConnection(() -> handleConnection(() -> device.setPositionCallbackConfiguration(milliseconds < 1 ? 4 : milliseconds, true, 'x', 0, 0)));
        return this;
    }

}
