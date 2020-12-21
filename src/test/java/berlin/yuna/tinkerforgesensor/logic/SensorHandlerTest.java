package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.handler.DummyHandler;
import com.tinkerforge.BrickMaster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_BRIGHTNESS;
import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_FUNCTION_A;
import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_HIGH_CONTRAST;
import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE_USB;
import static com.tinkerforge.Base58Utils.base58Random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Tag("UnitTest")
class SensorHandlerTest {

    private Stack stack;
    private Sensor sensor;
    private DummyHandler handler;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        sensor = new Sensor(
                -1,
                base58Random(),
                stack,
                '\n',
                base58Random(),
                BrickMaster.class,
                DummyHandler.class
        );
        handler = (DummyHandler) sensor.handler();
        handler.triggers.clear();
    }

    @Test
    void send() {
        handler.send(1234);
        assertThat(handler.triggers.size(), is(1));
        assertThat(handler.triggers.containsKey("method_send"), is(true));
    }

    @Test
    void initListener() {
        assertThat(handler.hasStatusLed(), is(false));
        handler.init();
        assertThat(handler.hasStatusLed(), is(true));
        assertThat(handler.triggers.size(), is(1));
        assertThat(handler.triggers.containsKey("method_initListener"), is(true));
    }

    @Test
    void setRefreshPeriod() {
        handler.setRefreshPeriod(1000);
        assertThat(handler.triggers.size(), is(1));
        assertThat(handler.triggers.containsKey("method_setRefreshPeriod"), is(true));
    }

    @Test
    void setBrightness() {
        handler.setConfig(CONFIG_BRIGHTNESS, 1);
        assertThat(handler.getConfig(CONFIG_FUNCTION_A), is(nullValue()));
        handler.setBrightness(101);
        assertThat(handler.getBrightness(), is(101));
        assertThat(handler.getConfig(CONFIG_FUNCTION_A), is(notNullValue()));
        assertThat(handler.triggers.size(), is(2));
        assertThat(handler.triggers.containsKey("method_setSecondLed"), is(true));
        assertThat(handler.triggers.containsKey("method_setBrightness"), is(true));
    }

    @Test
    void hasBrightness() {
        assertThat(handler.hasBrightness(), is(false));
        handler.setConfig(CONFIG_BRIGHTNESS, 1);
        assertThat(handler.hasBrightness(), is(true));
    }

    @Test
    void setStatusLed() {
        assertThat(handler.getConfig(CONFIG_LED_STATUS), is(nullValue()));
        assertThat(handler.triggers.containsKey("method_setStatusLed"), is(false));
        handler.setStatusLed(101);
        assertThat(handler.getConfig(CONFIG_LED_STATUS), is(nullValue()));
        assertThat(handler.triggers.containsKey("method_setStatusLed"), is(false));

        handler.setConfig(CONFIG_LED_STATUS, 1);
        handler.setStatusLed(101);
        assertThat(handler.getConfig(CONFIG_LED_STATUS), is(notNullValue()));
        assertThat(handler.triggers.containsKey("method_setStatusLed"), is(true));
    }

    @Test
    void setHighContrast() {
        assertThat(handler.hasHighContrast(), is(false));
        sensor.handler().setConfig(CONFIG_HIGH_CONTRAST, 1);
        assertThat(handler.hasHighContrast(), is(true));
        assertThat(handler.triggers.size(), is(0));
    }

    @Test
    void sensor() {
        assertThat(handler.sensor(), is(equalTo(sensor)));
    }

    @Test
    void device() {
        assertThat(handler.device().getClass(), is(equalTo(BrickMaster.class)));
    }

    @Test
    void animateStatuesLed() {
        handler.setConfig(CONFIG_LED_STATUS, 1);
        assertThat(handler.hasStatusLed(), is(true));
        assertThat(handler.animateStatuesLed(), is(equalTo(handler)));
    }

    @Test
    void sendEvent() {
        final AtomicReference<SensorEvent> triggeredEvent = new AtomicReference(null);
        stack.addListener(triggeredEvent::set);
        assertThat(triggeredEvent.get(), is(nullValue()));

        handler.sendEvent(VOLTAGE_USB, 3000);
        assertThat(triggeredEvent.get(), is(notNullValue()));
        assertThat(triggeredEvent.get().getType(), is(VOLTAGE_USB));
        assertThat(triggeredEvent.get().getValue(), is(equalTo(3000L)));
    }

    @Test
    void isLedOn() {
        assertThat(LedStatusType.isLedOn(0), is(false));
        assertThat(LedStatusType.isLedOn(1), is(true));
        assertThat(LedStatusType.isLedOn(2), is(true));
        assertThat(LedStatusType.isLedOn(3), is(true));
        assertThat(LedStatusType.isLedOn(4), is(false));
    }
}