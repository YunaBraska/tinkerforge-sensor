package berlin.yuna.tinkerforgesensor.model.builder;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.AccelerometerV2;
import berlin.yuna.tinkerforgesensor.model.sensor.BarometerV2;
import berlin.yuna.tinkerforgesensor.model.sensor.Default;
import berlin.yuna.tinkerforgesensor.model.sensor.DisplaySegmentV2;
import berlin.yuna.tinkerforgesensor.model.sensor.DistanceIRV2;
import berlin.yuna.tinkerforgesensor.model.sensor.DistanceUSV2;
import berlin.yuna.tinkerforgesensor.model.sensor.HallEffectV2;
import berlin.yuna.tinkerforgesensor.model.sensor.HumidityV2;
import berlin.yuna.tinkerforgesensor.model.sensor.IMUV2;
import berlin.yuna.tinkerforgesensor.model.sensor.IO16V2;
import berlin.yuna.tinkerforgesensor.model.sensor.JoystickV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LedRGBV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LedStripV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LightAmbientV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LightColorV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LightUvV2;
import berlin.yuna.tinkerforgesensor.model.sensor.MotionDetectorV2;
import berlin.yuna.tinkerforgesensor.model.sensor.PoiLinearV2;
import berlin.yuna.tinkerforgesensor.model.sensor.PotiRotaryV2;
import berlin.yuna.tinkerforgesensor.model.sensor.RotaryV2;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.sensor.SpeakerV2;
import berlin.yuna.tinkerforgesensor.model.sensor.TemperatureV2;
import java.lang.Class;
import java.lang.RuntimeException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Autogenerated with [GeneratorSensors:generate]
 */
public class SensorsV2 extends CopyOnWriteArrayList<Sensor> {
    public SensorsV2() {
    }

    public SensorsV2(final Collection<? extends Sensor> collection) {
        super(collection);
    }

    public SensorsV2(final Sensor[] toCopyIn) {
        super(toCopyIn);
    }

    public AccelerometerV2 accelerometer() {
        return accelerometer(0);
    }

    public AccelerometerV2 accelerometer(final int number) {
        return (AccelerometerV2) getSensor(number, AccelerometerV2.class);
    }

    public BarometerV2 barometer() {
        return barometer(0);
    }

    public BarometerV2 barometer(final int number) {
        return (BarometerV2) getSensor(number, BarometerV2.class);
    }

    public DisplaySegmentV2 displaySegment() {
        return displaySegment(0);
    }

    public DisplaySegmentV2 displaySegment(final int number) {
        return (DisplaySegmentV2) getSensor(number, DisplaySegmentV2.class);
    }

    public DistanceIRV2 distanceIR() {
        return distanceIR(0);
    }

    public DistanceIRV2 distanceIR(final int number) {
        return (DistanceIRV2) getSensor(number, DistanceIRV2.class);
    }

    public DistanceUSV2 distanceUS() {
        return distanceUS(0);
    }

    public DistanceUSV2 distanceUS(final int number) {
        return (DistanceUSV2) getSensor(number, DistanceUSV2.class);
    }

    public HallEffectV2 hallEffect() {
        return hallEffect(0);
    }

    public HallEffectV2 hallEffect(final int number) {
        return (HallEffectV2) getSensor(number, HallEffectV2.class);
    }

    public HumidityV2 humidity() {
        return humidity(0);
    }

    public HumidityV2 humidity(final int number) {
        return (HumidityV2) getSensor(number, HumidityV2.class);
    }

    public IMUV2 iMU() {
        return iMU(0);
    }

    public IMUV2 iMU(final int number) {
        return (IMUV2) getSensor(number, IMUV2.class);
    }

    public IO16V2 iO16() {
        return iO16(0);
    }

    public IO16V2 iO16(final int number) {
        return (IO16V2) getSensor(number, IO16V2.class);
    }

    public JoystickV2 joystick() {
        return joystick(0);
    }

    public JoystickV2 joystick(final int number) {
        return (JoystickV2) getSensor(number, JoystickV2.class);
    }

    public LedRGBV2 ledRGB() {
        return ledRGB(0);
    }

    public LedRGBV2 ledRGB(final int number) {
        return (LedRGBV2) getSensor(number, LedRGBV2.class);
    }

    public LedStripV2 ledStrip() {
        return ledStrip(0);
    }

    public LedStripV2 ledStrip(final int number) {
        return (LedStripV2) getSensor(number, LedStripV2.class);
    }

    public LightAmbientV2 lightAmbient() {
        return lightAmbient(0);
    }

    public LightAmbientV2 lightAmbient(final int number) {
        return (LightAmbientV2) getSensor(number, LightAmbientV2.class);
    }

    public LightColorV2 lightColor() {
        return lightColor(0);
    }

    public LightColorV2 lightColor(final int number) {
        return (LightColorV2) getSensor(number, LightColorV2.class);
    }

    public LightUvV2 lightUv() {
        return lightUv(0);
    }

    public LightUvV2 lightUv(final int number) {
        return (LightUvV2) getSensor(number, LightUvV2.class);
    }

    public MotionDetectorV2 motionDetector() {
        return motionDetector(0);
    }

    public MotionDetectorV2 motionDetector(final int number) {
        return (MotionDetectorV2) getSensor(number, MotionDetectorV2.class);
    }

    public PoiLinearV2 poiLinear() {
        return poiLinear(0);
    }

    public PoiLinearV2 poiLinear(final int number) {
        return (PoiLinearV2) getSensor(number, PoiLinearV2.class);
    }

    public PotiRotaryV2 potiRotary() {
        return potiRotary(0);
    }

    public PotiRotaryV2 potiRotary(final int number) {
        return (PotiRotaryV2) getSensor(number, PotiRotaryV2.class);
    }

    public RotaryV2 rotary() {
        return rotary(0);
    }

    public RotaryV2 rotary(final int number) {
        return (RotaryV2) getSensor(number, RotaryV2.class);
    }

    public SpeakerV2 speaker() {
        return speaker(0);
    }

    public SpeakerV2 speaker(final int number) {
        return (SpeakerV2) getSensor(number, SpeakerV2.class);
    }

    public TemperatureV2 temperature() {
        return temperature(0);
    }

    public TemperatureV2 temperature(final int number) {
        return (TemperatureV2) getSensor(number, TemperatureV2.class);
    }

    private synchronized List<Sensor> getSensor(final Class... sensorClasses) {
        return stream().filter(sensor -> sensor.compare().is(sensorClasses)).sorted(java.util.Comparator.comparingInt(Sensor::port)).collect(java.util.stream.Collectors.toList());
    }

    private synchronized Sensor getSensor(final int number, final Class... sensorClasses) {
        final List<Sensor> sensors = getSensor(sensorClasses);
        return number < sensors.size() ? sensors.get(number) : getDefaultSensor(sensorClasses[0]);
    }

    private Sensor getDefaultSensor(final Class sensorClass) {
        try {
            return new Default(sensorClass);
        } catch (NetworkConnectionException e) {
            throw new RuntimeException("Default device should not run into an exception", e);
        }
    }
}
