package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.exception.SensorInitialisationException;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.handler.DummyHandler;
import berlin.yuna.tinkerforgesensor.model.threads.Color;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.BrickMaster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_BRIGHTNESS;
import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_HIGH_CONTRAST;
import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_LED_STATUS;
import static com.tinkerforge.Base58Utils.base58Random;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class SensorTest {

    private Stack stack;
    private Sensor sensor;

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
    }

    @Test
    void setHighContrast() {
        assertThat(sensor.hasHighContrast(), is(false));
        sensor.handler().setConfig(CONFIG_HIGH_CONTRAST, 1);
        assertThat(sensor.hasHighContrast(), is(true));
    }

    @Test
    void getBrightness() {
        assertThat(sensor.getBrightness(), is(0));
    }

    @Test
    void setBrightness() {
        sensor.setBrightness(101);
        assertThat(sensor.getBrightness(), is(0));

        sensor.handler().setConfig(CONFIG_BRIGHTNESS, 1);
        assertThat(sensor.getBrightness(), is(1));
        sensor.setBrightness(101);
        assertThat(sensor.getBrightness(), is(101));
    }

    @Test
    void setColor() {
        assertThat(sensor.setColor(new Color(1, 2, 3)), is(equalTo(sensor)));
    }

    @Test
    void setColorNumber() {
        assertThat(sensor.setColor(123), is(equalTo(sensor)));
    }

    @Test
    void hasStatusLed() {
        assertThat(sensor.hasStatusLed(), is(false));
        sensor.handler().setConfig(CONFIG_LED_STATUS, 1);
        assertThat(sensor.hasStatusLed(), is(true));
    }

    @Test
    void setStatus() {
        assertThat(sensor.setStatusLedOn(), is(equalTo(sensor)));
        assertThat(sensor.setStatusLedOff(), is(equalTo(sensor)));
        assertThat(sensor.setStatusLedDefault(), is(equalTo(sensor)));
        assertThat(sensor.setStatusLedHeartbeat(), is(equalTo(sensor)));
    }

    @Test
    void defaults() {
        sensor.setPort(11);
        sensor.isBrick(true);
        assertThat(sensor.getPort(), is(11));
        assertThat(sensor.isBrick(), is(true));
        assertThat(sensor.getId(), is(-1));
        assertThat(sensor.getUid(), is(notNullValue()));
        assertThat(sensor.getParentUid(), is(notNullValue()));
        assertThat(sensor.getName(), is(equalTo(DummyHandler.class.getSimpleName())));
        assertThat(sensor.getStack(), is(equalTo(stack)));
        assertThat(sensor.device().getClass(), is(equalTo(BrickMaster.class)));
        assertThat(sensor.getPosition(), is(equalTo('\n')));
        assertThat(sensor.is(DummyHandler.class), is(true));
        assertThat(sensor.is(BrickMaster.class), is(true));
        assertThat(sensor.is(BrickIMU.class), is(false));
        assertThat(sensor.is(String.class), is(false));
        assertThat(sensor.is(ButtonRGB.class), is(false));
        assertThat(sensor.getParent(), is(Optional.empty()));
        assertThat(sensor.equals(sensor), is(true));
        assertThat(sensor.hashCode(), is(not(0)));
        assertThat(sensor.toString(), is(containsString("Sensor{name='DummyHandler', uid=")));
        assertThat(sensor.compareTo(sensor), is(0));
    }

    @Test
    void initFail() {
        assertThrows(SensorInitialisationException.class, () ->
                new Sensor(-1, null, null, '\n', null, null, null)
        );
    }
}