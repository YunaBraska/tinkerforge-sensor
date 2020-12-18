package berlin.yuna.tinkerforgesensor.model.helper;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.handler.Accelerometer;
import berlin.yuna.tinkerforgesensor.model.handler.AccelerometerV2;
import berlin.yuna.tinkerforgesensor.model.handler.AirQuality;
import berlin.yuna.tinkerforgesensor.model.handler.Barometer;
import berlin.yuna.tinkerforgesensor.model.handler.BarometerV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonDualV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonMultiTouchV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.handler.Compass;
import berlin.yuna.tinkerforgesensor.model.handler.DcBrick;
import berlin.yuna.tinkerforgesensor.model.handler.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.handler.DisplaySegmentV2;
import berlin.yuna.tinkerforgesensor.model.handler.DistanceIR;
import berlin.yuna.tinkerforgesensor.model.handler.DistanceIRV2;
import berlin.yuna.tinkerforgesensor.model.handler.DistanceUs;
import berlin.yuna.tinkerforgesensor.model.handler.DistanceUsV2;
import berlin.yuna.tinkerforgesensor.model.handler.DummyHandler;
import berlin.yuna.tinkerforgesensor.model.handler.HallEffectV2;
import berlin.yuna.tinkerforgesensor.model.handler.Humidity;
import berlin.yuna.tinkerforgesensor.model.handler.HumidityV2;
import berlin.yuna.tinkerforgesensor.model.handler.ImuBrick;
import berlin.yuna.tinkerforgesensor.model.handler.ImuBrickV2;
import berlin.yuna.tinkerforgesensor.model.handler.JoystickV2;
import berlin.yuna.tinkerforgesensor.model.handler.LedRGBV2;
import berlin.yuna.tinkerforgesensor.model.handler.LightAmbient;
import berlin.yuna.tinkerforgesensor.model.handler.LightAmbientV2;
import berlin.yuna.tinkerforgesensor.model.handler.LightAmbientV3;
import berlin.yuna.tinkerforgesensor.model.handler.LightColor;
import berlin.yuna.tinkerforgesensor.model.handler.LightColorV2;
import berlin.yuna.tinkerforgesensor.model.handler.LightUv;
import berlin.yuna.tinkerforgesensor.model.handler.LightUvV2;
import berlin.yuna.tinkerforgesensor.model.handler.MasterBrick;
import berlin.yuna.tinkerforgesensor.model.handler.MotionDetector;
import berlin.yuna.tinkerforgesensor.model.handler.MotionDetectorV2;
import berlin.yuna.tinkerforgesensor.model.handler.PotiLinearMotored;
import berlin.yuna.tinkerforgesensor.model.handler.PotiLiniarV2;
import berlin.yuna.tinkerforgesensor.model.handler.PotiRotaryEncoderV2;
import berlin.yuna.tinkerforgesensor.model.handler.PotiRotaryV2;
import berlin.yuna.tinkerforgesensor.model.handler.SoundIntensity;
import berlin.yuna.tinkerforgesensor.model.handler.Speaker;
import berlin.yuna.tinkerforgesensor.model.handler.SpeakerV2;
import berlin.yuna.tinkerforgesensor.model.handler.Temperature;
import berlin.yuna.tinkerforgesensor.model.handler.TemperatureV2;
import berlin.yuna.tinkerforgesensor.model.handler.Tilt;

@SuppressWarnings("unused")
public class IsSensor {
    private final Sensor sensor;

    public IsSensor(final Sensor sensor) {
        this.sensor = sensor;
    }

    public boolean accelerometer() {
        return sensor.is(AccelerometerV2.class, Accelerometer.class);
    }

    public boolean airQuality() {
        return sensor.is(AirQuality.class);
    }

    public boolean barometer() {
        return sensor.is(BarometerV2.class, Barometer.class);
    }

    public boolean buttonDual() {
        return sensor.is(ButtonDualV2.class);
    }

    public boolean buttonMultiTouch() {
        return sensor.is(ButtonMultiTouchV2.class);
    }

    public boolean buttonRGB() {
        return sensor.is(ButtonRGB.class);
    }

    public boolean compass() {
        return sensor.is(Compass.class);
    }

    public boolean dcBrick() {
        return sensor.is(DcBrick.class);
    }

    public boolean displaySegment() {
        return sensor.is(DisplaySegmentV2.class, DisplaySegment.class);
    }

    public boolean distanceIR() {
        return sensor.is(DistanceIRV2.class, DistanceIR.class);
    }

    public boolean distanceUs() {
        return sensor.is(DistanceUsV2.class, DistanceUs.class);
    }

    public boolean dummyHandler() {
        return sensor.is(DummyHandler.class);
    }

    public boolean hallEffect() {
        return sensor.is(HallEffectV2.class);
    }

    public boolean humidity() {
        return sensor.is(HumidityV2.class, Humidity.class);
    }

    public boolean imuBrick() {
        return sensor.is(ImuBrickV2.class, ImuBrick.class);
    }

    public boolean joystick() {
        return sensor.is(JoystickV2.class);
    }

    public boolean ledRGB() {
        return sensor.is(LedRGBV2.class);
    }

    public boolean lightAmbient() {
        return sensor.is(LightAmbientV3.class, LightAmbientV2.class, LightAmbient.class);
    }

    public boolean lightColor() {
        return sensor.is(LightColorV2.class, LightColor.class);
    }

    public boolean lightUv() {
        return sensor.is(LightUvV2.class, LightUv.class);
    }

    public boolean masterBrick() {
        return sensor.is(MasterBrick.class);
    }

    public boolean motionDetector() {
        return sensor.is(MotionDetectorV2.class, MotionDetector.class);
    }

    public boolean potiLinearMotored() {
        return sensor.is(PotiLinearMotored.class);
    }

    public boolean potiLiniar() {
        return sensor.is(PotiLiniarV2.class);
    }

    public boolean potiRotaryEncoder() {
        return sensor.is(PotiRotaryEncoderV2.class);
    }

    public boolean potiRotary() {
        return sensor.is(PotiRotaryV2.class);
    }

    public boolean soundIntensity() {
        return sensor.is(SoundIntensity.class);
    }

    public boolean speaker() {
        return sensor.is(SpeakerV2.class, Speaker.class);
    }

    public boolean temperature() {
        return sensor.is(TemperatureV2.class, Temperature.class);
    }

    public boolean tilt() {
        return sensor.is(Tilt.class);
    }
}
