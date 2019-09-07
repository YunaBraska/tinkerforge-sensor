package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.util.SegmentFormatter;
import berlin.yuna.tinkerforgesensor.util.SegmentsV2;
import com.tinkerforge.BrickletSegmentDisplay4x7V2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * <h3>{@link DisplaySegmentV2}</h3><br />
 * <i>Four 7-segment displays with switchable colon</i><br />
 *
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Segment_Display_4x7_V2.html">Official documentation</a></li>
 * <li><a href="https://en.wikichip.org/wiki/seven-segment_display/representing_letters">Representing Letters</a></li>
 * </ul>
 * <h6>Send text to display</h6>
 * <code>sensor.send("1.2.:3.‘4.");</code>
 * <h6>Send current time</h6>
 * <i>(use {@link TemporalAccessor})</i><br/>
 * <code>sensor.send(LocalDateTime#now());</code>
 * <h6>Send own time format</h6>
 * <i>(use {@link DateTimeFormatter})</i><br/>
 * <code>sensor.send(DateTimeFormatter.ofPattern("HH:mm"));</code>
 * <h6>LED Brightness (2-9)</h6>
 * <code>sensor.ledAdditional(7);</code>
 * <h6>Display ON</h6>
 * <code>sensor.ledAdditional_setOn;</code>
 */
public class DisplaySegmentV2 extends Sensor<BrickletSegmentDisplay4x7V2> {

    private static final int DIGIT_LIMIT = 4;
    private int brightness = -1;
    private Object lastText = ' ';
    private DateTimeFormatter timeFormatter = ofPattern("HH:mm");

    public DisplaySegmentV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletSegmentDisplay4x7V2) device, uid);
    }

    @Override
    protected Sensor<BrickletSegmentDisplay4x7V2> initListener() {
        setBrightness(5);
        return this;
    }

    @Override
    public Sensor<BrickletSegmentDisplay4x7V2> send(final Object value) {
        try {
            final SegmentFormatter sf = new SegmentFormatter(value, lastText, timeFormatter, DIGIT_LIMIT);
            final boolean[][] digits = toSegments(sf.getDigits(), sf.getDots());
            final boolean[] colons;
            timeFormatter = sf.getDateTimeFormatter();
            lastText = brightness == 0 ? lastText : value;

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
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private boolean[][] toSegments(final char[] chars, final boolean[] dots) {
        final boolean[][] result = new boolean[DIGIT_LIMIT][8];
        for (int i = 0; i < DIGIT_LIMIT; i++) {
            result[i] = SegmentsV2.get(chars[i]);
            result[i][7] = dots[i];
        }
        return result;
    }

    @Override
    public Sensor<BrickletSegmentDisplay4x7V2> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.setStatusLEDConfig((short) LED_STATUS_OFF.bit);
            } else if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.setStatusLEDConfig((short) LED_STATUS_ON.bit);
            } else if (value == LED_STATUS_HEARTBEAT.bit) {
                ledStatus = LED_STATUS_HEARTBEAT;
                device.setStatusLEDConfig((short) LED_STATUS_HEARTBEAT.bit);
            } else if (value == LED_STATUS.bit) {
                ledStatus = LED_STATUS;
                device.setStatusLEDConfig((short) LED_STATUS.bit);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletSegmentDisplay4x7V2> ledAdditional(final Integer value) {
        if (value == LED_ADDITIONAL_ON.bit) {
            setBrightness(7);
            send(lastText);
        } else if (value == LED_ADDITIONAL_OFF.bit) {
            setBrightness(0);
            send(lastText);
            send("");
        } else {
            setBrightness(value - 2);
        }
        return this;
    }

    @Override
    public Sensor<BrickletSegmentDisplay4x7V2> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_ADDITIONAL_OFF;
        return this;
    }

    @Override
    public Sensor<BrickletSegmentDisplay4x7V2> flashLed() {
        try {
            ledAdditional_setOn();
            send("1.2.:3.‘4.");
            Thread.sleep(128);
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0) {
                    this.ledStatus_setOn();
                } else {
                    this.ledStatus_setOff();
                }
                ledAdditional(i);
                send(LocalDateTime.now());
                Thread.sleep(128);
            }
            send(" ");
            ledAdditional_setOff();
            ledAdditional(5);
        } catch (Exception ignore) {
        }
        return this;
    }

    private void setBrightness(final int value) {
        try {
            if (value >= 0 && value <= 7 && brightness != value) {
                brightness = value;
                device.setBrightness(value - 2);
                send(brightness == 0 ? "" : lastText);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    @Override
    public Sensor<BrickletSegmentDisplay4x7V2> refreshPeriod(final int milliseconds) {
        return this;
    }
}
