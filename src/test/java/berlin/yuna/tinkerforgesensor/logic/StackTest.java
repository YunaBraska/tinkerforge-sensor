package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.exception.ConnectionException;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tinkerforge.IPTestConnection;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static berlin.yuna.hamcrest.matcher.AndWait.andWait;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE_USB;
import static com.tinkerforge.Base58Utils.base58Random;
import static com.tinkerforge.BrickMaster.DEVICE_IDENTIFIER;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
class StackTest {

    private Stack stack;
    private IPTestConnection connection;

    @BeforeEach
    void setUp() throws NotConnectedException, NetworkException, AlreadyConnectedException {
        final Stack spyStack = new Stack();
        connection = Mockito.spy(new IPTestConnection());
        stack = Mockito.spy(spyStack);
        when(stack.createIPConnection()).thenReturn(connection);
        doNothing().when(connection).enumerate();
        doNothing().when(connection).connect(eq("localhost"), any(int.class));
        doThrow(NetworkException.class).when(connection).connect(eq("NetworkException"), any(int.class));
        doThrow(AlreadyConnectedException.class).when(connection).connect(eq("AlreadyConnectedException"), any(int.class));
    }

    @Test
    void connectionListener() {
        stack.connect();
        final String bricklet = base58Random();
        final String brickOne = base58Random();
        final String brickTwo = base58Random();
        connection.callEnumerateListeners(bricklet, brickOne, 'B', DEVICE_IDENTIFIER, ENUMERATION_TYPE_CONNECTED);
        connection.callEnumerateListeners(bricklet, brickOne, 'B', DEVICE_IDENTIFIER, ENUMERATION_TYPE_AVAILABLE);
        connection.callEnumerateListeners(brickOne, "0", 'A', DEVICE_IDENTIFIER, ENUMERATION_TYPE_CONNECTED);
        connection.callEnumerateListeners(brickTwo, brickOne, '1', DEVICE_IDENTIFIER, ENUMERATION_TYPE_CONNECTED);
        assertThat(() -> stack.getSensors(), andWait(hasSize(3), 1000));
        assertThat(stack.getSensorsSorted().get(0).getUid(), is(equalTo(brickOne)));
        assertThat(stack.getSensorsSorted().get(1).getUid(), is(equalTo(bricklet)));
        assertThat(stack.getSensorsSorted().get(2).getUid(), is(equalTo(brickTwo)));
        connection.callEnumerateListeners(brickTwo, brickOne, '1', DEVICE_IDENTIFIER, ENUMERATION_TYPE_DISCONNECTED);
    }

    @Test
    void addListener() {
        stack.addListener(System.out::println);
    }

    @Test
    void findSensor() {
        assertThat(stack.findSensor(UUID.randomUUID().toString()), is(nullValue()));
    }

    @Test
    void getSensors() {
        assertThat(stack.getSensors(), hasSize(0));
        assertThat(stack.getSensors(Sensor::hasHighContrast), hasSize(0));
    }

    @Test
    void getSensorsSorted() {
        assertThat(stack.getSensorsSorted(), hasSize(0));
        assertThat(stack.getSensorsSorted(Sensor::hasHighContrast), hasSize(0));
    }

    @Test
    void connect() {
        assertThrows(ConnectionException.class, () -> stack.connect("invalid"));
    }

    @Test
    void connectSuccessful() {
        stack.connect();
        stack.connect("AlreadyConnectedException");
        stack.disconnect();
    }

    @Test
    void connectNetworkException() {
        assertThrows(ConnectionException.class, () -> stack.connect("NetworkException"));
    }

    @Test
    void isConnected() {
        assertThat(stack.isConnected(), is(false));
    }

    @Test
    void close() {
        stack.close();
    }

    @Test
    void disconnect() {
        stack.disconnect();
    }

    @Test
    void sendEvent() {
        final AtomicReference<SensorEvent> triggeredEvent = new AtomicReference<>(null);
        stack.addListener(triggeredEvent::set);
        assertThat(triggeredEvent.get(), is(nullValue()));

        stack.sendEvent(new SensorEvent(null, 3000, VOLTAGE_USB));
        assertThat(triggeredEvent.get(), is(notNullValue()));
        assertThat(triggeredEvent.get().getValueType(), is(VOLTAGE_USB));
        assertThat(triggeredEvent.get().getValue(), is(equalTo(3000L)));
    }

    @Test
    void defaults() {
        assertThat(stack.equals(stack), is(true));
        assertThat(stack.hashCode(), is(not(0)));
        assertThat(stack.toString(), is(containsString("Stack{connectedTo='null'}")));
    }
}