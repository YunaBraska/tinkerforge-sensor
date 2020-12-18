package berlin.yuna.tinkerforgesensor.model.handler;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.threads.Color;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.Optional;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.isLedOn;
import static berlin.yuna.tinkerforgesensor.util.RunThrowable.handleConnection;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.ValueType.BUTTON_RELEASED;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ButtonRGB extends SensorHandler<BrickletRGBLEDButton> {

    public ButtonRGB(final Sensor sensor, final Device device) {
        super(sensor, device);
    }

    @Override
    public SensorHandler<BrickletRGBLEDButton> send(final Object value) {
        handleConnection(() -> {
            final Optional<Integer> rgbValue = Color.toRGB(value);
            rgbValue.ifPresent(rgb -> applyOnNewValue(CONFIG_COLOR, rgb, () -> sendColor(rgb)));
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDButton> init() {
        device.addButtonStateChangedListener(value -> sendEvent((value == 1 ? BUTTON_RELEASED : BUTTON_PRESSED), 0));
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDButton> initConfig() {
        handleConnection(() -> {
            final BrickletRGBLEDButton.Color color = device.getColor();
            config.put(CONFIG_HIGH_CONTRAST, 0);
            config.put(CONFIG_LED_STATUS, device.getStatusLEDConfig());
            config.put(CONFIG_COLOR, Color.toRGB(new Color(color.red, color.green, color.blue)).orElse(0));
            config.put(CONFIG_FUNCTION_A, color.red == 0 && color.green == 0 && color.blue == 0 ? 0 : 1);
        });
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDButton> runTest() {
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
    public SensorHandler<BrickletRGBLEDButton> setStatusLedHandler(final int value) {
        applyOnNewValue(CONFIG_LED_STATUS, value, () -> device.setStatusLEDConfig(value));
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDButton> triggerFunctionA(int value) {
        if (isLedOn(value)) {
            applyOnNewValue(CONFIG_FUNCTION_A, 1, () -> sendColor(getConfig(CONFIG_COLOR).intValue()));
        } else if (value == LedStatusType.LED_ON.bit) {
            applyOnNewValue(CONFIG_FUNCTION_A, 0, () -> sendColor(Color.BLACK));
        }
        return this;
    }

    @Override
    public SensorHandler<BrickletRGBLEDButton> setRefreshPeriod(final int milliseconds) {
        return this;
    }

    private void sendColor(final int rgb) throws TinkerforgeException {
        Color color = new Color(rgb);
        color = getConfig(CONFIG_HIGH_CONTRAST).intValue() == 1 ? Color.convertToHighContrast(color) : color;
        device.setColor(color.getRed(), color.getGreen(), color.getBlue());
    }
}
