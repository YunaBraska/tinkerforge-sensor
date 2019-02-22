package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.driver.brick.DC;
import berlin.yuna.tinkerforgesensor.model.driver.brick.IMU;
import berlin.yuna.tinkerforgesensor.model.driver.brick.IMU2;
import berlin.yuna.tinkerforgesensor.model.driver.brick.Master;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.AirQuality;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Barometer;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.BarometerV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Default;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DisplayLcd20x4;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DistanceIR;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DistanceIRV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DistanceUS;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Humidity;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.HumidityV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.IO16;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.IO16V2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightAmbient;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightAmbientV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightAmbientV3;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightColor;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightUv;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightUvV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.MotionDetector;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.MotionDetectorV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.RotaryV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.SoundIntensity;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Speaker;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Temperature;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.TemperatureV2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Tilt;

/**
 * Autogenerated with [GeneratorSensorList:generate]
 */
public class SensorList<T extends Sensor> extends SensorListBasic<T> {
  public Sensor getTilt() {
    return first(Tilt.class);
  }

  public Sensor getTemperature() {
    Sensor sensor = first(TemperatureV2.class);
    return sensor.isPresent() ? sensor : first(Temperature.class);
  }

  public Sensor getSpeaker() {
    return first(Speaker.class);
  }

  public Sensor getSoundIntensity() {
    return first(SoundIntensity.class);
  }

  public Sensor getRotary() {
    return first(RotaryV2.class);
  }

  public Sensor getMotionDetector() {
    Sensor sensor = first(MotionDetectorV2.class);
    return sensor.isPresent() ? sensor : first(MotionDetector.class);
  }

  public Sensor getMaster() {
    return first(Master.class);
  }

  public Sensor getLightUv() {
    Sensor sensor = first(LightUvV2.class);
    return sensor.isPresent() ? sensor : first(LightUv.class);
  }

  public Sensor getLightColor() {
    return first(LightColor.class);
  }

  public Sensor getLightAmbient() {
    Sensor sensor = first(LightAmbientV3.class);
    sensor = sensor.isPresent() ? sensor : first(LightAmbientV2.class);
    return sensor.isPresent() ? sensor : first(LightAmbient.class);
  }

  public Sensor getIO16() {
    Sensor sensor = first(IO16V2.class);
    return sensor.isPresent() ? sensor : first(IO16.class);
  }

  public Sensor getIMU2() {
    return first(IMU2.class);
  }

  public Sensor getIMU() {
    return first(IMU.class);
  }

  public Sensor getHumidity() {
    Sensor sensor = first(HumidityV2.class);
    return sensor.isPresent() ? sensor : first(Humidity.class);
  }

  public Sensor getDistanceUS() {
    return first(DistanceUS.class);
  }

  public Sensor getDistanceIR() {
    Sensor sensor = first(DistanceIRV2.class);
    return sensor.isPresent() ? sensor : first(DistanceIR.class);
  }

  public Sensor getDisplaySegment() {
    return first(DisplaySegment.class);
  }

  public Sensor getDisplayLcd20x4() {
    return first(DisplayLcd20x4.class);
  }

  public Sensor getDefault() {
    return first(Default.class);
  }

  public Sensor getDC() {
    return first(DC.class);
  }

  public Sensor getButtonRGB() {
    return first(ButtonRGB.class);
  }

  public Sensor getBarometer() {
    Sensor sensor = first(BarometerV2.class);
    return sensor.isPresent() ? sensor : first(Barometer.class);
  }

  public Sensor getAirQuality() {
    return first(AirQuality.class);
  }
}
