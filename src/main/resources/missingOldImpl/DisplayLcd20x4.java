package berlin.yuna.tinkerforgesensor.model.missing;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_ON;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_RELEASED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.util.StringUtils.spaces;
import static berlin.yuna.tinkerforgesensor.util.StringUtils.utf16ToKS0066U;
import static java.util.Arrays.asList;

/**
 * <h3>{@link DisplayLcd20x4}</h3>
 * <i>20x4 character alphanumeric display with blue backlight</i>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BUTTON_PRESSED} [1] = Pressed</li>
 * <li>{@link ValueType#BUTTON_RELEASED} [0] = Released</li>
 * <li>{@link ValueType#BUTTON} [0/1,0/1,0/1,0/1] = 4x Button Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/LCD_20x4.html">Official documentation</a></li>
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
 * <h6>Send text with dynamic spaces between)</h6>
 * <code>sensor.send("H ${s} O ${s} W ${s} D ${s} Y");</code>
 * <h6>Display ON</h6>
 * <code>sensor.ledAdditional_setOn;</code>
 * <h6>Getting button state from second button (0=Released, 1= pressed)</h6>
 * <code>sensor.values().button(1);</code>
 * <h6>Getting button state list of 0/1 (0=Released, 1= pressed) value for each button</h6>
 * <code>sensor.values().button_List();</code>
 * <h6>Getting button pressed</h6>
 * <code>sensor.values().buttonPressed();</code>
 */
public class DisplayLcd20x4 extends Sensor<BrickletLCD20x4> {

    public static final String DYNAMIC_SPACE = "${s}";
    public static final int COLUMN_LIMIT = 20;
    final Integer[] buttons = {0, 0, 0, 0};

    private static final String SPLIT_LINE = System.lineSeparator();
//    private final String[] cachedRows = new String[]{"", "", "", ""};

    public DisplayLcd20x4(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletLCD20x4) device, uid);
    }

    @Override
    protected Sensor<BrickletLCD20x4> initListener() {
        device.addButtonPressedListener(value -> {
            buttons[value] = 1;
            sendEvent(BUTTON_PRESSED, 1, true);
            sendEvent(BUTTON, asList(buttons), true);
        });
        device.addButtonReleasedListener(value -> {
            buttons[value] = 0;
            sendEvent(BUTTON_RELEASED, 0, true);
            sendEvent(BUTTON, asList(buttons), true);
        });
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> send(final Object... values) {
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

        //TODO: validate positions
        if (text != null && !text.trim().isEmpty()) {
            writeLines(posX, posY, text, center);
        }
    }

    @Override
    public Sensor<BrickletLCD20x4> send(final Object value) {
        return send(new Object[]{value});
    }

    @Override
    public Sensor<BrickletLCD20x4> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> ledAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ON.bit) {
                ledAdditional = LED_ON;
                device.backlightOn();
            } else if (value == LED_OFF.bit) {
                ledAdditional = LED_OFF;
                device.backlightOff();
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> flashLed() {
        try {
            this.ledAdditional_setOn();
            for (int i = 0; i < 7; i++) {
                send("H ${s} O ${s} W ${s} D ${s} Y [" + i + "]");
                send(DYNAMIC_SPACE + UUID.randomUUID() + DYNAMIC_SPACE, 0, 1);
                Thread.sleep(128);
            }
            send(true);
            this.ledAdditional_setOff();
        } catch (Exception ignore) {
        }
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> refreshPeriod(final int milliseconds) {
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_OFF;
        return this;
    }

    private Sensor<BrickletLCD20x4> clearDisplay() {
        try {
            device.clearDisplay();
        } catch (TinkerforgeException e) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    private void writeLines(final int posX, final int posY, final String text, final boolean center) {
        int y = posY;
        final String[] lines = text.split(SPLIT_LINE);

        String leftOverText = "";
        for (String line : lines) {
            line += leftOverText;
            line = spaceUp(line);
            leftOverText = "";
            if (line.length() > COLUMN_LIMIT) {
                leftOverText = line.substring(COLUMN_LIMIT);
                line = line.substring(0, COLUMN_LIMIT);
            } else if (center) {
                line = centerLine(line);
            }
            sendToDisplay((short) posX, (short) y, utf16ToKS0066U(line));
            y++;
        }

        if (!leftOverText.isEmpty()) {
            writeLines(posX, y, leftOverText, center);
        }
    }

    private String centerLine(final String line) {
        final StringBuilder stringBuilder = new StringBuilder(line);
        final int spaces = (COLUMN_LIMIT - line.length()) / 2;
        for (int i = 0; i < spaces && line.length() < COLUMN_LIMIT; i++) {
            stringBuilder.insert(0, " ");
        }
        for (int i = 0; i < spaces && line.length() < COLUMN_LIMIT; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private void sendToDisplay(final short x, final short y, final String line) {
        try {
//            if (!line.equals(cachedRows[y])) {
//                cachedRows[y] = line;
            device.writeLine(y, x, line);
//            }
        } catch (TinkerforgeException e) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private static String spaceUp(final String line) {
        String text = line;
        if (text.contains(DYNAMIC_SPACE)) {
            int spaceUps;
            while ((spaceUps = ("splitStart" + text + "splitEnd").split("\\$\\{s}").length - 1) > 0) {
                final int length = text.length() - ((DYNAMIC_SPACE).length() * spaceUps);
                text = text.replaceFirst("\\$\\{s}", spaces((COLUMN_LIMIT - length) / spaceUps));
            }
        }
        return text;
    }

}
