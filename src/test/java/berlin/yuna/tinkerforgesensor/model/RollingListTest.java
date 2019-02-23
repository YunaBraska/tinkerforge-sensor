package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;

public class RollingListTest {

    private RollingList<Long> rollingList = new RollingList<>(10);

    @Before
    public void setUp() {
        rollingList.add(-33L, 1L, 2L, 3L, 4L, 5L, -33L, 6L, 5L, 4L, 7L, 8L, 9L, 33L, 3L, 1L, 4L, 0L, 6L, 33L, 22L, 8L, 9L, 7L, 4L, 1L, -14L, 5L);
        if (rollingList.size() > 10) {
            throw new RuntimeException(format("Expected rollingList size is [%s] but was [%s]", 10, rollingList.size()));
        }
    }

    @Test
    public void rollingList_withGetPeaks_shouldHaveTheRightPeaks() {
        List<Long> lastPeaks = rollingList.getLastPeaks();

        if (lastPeaks.size() != 6) {
            throw new RuntimeException(format("Expected rollingList size is [%s] but was [%s]", 6, lastPeaks.size()));
        } else if (!lastPeaks.get(0).equals(33L)) {
            throw new RuntimeException(format("Expected rollingList first is [%s] but was [%s]", 33L, lastPeaks.get(0)));
        } else if (!lastPeaks.get(lastPeaks.size() - 1).equals(5L)) {
            throw new RuntimeException(format("Expected rollingList first is [%s] but was [%s]", 5L, lastPeaks.get(lastPeaks.size() - 1)));
        }
    }

    @Test
    public void peek_withIO_shouldHaveNewPeek() {
        rollingList = new RollingList<Long>(10).add(0L, 1L, 1L, 1L, 0L);
        if (!rollingList.addAndCheckIfItsNewPeak(1L)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", 1L, false, true));
        }

        rollingList = new RollingList<Long>(10).add(0L, 1L, 1L, 1L, 0L, 1L);
        if (!rollingList.addAndCheckIfItsNewPeak(0L)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", 0L, false, true));
        }
    }

    @Test
    public void peek_withIO_shouldNotHaveNewPeek() {
        rollingList = new RollingList<Long>(10).add(0L, 1L, 1L, 1L, 1L);
        if (rollingList.addAndCheckIfItsNewPeak(1L)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", 1L, true, false));
        }

        rollingList = new RollingList<Long>(10).add(0L, 1L, 1L, 1L, 0L, 0L);
        if (rollingList.addAndCheckIfItsNewPeak(0L)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", 0L, true, false));
        }
    }

    @Test
    public void value_withEmptyTimeSeries_shouldBeNewPeak() {
        long value = 16L;
        rollingList.clear();
        if (!rollingList.addAndCheckIfItsNewPeak(value)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", value, false, true));
        }
    }

    @Test
    public void value_shouldBeNewPeak() {
        long value = -16L;
        if (!rollingList.addAndCheckIfItsNewPeak(value)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", value, false, true));
        }
    }

    @Test
    public void value_withSameAsLastAddedValue_shouldNotBePeak() {
        long value = rollingList.getLast();
        if (rollingList.addAndCheckIfItsNewPeak(value)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", value, true, false));
        }
    }

}