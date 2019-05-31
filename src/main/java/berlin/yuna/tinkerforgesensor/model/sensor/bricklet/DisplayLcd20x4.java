package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.Arrays;
import java.util.UUID;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
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
 * <h6>Sending text to display</h6>
 * <code>
 * display.send("MyText");
 * </code>
 * <h6>Sending text to specific line</h6>
 * <code>
 * display.send(2, "MyText");
 * </code>
 * <h6>Sending text to specific line and position</h6>
 * <code>
 * display.send(2, 2, "MyText");
 * </code>
 * <h6>Sending text with new line</h6>
 * <code>
 * display.send("Line 1 \n text 2");
 * </code>
 * <h6>Dynamic space</h6>
 * <code>
 * display.send("${s} TextMiddle ${s}");
 * </code>
 * <h6>Center text</h6>
 * <code>
 * display.send(true, "MyText");
 * </code>
 * <h6>Clear display</h6>
 * <code>
 * display.send(true);
 * </code>
 */
public class DisplayLcd20x4 extends Sensor<BrickletLCD20x4> {

    public static final String DYNAMIC_SPACE = "${s}";
    private static final String SPLIT_LINE = System.lineSeparator();

    public DisplayLcd20x4(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletLCD20x4) device, uid, false);
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
        final String text = prepareText(values);
        final int posX = preparePosX(values);
        final int posY = preparePosY(values);
        final boolean center = prepareCenter(values);
        prepareClear(values);

        if (text != null && !text.trim().isEmpty()) {
            writeLines(posX, posY, text, center);
        }
        sendRest(values);
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> send(final Object value) {
        return send(new Object[]{value});
    }

    @Override
    public Sensor<BrickletLCD20x4> ledStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletLCD20x4> ledAdditional(final Integer value) {
        try {
            if (value == LED_ADDITIONAL_ON.bit) {
                device.backlightOn();
            } else if (value == LED_ADDITIONAL_OFF.bit) {
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
            this.ledAdditionalOn();
            for (int i = 0; i < 7; i++) {
                send(true, "HOWDY [" + i + "]");
                send(1, DYNAMIC_SPACE + UUID.randomUUID() + DYNAMIC_SPACE);
                Thread.sleep(128);
            }
            send(true);
            this.ledAdditionalOff();
        } catch (Exception ignore) {
        }
        return this;
    }

    private String sendRest(final Object... values) {
        if (values.length >= 1 && values[0] instanceof String) {
            send(Arrays.copyOfRange(values, 1, values.length));
        } else if (values.length >= 2 && values[1] instanceof String) {
            send(Arrays.copyOfRange(values, 2, values.length));
        } else if (values.length >= 3 && values[2] instanceof String) {
            send(Arrays.copyOfRange(values, 3, values.length));
        }
        return null;
    }

    private String prepareText(final Object... values) {
        if (values.length >= 1 && values[0] instanceof String) {
            return (String) values[0];
        } else if (values.length >= 2 && values[1] instanceof String) {
            return (String) values[1];
        } else if (values.length >= 3 && values[2] instanceof String) {
            return (String) values[2];
        }
        return null;
    }

    private int preparePosX(final Object... values) {
        if (values.length >= 2 && values[1] instanceof Number) {
            final int preNumber = ((Number) values[1]).intValue();
            return (preNumber >= 1 && preNumber <= 20 ? preNumber : 0);
        }
        return 0;
    }

    private int preparePosY(final Object... values) {
        if (values.length >= 1 && values[0] instanceof Number) {
            final int preNumber = ((Number) values[0]).intValue();
            return (preNumber >= 0 && preNumber <= 3 ? preNumber : 0);
        }
        return 0;
    }

    private boolean prepareCenter(final Object... values) {
        return (values.length >= 2 && values[0] instanceof Boolean);
    }

    private void prepareClear(final Object... values) {
        if (values.length == 1 && values[0] instanceof Boolean) {
            clearDisplay();
        }
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
            if (line.length() > 20) {
                leftOverText = line.substring(20);
                line = line.substring(0, 20);
            } else if (center) {
                line = centerLine(line);
            }
            sentToDisplay((short) posX, (short) y, utf16ToKS0066U(line));
            y++;
        }
    }

    private String centerLine(final String line) {
        final StringBuilder stringBuilder = new StringBuilder(line);
        final int spaces = (20 - line.length()) / 2;
        for (int i = 0; i < spaces && line.length() < 20; i++) {
            stringBuilder.insert(0, " ");
        }
        for (int i = 0; i < spaces && line.length() < 20; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private void sentToDisplay(final short x, final short y, final String line) {
        try {
            device.writeLine(y, x, line);
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
                text = text.replaceFirst("\\$\\{s}", spaces((20 - length) / spaceUps));
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

    @Override
    public Sensor<BrickletLCD20x4> refreshPeriod(final int milliseconds) {
        return this;
    }
}
