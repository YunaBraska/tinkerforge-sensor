package berlin.yuna.tinkerforgesensor.model.type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;

import static java.util.stream.Collectors.toList;

public class RollingList<T> extends LinkedList<T> {

    private final int capacity;

    /**
     * Creates a new RollingList of the specified capacity, with the specified
     * "empty" element appended to the end.
     *
     * @param capacity The capacity of this list.
     */
    public RollingList(final int capacity) {
        this.capacity = capacity;
    }

    public RollingList<T> add(final T... values) {
        for (T value : values) {
            add(value);
        }
        return this;
    }

    public boolean add(final T value) {
        while (size() > capacity - 1) {
            super.removeFirst();
        }

        super.addLast(value);
        return true;
    }

    public List<T> cloneList() {
        return new ArrayList<>(this);
    }

    /**
     * FIXME: add and check value AND plus minus average boolean - check 0 / 1 value
     * To check if its a new peak you will have to add the value first
     *
     * @param valueToCheck
     * @return
     */
    public boolean addAndCheckIfItsNewPeak(final T valueToCheck) {
        add(valueToCheck);
        List<Long> lastPeaks = getLastPeaks();
        return lastPeaks.size() < 2 || (lastPeaks.contains(((Number) valueToCheck).longValue()) && !lastPeaks.get(lastPeaks.size() - 2).equals(valueToCheck));
    }

    public List<Long> getLastPeaks() {
        List<Long> values = this.stream().map(value -> ((Number) value).longValue()).collect(toList());
        List<Long> lastPeek = new ArrayList<>(values);
        List<Long> current = new ArrayList<>(values);
        LongSummaryStatistics statistics = values.stream().mapToLong(value -> value).summaryStatistics();
        double average = statistics.getAverage();
        long max = statistics.getMax();
        long min = statistics.getMin();

        for (int i = 0; i < 100; i++) {
            current = getLastPeaks((long) (100 - i), current, average);
            if (!current.contains(max) || !current.contains(min)) {
                break;
            }
            lastPeek = new ArrayList<>(current);
        }
        return lastPeek;
    }

    private List<Long> getLastPeaks(final long sensitivity, final List<Long> values, final double average) {
        List<Long> lastPeek = new ArrayList<>();
        for (long value : values) {
            double percentage = calcPercentage(average, value);
            if (percentage > (100 - sensitivity)) {
                lastPeek.add(value);
            }
        }
        return lastPeek;
    }

    //Zero value is not possible to calc
    private double calcPercentage(final double x, final double y) {
        double min = x > y ? y : x;
        double max = min == x ? y : x;

        //Handle negative values
        if (min < 0) {
            min = min * -1;
            max = max + min;
        }
        return 100d - ((100d * min) / max);
    }

}