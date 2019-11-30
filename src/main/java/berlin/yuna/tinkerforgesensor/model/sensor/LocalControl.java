package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.logic.HumanInput;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURSOR_CLICK_COUNT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.CURSOR_MOVE_Y;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.KEY_CHAR;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.KEY_INPUT;

/**
 * <h3>{@link LocalControl}</h3><br>
 * <i>Displays a tiny human input board on activation</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#KEY_PRESSED}</li>
 * <li>{@link ValueType#KEY_CHAR}</li>
 * <li>{@link ValueType#CURSOR_PRESSED}</li>
 * <li>{@link ValueType#CURSOR_CLICK_COUNT}</li>
 * <li>{@link ValueType#CURSOR_MOVE_X}</li>
 * <li>{@link ValueType#CURSOR_MOVE_Y}</li>
 * </ul>
 *
 * <h3>Technical Info</h3>
 * <h6>Activation</h6>
 * <code>sensor.ledAdditional_setOn()</code>
 * <h6>Key code</h6>
 * <code>sensor.values().keyCode()</code>
 * <h6>Readable Key Character</h6>
 * <code>Char char = (char) stack.values().keyChar().intValue()</code>
 */
public class LocalControl extends Sensor<DummyDevice> {

    private static HumanInput humanInput;

    public LocalControl(final Device device, final String uid) throws NetworkConnectionException {
        super((DummyDevice) device, uid);
    }

    @Override
    protected Sensor<DummyDevice> initListener() {
        return this;
    }


    public boolean getIsKeyPressed() {
        return getValue(KEY_INPUT, -1, -1).intValue() == 1;
    }

    public Character getChar() {
        return (char) getValue(KEY_CHAR, -1, -1).intValue();
    }

    public int getClickCount() {
        return getValue(CURSOR_CLICK_COUNT, -1, -1).intValue();
    }

    public int getPosX() {
        return getValue(CURSOR_MOVE_Y, -1, -1).intValue();
    }

    public int getPosY() {
        return getValue(CURSOR_MOVE_Y, -1, -1).intValue();
    }

    @Override
    public Sensor<DummyDevice> send(final Object... valuesArray) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> send(final Object value) {
        return send(new Object[]{value});
    }

    @Override
    public Sensor<DummyDevice> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> ledAdditional(final Integer value) {
        if (LED_ADDITIONAL_ON.bit == value) {
            start();
        }
        return this;
    }

    public void start() {
        if (humanInput == null) {
            humanInput = new HumanInput();
            humanInput.sensorEventConsumerList.add(this::sendEventUnchecked);
        }
    }

    @Override
    public Sensor<DummyDevice> refreshPeriod(final int milliseconds) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> initLedConfig() {
        return this;
    }
}
