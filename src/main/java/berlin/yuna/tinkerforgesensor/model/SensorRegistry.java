package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.sensor.Accelerometer;
import berlin.yuna.tinkerforgesensor.model.sensor.AccelerometerV2;
import berlin.yuna.tinkerforgesensor.model.sensor.AirQuality;
import berlin.yuna.tinkerforgesensor.model.sensor.Barometer;
import berlin.yuna.tinkerforgesensor.model.sensor.BarometerV2;
import berlin.yuna.tinkerforgesensor.model.sensor.ButtonDual;
import berlin.yuna.tinkerforgesensor.model.sensor.ButtonMultiTouch;
import berlin.yuna.tinkerforgesensor.model.sensor.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.sensor.Compass;
import berlin.yuna.tinkerforgesensor.model.sensor.DC;
import berlin.yuna.tinkerforgesensor.model.sensor.Default;
import berlin.yuna.tinkerforgesensor.model.sensor.DisplayLcd128x64;
import berlin.yuna.tinkerforgesensor.model.sensor.DisplayLcd20x4;
import berlin.yuna.tinkerforgesensor.model.sensor.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.sensor.DisplaySegmentV2;
import berlin.yuna.tinkerforgesensor.model.sensor.DistanceIR;
import berlin.yuna.tinkerforgesensor.model.sensor.DistanceIRV2;
import berlin.yuna.tinkerforgesensor.model.sensor.DistanceUS;
import berlin.yuna.tinkerforgesensor.model.sensor.DistanceUSV2;
import berlin.yuna.tinkerforgesensor.model.sensor.HallEffectV2;
import berlin.yuna.tinkerforgesensor.model.sensor.Humidity;
import berlin.yuna.tinkerforgesensor.model.sensor.HumidityV2;
import berlin.yuna.tinkerforgesensor.model.sensor.IMU;
import berlin.yuna.tinkerforgesensor.model.sensor.IMUV2;
import berlin.yuna.tinkerforgesensor.model.sensor.IO16;
import berlin.yuna.tinkerforgesensor.model.sensor.IO16V2;
import berlin.yuna.tinkerforgesensor.model.sensor.JoystickV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LedRGBV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LedStripV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LightAmbient;
import berlin.yuna.tinkerforgesensor.model.sensor.LightAmbientV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LightAmbientV3;
import berlin.yuna.tinkerforgesensor.model.sensor.LightColor;
import berlin.yuna.tinkerforgesensor.model.sensor.LightColorV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LightUv;
import berlin.yuna.tinkerforgesensor.model.sensor.LightUvV2;
import berlin.yuna.tinkerforgesensor.model.sensor.LocalAudio;
import berlin.yuna.tinkerforgesensor.model.sensor.LocalControl;
import berlin.yuna.tinkerforgesensor.model.sensor.Master;
import berlin.yuna.tinkerforgesensor.model.sensor.MotionDetector;
import berlin.yuna.tinkerforgesensor.model.sensor.MotionDetectorV2;
import berlin.yuna.tinkerforgesensor.model.sensor.PoiLinearMotor;
import berlin.yuna.tinkerforgesensor.model.sensor.PoiLinearV2;
import berlin.yuna.tinkerforgesensor.model.sensor.PotiRotaryV2;
import berlin.yuna.tinkerforgesensor.model.sensor.RotaryV2;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.sensor.Servo;
import berlin.yuna.tinkerforgesensor.model.sensor.SoundIntensity;
import berlin.yuna.tinkerforgesensor.model.sensor.SoundPressure;
import berlin.yuna.tinkerforgesensor.model.sensor.Speaker;
import berlin.yuna.tinkerforgesensor.model.sensor.Temperature;
import berlin.yuna.tinkerforgesensor.model.sensor.TemperatureV2;
import berlin.yuna.tinkerforgesensor.model.sensor.Tilt;
import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickIMU;
import com.tinkerforge.BrickIMUV2;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.BrickServo;
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
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.BrickletIO16V2;
import com.tinkerforge.BrickletJoystickV2;
import com.tinkerforge.BrickletLCD128x64;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletLEDStripV2;
import com.tinkerforge.BrickletLinearPotiV2;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMotionDetectorV2;
import com.tinkerforge.BrickletMotorizedLinearPoti;
import com.tinkerforge.BrickletMultiTouchV2;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.BrickletRGBLEDV2;
import com.tinkerforge.BrickletRotaryEncoderV2;
import com.tinkerforge.BrickletRotaryPotiV2;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.BrickletSegmentDisplay4x7V2;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickletSoundPressureLevel;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.BrickletTemperatureV2;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.BrickletUVLight;
import com.tinkerforge.BrickletUVLightV2;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;
import java.lang.Class;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>{@link SensorRegistry}</h3>
 * <i>contains mapping of {@link Device} and {@link Sensor}</i><br>
 * <i>Autogenerated with [GeneratorSensorRegistry:generate]</i><br>
 * <hr>
 * <ul>
 * <li>Device {@link BrickletAccelerometer} =&gt; Sensor {@link Accelerometer}</li>
 * <li>Device {@link BrickletAccelerometerV2} =&gt; Sensor {@link AccelerometerV2}</li>
 * <li>Device {@link BrickletAirQuality} =&gt; Sensor {@link AirQuality}</li>
 * <li>Device {@link BrickletBarometer} =&gt; Sensor {@link Barometer}</li>
 * <li>Device {@link BrickletBarometerV2} =&gt; Sensor {@link BarometerV2}</li>
 * <li>Device {@link BrickletDualButtonV2} =&gt; Sensor {@link ButtonDual}</li>
 * <li>Device {@link BrickletMultiTouchV2} =&gt; Sensor {@link ButtonMultiTouch}</li>
 * <li>Device {@link BrickletRGBLEDButton} =&gt; Sensor {@link ButtonRGB}</li>
 * <li>Device {@link BrickletCompass} =&gt; Sensor {@link Compass}</li>
 * <li>Device {@link BrickDC} =&gt; Sensor {@link DC}</li>
 * <li>Device {@link DummyDevice} =&gt; Sensor {@link Default}</li>
 * <li>Device {@link BrickletLCD128x64} =&gt; Sensor {@link DisplayLcd128x64}</li>
 * <li>Device {@link BrickletLCD20x4} =&gt; Sensor {@link DisplayLcd20x4}</li>
 * <li>Device {@link BrickletSegmentDisplay4x7} =&gt; Sensor {@link DisplaySegment}</li>
 * <li>Device {@link BrickletSegmentDisplay4x7V2} =&gt; Sensor {@link DisplaySegmentV2}</li>
 * <li>Device {@link BrickletDistanceIR} =&gt; Sensor {@link DistanceIR}</li>
 * <li>Device {@link BrickletDistanceIRV2} =&gt; Sensor {@link DistanceIRV2}</li>
 * <li>Device {@link BrickletDistanceUS} =&gt; Sensor {@link DistanceUS}</li>
 * <li>Device {@link BrickletDistanceUSV2} =&gt; Sensor {@link DistanceUSV2}</li>
 * <li>Device {@link BrickletHallEffectV2} =&gt; Sensor {@link HallEffectV2}</li>
 * <li>Device {@link BrickletHumidity} =&gt; Sensor {@link Humidity}</li>
 * <li>Device {@link BrickletHumidityV2} =&gt; Sensor {@link HumidityV2}</li>
 * <li>Device {@link BrickIMU} =&gt; Sensor {@link IMU}</li>
 * <li>Device {@link BrickIMUV2} =&gt; Sensor {@link IMUV2}</li>
 * <li>Device {@link BrickletIO16} =&gt; Sensor {@link IO16}</li>
 * <li>Device {@link BrickletIO16V2} =&gt; Sensor {@link IO16V2}</li>
 * <li>Device {@link BrickletJoystickV2} =&gt; Sensor {@link JoystickV2}</li>
 * <li>Device {@link BrickletRGBLEDV2} =&gt; Sensor {@link LedRGBV2}</li>
 * <li>Device {@link BrickletLEDStripV2} =&gt; Sensor {@link LedStripV2}</li>
 * <li>Device {@link BrickletAmbientLight} =&gt; Sensor {@link LightAmbient}</li>
 * <li>Device {@link BrickletAmbientLightV2} =&gt; Sensor {@link LightAmbientV2}</li>
 * <li>Device {@link BrickletAmbientLightV3} =&gt; Sensor {@link LightAmbientV3}</li>
 * <li>Device {@link BrickletColor} =&gt; Sensor {@link LightColor}</li>
 * <li>Device {@link BrickletColorV2} =&gt; Sensor {@link LightColorV2}</li>
 * <li>Device {@link BrickletUVLight} =&gt; Sensor {@link LightUv}</li>
 * <li>Device {@link BrickletUVLightV2} =&gt; Sensor {@link LightUvV2}</li>
 * <li>Device {@link DummyDevice} =&gt; Sensor {@link LocalAudio}</li>
 * <li>Device {@link DummyDevice} =&gt; Sensor {@link LocalControl}</li>
 * <li>Device {@link BrickMaster} =&gt; Sensor {@link Master}</li>
 * <li>Device {@link BrickletMotionDetector} =&gt; Sensor {@link MotionDetector}</li>
 * <li>Device {@link BrickletMotionDetectorV2} =&gt; Sensor {@link MotionDetectorV2}</li>
 * <li>Device {@link BrickletMotorizedLinearPoti} =&gt; Sensor {@link PoiLinearMotor}</li>
 * <li>Device {@link BrickletLinearPotiV2} =&gt; Sensor {@link PoiLinearV2}</li>
 * <li>Device {@link BrickletRotaryPotiV2} =&gt; Sensor {@link PotiRotaryV2}</li>
 * <li>Device {@link BrickletRotaryEncoderV2} =&gt; Sensor {@link RotaryV2}</li>
 * <li>Device {@link BrickServo} =&gt; Sensor {@link Servo}</li>
 * <li>Device {@link BrickletSoundIntensity} =&gt; Sensor {@link SoundIntensity}</li>
 * <li>Device {@link BrickletSoundPressureLevel} =&gt; Sensor {@link SoundPressure}</li>
 * <li>Device {@link BrickletPiezoSpeaker} =&gt; Sensor {@link Speaker}</li>
 * <li>Device {@link BrickletTemperature} =&gt; Sensor {@link Temperature}</li>
 * <li>Device {@link BrickletTemperatureV2} =&gt; Sensor {@link TemperatureV2}</li>
 * <li>Device {@link BrickletTilt} =&gt; Sensor {@link Tilt}</li>
 * </ul> */
public class SensorRegistry {
    public static final int CALLBACK_PERIOD = 64;

    private static final ConcurrentHashMap<Class<? extends Device>, Sensor.SensorFactory> sensorMap = initSensor();

    private static final ConcurrentHashMap<Integer, Sensor.DeviceFactory> deviceMap = initDevice();

    public static Sensor.SensorFactory getSensor(final Class<? extends Device> device) {
        return sensorMap.get(device);
    }

    public static Sensor.DeviceFactory getDevice(final Integer deviceIdentifier) {
        return deviceMap.get(deviceIdentifier);
    }

    public static List<Class> getDeviceAvailableDevices() {
        return new ArrayList<>(sensorMap.keySet());
    }

    private static ConcurrentHashMap<Class<? extends Device>, Sensor.SensorFactory> initSensor() {
        final ConcurrentHashMap<Class<? extends Device>, Sensor.SensorFactory> registry = new ConcurrentHashMap<>();
        registry.put(BrickletAccelerometer.class, Accelerometer::new);
        registry.put(BrickletAccelerometerV2.class, AccelerometerV2::new);
        registry.put(BrickletAirQuality.class, AirQuality::new);
        registry.put(BrickletBarometer.class, Barometer::new);
        registry.put(BrickletBarometerV2.class, BarometerV2::new);
        registry.put(BrickletDualButtonV2.class, ButtonDual::new);
        registry.put(BrickletMultiTouchV2.class, ButtonMultiTouch::new);
        registry.put(BrickletRGBLEDButton.class, ButtonRGB::new);
        registry.put(BrickletCompass.class, Compass::new);
        registry.put(BrickDC.class, DC::new);
        registry.put(DummyDevice.class, Default::new);
        registry.put(BrickletLCD128x64.class, DisplayLcd128x64::new);
        registry.put(BrickletLCD20x4.class, DisplayLcd20x4::new);
        registry.put(BrickletSegmentDisplay4x7.class, DisplaySegment::new);
        registry.put(BrickletSegmentDisplay4x7V2.class, DisplaySegmentV2::new);
        registry.put(BrickletDistanceIR.class, DistanceIR::new);
        registry.put(BrickletDistanceIRV2.class, DistanceIRV2::new);
        registry.put(BrickletDistanceUS.class, DistanceUS::new);
        registry.put(BrickletDistanceUSV2.class, DistanceUSV2::new);
        registry.put(BrickletHallEffectV2.class, HallEffectV2::new);
        registry.put(BrickletHumidity.class, Humidity::new);
        registry.put(BrickletHumidityV2.class, HumidityV2::new);
        registry.put(BrickIMU.class, IMU::new);
        registry.put(BrickIMUV2.class, IMUV2::new);
        registry.put(BrickletIO16.class, IO16::new);
        registry.put(BrickletIO16V2.class, IO16V2::new);
        registry.put(BrickletJoystickV2.class, JoystickV2::new);
        registry.put(BrickletRGBLEDV2.class, LedRGBV2::new);
        registry.put(BrickletLEDStripV2.class, LedStripV2::new);
        registry.put(BrickletAmbientLight.class, LightAmbient::new);
        registry.put(BrickletAmbientLightV2.class, LightAmbientV2::new);
        registry.put(BrickletAmbientLightV3.class, LightAmbientV3::new);
        registry.put(BrickletColor.class, LightColor::new);
        registry.put(BrickletColorV2.class, LightColorV2::new);
        registry.put(BrickletUVLight.class, LightUv::new);
        registry.put(BrickletUVLightV2.class, LightUvV2::new);
        registry.put(DummyDevice.class, LocalAudio::new);
        registry.put(DummyDevice.class, LocalControl::new);
        registry.put(BrickMaster.class, Master::new);
        registry.put(BrickletMotionDetector.class, MotionDetector::new);
        registry.put(BrickletMotionDetectorV2.class, MotionDetectorV2::new);
        registry.put(BrickletMotorizedLinearPoti.class, PoiLinearMotor::new);
        registry.put(BrickletLinearPotiV2.class, PoiLinearV2::new);
        registry.put(BrickletRotaryPotiV2.class, PotiRotaryV2::new);
        registry.put(BrickletRotaryEncoderV2.class, RotaryV2::new);
        registry.put(BrickServo.class, Servo::new);
        registry.put(BrickletSoundIntensity.class, SoundIntensity::new);
        registry.put(BrickletSoundPressureLevel.class, SoundPressure::new);
        registry.put(BrickletPiezoSpeaker.class, Speaker::new);
        registry.put(BrickletTemperature.class, Temperature::new);
        registry.put(BrickletTemperatureV2.class, TemperatureV2::new);
        registry.put(BrickletTilt.class, Tilt::new);
        return registry;
    }

    private static ConcurrentHashMap<Integer, Sensor.DeviceFactory> initDevice() {
        final ConcurrentHashMap<Integer, Sensor.DeviceFactory> registry = new ConcurrentHashMap<>();
        registry.put(BrickletAccelerometer.DEVICE_IDENTIFIER, BrickletAccelerometer::new);
        registry.put(BrickletAccelerometerV2.DEVICE_IDENTIFIER, BrickletAccelerometerV2::new);
        registry.put(BrickletAirQuality.DEVICE_IDENTIFIER, BrickletAirQuality::new);
        registry.put(BrickletBarometer.DEVICE_IDENTIFIER, BrickletBarometer::new);
        registry.put(BrickletBarometerV2.DEVICE_IDENTIFIER, BrickletBarometerV2::new);
        registry.put(BrickletDualButtonV2.DEVICE_IDENTIFIER, BrickletDualButtonV2::new);
        registry.put(BrickletMultiTouchV2.DEVICE_IDENTIFIER, BrickletMultiTouchV2::new);
        registry.put(BrickletRGBLEDButton.DEVICE_IDENTIFIER, BrickletRGBLEDButton::new);
        registry.put(BrickletCompass.DEVICE_IDENTIFIER, BrickletCompass::new);
        registry.put(BrickDC.DEVICE_IDENTIFIER, BrickDC::new);
        registry.put(DummyDevice.DEVICE_IDENTIFIER, DummyDevice::new);
        registry.put(BrickletLCD128x64.DEVICE_IDENTIFIER, BrickletLCD128x64::new);
        registry.put(BrickletLCD20x4.DEVICE_IDENTIFIER, BrickletLCD20x4::new);
        registry.put(BrickletSegmentDisplay4x7.DEVICE_IDENTIFIER, BrickletSegmentDisplay4x7::new);
        registry.put(BrickletSegmentDisplay4x7V2.DEVICE_IDENTIFIER, BrickletSegmentDisplay4x7V2::new);
        registry.put(BrickletDistanceIR.DEVICE_IDENTIFIER, BrickletDistanceIR::new);
        registry.put(BrickletDistanceIRV2.DEVICE_IDENTIFIER, BrickletDistanceIRV2::new);
        registry.put(BrickletDistanceUS.DEVICE_IDENTIFIER, BrickletDistanceUS::new);
        registry.put(BrickletDistanceUSV2.DEVICE_IDENTIFIER, BrickletDistanceUSV2::new);
        registry.put(BrickletHallEffectV2.DEVICE_IDENTIFIER, BrickletHallEffectV2::new);
        registry.put(BrickletHumidity.DEVICE_IDENTIFIER, BrickletHumidity::new);
        registry.put(BrickletHumidityV2.DEVICE_IDENTIFIER, BrickletHumidityV2::new);
        registry.put(BrickIMU.DEVICE_IDENTIFIER, BrickIMU::new);
        registry.put(BrickIMUV2.DEVICE_IDENTIFIER, BrickIMUV2::new);
        registry.put(BrickletIO16.DEVICE_IDENTIFIER, BrickletIO16::new);
        registry.put(BrickletIO16V2.DEVICE_IDENTIFIER, BrickletIO16V2::new);
        registry.put(BrickletJoystickV2.DEVICE_IDENTIFIER, BrickletJoystickV2::new);
        registry.put(BrickletRGBLEDV2.DEVICE_IDENTIFIER, BrickletRGBLEDV2::new);
        registry.put(BrickletLEDStripV2.DEVICE_IDENTIFIER, BrickletLEDStripV2::new);
        registry.put(BrickletAmbientLight.DEVICE_IDENTIFIER, BrickletAmbientLight::new);
        registry.put(BrickletAmbientLightV2.DEVICE_IDENTIFIER, BrickletAmbientLightV2::new);
        registry.put(BrickletAmbientLightV3.DEVICE_IDENTIFIER, BrickletAmbientLightV3::new);
        registry.put(BrickletColor.DEVICE_IDENTIFIER, BrickletColor::new);
        registry.put(BrickletColorV2.DEVICE_IDENTIFIER, BrickletColorV2::new);
        registry.put(BrickletUVLight.DEVICE_IDENTIFIER, BrickletUVLight::new);
        registry.put(BrickletUVLightV2.DEVICE_IDENTIFIER, BrickletUVLightV2::new);
        registry.put(DummyDevice.DEVICE_IDENTIFIER, DummyDevice::new);
        registry.put(DummyDevice.DEVICE_IDENTIFIER, DummyDevice::new);
        registry.put(BrickMaster.DEVICE_IDENTIFIER, BrickMaster::new);
        registry.put(BrickletMotionDetector.DEVICE_IDENTIFIER, BrickletMotionDetector::new);
        registry.put(BrickletMotionDetectorV2.DEVICE_IDENTIFIER, BrickletMotionDetectorV2::new);
        registry.put(BrickletMotorizedLinearPoti.DEVICE_IDENTIFIER, BrickletMotorizedLinearPoti::new);
        registry.put(BrickletLinearPotiV2.DEVICE_IDENTIFIER, BrickletLinearPotiV2::new);
        registry.put(BrickletRotaryPotiV2.DEVICE_IDENTIFIER, BrickletRotaryPotiV2::new);
        registry.put(BrickletRotaryEncoderV2.DEVICE_IDENTIFIER, BrickletRotaryEncoderV2::new);
        registry.put(BrickServo.DEVICE_IDENTIFIER, BrickServo::new);
        registry.put(BrickletSoundIntensity.DEVICE_IDENTIFIER, BrickletSoundIntensity::new);
        registry.put(BrickletSoundPressureLevel.DEVICE_IDENTIFIER, BrickletSoundPressureLevel::new);
        registry.put(BrickletPiezoSpeaker.DEVICE_IDENTIFIER, BrickletPiezoSpeaker::new);
        registry.put(BrickletTemperature.DEVICE_IDENTIFIER, BrickletTemperature::new);
        registry.put(BrickletTemperatureV2.DEVICE_IDENTIFIER, BrickletTemperatureV2::new);
        registry.put(BrickletTilt.DEVICE_IDENTIFIER, BrickletTilt::new);
        return registry;
    }
}
