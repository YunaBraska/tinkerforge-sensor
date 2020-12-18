package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.Beep;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.util.ThreadUtil;
import com.tinkerforge.BrickletPiezoSpeakerV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BEEP_FINISH;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class SpeakerV2 extends SensorHandler<BrickletPiezoSpeakerV2> {

    public SpeakerV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> send(final Object value) {
        handleConnection(() -> {
            if (value instanceof Beep) {
                final Beep beep = (Beep) value;
                sendBeep(
                        beep.getDurationMs(),
                        isInRange(beep.getFrequency()) ? beep.getFrequency() : getFrequency(),
                        isInRangeVol(beep.getVolume()) ? beep.getVolume() : getVolume(),
                        beep.getWait()
                );
            } else if (value instanceof Number) {
                sendBeep(((Number) value).intValue(), getFrequency(), getVolume(), false);
            }
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> init() {
        setConfig(CONFIG_FREQUENCY, 2000);
        handleConnection(() -> device.addBeepFinishedListener(() -> {
            sendEvent(BEEP, 0L);
            sendEvent(BEEP_FINISH, 0L);
        }));
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> initConfig() {
        handleConnection(() -> config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig()));
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> runTest() {
        handleConnection(() -> {
            for (int i = 600; i < 2000; i++) {
                sensor.send(new Beep(50, i, 1, false));
            }
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> triggerFunctionA(int value) {
        if (isInRange(value)) {
            setConfig(CONFIG_FREQUENCY, value);
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletPiezoSpeakerV2> setRefreshPeriod(final int milliseconds) {
        return this;
    }

    private void sendBeep(final int durationMs, final int frequency, final int volume, final boolean wait) {
        handleConnection(() -> {
            device.setBeep(frequency, volume, durationMs);
            if (wait) {
                ThreadUtil.sleep(durationMs);
            }
        });
    }

    private boolean isInRange(int value) {
        return value > 50 && value < 15000;
    }

    private boolean isInRangeVol(int value) {
        return value > 0 && value < 10;
    }
}
