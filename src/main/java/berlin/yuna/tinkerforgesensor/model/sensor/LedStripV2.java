package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.Color;
import berlin.yuna.tinkerforgesensor.model.type.LedChipType;
import com.tinkerforge.BrickletLEDStripV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.Color.BLACK;
import static berlin.yuna.tinkerforgesensor.model.type.Color.GREEN;
import static berlin.yuna.tinkerforgesensor.model.type.Color.RAINBOW;
import static berlin.yuna.tinkerforgesensor.model.type.Color.toRGB;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;
import static java.util.Collections.nCopies;

/**
 * <h3>{@link LedStripV2}</h3><br>
 * <i>Controls up to 2048 RGB(W) LEDs</i><br>
 *
 * <h3>Before the start</h3>
 * <i>This bricklet is not starting without knowing the number of LEDs and the ChipType</i><br>
 * <h6>[Setup] Setting number of leds to 30 and chip type to "WS2812" {@link LedChipType}</h6>
 * <code>sensor.send('C', 30, "WS2812");</code>
 * <code>sensor.send('C', 30, LED_TYPE_WS2812);</code>
 *
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/LED_Strip_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Set led 1 to magenta</h6>
 * <code>sensor.send(Color.Magenta);</code>
 * <h6>Set led 1 to magenta and led 2 to green</h6>
 * <code>sensor.send(Color.Magenta, Color.Green);</code>
 * <h6>Set led 4 to red</h6>
 * <code>sensor.send(4, Color.Red);</code>
 * <h6>Set all led's to black/off</h6>
 * <code>sensor.send(-1, Color.Black);</code>
 * <h6>Set all led's to Black but don't update</h6>
 * <code>sensor.send(false, -1, Color.Black);</code>
 * <h6>[Config] Set the refresh period of the led's</h6>
 * <code>sensor.refreshLimit(30);</code>
 */
public class LedStripV2 extends Sensor<BrickletLEDStripV2> {

    private LedChipType chipType;
    private final List<Integer> ledList = new ArrayList<>();

    public LedStripV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletLEDStripV2) device, uid);
    }

    @Override
    protected Sensor<BrickletLEDStripV2> initListener() {
        refreshPeriod(50);
        return this;
    }

    @Override
    public Sensor<BrickletLEDStripV2> send(final Object value) {
        return send(1, value);
    }

    @Override
    public Sensor<BrickletLEDStripV2> send(final Object... values) {
        if (values.length > 0 && values[0] instanceof Boolean) {
            if (values.length == 1 && ((Boolean) values[0])) {
                update();
            }
            return process(((Boolean) values[0]), copyOfRange(values, 1, values.length));
        }
        return process(true, values);
    }

    public Sensor<BrickletLEDStripV2> process(final boolean update, final Object... values) {
        if (values.length > 2 && values[0] instanceof Character && values[1] instanceof Number) {
            //[LedNumber, chipType]
            setChipTypeAndLedNumber(((Number) values[1]), values[2]);
        } else if (everyTypeIsColorAwt(values)) {
            //[awt.Color...] to [Color...]
            process(update, stream(values).map(Color::toColor).toArray());
        } else if (everyTypeIsColor(values)) {
            //[Color...] to [Index, Rgb...]
            process(update, addIndexToColor(values));
        } else if (containsNumbers(values) && (containsColor(values) || containsColorAwt(values))) {
            //[Index, Color...] to [Index Rgb...]
            process(update, stream(values).map(Color::toRGB).toArray());
        } else if (values.length > 1 && stream(values).allMatch(v -> v instanceof Number)) {
            //[Index, Rgb...] Take Action
            setLeds(update, stream(values).mapToInt(o -> ((Number) o).intValue()).toArray());
        }
        return this;
    }

    @Override
    public Sensor<BrickletLEDStripV2> setLedStatus(final Integer value) {
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
    public Sensor<BrickletLEDStripV2> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletLEDStripV2> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_ADDITIONAL_OFF;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletLEDStripV2> refreshPeriod(final int milliseconds) {
        try {
            device.setFrameDuration(milliseconds);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private Object[] addIndexToColor(final Object[] values) {
        final List<Object> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            result.add(i);
            result.add(toRGB(values[0]).orElse(0));
        }
        return result.toArray();
    }

    private boolean everyTypeIsColorAwt(final Object[] values) {
        return stream(values).allMatch(v -> v instanceof java.awt.Color);
    }

    private boolean everyTypeIsColor(final Object[] values) {
        return stream(values).allMatch(v -> v instanceof Color);
    }

    private boolean containsColorAwt(final Object[] values) {
        return stream(values).anyMatch(v -> v instanceof java.awt.Color);
    }

    private boolean containsColor(final Object[] values) {
        return stream(values).anyMatch(v -> v instanceof Color);
    }

    private boolean containsNumbers(final Object[] values) {
        return stream(values).anyMatch(v -> v instanceof Number);
    }

    private void setChipTypeAndLedNumber(final Number LedNumber, final Object chipTypeObj) {
        final Optional<LedChipType> chipType = getChipType(chipTypeObj);
        if (chipType.isPresent()) {
            if (chipType.get() != this.chipType) {
                setChipType(chipType.get());
            }
            if(LedNumber.intValue() != ledList.size()) {
                setLedNumber(LedNumber.intValue());
            }
        }
    }

    private Optional<LedChipType> getChipType(final Object value) {
        if (value instanceof String) {
            final String chipType = (String) value;
            return stream(LedChipType.values()).filter(lt -> lt.name().toLowerCase().contains((chipType).toLowerCase())).findFirst();
        } else if (value instanceof LedChipType) {
            return Optional.of((LedChipType) value);
        }
//        else if (value instanceof Number) {
//            final Integer chipType = (Integer) value;
//            return stream(LedChipType.values()).filter(lt -> lt.value() == chipType).findFirst().orElse(null);
//    }
        return Optional.empty();
    }

    private void setChipType(final LedChipType ledChipType) {
        try {
            this.chipType = ledChipType;
            device.setChipType(ledChipType.value());
            device.setChannelMapping(ledChipType.channel());
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void setLedNumber(final int ledNumber) {
        final List<Integer> rainbow = new ArrayList<>();
        do {
            rainbow.addAll(stream(RAINBOW).boxed().collect(Collectors.toList()));
        } while (rainbow.size() < ledNumber);
        ledList.clear();
        ledList.addAll(new ArrayList<>(nCopies(ledNumber, BLACK)));
        try {
            for (int i = 0; i < ledList.size(); i++) {
                send(i, GREEN);
                if (i != 0) {
                    send(i - 1, rainbow.get(i * (rainbow.size() / 150)));
                }
                Thread.sleep(4);
            }
            ledList.clear();
            ledList.addAll(new ArrayList<>(nCopies(ledNumber, BLACK)));
            update();
        } catch (InterruptedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void setLed(final int index, final int color) {
        if (index >= 0 && index < ledList.size()) {
            ledList.set(index, color);
        }
    }

    private Sensor<BrickletLEDStripV2> setLeds(final boolean update, final int[] colors) {
        //[-1, AllInt] Take Action
        if (((Number) colors[0]).intValue() == -1) {
            final int ledNumber = ledList.size();
            ledList.clear();
            ledList.addAll(new ArrayList<>(nCopies(ledNumber, ((Number) colors[1]).intValue())));
            if (update) {
                update();
            }
            return this;
        } else {
            //[Int, Int...] Take Action
            for (int i = 0; i < colors.length; i = i + 2) {
                setLed(((Number) colors[i]).intValue() - 1, ((Number) colors[i + 1]).intValue());
            }
            if (update) {
                update();
            }
            return this;
        }
    }

    private void update() {
        try {
            device.setLEDValues(0, ledList.stream().map(this::mapColor).flatMapToInt(Arrays::stream).toArray());
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private int[] mapColor(final int rgb) {
        return mapColor(new Color(rgb));
    }

    private int[] mapColor(final Color color) {
        final int[] result = new int[chipType.mapping().length];
        result[0] = color.getRed();
        result[1] = color.getGreen();
        result[2] = color.getBlue();
        if (result.length > 3) {
            //TODO: how to get brightness from color?
            result[3] = color.getAlpha();
        }
        return result;
    }
}
