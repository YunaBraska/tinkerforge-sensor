package berlin.yuna.tinkerforgesensor.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

}