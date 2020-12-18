package berlin.yuna.tinkerforgesensor.util;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;

import static java.time.format.DateTimeFormatter.ofPattern;

public class SegmentFormatter {
    private final boolean[] colons = new boolean[]{false, false};
    private final boolean blinkColon;

    private final boolean[] dots;
    private boolean tick = false;
    private final char[] digits;
    private final DateTimeFormatter dateTimeFormatter;

    public SegmentFormatter(final Object newValue, final Object oldValue, final DateTimeFormatter timeFormatter, final int limit) {
        dots = new boolean[limit];
        final Object value;
        if (newValue instanceof DateTimeFormatter) {
            dateTimeFormatter = (DateTimeFormatter) newValue;
            value = oldValue;
        } else {
            dateTimeFormatter = timeFormatter == null ? ofPattern("HH:mm") : timeFormatter;
            value = newValue;
        }
        digits = value == null ? new char[limit] : format(toChars(value), limit);

        final String dateTimePattern = dateTimeFormatter.toString();
        blinkColon = dateTimePattern.contains("Second") || dateTimePattern.contains("Minute");
    }

    private char[] toChars(final Object value) {
        final char[] chars;
        if (value instanceof TemporalAccessor) {
            chars = dateTimeFormatter.format((TemporalAccessor) value).toCharArray();
        } else if (value instanceof String) {
            chars = ((String) value).toCharArray();
        } else {
            chars = String.valueOf(value).toCharArray();
        }
        return chars;
    }

    private boolean isColon(final char c) {
        return c == '\'' || c == '°' || c == '•' || c == '‘' || c == '“' || c == '`';
    }

    private boolean isDot(final char c) {
        return c == '.' || c == ',';
    }

    private boolean isDoubleDot(final char c) {
        return c == ':';
    }

    private char[] format(final char[] chars, final int limit) {
        final char[] digits = new char[limit];
        int digitNumber = 0;
        for (char c : chars) {
            if (digitNumber == 3 && isColon(c)) {
                tick = true;
            } else if (digitNumber == 2 && isDoubleDot(c)) {
                colons[0] = true;
                colons[1] = true;
            } else if (isDot(c)) {
                if ((digitNumber - 1) < 0) {
                    digitNumber++;
                }
                dots[digitNumber - 1] = true;
            } else {
                digits[digitNumber] = c;
                digitNumber++;
            }
            if (digitNumber == limit) {
                break;
            }
        }
        return rearrange(limit, digits, digitNumber);
    }

    private char[] rearrange(final int limit, final char[] digits, final int digitNumber) {
        if (digitNumber != 0 && digitNumber < 4) {
            final char[] result = new char[limit];
            if (digitNumber >= 0) System.arraycopy(digits, 0, result, limit - digitNumber, digitNumber);
            return result;
        }
        return digits;
    }

    public boolean[] getColons() {
        return colons;
    }

    public boolean[] getDots() {
        return dots;
    }

    public boolean isBlinkColon() {
        return blinkColon;
    }

    public boolean isTick() {
        return tick;
    }

    public char[] getDigits() {
        return digits;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SegmentFormatter that = (SegmentFormatter) o;

        if (tick != that.tick) return false;
        if (!Arrays.equals(colons, that.colons)) return false;
        if (!Arrays.equals(dots, that.dots)) return false;
        return Arrays.equals(digits, that.digits);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(colons);
        result = 31 * result + Arrays.hashCode(dots);
        result = 31 * result + (tick ? 1 : 0);
        result = 31 * result + Arrays.hashCode(digits);
        return result;
    }
}