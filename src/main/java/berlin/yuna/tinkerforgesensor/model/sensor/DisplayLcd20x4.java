package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * <h3>{@link DisplayLcd20x4}</h3>
 * <i>20x4 character alphanumeric display with blue backlight</i>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#BUTTON} [10, 20, 30, 40] = Released</li>
 * <li>{@link ValueType#BUTTON} [11, 21, 31, 41] = Pressed</li>
 * <li>{@link ValueType#BUTTON_PRESSED} [0/1] = Released/Pressed</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/LCD_20x4.html">Official documentation</a></li>
 * </ul>
 * <h6>Clear display</h6>
 * <code>display.send(true);</code>
 * <h6>Send text</h6>
 * <code>display.send("Howdy");</code>
 * <h6>Send text centered</h6>
 * <code>display.send("Line1 center", true);</code>
 * <h6>Send text centered on row 2</h6>
 * <code>display.send("Line2 center", true, 1);</code>
 * <h6>Send text on position 5 and row 3</h6>
 * <code>display.send("Line3 posX=5", 4, 2);</code>
 * <h6>Send text with dynamic spaces between)</h6>
 * <code>display.send("H ${s} O ${s} W ${s} D ${s} Y");</code>
 * <h6>Display ON</h6>
 * <code>display.setLedAdditional_On;</code>
 * <h6>Getting button with pressed value (digit_1= button, digit_2 = pressed/released) example</h6>
 * <code>stack.values().button();</code>
 * <h6>Getting button pressed example</h6>
 * <code>stack.values().buttonPressed();</code>
 */
public class DisplayLcd20x4 extends Sensor<BrickletLCD20x4> {

    public static final String DYNAMIC_SPACE = "${s}";
    public static final int COLUMN_LIMIT = 20;

    private static final String SPLIT_LINE = System.lineSeparator();
    private final String[] cachedRows = new String[]{"", "", "", ""};

    public DisplayLcd20x4(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletLCD20x4) device, uid);
    }

    @Override
    protected Sensor<BrickletLCD20x4> initListener() {
        device.addButtonPressedListener(value -> {
            sendEvent(BUTTON_PRESSED, 1L);
            sendEvent(BUTTON, (value * 10L) + 1L);
        });
        device.addButtonReleasedListener(value -> {
            sendEvent(BUTTON_PRESSED, 0L);
            sendEvent(BUTTON_PRESSED, (value * 10L));
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
    public Sensor<BrickletLCD20x4> setLedAdditional(final Integer value) {
        if (ledAdditional.bit == value) return this;
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                ledAdditional = LED_ADDITIONAL_ON;
                device.backlightOn();
            } else if (value == LED_ADDITIONAL_OFF.bit) {
                ledAdditional = LED_ADDITIONAL_OFF;
                device.backlightOff();
            }
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> flashLed() {
        try {
            this.setLedAdditional_On();
            for (int i = 0; i < 7; i++) {
                send("H ${s} O ${s} W ${s} D ${s} Y [" + i + "]");
                send(DYNAMIC_SPACE + UUID.randomUUID() + DYNAMIC_SPACE, 0, 1);
                Thread.sleep(128);
            }
            send(true);
            this.setLedAdditional_Off();
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
        ledAdditional = LED_ADDITIONAL_OFF;
        return this;
    }

    private Sensor<BrickletLCD20x4> clearDisplay() {
        try {
            device.clearDisplay();
        } catch (TimeoutException | NotConnectedException e) {
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
            if (!line.equals(cachedRows[y])) {
                cachedRows[y] = line;
                device.writeLine(y, x, line);
            }
        } catch (TimeoutException | NotConnectedException e) {
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

    private static String spaces(final int number) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private static String utf16ToKS0066U(final String utf16) {
        StringBuilder ks0066u = new StringBuilder();
        char c;

        for (int i = 0; i < utf16.length(); i++) {
            final int codePoint = utf16.codePointAt(i);

            if (Character.isHighSurrogate(utf16.charAt(i))) {
                // Skip low surrogate
                i++;
            }

            // ASCII subset from JIS X 0201
            if (codePoint >= 0x0020 && codePoint <= 0x007e) {
                // The LCD charset doesn't include '\' and '~', use similar characters instead
                switch (codePoint) {
                    case 0x005c:
                        c = (char) 0xa4; break; // REVERSE SOLIDUS maps to IDEOGRAPHIC COMMA
                    case 0x007e:
                        c = (char) 0x2d; break; // TILDE maps to HYPHEN-MINUS
                    default:
                        c = (char) codePoint; break;
                }
            }
            // Katakana subset from JIS X 0201
            else if (codePoint >= 0xff61 && codePoint <= 0xff9f) {
                c = (char) (codePoint - 0xfec0);
            }
            // Special characters
            else {
                switch (codePoint) {
                    case 0x00a5:
                        c = (char) 0x5c; break; // YEN SIGN
                    case 0x2192:
                        c = (char) 0x7e; break; // RIGHTWARDS ARROW
                    case 0x2190:
                        c = (char) 0x7f; break; // LEFTWARDS ARROW
                    case 0x00b0:
                        c = (char) 0xdf; break; // DEGREE SIGN maps to KATAKANA SEMI-VOICED SOUND MARK
                    case 0x03b1:
                        c = (char) 0xe0; break; // GREEK SMALL LETTER ALPHA
                    case 0x00c4:
                        c = (char) 0xe1; break; // LATIN CAPITAL LETTER A WITH DIAERESIS
                    case 0x00e4:
                        c = (char) 0xe1; break; // LATIN SMALL LETTER A WITH DIAERESIS
                    case 0x00df:
                        c = (char) 0xe2; break; // LATIN SMALL LETTER SHARP S
                    case 0x03b5:
                        c = (char) 0xe3; break; // GREEK SMALL LETTER EPSILON
                    case 0x00b5:
                        c = (char) 0xe4; break; // MICRO SIGN
                    case 0x03bc:
                        c = (char) 0xe4; break; // GREEK SMALL LETTER MU
                    case 0x03c2:
                        c = (char) 0xe5; break; // GREEK SMALL LETTER FINAL SIGMA
                    case 0x03c1:
                        c = (char) 0xe6; break; // GREEK SMALL LETTER RHO
                    case 0x221a:
                        c = (char) 0xe8; break; // SQUARE ROOT
                    case 0x00b9:
                        c = (char) 0xe9; break; // SUPERSCRIPT ONE maps to SUPERSCRIPT (minus) ONE
                    case 0x00a4:
                        c = (char) 0xeb; break; // CURRENCY SIGN
                    case 0x00a2:
                        c = (char) 0xec; break; // CENT SIGN
                    case 0x2c60:
                        c = (char) 0xed; break; // LATIN CAPITAL LETTER L WITH DOUBLE BAR
                    case 0x00f1:
                        c = (char) 0xee; break; // LATIN SMALL LETTER N WITH TILDE
                    case 0x00d6:
                        c = (char) 0xef; break; // LATIN CAPITAL LETTER O WITH DIAERESIS
                    case 0x00f6:
                        c = (char) 0xef; break; // LATIN SMALL LETTER O WITH DIAERESIS
                    case 0x03f4:
                        c = (char) 0xf2; break; // GREEK CAPITAL THETA SYMBOL
                    case 0x221e:
                        c = (char) 0xf3; break; // INFINITY
                    case 0x03a9:
                        c = (char) 0xf4; break; // GREEK CAPITAL LETTER OMEGA
                    case 0x00dc:
                        c = (char) 0xf5; break; // LATIN CAPITAL LETTER U WITH DIAERESIS
                    case 0x00fc:
                        c = (char) 0xf5; break; // LATIN SMALL LETTER U WITH DIAERESIS
                    case 0x03a3:
                        c = (char) 0xf6; break; // GREEK CAPITAL LETTER SIGMA
                    case 0x03c0:
                        c = (char) 0xf7; break; // GREEK SMALL LETTER PI
                    case 0x0304:
                        c = (char) 0xf8; break; // COMBINING MACRON
                    case 0x00f7:
                        c = (char) 0xfd; break; // DIVISION SIGN

                    default:
                    case 0x25a0:
                        c = (char) 0xff; break; // BLACK SQUARE
                }
            }

            // Special handling for 'x' followed by COMBINING MACRON
            if (c == (char) 0xf8) {
                if (!ks0066u.toString().endsWith("x")) {
                    c = (char) 0xff; // BLACK SQUARE
                }

                if (ks0066u.length() > 0) {
                    ks0066u = new StringBuilder(ks0066u.substring(0, ks0066u.length() - 1));
                }
            }

            ks0066u.append(c);
        }

        return ks0066u.toString();
    }
}
