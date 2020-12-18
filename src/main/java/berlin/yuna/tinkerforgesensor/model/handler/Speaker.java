package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.Beep;
import berlin.yuna.tinkerforgesensor.util.ThreadUtil;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP_FINISH;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Speaker extends SensorHandler<BrickletPiezoSpeaker> {

    public Speaker(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> send(final Object value) {
        handleConnection(() -> {
            if (value instanceof Beep) {
                final Beep beep = (Beep) value;
                sendBeep(beep.getDurationMs(), isInRange(beep.getFrequency()) ? beep.getFrequency() : getFrequency(), beep.getWait());
            } else if (value instanceof Number) {
                sendBeep(((Number) value).intValue(), getFrequency(), false);
            }
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> init() {
        setConfig(CONFIG_FREQUENCY, 2000);
        handleConnection(() -> device.addBeepFinishedListener(() -> {
            sendEvent(BEEP, 0L);
            sendEvent(BEEP_FINISH, 0L);
        }));
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> initConfig() {
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> runTest() {
        handleConnection(() -> {
            for (int i = 600; i < 2000; i++) {
                sensor.send(new Beep(50, i, 1, false));
            }
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> triggerFunctionA(int value) {
        if (isInRange(value)) {
            setConfig(CONFIG_FREQUENCY, value);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeaker> setRefreshPeriod(final int milliseconds) {
        return this;
    }

    private void sendBeep(final int durationMs, final int frequency, final boolean wait) {
        handleConnection(() -> device.beep(durationMs, frequency));
        if (wait) {
            ThreadUtil.sleep(durationMs);
        }
    }

    private boolean isInRange(int value) {
        return value > 585 && value < 7100;
    }
}
