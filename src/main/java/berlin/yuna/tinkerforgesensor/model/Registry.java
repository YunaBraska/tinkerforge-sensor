package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.handler.Accelerometer;
import berlin.yuna.tinkerforgesensor.model.handler.AccelerometerV2;
import berlin.yuna.tinkerforgesensor.model.handler.AirQuality;
import berlin.yuna.tinkerforgesensor.model.handler.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.handler.LedRGBV2;
import berlin.yuna.tinkerforgesensor.model.handler.LightAmbient;
import berlin.yuna.tinkerforgesensor.model.handler.LightAmbientV2;
import berlin.yuna.tinkerforgesensor.model.handler.LightAmbientV3;
import berlin.yuna.tinkerforgesensor.model.handler.LightColor;
import berlin.yuna.tinkerforgesensor.model.handler.MasterBrick;
import berlin.yuna.tinkerforgesensor.model.handler.PotiLinearMotored;
import berlin.yuna.tinkerforgesensor.model.handler.PotiLiniarV2;
import berlin.yuna.tinkerforgesensor.model.handler.PotiRotaryEncoderV2;
import berlin.yuna.tinkerforgesensor.model.handler.PotiRotaryV2;
import berlin.yuna.tinkerforgesensor.model.handler.SoundIntensity;
import berlin.yuna.tinkerforgesensor.model.handler.Speaker;
import berlin.yuna.tinkerforgesensor.model.handler.SpeakerV2;
import berlin.yuna.tinkerforgesensor.logic.SensorHandler;
import berlin.yuna.tinkerforgesensor.model.handler.Barometer;
import berlin.yuna.tinkerforgesensor.model.handler.BarometerV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonDualV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonMultiTouchV2;
import berlin.yuna.tinkerforgesensor.model.handler.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.handler.Compass;
import berlin.yuna.tinkerforgesensor.model.handler.DcBrick;
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
import berlin.yuna.tinkerforgesensor.model.handler.LightColorV2;
import berlin.yuna.tinkerforgesensor.model.handler.LightUv;
import berlin.yuna.tinkerforgesensor.model.handler.LightUvV2;
import berlin.yuna.tinkerforgesensor.model.handler.MotionDetector;
import berlin.yuna.tinkerforgesensor.model.handler.MotionDetectorV2;
import berlin.yuna.tinkerforgesensor.model.handler.Temperature;
import berlin.yuna.tinkerforgesensor.model.handler.TemperatureV2;
import berlin.yuna.tinkerforgesensor.model.handler.Tilt;
import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.BrickIMUV2;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.BrickletAccelerometer;
import com.tinkerforge.BrickletAccelerometerV2;
import com.tinkerforge.BrickletAirQuality;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.BrickletAmbientLightV3;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletBarometerV2;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.BrickletColorV2;
import com.tinkerforge.BrickletCompass;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.BrickletDistanceIRV2;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.BrickletDistanceUSV2;
import com.tinkerforge.BrickletDualButtonV2;
import com.tinkerforge.BrickletHallEffectV2;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletHumidityV2;
import com.tinkerforge.BrickletJoystickV2;
import com.tinkerforge.BrickletLinearPotiV2;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMotionDetectorV2;
import com.tinkerforge.BrickletMotorizedLinearPoti;
import com.tinkerforge.BrickletMultiTouchV2;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.BrickletPiezoSpeakerV2;
import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.BrickletRGBLEDV2;
import com.tinkerforge.BrickletRotaryEncoderV2;
import com.tinkerforge.BrickletRotaryPotiV2;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.BrickletSegmentDisplay4x7V2;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.BrickletTemperatureIRV2;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.BrickletUVLight;
import com.tinkerforge.BrickletUVLightV2;
import com.tinkerforge.Device;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.model.Registry.Register.of;

public class Registry {

    private Registry() {
    }

    private static final Map<Integer, Register> REGISTER = init();

    public static Optional<Register> findById(final int id) {
        return REGISTER.entrySet().stream().filter(entry -> entry.getKey() == id).findFirst().map(Map.Entry::getValue);
    }

    private static Map<Integer, Register> init() {
        final Map<Integer, Register> result = new HashMap<>();
        result.put(BrickDC.DEVICE_IDENTIFIER, of(DcBrick.class, BrickDC.class));
        result.put(BrickIMU.DEVICE_IDENTIFIER, of(ImuBrick.class, BrickIMU.class));
        result.put(BrickletTilt.DEVICE_IDENTIFIER, of(Tilt.class, BrickletTilt.class));
        result.put(BrickIMUV2.DEVICE_IDENTIFIER, of(ImuBrickV2.class, BrickIMUV2.class));
        result.put(BrickMaster.DEVICE_IDENTIFIER, of(MasterBrick.class, BrickMaster.class));
        result.put(BrickletColor.DEVICE_IDENTIFIER, of(LightColor.class, BrickletColor.class));
        result.put(BrickletUVLight.DEVICE_IDENTIFIER, of(LightUv.class, BrickletUVLight.class));
        result.put(BrickletCompass.DEVICE_IDENTIFIER, of(Compass.class, BrickletCompass.class));
        result.put(BrickletRGBLEDV2.DEVICE_IDENTIFIER, of(LedRGBV2.class, BrickletRGBLEDV2.class));
        result.put(BrickletHumidity.DEVICE_IDENTIFIER, of(Humidity.class, BrickletHumidity.class));
        result.put(BrickletColorV2.DEVICE_IDENTIFIER, of(LightColorV2.class, BrickletColorV2.class));
        result.put(BrickletBarometer.DEVICE_IDENTIFIER, of(Barometer.class, BrickletBarometer.class));
        result.put(BrickletUVLightV2.DEVICE_IDENTIFIER, of(LightUvV2.class, BrickletUVLightV2.class));
        result.put(BrickletJoystickV2.DEVICE_IDENTIFIER, of(JoystickV2.class, BrickletJoystickV2.class));
        result.put(BrickletHumidityV2.DEVICE_IDENTIFIER, of(HumidityV2.class, BrickletHumidityV2.class));
        result.put(BrickletDistanceIR.DEVICE_IDENTIFIER, of(DistanceIR.class, BrickletDistanceIR.class));
        result.put(BrickletDistanceUS.DEVICE_IDENTIFIER, of(DistanceUs.class, BrickletDistanceUS.class));
        result.put(BrickletAirQuality.DEVICE_IDENTIFIER, of(AirQuality.class, BrickletAirQuality.class));
        result.put(BrickletPiezoSpeaker.DEVICE_IDENTIFIER, of(Speaker.class, BrickletPiezoSpeaker.class));
        result.put(BrickletTemperature.DEVICE_IDENTIFIER, of(Temperature.class, BrickletTemperature.class));
        result.put(BrickletBarometerV2.DEVICE_IDENTIFIER, of(BarometerV2.class, BrickletBarometerV2.class));
        result.put(BrickletRGBLEDButton.DEVICE_IDENTIFIER, of(ButtonRGB.class, BrickletRGBLEDButton.class));
        result.put(BrickletLinearPotiV2.DEVICE_IDENTIFIER, of(PotiLiniarV2.class, BrickletLinearPotiV2.class));
        result.put(BrickletRotaryPotiV2.DEVICE_IDENTIFIER, of(PotiRotaryV2.class, BrickletRotaryPotiV2.class));
        result.put(BrickletAmbientLight.DEVICE_IDENTIFIER, of(LightAmbient.class, BrickletAmbientLight.class));
        result.put(BrickletHallEffectV2.DEVICE_IDENTIFIER, of(HallEffectV2.class, BrickletHallEffectV2.class));
        result.put(BrickletDistanceIRV2.DEVICE_IDENTIFIER, of(DistanceIRV2.class, BrickletDistanceIRV2.class));
        result.put(BrickletDistanceUSV2.DEVICE_IDENTIFIER, of(DistanceUsV2.class, BrickletDistanceUSV2.class));
        result.put(BrickletDualButtonV2.DEVICE_IDENTIFIER, of(ButtonDualV2.class, BrickletDualButtonV2.class));
        result.put(BrickletPiezoSpeakerV2.DEVICE_IDENTIFIER, of(SpeakerV2.class, BrickletPiezoSpeakerV2.class));
        result.put(BrickletAccelerometer.DEVICE_IDENTIFIER, of(Accelerometer.class, BrickletAccelerometer.class));
        result.put(BrickletSoundIntensity.DEVICE_IDENTIFIER, of(SoundIntensity.class, BrickletSoundIntensity.class));
        result.put(BrickletMotionDetector.DEVICE_IDENTIFIER, of(MotionDetector.class, BrickletMotionDetector.class));
        result.put(BrickletAmbientLightV2.DEVICE_IDENTIFIER, of(LightAmbientV2.class, BrickletAmbientLightV2.class));
        result.put(BrickletAmbientLightV3.DEVICE_IDENTIFIER, of(LightAmbientV3.class, BrickletAmbientLightV3.class));
        result.put(BrickletMultiTouchV2.DEVICE_IDENTIFIER, of(ButtonMultiTouchV2.class, BrickletMultiTouchV2.class));
        result.put(BrickletTemperatureIRV2.DEVICE_IDENTIFIER, of(TemperatureV2.class, BrickletTemperatureIRV2.class));
        result.put(BrickletAccelerometerV2.DEVICE_IDENTIFIER, of(AccelerometerV2.class, BrickletAccelerometerV2.class));
        result.put(BrickletMotionDetectorV2.DEVICE_IDENTIFIER, of(MotionDetectorV2.class, BrickletMotionDetectorV2.class));
        result.put(BrickletSegmentDisplay4x7.DEVICE_IDENTIFIER, of(DisplaySegment.class, BrickletSegmentDisplay4x7.class));
        result.put(BrickletRotaryEncoderV2.DEVICE_IDENTIFIER, of(PotiRotaryEncoderV2.class, BrickletRotaryEncoderV2.class));
        result.put(BrickletSegmentDisplay4x7V2.DEVICE_IDENTIFIER, of(DisplaySegmentV2.class, BrickletSegmentDisplay4x7V2.class));
        result.put(BrickletMotorizedLinearPoti.DEVICE_IDENTIFIER, of(PotiLinearMotored.class, BrickletMotorizedLinearPoti.class));
        result.put(-1, of(DummyHandler.class, BrickMaster.class));
        return result;
    }

    public static Set<Class<? extends Device>> getDeviceAvailableDevices() {
        return REGISTER.values().stream().map(Register::getType).collect(Collectors.toSet());
    }

    public static class Register {
        final Class<? extends SensorHandler<?>> handler;
        final Class<? extends Device> type;

        public static Register of(final Class<? extends SensorHandler<?>> handler, final Class<? extends Device> deviceClass) {
            return new Register(handler, deviceClass);
        }

        private Register(final Class<? extends SensorHandler<?>> handler, final Class<? extends Device> type) {
            this.handler = handler;
            this.type = type;
        }

        public Class<? extends SensorHandler<?>> getHandler() {
            return handler;
        }

        public Class<? extends Device> getType() {
            return type;
        }
    }
}
