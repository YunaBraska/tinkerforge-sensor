
## Index
* [Accelerometer](#accelerometer)
* [AccelerometerV2](#accelerometerv2)
* [AirQuality](#airquality)
* [Barometer](#barometer)
* [BarometerV2](#barometerv2)
* [ButtonRGB](#buttonrgb)
* [DC](#dc)
* [Default](#default)
* [DisplayLcd128x64](#displaylcd128x64)
* [DisplayLcd20x4](#displaylcd20x4)
* [DisplaySegment](#displaysegment)
* [DistanceIR](#distanceir)
* [DistanceIRV2](#distanceirv2)
* [DistanceUS](#distanceus)
* [DualButton](#dualbutton)
* [Humidity](#humidity)
* [HumidityV2](#humidityv2)
* [IMU](#imu)
* [IMU2](#imu2)
* [IO16](#io16)
* [IO16V2](#io16v2)
* [LightAmbient](#lightambient)
* [LightAmbientV2](#lightambientv2)
* [LightAmbientV3](#lightambientv3)
* [LightColor](#lightcolor)
* [LightUv](#lightuv)
* [LightUvV2](#lightuvv2)
* [Master](#master)
* [MotionDetector](#motiondetector)
* [MotionDetectorV2](#motiondetectorv2)
* [RotaryV2](#rotaryv2)
* [SoundIntensity](#soundintensity)
* [SoundPressure](#soundpressure)
* [Speaker](#speaker)
* [Temperature](#temperature)
* [TemperatureV2](#temperaturev2)
* [Tilt](#tilt)
---

###### Accelerometer
### [AccelerometerV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AccelerometerV2.java)

*Measures acceleration in three axis*

### Values

* [ACCELERATION_X (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ACCELERATION_Y (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ACCELERATION_Z (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Accelerometer_V2.html)
###### Getting accelerationX examples
```java
stack.values().accelerationX();
stack.values().accelerationX_Avg();
stack.values().accelerationX_Min();
stack.values().accelerationX_Max();
stack.values().accelerationX_Sum();
```

--- 

### [AirQuality](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AirQuality.java)

*Measures acceleration in three axis*

### Values

* [IAQ_INDEX (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x = IAQ]
* [TEMPERATURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = °C]
* [HUMIDITY (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = %RH]
* [AIR_PRESSURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = mbar]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
###### Getting air pressure examples
```java
stack.values().airPressure();
stack.values().airPressure_Avg();
stack.values().airPressure_Min();
stack.values().airPressure_Max();
stack.values().airPressure_Sum();
```

--- 

###### Barometer
### [BarometerV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/BarometerV2.java)

*Measures air pressure and altitude changes*

### Values

* [AIR_PRESSURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = mbar]
* [ALTITUDE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = m]
* [TEMPERATURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = °C]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.htm)
###### Getting air pressure examples
```java
stack.values().airPressure();
stack.values().airPressure_Avg();
stack.values().airPressure_Min();
stack.values().airPressure_Max();
stack.values().airPressure_Sum();
```

--- 

### [ButtonRGB](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/ButtonRGB.java)

*Push button with built-in RGB LED*

### Values

* [BUTTON_PRESSED (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1] = Released/Pressed
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/RGB_LED_Button.htm)
###### Getting button pressed example
```java
stack.values().buttonPressed();
```

### Send

###### Set LED color
```java
sensor.send(Color.MAGENTA);
sensor.send(new Color(255, 128, 64));
sensor.send(12367);
```

###### Set auto contrast on=true off=false
```java
sensor.send(true);
```

--- 

--- 

### [Default](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Default.java)

*Default sensor is representing a non existing but requested sensor*

###### Check if the current sensor is [Default](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Default.java)
```java
//false = (DefaultSensor) means that the current sensor not available
sensors.isPresent();
```

###### All methods wont do anything
```java
sensors.send();
```

--- 

### [DisplayLcd128x64](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DisplayLcd128x64.java)
*7.1cm (2.8") display with 128x64 pixel and touch screen**GUI elements are coming soon*
### Values

* TouchPosition (coming soon)
* TouchGesture (coming soon)
* GuiTabSelected (coming soon)
* GuiSliver (coming soon)
* GuiButton (coming soon)
### Technical Info

* [Official documentation](href=)
###### Clear display
```java
display.send(true);
```

###### Send text
```java
display.send("Howdy");
```

###### Send text centered
```java
display.send("Line1 center", true);
```

###### Send text centered on row 2
```java
display.send("Line2 center", true, 1);
```

###### Send text on position 5 and row 3
```java
display.send("Line3 posX=5", 4, 2);
```

###### Send text on position 9 and row 4 with font (0-9)
```java
display.send("Line4 posX=8 font=2", 8, 3, 2);
```

###### Send text with dynamic spaces between)
```java
display.send("H ${s} O ${s} W ${s} D ${s} Y");
```

###### LED Brightness (2-100)
```java
display.ledAdditional(7);
```

###### Display ON
```java
display.ledAdditionalOn;
```

--- 

### [DisplayLcd20x4](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DisplayLcd20x4.java)
*20x4 character alphanumeric display with blue backlight*
### Values

* [BUTTON (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [10, 20, 30, 40] = Released
* [BUTTON (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [11, 21, 31, 41] = Pressed
* [BUTTON_PRESSED (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1] = Released/Pressed
### Technical Info

* [Official documentation](href=)
###### Clear display
```java
display.send(true);
```

###### Send text
```java
display.send("Howdy");
```

###### Send text centered
```java
display.send("Line1 center", true);
```

###### Send text centered on row 2
```java
display.send("Line2 center", true, 1);
```

###### Send text on position 5 and row 3
```java
display.send("Line3 posX=5", 4, 2);
```

###### Send text with dynamic spaces between)
```java
display.send("H ${s} O ${s} W ${s} D ${s} Y");
```

###### Display ON
```java
display.ledAdditionalOn;
```

###### Getting button with pressed value (digit_1= button, digit_2 = pressed/released) example
```java
stack.values().button();
```

###### Getting button pressed example
```java
stack.values().buttonPressed();
```

--- 

### [DisplaySegment](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DisplaySegment.java)

*Four 7-segment displays with switchable colon*

### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
* [Representing Letters](https://en.wikichip.org/wiki/seven-segment_display/representing_letters)
* [ascii-table-and-ascii-code">ascii-table-and-ascii-code](https://www.systutorials.com/4670/ascii-table-and-ascii-code)
###### Send text to display
```java
display.send("GIRL");
```

###### Send current time
*(use [TemporalAccessor](src/main/java/java/time/temporal/TemporalAccessor.java))*
```java
display.send(LocalDateTime#now());
```

###### Send own time format
*(use [DateTimeFormatter](src/main/java/java/time/format/DateTimeFormatter.java))*
```java
display.send(DateTimeFormatter.ofPattern("HH:mm"));
```

###### LED Brightness (2-9)
```java
display.ledAdditional(7);
```

###### Display ON
```java
display.ledAdditionalOn;
```

--- 

###### DistanceIR
### [DistanceIRV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DistanceIRV2.java)

*Measures distance up to 150cm with infrared light*

### Values

* [DISTANCE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10.0 = cm]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_IR_V2.html)
###### Getting distance examples
```java
stack.values().distance();
stack.values().distance();
stack.values().distance();
stack.values().distance();
stack.values().distance();
```

--- 

### [DistanceIR](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DistanceIR.java)

*Measures distance between 2cm and 400cm with ultrasound*

### Values

* [DISTANCE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10.0 = cm]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_US.html)
###### Getting distance examples
```java
stack.values().distance();
stack.values().distance();
stack.values().distance();
stack.values().distance();
stack.values().distance();
```

--- 

### [DualButton](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DualButton.java)
*Two tactile buttons with built-in blue LEDs*
### Values

* [BUTTON (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [10, 20] = Released
* [BUTTON (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [11, 21] = Pressed
* [BUTTON_PRESSED (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1] = Released/Pressed
### Technical Info

* [Official documentation](href=)
###### Getting button with pressed value (digit_1= button, digit_2 = pressed/released) example
```java
stack.values().button();
```

###### Getting button pressed example
```java
stack.values().buttonPressed();
```

###### Set LEDs on
```java
button.ledAdditionalOn();
```

###### Set LEDs off
```java
button.ledAdditionalOff();
```

###### Set LEDs active on press
```java
button.ledAdditionalStatus();
```

###### Set LEDs active on release
```java
button.ledAdditionalHeartbeat();
```

--- 

###### Humidity
### [Humidity](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Humidity.java)

*Measures relative humidity*

### Values

* [HUMIDITY (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = %RH]
* [TEMPERATURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = °C]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity_V2.html)
###### Getting humidity examples
```java
stack.values().humidity();
stack.values().humidity_Avg();
stack.values().humidity_Min();
stack.values().humidity_Max();
stack.values().humidity_Sum();
```

--- 

###### IMU
 Full fledged AHRS with 9 degrees of freedom
--- 

###### IO16
### [IO16](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/IO16.java)

*16-channel digital input/output*

### Values
*input values coming soon*

### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16_V2.html)
###### Set all LEDs on
```java
io16.ledAdditionalOn();
```

###### Turn on LED 4
```java
io16.send(4);
```

###### Turn off LED 12
```java
io16.send(-12);
```

--- 

###### LightAmbientV2
###### LightAmbient
### [LightAmbientV3](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/LightAmbientV3.java)

*Measures ambient light up to 64000lux*

### Values

* [LIGHT_LUX (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = lx]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light_V3.html)
###### Getting lightLux examples
```java
stack.values().lightLux();
stack.values().lightLux_Avg();
stack.values().lightLux_Min();
stack.values().lightLux_Max();
stack.values().lightLux_Sum();
```

--- 

### [LightColor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/LightColor.java)

*Measures color (RGB send), illuminance and color temperature*

### Values

* [COLOR (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [COLOR_R (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [COLOR_G (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [COLOR_B (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [COLOR_LUX (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [COLOR_TEMPERATURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Color.html)
###### Turn on flash LED
```java
color.ledAdditionalOn();
```

###### Getting color examples
```java
stack.values().color();
stack.values().color_Avg();
stack.values().color_Min();
stack.values().color_Max();
stack.values().color_Sum();
```

--- 

###### LightUv
### [LightUvV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/LightUvV2.java)

*Measures UV-A, UV-B and UV index*

### Values

* [LIGHT_UV (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10.0 = index]
* [LIGHT_UVA (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10.0 = mW/m²]
* [LIGHT_UVB (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10.0 = mW/m²]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light.html_V2)
###### Getting lightUv examples
```java
stack.values().lightUv();
stack.values().lightUv_Avg();
stack.values().lightUv_Min();
stack.values().lightUv_Max();
stack.values().lightUv_Sum();
```

--- 

 Basis to build stacks and has 4 Bricklet ports
--- 

###### MotionDetector
### [MotionDetectorV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/MotionDetectorV2.java)

*Passive infrared (PIR) motion sensor, 12m range with 120° angle*

### Values

* [MOTION_DETECTED (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1 cycleOff/detect]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motion_Detector_V2.html)
###### Getting motion detected example
```java
stack.values().motionDetected();
```

--- 

### [RotaryV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/RotaryV2.java)

*360° rotary encoder with push-button*

### Values

* [ROTARY (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x = number]
* [BUTTON_PRESSED (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1] = Released/Pressed
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Rotary_Encoder_V2.html)
###### Getting rotary number example
```java
stack.values().rotary();
```

--- 

### [SoundIntensity](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/SoundIntensity.java)

*Measures sound intensity*

### Values

* [SOUND_INTENSITY (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10 = db]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Sound_Intensity.html)
###### Getting sound intensity example
```java
stack.values().soundIntensity();
```

--- 

### [SoundPressure](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/SoundPressure.java)

*Measures Sound Pressure Level in dB(A/B/C/D/Z)*

### Values

* [SOUND_INTENSITY (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 10 = db]
* [SOUND_SPECTRUM_OFFSET (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [SOUND_SPECTRUM_LENGTH (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [SOUND_SPECTRUM (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x = x[]]
* [SOUND_SPECTRUM_CHUNK (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x = x[]]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc//Hardware/Bricklets/Sound_Pressure_Level.html)
###### Getting sound spectrum examples
```java
stack.values().listSoundSpectrum();
```
```java
stack.values().listSoundSpectrumChunk();
```

--- 

### [Speaker](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Speaker.java)

*Creates beep with configurable frequency*

### Values

* [BEEP_ACTIVE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [1 = active]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html)
* [Morse generator](https://morsecode.scphillips.com/translator.html)
###### Send 1 second beep
```java
speaker.send(1000)
```

###### Send 2 second beep with frequency (min 585 - max 7100)
```java
speaker.send(1000, 2000)
```

###### Send morse
```java
speaker.send("... --- ...")
```

###### Send morse with frequency (min 585 - max 7100)
```java
speaker.send("... --- ...", 3000)
```

###### Wait until sound is finished
```java
speaker.send(256, 4000, true)
```

--- 

###### Temperature
### [TemperatureV2](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/TemperatureV2.java)

*Measures ambient temperature with 0.2°C accuracy*

### Values

* [TEMPERATURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = °C]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Temperature_V2.html)
###### Getting temperature examples
```java
stack.values().temperature();
stack.values().temperature_Avg();
stack.values().temperature_Min();
stack.values().temperature_Max();
stack.values().temperature_Sum();
```

--- 

### [Tilt](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Tilt.java)

*Detects inclination of Bricklet (tilt switch open/closed)*

### Values

* [TILT (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1/2 = closed/open/vibrating]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Tilt.html)
###### Getting tilt examples
```java
stack.values().tilt();
```

--- 
