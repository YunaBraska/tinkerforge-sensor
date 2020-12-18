package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.util.SegmentFormatter;
import berlin.yuna.tinkerforgesensor.util.SegmentsV2;
import com.tinkerforge.BrickletSegmentDisplay4x7V2;
import com.tinkerforge.Device;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_ON;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static java.time.format.DateTimeFormatter.ofPattern;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DisplaySegmentV2 extends SensorHandler<BrickletSegmentDisplay4x7V2> {

    private static final int DIGIT_LIMIT = 4;
    private Object lastText = ' ';
    private DateTimeFormatter timeFormatter = ofPattern("HH:mm");

    public DisplaySegmentV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> init() {
        config.put(CONFIG_BRIGHTNESS, 5);
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> setRefreshPeriod(int milliseconds) {
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> initConfig() {
        handleConnection(() -> {
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_BRIGHTNESS, device.getBrightness() > 0 ? 1 : 0);
            config.put(CONFIG_FUNCTION_A, config.get(CONFIG_BRIGHTNESS));
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> runTest() {
        int statusBefore = config.getOrDefault(CONFIG_LED_STATUS, LED_ON.bit).intValue();
        int brightnessBefore = getBrightness();
        handleConnection(() -> {
            setBrightness(7);
            send("1.2.:3.‘4.");
            Thread.sleep(128);
            for (int i = 0; i < 14; i++) {
                setBrightness(i > 7 ? 7 - (i - 7) : i);
                if (i % 4 == 0) {
                    setStatusLed(LED_ON.bit);
                } else {
                    setStatusLed(LED_OFF.bit);
                }
                send(LocalDateTime.now());
                Thread.sleep(64);
            }
            send(" ");
            setBrightness(brightnessBefore);
            setStatusLed(statusBefore);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> triggerFunctionA(int value) {
        if (value > -1 && value < 8) {
            applyOnNewValue(CONFIG_BRIGHTNESS, value, () -> {
                config.put(CONFIG_BRIGHTNESS, value);
                config.put(CONFIG_FUNCTION_A, value);
                handleConnection(() -> device.setBrightness(value));
                if (value == 0) {
                    send(" ");
                }
            });
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7V2> send(Object value) {
        handleConnection(() -> {
            final SegmentFormatter sf = new SegmentFormatter(value, lastText, timeFormatter, DIGIT_LIMIT);
            final boolean[][] digits = SegmentsV2.toSegments(sf.getDigits(), sf.getDots(), DIGIT_LIMIT);
            final boolean[] colons;
            timeFormatter = sf.getDateTimeFormatter();
            lastText = getBrightness() == 0 ? lastText : value;

            if (value instanceof TemporalAccessor) {
                if (sf.isBlinkColon()) {
                    colons = (((LocalDateTime) value).getSecond() % 2 == 0) ? new boolean[]{false, false} : new boolean[]{true, true};
                } else {
                    digits[1][7] = true;
                    colons = new boolean[]{false, false};
                }
            } else {
                colons = sf.getColons();
            }

            device.setSegments(digits[0], digits[1], digits[2], digits[3], colons, sf.isTick());
        });
        return this;
    }
}
