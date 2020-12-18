package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.util.SegmentFormatter;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.Device;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.util.Segments.toSegments;
import static java.time.format.DateTimeFormatter.ofPattern;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DisplaySegment extends SensorHandler<BrickletSegmentDisplay4x7> {

    private static final int DIGIT_LIMIT = 4;
    private Object lastText = ' ';
    private DateTimeFormatter timeFormatter = ofPattern("HH:mm");

    public DisplaySegment(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> init() {
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> setRefreshPeriod(int milliseconds) {
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> initConfig() {
        handleConnection(() -> {
            config.put(CONFIG_BRIGHTNESS, 7);
            config.put(CONFIG_FUNCTION_A, config.get(CONFIG_BRIGHTNESS));
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> runTest() {
        handleConnection(() -> {
            setBrightness(7);
            send("1.2.:3.‘4.");
            Thread.sleep(128);
            for (int i = 0; i < 14; i++) {
                setBrightness(i > 7 ? 7 - (i - 7) : i);
                send(LocalDateTime.now());
                Thread.sleep(64);
            }
            send(" ");
            setBrightness(0);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> setStatusLedHandler(final int value) {
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> triggerFunctionA(int value) {
        if (value > -1 && value < 8) {
            applyOnNewValue(CONFIG_BRIGHTNESS, value, () -> {
                config.put(CONFIG_BRIGHTNESS, value);
                config.put(CONFIG_FUNCTION_A, getBrightness());
                if (value == 0) {
                    send(" ");
                }
            });
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletSegmentDisplay4x7> send(final Object value) {
        handleConnection(() -> {
            final SegmentFormatter sf = new SegmentFormatter(value, lastText, timeFormatter, DIGIT_LIMIT);
            final short[] digits = toSegments(sf.getDigits(), DIGIT_LIMIT);
            final boolean colon;
            timeFormatter = sf.getDateTimeFormatter();
            lastText = getBrightness() == 0 ? lastText : value;

            if (value instanceof TemporalAccessor) {
                if (sf.isBlinkColon()) {
                    colon = ((LocalDateTime) value).getSecond() % 2 != 0;
                } else {
                    colon = false;
                }
            } else {
                colon = sf.getColons()[0];
            }

            device.setSegments(digits, (short) getBrightness(), colon);
            lastText = getBrightness() == 0? lastText : value;
        });
        return this;
    }
}
