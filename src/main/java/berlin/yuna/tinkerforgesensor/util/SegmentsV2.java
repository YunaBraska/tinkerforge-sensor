package berlin.yuna.tinkerforgesensor.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class SegmentsV2 {

    private SegmentsV2() {
    }

    private static final PairSmV2[] symbols = init();

    public static boolean[][] toSegments(final char[] chars, final boolean[] dots, final int digitLimit) {
        final boolean[][] result = new boolean[digitLimit][8];
        for (int i = 0; i < digitLimit; i++) {
            result[i] = SegmentsV2.get(chars[i]);
            result[i][7] = dots[i];
        }
        return result;
    }

    private static boolean[] get(final char chr) {
        for (PairSmV2 pairSmV2 : symbols) {
            if (pairSmV2.getKey() == chr) {
                return pairSmV2.getValue();
            }
        }
        return get(' ');
    }

    private static PairSmV2[] init() {
        final LinkedHashMap<Character, boolean[]> map = new LinkedHashMap<>();
        map.put(' ', d());
        map.put('0', d(0, 1, 2, 3, 4, 5));
        map.put('1', d(1, 2));
        map.put('2', d(0, 1, 3, 4, 6));
        map.put('3', d(0, 1, 2, 3, 6));
        map.put('4', d(1, 2, 5, 6));
        map.put('5', d(0, 2, 3, 5, 6));
        map.put('6', d(0, 2, 3, 4, 5, 6));
        map.put('7', d(0, 1, 2));
        map.put('8', d(0, 1, 2, 3, 4, 5, 6));
        map.put('9', d(0, 1, 2, 3, 5, 6));

        map.put('A', d(0, 1, 2, 4, 5, 6));
        map.put('B', d(2, 3, 4, 5, 6));
        map.put('C', d(0, 3, 4, 5));
        map.put('D', d(1, 2, 3, 4, 6));
        map.put('E', d(0, 3, 4, 5, 6));
        map.put('F', d(0, 4, 5, 6));
        map.put('G', d(0, 2, 3, 4, 5));
        map.put('H', d(1, 2, 4, 5, 6));
        map.put('I', d(4, 5));
        map.put('J', d(1, 2, 3, 4));
        map.put('K', d(0, 2, 4, 5, 6));
        map.put('L', d(3, 4, 5));
        map.put('M', d(0, 2, 4, 6));
        map.put('N', d(2, 4, 6));
        map.put('O', map.get('0'));
        map.put('P', d(0, 1, 4, 5, 6));
        map.put('Q', d(0, 1, 2, 5, 6));
        map.put('R', d(4, 6));
        map.put('S', map.get('5'));
        map.put('T', d(3, 4, 5, 6));
        map.put('U', d(1, 2, 3, 4, 5));
        map.put('V', map.get('U'));
        map.put('W', d(0, 2, 3, 4));
        map.put('X', map.get('H'));
        map.put('Y', d(1, 2, 3, 5, 6));
        map.put('Z', d(1, 4, 6));

        map.put('a', map.get('A'));
        map.put('b', map.get('B'));
        map.put('c', d(3, 4, 6));
        map.put('d', map.get('D'));
        map.put('e', map.get('E'));
        map.put('f', map.get('F'));
        map.put('g', map.get('G'));
        map.put('h', d(2, 4, 5, 6));
//        map.put('i', map.get('I'));
        map.put('i', d(4, 5));
        map.put('j', map.get('J'));
        map.put('k', map.get('K'));
        map.put('l', d(3, 4));
        map.put('m', map.get('M'));
        map.put('n', map.get('N'));
        map.put('o', d(2, 3, 4, 6));
        map.put('p', map.get('P'));
        map.put('q', map.get('Q'));
        map.put('r', map.get('R'));
        map.put('s', map.get('S'));
        map.put('t', map.get('T'));
        map.put('u', d(2, 3, 4));
        map.put('v', map.get('V'));
        map.put('w', map.get('W'));
        map.put('x', map.get('X'));
        map.put('y', map.get('Y'));
        map.put('z', map.get('Z'));

        map.put('-', d(6));
        map.put('=', d(3, 6));
        map.put('<', d(1, 2, 6));
        map.put('>', d(4, 5, 6));
        map.put('/', d(2, 5, 6));
        map.put('\\', map.get('Z'));
        map.put('?', d(0, 1, 4, 6));
        map.put('\'', d(1));
        map.put('|', map.get('I'));
        map.put(']', d(0, 1, 2, 3));
        map.put('[', map.get('C'));
        map.put('(', map.get('['));
        map.put(')', map.get(']'));
        map.put('{', map.get('['));
        map.put('}', map.get(']'));


        final PairSmV2[] pairSmV2 = new PairSmV2[map.size()];
        int index = 0;
        for (Map.Entry<Character, boolean[]> entry : map.entrySet()) {
            pairSmV2[index] = new PairSmV2(entry.getKey(), entry.getValue());
            index++;
        }
        return pairSmV2;
    }

    private static boolean[] d(final int... segments) {
        final boolean[] digit = {false, false, false, false, false, false, false, false};
        for (int segment : segments) {
            digit[segment] = true;
        }
        return digit;
    }

    public static class PairSmV2 {

        private final char key;
        private final boolean[] value;

        public PairSmV2(final char key) {
            this.key = key;
            this.value = new boolean[0];
        }

        public PairSmV2(final char key, final boolean[] value) {
            this.key = key;
            this.value = value;
        }

        public char getKey() { return key; }

        public boolean[] getValue() { return value; }
    }
}