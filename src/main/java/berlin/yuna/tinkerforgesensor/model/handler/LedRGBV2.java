package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.threads.Color;
import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletRGBLEDV2;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.Optional;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LedRGBV2 extends SensorHandler<BrickletRGBLEDV2> {

    public LedRGBV2(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> send(final Object value) {
        handleConnection(() -> {
            final Optional<Integer> rgbValue = Color.toRGB(value);
            rgbValue.ifPresent(rgb -> applyOnNewValue(CONFIG_COLOR, rgb, () -> sendColor(rgb)));
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> init() {
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> initConfig() {
        handleConnection(() -> {
            final BrickletRGBLEDV2.RGBValue color = device.getRGBValue();
            config.put(CONFIG_HIGH_CONTRAST, 0);
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_COLOR, Color.toRGB(new Color(color.r, color.g, color.b)).orElse(0));
            config.put(CONFIG_INFO_LED_STATUS, color.r == 0 && color.g == 0 && color.b == 0 ? 0 : 1);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> runTest() {
        final int before = getConfig(CONFIG_COLOR).intValue();
        handleConnection(() -> {
            for (int currentColor : Color.RAINBOW) {
                send(currentColor);
                Thread.sleep(1);
            }
            send(before);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 1, () -> sendColor(getConfig(CONFIG_COLOR).intValue()));
        } else if (value == LedStatusType.LED_OFF.bit) {
            applyOnNewValue(CONFIG_INFO_LED_STATUS, 0, () -> sendColor(Color.BLACK));
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDV2> setRefreshPeriod(final int milliseconds) {
        return this;
    }

    private void sendColor(final int rgb) throws TinkerforgeException {
        Color color = new Color(rgb);
        color = getConfig(CONFIG_HIGH_CONTRAST).intValue() == 1 ? Color.convertToHighContrast(color) : color;
        device.setRGBValue(color.getRed(), color.getGreen(), color.getBlue());
    }
}
