package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;

/**
 * <h3>{@link LightAmbientV2}</h3><br />
 * <i>Measures ambient light up to 64000lux</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#LIGHT_LUX} [x / 100.0 = lx]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light_V2.html">Official documentation</a></li>
 * </ul>
 * <h6>Getting lightLux examples</h6>
 * <code>
 * stack.values().lightLux();
 * stack.values().lightLux_Avg();
 * stack.values().lightLux_Min();
 * stack.values().lightLux_Max();
 * stack.values().lightLux_Sum();
 * </code>
 */
public class LightAmbientV2 extends Sensor<BrickletAmbientLightV2> {

    public LightAmbientV2(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAmbientLightV2) device, uid);
    }

    @Override
    protected Sensor<BrickletAmbientLightV2> initListener() {
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, value));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> setLedAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLightV2> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setIlluminanceCallbackPeriod(0);
            } else {
                device.setIlluminanceCallbackPeriod(milliseconds);
            }
            sendEvent(LIGHT_LUX, device.getIlluminance() * 10);
        } catch (TimeoutException | NotConnectedException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
