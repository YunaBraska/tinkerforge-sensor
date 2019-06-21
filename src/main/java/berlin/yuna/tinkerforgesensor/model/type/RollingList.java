package berlin.yuna.tinkerforgesensor.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;

import static java.util.stream.Collectors.toList;

/**
 * <h3>{@link RollingList} extends {@link LinkedList}</h3><br />
 * <i>Rolling list with limited capacity</i><br />
 */
public class RollingList<T> extends LinkedList<T> {

    private final int capacity;

    public RollingList(final int capacity) {
        this.capacity = capacity;
    }

    public RollingList(final Collection<? extends T> collection) {
        super(collection);
        this.capacity = collection.size();
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
     * <h3>addAndCheckIfItsNewPeak</h3><br />
     * To check if its a new peak you will have to add the send first
     * <i>Rolling list with limited capacity</i><br />
     * @param valueToCheck value to check for
     * @return returns true if its a new peak
     */
    public boolean addAndCheckIfItsNewPeak(final T valueToCheck) {
        add(valueToCheck);
        final List<Long> lastPeaks = getLastPeaks();
        return lastPeaks.size() < 2 || (lastPeaks.contains(((Number) valueToCheck).longValue()) && !lastPeaks.get(lastPeaks.size() - 2).equals(valueToCheck));
    }

    /**
     * <h3>getLastPeaks</h3><br />
     * @return {@link List} of peaks
     */
    public List<Long> getLastPeaks() {
        final List<Long> values = this.stream().map(value -> ((Number) value).longValue()).collect(toList());
        List<Long> lastPeek = new ArrayList<>(values);
        List<Long> current = new ArrayList<>(values);
        final LongSummaryStatistics statistics = values.stream().mapToLong(value -> value).summaryStatistics();
        final double average = statistics.getAverage();
        final long max = statistics.getMax();
        final long min = statistics.getMin();

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
        final List<Long> lastPeek = new ArrayList<>();
        for (long value : values) {
            final double percentage = calcPercentage(average, value);
            if (percentage > (100 - sensitivity)) {
                lastPeek.add(value);
            }
        }
        return lastPeek;
    }

    //Zero send is not possible to calc
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