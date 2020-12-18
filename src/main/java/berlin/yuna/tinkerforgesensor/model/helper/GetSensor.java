package berlin.yuna.tinkerforgesensor.model.helper;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.logic.Stack;
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
import java.util.List;

@SuppressWarnings("unused")
public class GetSensor {
    private final Stack stack;

    public GetSensor(final Stack stack) {
        this.stack = stack;
    }

    public Sensor accelerometer() {
        return stack.getSensor(0, AccelerometerV2.class, Accelerometer.class);
    }

    public Sensor accelerometer(int index) {
        return stack.getSensor(index, AccelerometerV2.class, Accelerometer.class);
    }

    public List<Sensor> accelerometerList() {
        return stack.getSensorList(AccelerometerV2.class, Accelerometer.class);
    }

    public Sensor airQuality() {
        return stack.getSensor(0, AirQuality.class);
    }

    public Sensor airQuality(int index) {
        return stack.getSensor(index, AirQuality.class);
    }

    public List<Sensor> airQualityList() {
        return stack.getSensorList(AirQuality.class);
    }

    public Sensor barometer() {
        return stack.getSensor(0, BarometerV2.class, Barometer.class);
    }

    public Sensor barometer(int index) {
        return stack.getSensor(index, BarometerV2.class, Barometer.class);
    }

    public List<Sensor> barometerList() {
        return stack.getSensorList(BarometerV2.class, Barometer.class);
    }

    public Sensor buttonDual() {
        return stack.getSensor(0, ButtonDualV2.class);
    }

    public Sensor buttonDual(int index) {
        return stack.getSensor(index, ButtonDualV2.class);
    }

    public List<Sensor> buttonDualList() {
        return stack.getSensorList(ButtonDualV2.class);
    }

    public Sensor buttonMultiTouch() {
        return stack.getSensor(0, ButtonMultiTouchV2.class);
    }

    public Sensor buttonMultiTouch(int index) {
        return stack.getSensor(index, ButtonMultiTouchV2.class);
    }

    public List<Sensor> buttonMultiTouchList() {
        return stack.getSensorList(ButtonMultiTouchV2.class);
    }

    public Sensor buttonRGB() {
        return stack.getSensor(0, ButtonRGB.class);
    }

    public Sensor buttonRGB(int index) {
        return stack.getSensor(index, ButtonRGB.class);
    }

    public List<Sensor> buttonRGBList() {
        return stack.getSensorList(ButtonRGB.class);
    }

    public Sensor compass() {
        return stack.getSensor(0, Compass.class);
    }

    public Sensor compass(int index) {
        return stack.getSensor(index, Compass.class);
    }

    public List<Sensor> compassList() {
        return stack.getSensorList(Compass.class);
    }

    public Sensor dcBrick() {
        return stack.getSensor(0, DcBrick.class);
    }

    public Sensor dcBrick(int index) {
        return stack.getSensor(index, DcBrick.class);
    }

    public List<Sensor> dcBrickList() {
        return stack.getSensorList(DcBrick.class);
    }

    public Sensor displaySegment() {
        return stack.getSensor(0, DisplaySegmentV2.class, DisplaySegment.class);
    }

    public Sensor displaySegment(int index) {
        return stack.getSensor(index, DisplaySegmentV2.class, DisplaySegment.class);
    }

    public List<Sensor> displaySegmentList() {
        return stack.getSensorList(DisplaySegmentV2.class, DisplaySegment.class);
    }

    public Sensor distanceIR() {
        return stack.getSensor(0, DistanceIRV2.class, DistanceIR.class);
    }

    public Sensor distanceIR(int index) {
        return stack.getSensor(index, DistanceIRV2.class, DistanceIR.class);
    }

    public List<Sensor> distanceIRList() {
        return stack.getSensorList(DistanceIRV2.class, DistanceIR.class);
    }

    public Sensor distanceUs() {
        return stack.getSensor(0, DistanceUsV2.class, DistanceUs.class);
    }

    public Sensor distanceUs(int index) {
        return stack.getSensor(index, DistanceUsV2.class, DistanceUs.class);
    }

    public List<Sensor> distanceUsList() {
        return stack.getSensorList(DistanceUsV2.class, DistanceUs.class);
    }

    public Sensor dummyHandler() {
        return stack.getSensor(0, DummyHandler.class);
    }

    public Sensor dummyHandler(int index) {
        return stack.getSensor(index, DummyHandler.class);
    }

    public List<Sensor> dummyHandlerList() {
        return stack.getSensorList(DummyHandler.class);
    }

    public Sensor hallEffect() {
        return stack.getSensor(0, HallEffectV2.class);
    }

    public Sensor hallEffect(int index) {
        return stack.getSensor(index, HallEffectV2.class);
    }

    public List<Sensor> hallEffectList() {
        return stack.getSensorList(HallEffectV2.class);
    }

    public Sensor humidity() {
        return stack.getSensor(0, HumidityV2.class, Humidity.class);
    }

    public Sensor humidity(int index) {
        return stack.getSensor(index, HumidityV2.class, Humidity.class);
    }

    public List<Sensor> humidityList() {
        return stack.getSensorList(HumidityV2.class, Humidity.class);
    }

    public Sensor imuBrick() {
        return stack.getSensor(0, ImuBrickV2.class, ImuBrick.class);
    }

    public Sensor imuBrick(int index) {
        return stack.getSensor(index, ImuBrickV2.class, ImuBrick.class);
    }

    public List<Sensor> imuBrickList() {
        return stack.getSensorList(ImuBrickV2.class, ImuBrick.class);
    }

    public Sensor joystick() {
        return stack.getSensor(0, JoystickV2.class);
    }

    public Sensor joystick(int index) {
        return stack.getSensor(index, JoystickV2.class);
    }

    public List<Sensor> joystickList() {
        return stack.getSensorList(JoystickV2.class);
    }

    public Sensor ledRGB() {
        return stack.getSensor(0, LedRGBV2.class);
    }

    public Sensor ledRGB(int index) {
        return stack.getSensor(index, LedRGBV2.class);
    }

    public List<Sensor> ledRGBList() {
        return stack.getSensorList(LedRGBV2.class);
    }

    public Sensor lightAmbient() {
        return stack.getSensor(0, LightAmbientV3.class, LightAmbientV2.class, LightAmbient.class);
    }

    public Sensor lightAmbient(int index) {
        return stack.getSensor(index, LightAmbientV3.class, LightAmbientV2.class, LightAmbient.class);
    }

    public List<Sensor> lightAmbientList() {
        return stack.getSensorList(LightAmbientV3.class, LightAmbientV2.class, LightAmbient.class);
    }

    public Sensor lightColor() {
        return stack.getSensor(0, LightColorV2.class, LightColor.class);
    }

    public Sensor lightColor(int index) {
        return stack.getSensor(index, LightColorV2.class, LightColor.class);
    }

    public List<Sensor> lightColorList() {
        return stack.getSensorList(LightColorV2.class, LightColor.class);
    }

    public Sensor lightUv() {
        return stack.getSensor(0, LightUvV2.class, LightUv.class);
    }

    public Sensor lightUv(int index) {
        return stack.getSensor(index, LightUvV2.class, LightUv.class);
    }

    public List<Sensor> lightUvList() {
        return stack.getSensorList(LightUvV2.class, LightUv.class);
    }

    public Sensor masterBrick() {
        return stack.getSensor(0, MasterBrick.class);
    }

    public Sensor masterBrick(int index) {
        return stack.getSensor(index, MasterBrick.class);
    }

    public List<Sensor> masterBrickList() {
        return stack.getSensorList(MasterBrick.class);
    }

    public Sensor motionDetector() {
        return stack.getSensor(0, MotionDetectorV2.class, MotionDetector.class);
    }

    public Sensor motionDetector(int index) {
        return stack.getSensor(index, MotionDetectorV2.class, MotionDetector.class);
    }

    public List<Sensor> motionDetectorList() {
        return stack.getSensorList(MotionDetectorV2.class, MotionDetector.class);
    }

    public Sensor potiLinearMotored() {
        return stack.getSensor(0, PotiLinearMotored.class);
    }

    public Sensor potiLinearMotored(int index) {
        return stack.getSensor(index, PotiLinearMotored.class);
    }

    public List<Sensor> potiLinearMotoredList() {
        return stack.getSensorList(PotiLinearMotored.class);
    }

    public Sensor potiLiniar() {
        return stack.getSensor(0, PotiLiniarV2.class);
    }

    public Sensor potiLiniar(int index) {
        return stack.getSensor(index, PotiLiniarV2.class);
    }

    public List<Sensor> potiLiniarList() {
        return stack.getSensorList(PotiLiniarV2.class);
    }

    public Sensor potiRotaryEncoder() {
        return stack.getSensor(0, PotiRotaryEncoderV2.class);
    }

    public Sensor potiRotaryEncoder(int index) {
        return stack.getSensor(index, PotiRotaryEncoderV2.class);
    }

    public List<Sensor> potiRotaryEncoderList() {
        return stack.getSensorList(PotiRotaryEncoderV2.class);
    }

    public Sensor potiRotary() {
        return stack.getSensor(0, PotiRotaryV2.class);
    }

    public Sensor potiRotary(int index) {
        return stack.getSensor(index, PotiRotaryV2.class);
    }

    public List<Sensor> potiRotaryList() {
        return stack.getSensorList(PotiRotaryV2.class);
    }

    public Sensor soundIntensity() {
        return stack.getSensor(0, SoundIntensity.class);
    }

    public Sensor soundIntensity(int index) {
        return stack.getSensor(index, SoundIntensity.class);
    }

    public List<Sensor> soundIntensityList() {
        return stack.getSensorList(SoundIntensity.class);
    }

    public Sensor speaker() {
        return stack.getSensor(0, SpeakerV2.class, Speaker.class);
    }

    public Sensor speaker(int index) {
        return stack.getSensor(index, SpeakerV2.class, Speaker.class);
    }

    public List<Sensor> speakerList() {
        return stack.getSensorList(SpeakerV2.class, Speaker.class);
    }

    public Sensor temperature() {
        return stack.getSensor(0, TemperatureV2.class, Temperature.class);
    }

    public Sensor temperature(int index) {
        return stack.getSensor(index, TemperatureV2.class, Temperature.class);
    }

    public List<Sensor> temperatureList() {
        return stack.getSensorList(TemperatureV2.class, Temperature.class);
    }

    public Sensor tilt() {
        return stack.getSensor(0, Tilt.class);
    }

    public Sensor tilt(int index) {
        return stack.getSensor(index, Tilt.class);
    }

    public List<Sensor> tiltList() {
        return stack.getSensorList(Tilt.class);
    }
}
