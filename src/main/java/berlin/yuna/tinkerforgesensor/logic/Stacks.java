package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.builder.Sensors;
import berlin.yuna.tinkerforgesensor.model.builder.Values;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.NotConnectedException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_RELEASED;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class Stacks {

    private static final int MAX_PRINT_VALUES = 50;
    private static final ConcurrentHashMap<String, Stack> stacks = new ConcurrentHashMap<>();
    private static final List<Consumer<SensorEvent>> consumers = new CopyOnWriteArrayList<>();

    /**
     * <h3>valuesToString</h3>
     *
     * @return all values from all sensors as string
     */
    public static String valuesToString() {
        final StringBuilder lineHead = new StringBuilder();
        final StringBuilder lineValue = new StringBuilder();
        final List<ValueType> IGNORE_TYPES = asList(BUTTON_PRESSED, BUTTON_RELEASED);
        for (Sensor sensor : sensors()) {
            for (ValueType valueType : ValueType.values()) {
                if (IGNORE_TYPES.contains(valueType)) {
                    continue;
                }
                final List<Long> values = sensor.values().getList(valueType, -1);
                if (!values.isEmpty()) {
                    String value = values.stream().map(Object::toString).collect(joining(", "));
                    value = value.length() > MAX_PRINT_VALUES ? value.substring(0, MAX_PRINT_VALUES) : value;
                    final int valueLength = value.length();
                    final int typeLength = valueType.toString().length();
                    final int fieldSize = (Math.max(valueLength, typeLength)) + 1;
                    lineHead.append(center(" " + valueType, fieldSize)).append(" |");
                    lineValue.append(format("%" + fieldSize + "s |", value));
                }
            }
        }
        lineHead.append(format("%9s |", "Sensors"));
        lineValue.append(format("%9s |", sensors().size()));
        return System.lineSeparator() + lineHead.toString() + System.lineSeparator() + lineValue.toString();
    }

    /**
     * <h3>addConsumer(EventConsumer)</h3>
     *
     * @param consumer to receive {@link SensorEvent}
     */
    public static void addConsumer(final Consumer<SensorEvent> consumer) {
        consumers.add(consumer);
    }

    /**
     * <h3>sensors(Stack)</h3>
     *
     * @param stack to identify from which {@link Stack}
     * @return {@link Sensors} (never null)
     */
    public static Sensors sensors(final Stack stack) {
        return sensors(stack.getId());
    }

    /**
     * <h3>sensors(StackId)</h3>
     *
     * @param stackId to identify from which {@link Stack}
     * @return {@link Sensors} (never null)
     */
    public static Sensors sensors(final String stackId) {
        final Optional<Stack> stack = getStack(stackId);
        return stack.isPresent() ? stack.get().sensors() : new Sensors(new SensorList());
    }

    /**
     * <h3>sensors</h3>
     *
     * @return {@link Sensors} (never null) from every {@link Stack}
     */
    public static Sensors sensors() {
        return new Sensors(getSensorList());
    }

    public Values values() {
        return new Values(getSensorList());
    }

    /**
     * <h3>connect(Host, Port)</h3>
     *
     * @param host name or ip to connect with
     * @param port port to connect with
     * @return {@link Stack} element
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public static Optional<Stack> connect(final String host, final int port) throws NetworkConnectionException {
        return connect(new Stack(host, port, true));
    }

    /**
     * <h3>connect(Stack)</h3>
     *
     * @param stack to add and connect with
     * @return {@link Stack} element
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public static Optional<Stack> connect(final Stack stack) throws NetworkConnectionException {
        addStack(stack);
        return connect(stack.getId());
    }

    /**
     * <h3>connect(StackId)</h3>
     *
     * @param stackId to connect
     * @return {@link Stack} element
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public static Optional<Stack> connect(final String stackId) throws NetworkConnectionException {
        final Optional<Stack> stack = stacks.values().stream().filter(s -> s.getId().equals(stackId)).findFirst();
        if (stack.isPresent()) {
            stack.get().connect();
        }
        return stack;
    }

    /**
     * <h3>connectAll</h3>
     * <p>
     * connect / reconnect all stacks
     *
     * @throws NetworkConnectionException if connection fails due/contains {@link NotConnectedException} {@link com.tinkerforge.AlreadyConnectedException} {@link com.tinkerforge.NetworkException}
     */
    public static void connectAll() throws NetworkConnectionException {
        for (Stack stack : stacks.values()) {
            stack.connect();
        }
    }

    /**
     * <h3>disconnectAll</h3>
     * <p>
     * disconnects all stacks
     */
    public static void disconnectAll() {
        stacks.values().forEach(Stack::disconnect);
    }

    /**
     * <h3>disconnect(Stack)</h3>
     *
     * @param stack to identify for disconnecting
     */
    public static void disconnect(final Stack stack) {
        disconnect(stack.getId());
    }

    /**
     * <h3>disconnect(StackId)</h3>
     *
     * @param stackId to identify for disconnecting
     */
    public static void disconnect(final String stackId) {
        stacks.values().stream().filter(stack -> stack.getId().equals(stackId)).forEach(Stack::disconnect);
    }

    /**
     * <h3>addStack(Stack)</h3>
     *
     * @param stack to add
     */
    public static void addStack(final Stack stack) {
        stack.consumers.add(event -> consumers.forEach(consumer -> consumer.accept(event)));
        stacks.put(stack.getId(), stack);
    }

    /**
     * <h3>getStack(StackId)</h3>
     *
     * @return first {@link Stack} element or null if there is none
     */
    public static Stack getFirstStack() {
        return stacks.values().stream().findFirst().orElse(null);
    }

    /**
     * <h3>getStack(StackId)</h3>
     *
     * @param stackId to identify
     * @return {@link Stack} element
     */
    public static Optional<Stack> getStack(final String stackId) {
        return Optional.ofNullable(stacks.get(stackId));
    }

    /**
     * <h3>getStacks</h3>
     *
     * @return List of {@link Stack}
     */
    public static Collection<Stack> getStacks() {
        return stacks.values();
    }

    private static SensorList<Sensor> getSensorList() {
        final SensorList<Sensor> sensorList = new SensorList<>();
        stacks.values().forEach(stack -> sensorList.addAll(stack.getSensorList()));
        return sensorList;
    }

    //TODO: move to utils
    private static String center(final String text, final int length) {
        if (text.length() < length) {
            final int left = (length - text.length()) / 2;
            final int right = (length - text.length()) - left;
            final String leftS = IntStream.range(0, left).mapToObj(i -> " ").collect(joining(""));
            final String rightS = IntStream.range(0, right).mapToObj(i -> " ").collect(joining(""));
            return leftS + text + rightS;
        }
        return text;
    }
}
