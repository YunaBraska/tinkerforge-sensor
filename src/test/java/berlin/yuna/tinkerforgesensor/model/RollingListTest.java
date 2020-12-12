package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.type.RollingList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;

@Tag("UnitTest")
class RollingListTest {

    private RollingList<Long> rollingList = new RollingList<>(10);

    @BeforeEach
    void setUp() {
        rollingList.add(-33L, 1L, 2L, 3L, 4L, 5L, -33L, 6L, 5L, 4L, 7L, 8L, 9L, 33L, 3L, 1L, 4L, 0L, 6L, 33L, 22L, 8L, 9L, 7L, 4L, 1L, -14L, 5L);
        if (rollingList.size() > 10) {
            throw new RuntimeException(format("Expected rollingList size is [%s] but was [%s]", 10, rollingList.size()));
        }
    }

    @Test
    void rollingList_withGetPeaks_shouldHaveTheRightPeaks() {
        final List<Long> lastPeaks = rollingList.getLastPeaks();

        if (lastPeaks.size() != 6) {
            throw new RuntimeException(format("Expected rollingList size is [%s] but was [%s]", 6, lastPeaks.size()));
        } else if (!lastPeaks.get(0).equals(33L)) {
            throw new RuntimeException(format("Expected rollingList first is [%s] but was [%s]", 33L, lastPeaks.get(0)));
        } else if (!lastPeaks.get(lastPeaks.size() - 1).equals(5L)) {
            throw new RuntimeException(format("Expected rollingList first is [%s] but was [%s]", 5L, lastPeaks.get(lastPeaks.size() - 1)));
        }
    }

    @Test
    void peek_withIO_shouldHaveNewPeek() {
        rollingList = new RollingList<Long>(10).add(0L, 1L, 1L, 1L, 0L);
        if (!rollingList.addAndCheckIfItsNewPeak(1L)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", 1L, false, true));
        }

        rollingList = new RollingList<Long>(10).add(0L, 1L, 1L, 1L, 0L, 1L);
        if (!rollingList.addAndCheckIfItsNewPeak(0L)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", 0L, false, true));
        }
    }

    //TODO: reintegrate peak calculation - broken by mutli value support
    @Disabled
    @Test
    void peek_withIO_shouldNotHaveNewPeek() {
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
    void value_withEmptyTimeSeries_shouldBeNewPeak() {
        final long value = 16L;
        rollingList.clear();
        if (!rollingList.addAndCheckIfItsNewPeak(value)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", value, false, true));
        }
    }

    @Test
    void value_shouldBeNewPeak() {
        final long value = -16L;
        if (!rollingList.addAndCheckIfItsNewPeak(value)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", value, false, true));
        }
    }

    //TODO: reintegrate peak calculation - broken by mutli value support
    @Disabled
    @Test
    void value_withSameAsLastAddedValue_shouldNotBePeak() {
        final long value = rollingList.getLast();
        if (rollingList.addAndCheckIfItsNewPeak(value)) {
            throw new RuntimeException(format("Expected newPeak [%s] is [%s] but was [%s]", value, true, false));
        }
    }

}