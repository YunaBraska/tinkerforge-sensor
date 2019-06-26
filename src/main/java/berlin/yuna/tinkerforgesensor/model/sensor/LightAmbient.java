package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import static berlin.yuna.tinkerforgesensor.model.SensorRegistry.CALLBACK_PERIOD;
import static berlin.yuna.tinkerforgesensor.model.sensor.Sensor.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;

/**
 * <h3>{@link LightAmbient}</h3><br />
 * <i>Measures ambient light up to 900lux</i><br />
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#LIGHT_LUX} [x / 100.0 = lx]</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light.html">Official documentation</a></li>
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
public class LightAmbient extends Sensor<BrickletAmbientLight> {

    public LightAmbient(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickletAmbientLight) device, uid);
    }

    @Override
    protected Sensor<BrickletAmbientLight> initListener() {
        device.addIlluminanceListener(value -> sendEvent(LIGHT_LUX, (long) (value * 10)));
        refreshPeriod(CALLBACK_PERIOD);
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> send(final Object value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> initLedConfig() {
        ledStatus = LED_NONE;
        ledAdditional = LED_NONE;
        return this;
    }

    @Override
    public Sensor<BrickletAmbientLight> refreshPeriod(final int milliseconds) {
        try {
            if (milliseconds < 1) {
                device.setIlluminanceCallbackPeriod(1000);
            } else {
                device.setIlluminanceCallbackPeriod(milliseconds);
            }
            sendEvent(LIGHT_LUX, (long) (device.getIlluminance() * 10));
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }
}
