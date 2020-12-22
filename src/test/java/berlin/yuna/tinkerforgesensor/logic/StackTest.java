package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.exception.ConnectionException;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.handler.DummyHandler;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tinkerforge.IPTestConnection;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static berlin.yuna.hamcrest.matcher.AndWait.andWait;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE_USB;
import static berlin.yuna.tinkerforgesensor.util.ThreadUtil.createAsync;
import static com.tinkerforge.Base58Utils.base58Random;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_AVAILABLE;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_CONNECTED;
import static com.tinkerforge.IPConnectionBase.ENUMERATION_TYPE_DISCONNECTED;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
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
        connection = Mockito.spy(new IPTestConnection());
        stack = Mockito.spy(new Stack());
        when(stack.createIPConnection()).thenReturn(connection);
        doNothing().when(connection).enumerate();
        doNothing().when(connection).connect(eq("localhost"), any(int.class));
        doThrow(NetworkException.class).when(connection).connect(eq("NetworkException"), any(int.class));
        doThrow(AlreadyConnectedException.class).when(connection).connect(eq("AlreadyConnectedException"), any(int.class));
    }

    @AfterEach
    void tearDown() {
        stack.disconnect();
    }

    @Test
    void connectionListener() {
        final String bricklet = base58Random();
        final String brickOne = base58Random();
        final String brickTwo = base58Random();
        connectWithDevices(null, bricklet, brickOne, brickTwo);
        final String reason = "List was " + stack.getSensorsSorted();
        assertThat(reason, stack.getSensorsSorted().get(0).getUid(), is(equalTo(brickOne)));
        assertThat(reason, stack.getSensorsSorted().get(1).getUid(), is(equalTo(bricklet)));
        assertThat(reason, stack.getSensorsSorted().get(2).getUid(), is(equalTo(brickTwo)));
        stack.setPostStart(true);
        connectWithDevices("AlreadyConnectedException", base58Random(), base58Random(), base58Random());
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
        assertThat(triggeredEvent.get().getType(), is(VOLTAGE_USB));
        assertThat(triggeredEvent.get().getValue(), is(equalTo(3000L)));
    }

    @Test
    void postStart() {
        assertThat(stack.hasPostStart(), is(true));
        stack.setPostStart(false);
        assertThat(stack.hasPostStart(), is(false));
    }

    @Test
    void defaults() {
        assertThat(stack.get(), is(notNullValue()));
        assertThat(stack.getSensor(0, DummyHandler.class), is(nullValue()));
        assertThat(stack.getSensorList(DummyHandler.class), is(empty()));
        assertThat(stack.equals(new Stack()), is(false));
        assertThat(stack.hashCode(), is(not(0)));
        assertThat(stack.toString(), is(containsString("Stack{connectedTo='null'}")));
    }

    private void connectWithDevices(final String host, final String uidBricklet, final String uidOne, final String uidTwo) {
        createAsync("connectionListener_" + UUID.randomUUID(), run -> {
            if (host != null) {
                stack.connect(host);
            } else {
                stack.connect();
            }
        });
        assertThat(() -> {
            connection.callEnumerateListeners(base58Random(), base58Random(), '1', -1, ENUMERATION_TYPE_DISCONNECTED);
            connection.callEnumerateListeners(uidBricklet, uidOne, 'B', -1, ENUMERATION_TYPE_CONNECTED);
            connection.callEnumerateListeners(uidBricklet, uidOne, 'B', -1, ENUMERATION_TYPE_AVAILABLE);
            connection.callEnumerateListeners(uidOne, "0", 'A', -1, ENUMERATION_TYPE_CONNECTED);
            connection.callEnumerateListeners(uidTwo, uidOne, '2', -1, ENUMERATION_TYPE_CONNECTED);
            return stack.getSensors();
        }, andWait(hasSize(3), 10000));
    }
}