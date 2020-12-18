package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.exception.SensorInitialisationException;
import berlin.yuna.tinkerforgesensor.model.Beep;
import berlin.yuna.tinkerforgesensor.model.LedState;
import berlin.yuna.tinkerforgesensor.model.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonDualV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.handler.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.handler.DisplaySegmentV2;
import berlin.yuna.tinkerforgesensor.model.handler.LedRGBV2;
import berlin.yuna.tinkerforgesensor.model.handler.Speaker;
import berlin.yuna.tinkerforgesensor.model.handler.SpeakerV2;
import berlin.yuna.tinkerforgesensor.model.helper.IsSensor;
import berlin.yuna.tinkerforgesensor.model.threads.Color;
import com.tinkerforge.Device;

import java.util.Objects;
import java.util.Optional;

import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.CONFIG_INFO_LED_STATUS;
import static berlin.yuna.tinkerforgesensor.logic.SensorHandler.initHandler;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_ON;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_STATUS;
import static java.util.Arrays.stream;

public class Sensor implements Comparable<Sensor> {

    private final int id;
    private final String uid;
    private final Stack stack;
    private final char position;
    private final String parentUid;
    private final SensorHandler<?> handler;
    private int port = -1;
    private boolean isBrick = false;

    public Sensor(
            final int id,
            final String uid,
            final Stack stack,
            final char position,
            final String parentUid,
            final Class<? extends Device> deviceClass,
            final Class<? extends SensorHandler<?>> handlerClass
    ) {
        this.id = id;
        this.uid = uid;
        this.stack = stack;
        this.position = position;
        this.parentUid = parentUid;
        this.handler = initHandler(this, handlerClass, initDevice(uid, deviceClass, stack));
    }

    public Sensor send(final Object object) {
        handler.send(object);
        return this;
    }

    /**
     * @return true if the sensor uses {@link SensorHandler#CONFIG_HIGH_CONTRAST} see {@link Sensor#setHighContrast(boolean)}
     */
    public boolean hasHighContrast() {
        return handler.hasHighContrast();
    }

    /**
     * Sets {@link SensorHandler#CONFIG_HIGH_CONTRAST} for color inputs of {@link Sensor#setColor(Color)}
     * {@code used by} like {@link ButtonRGB}
     * {@link LedRGBV2}
     *
     * @param highContrast sets {@link SensorHandler#CONFIG_HIGH_CONTRAST}
     * @return self {@link Sensor}
     */
    public Sensor setHighContrast(boolean highContrast) {
        this.handler.setHighContrast(highContrast);
        return this;
    }

    /**
     * @return true if the sensor uses {@link SensorHandler#CONFIG_FREQUENCY} see {@link Sensor#setFrequency(int)}
     */
    public boolean hasFrequency() {
        return handler.hasFrequency();
    }

    /**
     * Sets {@link SensorHandler#CONFIG_FREQUENCY} for sensors
     * {@code used by} like {@link Speaker} (600Hz - 7100Hz)
     * {@link Speaker} (50Hz - 15000Hz)
     *
     * @param frequency sets {@link SensorHandler#CONFIG_FREQUENCY}
     * @return self {@link Sensor}
     */
    public Sensor setFrequency(final int frequency) {
        this.handler.setFrequency(frequency);
        return this;
    }

    /**
     * @return {@link SensorHandler#CONFIG_BRIGHTNESS} value see {@link SensorHandler#setBrightness(int)}
     */
    public int getBrightness() {
        return handler.getBrightness();
    }

    /**
     * Sets {@link SensorHandler#CONFIG_BRIGHTNESS} (0-7) for sensors
     * {@code used by} like {@link DisplaySegment},
     * {@link DisplaySegmentV2}
     *
     * @param brightness sets {@link SensorHandler#CONFIG_BRIGHTNESS}
     * @return self {@link Sensor}
     */
    public Sensor setBrightness(final int brightness) {
        handler.setBrightness(brightness);
        return this;
    }

    /**
     * Used for sensors like {@link {@link ButtonRGB }}
     * {@link LedRGBV2}
     *
     * @param color value to set for the sensor
     * @return self {@link Sensor}
     */
    public Sensor setColor(final Color color) {
        send(color);
        return this;
    }

    /**
     * Used for sensors like {@link {@link ButtonRGB }}
     * {@link LedRGBV2}
     *
     * @param color value to set for the sensor
     * @return self {@link Sensor}
     */
    public Sensor setColor(final Number color) {
        send(color);
        return this;
    }

    /**
     * @return true if this {@link Sensor} has a status led
     */
    public boolean hasStatusLed() {
        return handler.hasStatusLed();
    }

    /**
     * Sets status led to {@link berlin.yuna.tinkerforgesensor.model.LedStatusType#LED_ON}
     *
     * @return self {@link Sensor}
     */
    public Sensor setStatusLedOn() {
        handler.setStatusLed(LED_ON.bit);
        return this;
    }

    /**
     * Sets status led to {@link berlin.yuna.tinkerforgesensor.model.LedStatusType#LED_OFF}
     *
     * @return self {@link Sensor}
     */
    public Sensor setStatusLedOff() {
        handler.setStatusLed(LED_OFF.bit);
        return this;
    }

    /**
     * Sets info led to {@link berlin.yuna.tinkerforgesensor.model.LedStatusType#LED_ON}
     * {@code used by} {@link Sensor}s like
     * {@link berlin.yuna.tinkerforgesensor.model.handler.ImuBrick}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.ImuBrickV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.Accelerometer}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.AccelerometerV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.LedRGBV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.LightColor}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.LightColorV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.MasterBrick}
     *
     * @return self {@link Sensor}
     */
    public Sensor setStatusInfoLedOn() {
        handler.send(LED_ON.bit);
        return this;
    }

    /**
     * Sets info led to {@link berlin.yuna.tinkerforgesensor.model.LedStatusType#LED_OFF}
     * {@code used by} {@link Sensor}s like
     * {@link berlin.yuna.tinkerforgesensor.model.handler.ImuBrick}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.ImuBrickV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.Accelerometer}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.AccelerometerV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.LedRGBV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.LightColor}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.LightColorV2}
     * {@link berlin.yuna.tinkerforgesensor.model.handler.MasterBrick}
     *
     * @return self {@link Sensor}
     */
    public Sensor setInfoLedOff() {
        if (handler.config.containsKey(CONFIG_INFO_LED_STATUS)) {
            handler.send(LED_OFF.bit);
        }
        return this;
    }

    /**
     * Sets status led to {@link berlin.yuna.tinkerforgesensor.model.LedStatusType#LED_STATUS}
     *
     * @return self {@link Sensor}
     */
    public Sensor setStatusLedDefault() {
        if (handler.config.containsKey(CONFIG_INFO_LED_STATUS)) {
            handler.send(LED_STATUS.bit);
        }
        return this;
    }

    /**
     * Sets status led to {@link berlin.yuna.tinkerforgesensor.model.LedStatusType#LED_HEARTBEAT}
     *
     * @return self {@link Sensor}
     */
    public Sensor setStatusLedHeartbeat() {
        handler.setStatusLed(LED_HEARTBEAT.bit);
        return this;
    }

    /**
     * Set the state of an specific led
     * {@code used by} like {@link ButtonDualV2}
     *
     * @return self {@link Sensor}
     */
    public Sensor setLedState(final int ledId, final LedStatusType ledState) {
        return setLedState(ledId, ledState.bit);
    }

    /**
     * Set the state of an specific led
     * {@code used by} like {@link ButtonDualV2}
     *
     * @return self {@link Sensor}
     */
    public Sensor setLedState(final int ledId, final int ledState) {
        send(new LedState(ledId, ledState));
        return this;
    }

    /**
     * Sends a sound - see {@link Sensor#sendSound(int, int)} and {@link Sensor#setFrequency(int)}
     * {@code used by} like {@link Speaker} (600Hz - 7100Hz)
     * {@link SpeakerV2} (50Hz - 15000Hz)
     *
     * @param durationMs time to send the sound
     * @return self {@link Sensor}
     */
    public Sensor sendSound(final int durationMs) {
        return send(new Beep(durationMs, -1, -1, false));
    }

    /**
     * Sends a sound - see {@link Sensor#sendSound(int, int)} and {@link Sensor#setFrequency(int)}
     * {@code used by} like {@link Speaker} (600Hz - 7100Hz)
     * {@link SpeakerV2} (50Hz - 15000Hz)
     *
     * @param durationMs time to send the sound
     * @param wait       waits until the sound is done
     * @return self {@link Sensor}
     */
    public Sensor sendSound(final int durationMs, final boolean wait) {
        return send(new Beep(durationMs, -1, -1, wait));
    }

    /**
     * Sends a sound
     * {@code used by} like {@link Speaker} (600Hz - 7100Hz)
     * {@link SpeakerV2} (50Hz - 15000Hz)
     *
     * @param durationMs time to send the sound
     * @param frequency  frequency for the sound
     * @return self {@link Sensor}
     */
    public Sensor sendSound(final int durationMs, final int frequency) {
        return send(new Beep(durationMs, frequency, -1, false));
    }

    /**
     * Sends a sound
     * {@code used by} like {@link Speaker} (600Hz - 7100Hz) (no volume)
     * {@link SpeakerV2} (50Hz - 15000Hz) (volume: 0-10)
     *
     * @param durationMs time to send the sound
     * @param frequency  frequency for the sound
     * @param volume     for the sound
     * @return self {@link Sensor}
     */
    public Sensor sendSound(final int durationMs, final int frequency, final int volume) {
        return send(new Beep(durationMs, frequency, volume, false));
    }

    /**
     * A {@link Sensor} is just a wrapper of {@link SensorHandler} which can be identical used directly
     *
     * @return self {@link Sensor}
     */
    public SensorHandler<?> handler() {
        return handler;
    }

    private Device initDevice(final String uid, final Class<? extends Device> deviceClass, final Stack stack) {
        try {
            return (Device) deviceClass.getConstructors()[0].newInstance(uid, stack.getConnection());
        } catch (Exception e) {
            throw new SensorInitialisationException("Unable to initialise device id [" + id + "] uid [" + uid + "]", e);
        }
    }


    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return handler.getClass().getSimpleName();
    }

    public Stack getStack() {
        return stack;
    }

    public Device device() {
        return (Device) handler.device();
    }

    public char getPosition() {
        return position;
    }

    public String getParentUid() {
        return parentUid;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isBrick() {
        return isBrick;
    }

    public void isBrick(boolean brick) {
        isBrick = brick;
    }

    public Optional<Sensor> getParent() {
        return Optional.ofNullable(stack.findSensor(parentUid));
    }

    public IsSensor is() {
        return new IsSensor(this);
    }

    public boolean is(final Class<?>... types) {
        return stream(types).anyMatch(type -> {
            if (Device.class.isAssignableFrom(type)) {
                return device().getClass() == type;
            } else if (SensorHandler.class.isAssignableFrom(type)) {
                return handler().getClass() == type;
            }
            return false;
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        return Objects.equals(uid, sensor.uid);
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "name='" + getName() + '\'' +
                ", uid=" + uid +
                ", port=" + port +
                '}';
    }

    @Override
    public int compareTo(final Sensor o) {
        return Integer.compare(port, o.port);
    }

}
