package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletUVLight;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_UV;

/**
 * <h3>{@link LightUv}</h3><br />
 * <i>Measures ambient light up to 64000lux</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#LIGHT_UV} [x / 10.0 = index]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting lightUv examples</h6>
 * <code>
 * stack.values().lightUv();
 * stack.values().lightUv_Avg();
 * stack.values().lightUv_Min();
 * stack.values().lightUv_Max();
 * stack.values().lightUv_Sum();
 * </code>
 */
public class LightUv extends Sensor<BrickletUVLight> {

    public LightUv(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletUVLight) device, uid);
    }

    @Override
    protected Sensor<BrickletUVLight> initListener() {
        device.addUVLightListener(value -> sendEvent(LIGHT_UV, value));
        refreshPeriod(-1);
        return this;
    }

    @Override
    public Sensor<BrickletUVLight> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLight> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLight> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletUVLight> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletUVLight> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setUVLightCallbackPeriod(1000);
            } else {
                device.setUVLightCallbackPeriod(milliseconds);
            }
            sendEvent(LIGHT_UV, device.getUVLight());
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
