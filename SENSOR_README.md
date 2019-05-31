
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
--- 

### [AirQuality](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AirQuality.java)

*Measures acceleration in three axis*

### Values

* [IAQ_INDEX (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x = IAQ]
* [TEMPERATURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = mbar]
* [HUMIDITY (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = °C]
* [AIR_PRESSURE (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = %RH]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
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
--- 

### [ButtonRGB](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/ButtonRGB.java)

*Push button with built-in RGB LED*

### Values

* [BUTTON_PRESSED (ValueType)](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/RGB_LED_Button.htm)
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
display.send("Line1", true);
```

###### Send text centered on row 2
```java
display.send("Line2", true, 1);
```

###### Send text on position 5 and row 3
```java
display.send("Line2", 4, 2);
```

###### Send text on position 9 and row 4 with font (0-9)
```java
display.send("Line2", 8, 3, 2);
```

###### Send text with dynamic spaces between)
```java
display.send("H ${s} O ${s} W ${s} D ${s} Y");
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
display.send("Line1", true);
```

###### Send text centered on row 2
```java
display.send("Line2", true, 1);
```

###### Send text on position 5 and row 3
```java
display.send("Line2", 4, 2);
```

###### Send text on position 9 and row 4 with font (0-9)
```java
display.send("Line2", 8, 3, 2);
```

###### Send text with dynamic spaces between)
```java
display.send("H ${s} O ${s} W ${s} D ${s} Y");
```

--- 

 Four 7-segment displays with switchable colon
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Segment_Display_4x7.html)**Technical help**
[Representing Letters](https://en.wikichip.org/wiki/seven-segment_display/representing_letters)
[ascii-table-and-ascii-code](https://www.systutorials.com/4670/ascii-table-and-ascii-code)
 @param value 
 [String] print values on display
              
 [TemporalAccessor] prints the current time like [`now() (LocalDateTime)](src/main/java/java/time/LocalDateTime.java)
              
 [DateTimeFormatter] sets the time format default is "DateTimeFormatter.ofPattern("HH:mm")" [DATE_TIME_FORMAT (DisplaySegment)](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DisplaySegment.java)
 @return [Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)
/
 @param value 
 [0/1] LED ON/OFF
              
 [2 ... 9] Brightness
 @return [Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)
/
--- 

###### DistanceIR
 Measures distance up to 150cm with infrared light
 **Values**
DISTANCE[cm] = n / 10.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_IR_V2.html)
--- 

 Measures distance between 2cm and 400cm with ultrasound
 **Values**
DISTANCE[cm] = n / 10.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_US.html)
--- 

 Two tactile buttons with built-in blue LEDs
 **Values**
 BUTTON_PRESSED 0/1
 BUTTON 1 10 Released
 BUTTON 1 11 Pressed
 BUTTON 2 20 Released
 BUTTON 2 21 Pressed
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
--- 

###### Humidity
 Measures relative humidity
 **Values**
 HUMIDITY[%RH] = n / 100.0
 TEMPERATURE[°C] = n / 100.0
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity_V2.html)
--- 

###### IMU
 Full fledged AHRS with 9 degrees of freedom
--- 

###### IO16
 16-channel digital input/output
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16_V2.html)
 [true/false] = all LEDs on/off
 
 [0] = nothing
 
 [1 ... 17] turn on 16 LED ports
 
 [1 ... -17] turn off 16 LED ports
 Todo: [1000] = 3V output
 Todo: [2000] = 5V output
/
 [LED_ADDITIONAL_ON} / {@link LedStatusType (IO16V2)](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/IO16V2.java) = all LEDs on/off
 
 [0] = all LEDs off
 
 [1] = all LEDs on
 
 [2 ... 18] turn on 16 LED ports
 
 [2 ... -18] turn off 16 LED ports
 Todo: [1000] = 3V output
 Todo: [2000] = 5V output
/
--- 

###### LightAmbientV2
###### LightAmbient
 Measures ambient light up to 64000lux
 LIGHT_LUX[lx] = n / 100.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light.html_V3)
--- 

 Measures color (RGB send), illuminance and color temperature
 **Values**
COLOR[[Color](src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)] = n
 
COLOR_R[red] = n
 
COLOR_G[green] = n
 
COLOR_B[blue] = n
 
COLOR_LUX[lx] = n
 
COLOR_TEMPERATURE[] = n
 
[Official documentation]()
--- 

###### LightUv
 Measures UV-A, UV-B and UV index
 **Values**
 LIGHT_UV[index] = n / 10.0
 LIGHT_UVA[mW/m²] = n / 10.0
 LIGHT_UVB[mW/m²] = n / 10.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light_V2.html)
--- 

 Basis to build stacks and has 4 Bricklet ports
--- 

###### MotionDetector
 Passive infrared (PIR) motion sensor, 12m range with 120° angle
 **Values**
 MOTION_DETECTED = 0/1
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Motion_Detector_V2.html)
--- 

 Measures distance up to 150cm with infrared light
 **Values**
ROTARY[count] = n
 
BUTTON_PRESSED/BUTTON_RELEASED
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Rotary_Encoder_V2.html)
--- 

 Measures sound intensity
 **Values**
 SOUND_DECIBEL[db] = n / 10.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Sound_Intensity.html)
--- 

 Measures Sound Pressure Level in dB(A/B/C/D/Z)
--- 

 Creates beep with configurable frequency
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html)**Technical help**
[Morse generator](https://morsecode.scphillips.com/translator.html)
 @param values 
 Beep = duration
               
 Beep = duration, frequency
               
 Beep = duration, frequency, waitTime
               
 Beep = duration, frequency, waitBoolean
               
 Morse = "... --- ..."
               
 Morse = "... --- ...", frequency
               
 Morse = "... --- ...", frequency, waitBoolean
               
 Frequency device limits = [min 585 - max 7100]
 @return [Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)
/
 @param value 
 Beep time = n
              
 Morse = "... --- ..."
              
 Frequency = number with prefix "f" [min 585 - max 7100]
 @return [Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)
/
--- 

###### Temperature
 Measures ambient temperature with 0.2°C accuracy
 **Values**
 TEMPERATURE[°C] = n / 100.0
--- 

 Detects inclination of Bricklet (tilt switch open/closed)
 **Values**
TILT[012] = closed/open/vibrating
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Tilt.html)
--- 
