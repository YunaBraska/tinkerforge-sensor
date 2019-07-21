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
     *
     * @param valueToCheck values to check for
     * @return returns true if its a new peak
     */
    public boolean addAndCheckIfItsNewPeak(final T valueToCheck) {
        add(valueToCheck);
        final List<Number> valuesToCheck = (List<Number>) valueToCheck;
        for (int i = 0; i < valuesToCheck.size(); i++) {
//        for (Number valueToCheck : valuesToChecks) {
            final List<Number> lastPeaks = getLastPeaks(i);
            final Long value = valuesToCheck.get(i).longValue();
            if (lastPeaks.size() < 2 || (lastPeaks.contains(value) && !lastPeaks.get(lastPeaks.size() - 2).equals(value))) {
                return true;
            }
        }
        return false;
    }

    /**
     * <h3>getLastPeaks</h3><br />
     *
     * @return {@link List} of peaks
     */
    public List<Number> getLastPeaks(final int index) {
        final List<List<Number>> time = this.stream().map(value -> ((List<Number>) value)).collect(toList());
        if (!listIsEmpty(time) && (time.size() - 1) > index) {
            return getLastPeaks(time.get(index));
        }
        return new ArrayList<>();
    }

    /**
     * <h3>getLastPeaks</h3><br />
     *
     * @return {@link List} of peaks
     */
    private List<Number> getLastPeaks(final List<Number> values) {
//        final List<Long> values = this.stream().map(value -> ((Number) value).longValue()).collect(toList());
        List<Number> lastPeek = new ArrayList<>(values);
        List<Number> current = new ArrayList<>(values);
        final LongSummaryStatistics statistics = values.stream().mapToLong(Number::longValue).summaryStatistics();
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

    public static boolean listIsEmpty(final List list) {
        return list == null || list.isEmpty();
    }

    private List<Number> getLastPeaks(final long sensitivity, final List<Number> values, final double average) {
        final List<Number> lastPeek = new ArrayList<>();
        for (Number value : values) {
            final double percentage = calcPercentage(average, value.longValue());
            if (percentage > (100 - sensitivity)) {
                lastPeek.add(value.longValue());
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