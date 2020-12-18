package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.TILT;

/**
 * <li>{@link ValueType#TILT} [0/1/2 = closed/open/vibrating]</li>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Tilt extends SensorHandler<BrickletTilt> {

    public Tilt(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletTilt> send(final Object value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTilt> init() {
        device.addTiltStateListener(value -> sendEvent(TILT, value));
        return setRefreshPeriod(1000);
    }

    @Override
    public SensorHandler<BrickletTilt> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletTilt> runTest() {
        return this;
    }

    @Override
    public SensorHandler<BrickletTilt> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTilt> triggerFunctionA(int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletTilt> setRefreshPeriod(final int milliseconds) {
        handleConnection(device::enableTiltStateCallback);
        return this;
    }

}
