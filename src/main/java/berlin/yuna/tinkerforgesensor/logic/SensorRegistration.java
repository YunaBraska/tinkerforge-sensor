package berlin.yuna.tinkerforgesensor.logic;

import berlin.yuna.tinkerforgesensor.model.RollingList;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorRequest;
import berlin.yuna.tinkerforgesensor.model.driver.brick.DC;
import berlin.yuna.tinkerforgesensor.model.driver.brick.IMU;
import berlin.yuna.tinkerforgesensor.model.driver.brick.IMU2;
import berlin.yuna.tinkerforgesensor.model.driver.brick.Master;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.AirQuality;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Barometer;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.ButtonRGB;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DisplayLcd20x4;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.DisplaySegment;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Dummy;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Humidity;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Humidity2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightAmbient;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightAmbient2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightColor;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.LightUv;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.MotionDetector;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.MotionDetector2;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.SoundPressure;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.VoltageCurrent2;
import berlin.yuna.tinkerforgesensor.model.type.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static java.util.Arrays.stream;

public class SensorRegistration extends TinkerForgeUtil {

    public static final int SENSOR_VALUE_LIMIT = 99;

    public final ConcurrentHashMap<ValueType, RollingList<Long>> values = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<ValueType, Integer> sensitivity = new ConcurrentHashMap<>();
    public final List<Consumer<SensorRequest>> ledConsumer = new ArrayList<>();

    public void ledStatusOn() {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_STATUS_ON, null)));
    }

    public void ledStatusOff() {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_STATUS_OFF, null)));
    }

    public void ledStatus() {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_STATUS, null)));
    }

    public void ledStatusHeartbeat() {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_STATUS_HEARTBEAT, null)));
    }

    public void ledAdditionalOn() {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_ADDITIONAL_ON, null)));
    }

    public void ledAdditionalOff() {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_ADDITIONAL_OFF, null)));
    }

    public void ledBrightness(final Integer value) {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LED_ADDITIONAL_ON, value)));
    }

    public void led(final LedStatusType ledStatus) {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(ledStatus, null)));
    }

    public void led(final LedStatusType ledStatus, final Long value) {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(ledStatus, value)));
    }

    public void led(final LedStatusType ledStatus, final Integer red, final Integer green, final Integer blue) {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(ledStatus, (long) new Color(red, green, blue).getRGB())));
    }

    public void value(Object object) {
        ledConsumer.forEach(sensorLedEventConsumer -> sensorLedEventConsumer.accept(new SensorRequest(LedStatusType.LED_CUSTOM, object)));
    }

    public Long value(final ValueType sensorValueType) {
        return value(sensorValueType, 0L);
    }

    public Long value(final ValueType sensorValueType, final Long fallback) {
        RollingList<Long> valueList = values.get(sensorValueType);
        return valueList == null || valueList.isEmpty() || valueList.getLast() == null ? fallback : valueList.getLast();
    }

    /**
     * Configure sensor sensitivity for each single event
     *
     * @param sensorValueTypes      eventTypes to set sensitivity to (Use: all to set on all events for this sensor)
     * @param sensitivityPercentage value
     * @return this
     */
    public void sensitivity(final Integer sensitivityPercentage, final ValueType... sensorValueTypes) {
        for (ValueType sensorValueType : sensorValueTypes) {
            sensitivity.put(sensorValueType, sensitivityPercentage);
            stream(ValueType.values()).filter(type -> type.parent == sensorValueType).forEach(type -> sensitivity.put(type, sensitivityPercentage));
        }
    }

    public void addListener(final Sensor sensor, final List<Consumer<SensorEvent>> consumerList) {
        int period = 100;
        Device device = sensor.device;
        try {
            if (device instanceof DummyDevice) {
                Dummy.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletRGBLEDButton) {
                ButtonRGB.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickDC) {
                DC.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickIMU) {
                IMU.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickIMUV2) {
                IMU2.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickMaster) {
                Master.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletUVLight) {
                LightUv.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletAmbientLight) {
                LightAmbient.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletAmbientLightV2) {
                LightAmbient2.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletAirQuality) {
                AirQuality.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletBarometer) {
                Barometer.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletColor) {
                LightColor.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickRED) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickServo) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickSilentStepper) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickStepper) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAccelerometer) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAnalogIn) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAnalogInV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAnalogInV3) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAnalogOut) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAnalogOutV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletAnalogOutV3) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletBarometerV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletCAN) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletCANV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletCO2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletCurrent12) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletCurrent25) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDMX) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDistanceIR) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDistanceIRV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDistanceUS) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDualButton) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDualButtonV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDualRelay) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletDustDetector) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletGPS) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletGPSV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletHallEffect) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletHumidity) {
                Humidity.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletHumidityV2) {
                Humidity2.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletIO16) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIO16V2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIO4) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIO4V2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialAnalogOut) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialAnalogOutV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialCounter) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDigitalIn4) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDigitalIn4V2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDigitalOut4) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDigitalOut4V2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDual020mA) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDual020mAV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDualAnalogIn) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDualAnalogInV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialDualRelay) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialQuadRelay) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIndustrialQuadRelayV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletIsolator) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletJoystick) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLCD128x64) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLCD16x2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLCD20x4) {
                DisplayLcd20x4.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletLEDStrip) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLEDStripV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLaserRangeFinder) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLine) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLinearPoti) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLoadCell) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletLoadCellV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletMoisture) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletMotionDetector) {
                MotionDetector.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletMotionDetectorV2) {
                MotionDetector2.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletMotorizedLinearPoti) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletMultiTouch) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletNFC) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletNFCRFID) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletOLED128x64) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletOLED128x64V2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletOLED64x48) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletOneWire) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletOutdoorWeather) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletPTC) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletPTCV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletParticulateMatter) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletPiezoBuzzer) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletPiezoSpeaker) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRGBLED) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRGBLEDMatrix) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRS232) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRS232V2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRS485) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRealTimeClock) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRealTimeClockV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRemoteSwitch) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRemoteSwitchV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRotaryEncoder) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRotaryEncoderV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletRotaryPoti) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletSegmentDisplay4x7) {
                DisplaySegment.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletSolidStateRelay) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletSolidStateRelayV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletSoundIntensity) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletSoundPressureLevel) {
                SoundPressure.register(this, sensor, consumerList, period);
            } else if (device instanceof BrickletTemperature) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletTemperatureIR) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletTemperatureIRV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletTemperatureV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletThermalImaging) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletThermocouple) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletThermocoupleV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletTilt) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletUVLightV2) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletVoltage) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletVoltageCurrent) {
                deviceNotSupportedYet(device);
            } else if (device instanceof BrickletVoltageCurrentV2) {
                VoltageCurrent2.register(this, sensor, consumerList, period);
            }
        } catch (NotConnectedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void deviceNotSupportedYet(Device device) {
        error("Device [%s] not supported yet", device.getClass().getSimpleName());
    }

    public void sendEvent(final List<Consumer<SensorEvent>> consumerList, final ValueType sensorValueType, final Sensor sensor, final Long value) {
        RollingList<Long> sensorValueList = values.computeIfAbsent(sensorValueType, item -> new RollingList<>(SENSOR_VALUE_LIMIT));
        Integer sensitivityValue = sensitivity.computeIfAbsent(sensorValueType, v -> 0);

        if (percentageOccur(new ArrayList<>(sensorValueList), value) <= sensitivityValue) {
            consumerList.forEach(sensorConsumer -> sensorConsumer.accept(new SensorEvent(sensor, value, sensorValueType)));
        }
        values.get(sensorValueType).add(value);
    }

    //FIXME: make sensitivity better
    //FIXME values that occur % and are % over average
    //FIXME: dynamic RollingSize depends on how many different numbers there are in a second/minute...?
    //FIXME: range = max - min From there the percentage could count....
    //FIXME: make two ranges? - and + for calculations? or handle all as plus?
    //FIXME: average only on unique numbers
    private int percentageOccur(final ArrayList<Long> sensorValueList, final Long value) {
        int count = 1;
        for (Long sensorValue : sensorValueList) {
            if (sensorValue.equals(value)) {
                count++;
            }
        }
        return ((count * 100) / (sensorValueList.size() + 1));
    }
}
