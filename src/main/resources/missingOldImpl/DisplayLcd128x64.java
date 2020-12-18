package berlin.yuna.tinkerforgesensor.model.missing;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import com.tinkerforge.BrickletLCD128x64;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_ON;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.SensorUtils.ledStatusConfig;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.util.StringUtils.spaces;
import static berlin.yuna.tinkerforgesensor.util.StringUtils.utf16ToKS0066U;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * <h3>{@link DisplayLcd128x64}</h3>
 * <i>7.1cm (2.8") display with 128x64 pixel and touch screen</i>
 * <i>GUI elements are coming soon</i>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>TouchPosition (coming soon)</li>
 * <li>TouchGesture (coming soon)</li>
 * <li>GuiTabSelected (coming soon)</li>
 * <li>GuiSliver (coming soon)</li>
 * <li>GuiButton (coming soon)</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/LCD_128x64.html">Official documentation</a></li>
 * </ul>
 * <h6>Clear display</h6>
 * <code>sensor.send(true);</code>
 * <h6>Send text</h6>
 * <code>sensor.send("Howdy");</code>
 * <h6>Send text centered</h6>
 * <code>sensor.send("Line1 center", true);</code>
 * <h6>Send text centered on row 2</h6>
 * <code>sensor.send("Line2 center", true, 1);</code>
 * <h6>Send text on position 5 and row 3</h6>
 * <code>sensor.send("Line3 posX=5", 4, 2);</code>
 * <h6>Send text on position 9 and row 4 with font (0-9)</h6>
 * <code>sensor.send("Line4 posX=8 font=2", 8, 3, 2);</code>
 * <h6>Send text with dynamic spaces between)</h6>
 * <code>sensor.send("H ${s} O ${s} W ${s} D ${s} Y");</code>
 * <h6>LED Brightness (2-100)</h6>
 * <code>sensor.ledAdditional(7);</code>
 * <h6>Display ON</h6>
 * <code>sensor.ledAdditional_setOn;</code>
 */

public class DisplayLcd128x64 extends Sensor<BrickletLCD128x64> {

    public static final String DYNAMIC_SPACE = "${s}";
    private static final String SPLIT_LINE = System.lineSeparator();
    public static final int COLUMN_LIMIT = 128;
    public static final int ROW_LIMIT = 64;
    private BrickletLCD128x64.DisplayConfiguration config;
//    private final String[] cachedRows = new String[ROW_LIMIT];

    public DisplayLcd128x64(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletLCD128x64) device, uid);
    }

    @Override
    protected Sensor<BrickletLCD128x64> initListener() {
//        device.addGUIButtonPressedListener();
//        device.addGUISliderValueListener();
//        device.addGUITabSelectedListener();
//        device.addTouchGestureListener();
//        device.addTouchPositionListener();
        refreshPeriod(1);
        return this;
    }

    @Override
    public Sensor<BrickletLCD128x64> send(final Object... values) {
        final List<List<Object>> items = new ArrayList<>();
        List<Object> item = new ArrayList<>();
        for (Object value : values) {
            if (value instanceof String && !item.isEmpty()) {
                items.add(item);
                item = new ArrayList<>();
            }
            item.add(value);
        }
        items.add(item);
        items.stream().filter(i -> !i.isEmpty()).forEach(this::send);
        return this;
    }

    public void send(final List<Object> values) {
        if (values.get(0) instanceof Boolean) {
            clearDisplay();
            return;
        }

        //[0] == text
        final String text = (String) values.get(0);
        int posX = 0;
        int posY = 0;
        int font = 0;
        boolean center = false;

        //[1] == Number(X)/Boolean(Center)
        if (values.size() > 1 && values.get(1) instanceof Boolean) {
            posX = 0;
            center = true;
        } else if (values.size() > 1 && values.get(1) instanceof Number) {
            posX = ((Number) values.get(1)).intValue();
            center = false;
        }

        //[2] == Number(Y)
        if (values.size() > 2 && values.get(2) instanceof Number) {
            posY = ((Number) values.get(2)).intValue();
        }

        //[3] == Number(Font)
        if (values.size() > 3 && values.get(3) instanceof Boolean) {
            font = ((Number) values.get(3)).intValue();
        }

        //TODO: validate positions
        if (text != null && !text.trim().isEmpty()) {
            writeLines(posX, posY, text, font, center);
        }
    }

    @Override
    public Sensor<BrickletLCD128x64> send(final Object value) {
        return send(new Object[]{value});
    }

    @Override
    public Sensor<BrickletLCD128x64> setLedStatus(final Integer value) {
        return ledStatusConfig(this, value, result ->  device.setStatusLEDConfig((short) result.intValue()));
    }

    @Override
    public Sensor<BrickletLCD128x64> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ON.bit) {
                ledAdditional = LED_ON;
                config.backlight = 80;
            } else if (value == LED_OFF.bit) {
                ledAdditional = LED_STATUS_OFF;
                config.backlight = 0;
            } else {
                ledAdditional = LED_ON;
                config.backlight = (short) (value.shortValue() - 2);
            }
            device.setDisplayConfiguration(config.contrast, config.backlight, config.invert, config.automaticDraw);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletLCD128x64> initLedConfig() {
        try {
            ledStatus = LedStatusType.ledStatusTypeOf(device.getStatusLEDConfig());
            ledAdditional = LED_OFF;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletLCD128x64> flashLed() {
        try {
            for (int i = 0; i < 9; i++) {
                ledAdditional((i + 1) * 10);
                send("H ${s} O ${s} W ${s} D ${s} Y [" + i + "]");
                send(ofPattern("hh:mm:ss").format(LocalDateTime.now()), true, 1);
                send(DYNAMIC_SPACE + UUID.randomUUID() + DYNAMIC_SPACE, 0, 2);
                Thread.sleep(128);
            }
            send(true);
            ledAdditional_setOff();
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletLCD128x64> refreshPeriod(final int milliseconds) {
        try {
            config = device.getDisplayConfiguration();
            if (milliseconds < 1) {
                device.setTouchGestureCallbackConfiguration(1000, false);
                device.setTouchPositionCallbackConfiguration(1000, false);
                device.setGUIButtonPressedCallbackConfiguration(1000, false);
                device.setGUISliderValueCallbackConfiguration(1000, false);
                device.setGUITabSelectedCallbackConfiguration(1000, false);
            } else {
                device.setTouchGestureCallbackConfiguration(milliseconds, true);
                device.setTouchPositionCallbackConfiguration(milliseconds, true);
                device.setGUIButtonPressedCallbackConfiguration(milliseconds, true);
                device.setGUISliderValueCallbackConfiguration(milliseconds, true);
                device.setGUITabSelectedCallbackConfiguration(milliseconds, true);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private Sensor<BrickletLCD128x64> clearDisplay() {
        try {
            device.clearDisplay();
        } catch (TinkerforgeException e) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private void writeLines(final int posX, final int posY, final String text, final int font, final boolean center) {
        int y = posY;
        final String[] lines = text.split(SPLIT_LINE);

        String leftOverText = "";
        for (String line : lines) {
            line += leftOverText;
            line = spaceUp(line, font);
            leftOverText = "";
            final int charLimit = (COLUMN_LIMIT / getPosX(1, font));
            if (line.length() > charLimit) {
                leftOverText = line.substring(charLimit);
                line = line.substring(0, charLimit);
            } else if (center) {
                line = centerLine(line, font);
            }
            sendToDisplay((short) posX, (short) y, utf16ToKS0066U(line), font);
            y++;
        }

        if (!leftOverText.isEmpty()) {
            writeLines(posX, y, leftOverText, font, center);
        }
    }

    private String centerLine(final String line, final int font) {
        return spaceUp(DYNAMIC_SPACE + line + DYNAMIC_SPACE, font);
    }

    private void sendToDisplay(final short x, final short y, final String line, final int font) {
        try {
            final int posX = getPosX(x, font);
            final int posY = getPosY(y, font);
//            if (!line.equals(cachedRows[y])) {
//                cachedRows[y] = line;
            if (font < 0) {
                device.writeLine(posY, posX, line);
            } else {
                device.drawText(posX, posY, font, true, line);
//                }
            }
        } catch (TinkerforgeException e) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private static String spaceUp(final String line, final int font) {
        String text = line;
        final int charLimit = (COLUMN_LIMIT / getPosX(1, font));
        if (text.contains(DYNAMIC_SPACE)) {
            int spaceUps;
            while ((spaceUps = ("splitStart" + text + "splitEnd").split("\\$\\{s}").length - 1) > 0) {
                final int length = text.length() - ((DYNAMIC_SPACE).length() * spaceUps);
                text = text.replaceFirst("\\$\\{s}", spaces((charLimit - length) / spaceUps));
            }
        }
        return text;
    }

    private static int getPosX(final int x, final int font) {
        switch (font) {
            case 0:
                //FONT_6X8
                return 6 * x;
            case 1:
                //FONT_6X16
                return 6 * x;
            case 2:
                //FONT_6X24
                return 6 * x;
            case 3:
                //FONT_6X32
                return 6 * x;
            case 4:
                //FONT_12X16
                return 12 * x;
            case 5:
                //FONT_12X24
                return 12 * x;
            case 6:
                //FONT_12X32
                return 12 * x;
            case 7:
                //FONT_18X24
                return 18 * x;
            case 8:
                //FONT_18X32
                return 18 * x;
            case 9:
                //FONT_24X32
                return 24 * x;
            default:
                //FONT_6X8
                return 6 * x;
        }
    }

    private static int getPosY(final int y, final int font) {
        switch (font) {
            case 0:
                //FONT_6X8
                return 8 * y;
            case 1:
                //FONT_6X16
                return 16 * y;
            case 2:
                //FONT_6X24
                return 24 * y;
            case 3:
                //FONT_6X32
                return 32 * y;
            case 4:
                //FONT_12X16
                return 16 * y;
            case 5:
                //FONT_12X24
                return 24 * y;
            case 6:
                //FONT_12X32
                return 32 * y;
            case 7:
                //FONT_18X24
                return 24 * y;
            case 8:
                //FONT_18X32
                return 32 * y;
            case 9:
                //FONT_24X32
                return 32 * y;
            default:
                return y;
        }
    }
}
