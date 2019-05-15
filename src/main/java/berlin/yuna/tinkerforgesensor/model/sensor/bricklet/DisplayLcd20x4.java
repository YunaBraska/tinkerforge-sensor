package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.UUID;

import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;

/**
 * 20x4 character alphanumeric display with blue backlight
 * <b>Values</b>
 * BUTTON_PRESSED 0/1
 * BUTTON [10, 20, 30, 40] Released
 * BUTTON [11, 21, 31, 41] Pressed
 * <br /><a href="https://www.tinkerforge.com/en/doc/Hardware/Bricklets/LCD_20x4.html">Official doku</a>
 */
public class DisplayLcd20x4 extends Sensor<BrickletLCD20x4> {

    public static final String DISPLAY_LINE_ONE = "${0}";
    public static final String DISPLAY_LINE_TWO = "${1}";
    public static final String DISPLAY_LINE_THREE = "${2}";
    public static final String DISPLAY_LINE_FOUR = "${3}";
    public static final String DISPLAY_CLEAR = "${clear}";
    public static final String DISPLAY_DYNAMIC_SPACE = "${space}";
    private static final String DISPLAY_SPLIT_LINE_REGEX = "(\\n|(<br\\s*/*>))";

    public DisplayLcd20x4(final Device device, final Sensor parent, final String uid) throws NetworkConnectionException {
        super((BrickletLCD20x4) device, parent, uid, false);
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

    /**
     * @param value <br /> Will print everything from input using {@link String#valueOf(Object)} <- auto line break if input is too long
     *              <br /> new line = \n {@link this#DISPLAY_SPLIT_LINE_REGEX}
     *              <br /> line number =  {@link this#DISPLAY_LINE_ONE} {@link this#DISPLAY_LINE_TWO} {@link this#DISPLAY_LINE_THREE} {@link this#DISPLAY_LINE_FOUR}
     *              <br /> Dynamic space = {@link this#DISPLAY_DYNAMIC_SPACE}
     *              <br /> Clear display = {@link this#DISPLAY_CLEAR}
     * @return {@link Sensor}
     */
    @Override
    public Sensor<BrickletLCD20x4> value(final Object value) {
        try {
            String text;
            if (value instanceof String) {
                text = (String) value;
            } else {
                text = String.valueOf(value);
            }
            int y = 0;
            if (text != null && text.startsWith(DISPLAY_CLEAR)) {
                device.clearDisplay();
                text = text.substring(DISPLAY_CLEAR.length());
            }
            writeLines(y, device, text);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
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
                value(DISPLAY_LINE_TWO + DISPLAY_DYNAMIC_SPACE + "HOWDY [" + i + "]" + DISPLAY_DYNAMIC_SPACE);
                value(DISPLAY_LINE_THREE + DISPLAY_DYNAMIC_SPACE + UUID.randomUUID() + DISPLAY_DYNAMIC_SPACE);
                Thread.sleep(128);
            }
            value(DISPLAY_CLEAR);
            this.ledAdditionalOff();
        } catch (Exception ignore) {
        }
        return this;
    }

    private static void writeLines(final int posY, final BrickletLCD20x4 device, final String text) throws TimeoutException, NotConnectedException {
        if (text != null && !text.isEmpty()) {
            int y = posY;
            String leftOverText = "";
            String[] lines = text.split(DISPLAY_SPLIT_LINE_REGEX);
            for (String line : lines) {
                if (y > 3) {
                    break;
                }
                if (line.startsWith(DISPLAY_LINE_ONE)) {
                    y = 0;
                    line = line.substring(DISPLAY_LINE_ONE.length());
                } else if (line.startsWith(DISPLAY_LINE_TWO)) {
                    y = 1;
                    line = line.substring(DISPLAY_LINE_TWO.length());
                } else if (line.startsWith(DISPLAY_LINE_THREE)) {
                    y = 2;
                    line = line.substring(DISPLAY_LINE_THREE.length());
                } else if (line.startsWith(DISPLAY_LINE_FOUR)) {
                    y = 3;
                    line = line.substring(DISPLAY_LINE_FOUR.length());
                }
                line = spaceUp(line);
                line += leftOverText;
                leftOverText = "";
                if (line.length() > 20) {
                    leftOverText = line.substring(20);
                    line = line.substring(0, 20);
                }

                line = utf16ToKS0066U(line);

//                console("[%s] [%s] [%s]", DisplayLcd20x4.class.getSimpleName(), y, line);
                device.writeLine((short) y, (short) 0, line);
                y++;
            }
            writeLines(y, device, leftOverText);
        }
    }

    private static String spaceUp(final String line) {
        String text = line;
        if (text.contains(DISPLAY_DYNAMIC_SPACE)) {
            int spaceUps;
            while ((spaceUps = ("splitStart" + text + "splitEnd").split("\\$\\{space}").length - 1) > 0) {
                int length = text.length() - ((DISPLAY_DYNAMIC_SPACE).length() * spaceUps);
                text = text.replaceFirst("\\$\\{space}", spaces((20 - length) / spaceUps));
            }
        }
        return text;
    }

    private static String spaces(int number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private static String utf16ToKS0066U(String utf16) {
        StringBuilder ks0066u = new StringBuilder();
        char c;

        for (int i = 0; i < utf16.length(); i++) {
            int codePoint = utf16.codePointAt(i);

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
