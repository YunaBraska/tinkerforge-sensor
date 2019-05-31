
## Index
* [Accelerometer](#accelerometer)
* [AccelerometerV2](#accelerometerv2)
* [AirQuality](#airquality)
* [Barometer](#barometer)
* [BarometerV2](#barometerv2)
* [ButtonRGB](#buttonrgb)
* [DC](#dc)
* [Default](#default)
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

### [Accelerometer](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Accelerometer.java)

*Measures acceleration in three axis*

### Values

* [ValueType#ACCELERATION_X](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ValueType#ACCELERATION_Y](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ValueType#ACCELERATION_Z](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Accelerometer.html)
--- 

### [AirQuality](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AirQuality.java)

*Measures acceleration in three axis*

### Values

* [ValueType#IAQ_INDEX](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x = IAQ]
* [ValueType#TEMPERATURE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = mbar]
* [ValueType#HUMIDITY](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = °C]
* [ValueType#AIR_PRESSURE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 100.0 = %RH]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
--- 

### [Barometer](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Barometer.java)

*Measures air pressure and altitude changes*

### Values

* [ValueType#AIR_PRESSURE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = mbar]
* [ValueType#ALTITUDE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [x / 1000.0 = m]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.htm)
--- 

### [ButtonRGB](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/ButtonRGB.java)

*Push button with built-in RGB LED*

### Values

* [ValueType#BUTTON_PRESSED](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1]
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

### [DisplayLcd20x4](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DisplayLcd20x4.java)
*20x4 character alphanumeric display with blue backlight*
### Values

* [ValueType#BUTTON](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [10, 20, 30, 40] = Released
* [ValueType#BUTTON](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [11, 21, 31, 41] = Pressed
* [ValueType#BUTTON_PRESSED](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java) [0/1] = Released/Pressed
### Technical Info

* [Official documentation](href=)
###### Sending text to display
```java
display.send("MyText");
```

###### Sending text to specific line
```java
display.send(2, "MyText");
```

###### Sending text to specific line and position
```java
display.send(2, 2, "MyText");
```

###### Sending text with new line
```java
display.send("Line 1 \n text 2");
```

###### Dynamic space
```java
display.send("${s} TextMiddle ${s}");
```

###### Center text
```java
display.send(true, "MyText");
```

###### Clear display
```java
display.send(true);
```

--- 

 Four 7-segment displays with switchable colon
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Segment_Display_4x7.html)**Technical help**
[Representing Letters](https://en.wikichip.org/wiki/seven-segment_display/representing_letters)
[ascii-table-and-ascii-code](https://www.systutorials.com/4670/ascii-table-and-ascii-code)
 @param value 
 [String] print values on display
              
 [TemporalAccessor] prints the current time like [LocalDateTime#`now()](src/main/java/java/time/LocalDateTime.java)
              
 [DateTimeFormatter] sets the time format default is "DateTimeFormatter.ofPattern("HH:mm")" [DisplaySegment#DATE_TIME_FORMAT](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DisplaySegment.java)
 @return [Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)
/
 @param value 
 [0/1] LED ON/OFF
              
 [2 ... 9] Brightness
 @return [Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)
/
--- 

 Measures distance up to 150cm with infrared light
 **Values**
DISTANCE[cm] = n / 10.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Distance_IR.html)
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

 Measures relative humidity
 **Values**
 HUMIDITY[%RH] = n / 100.0
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity.html)
--- 

 Full fledged AHRS with 9 degrees of freedom
/
--- 

 16-channel digital input/output
 
[Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16.html)
 [true/false] = all LEDs on/off
 
 [0] = nothing
 
 [1 ... 17] turn on 16 LED ports
 
 [1 ... -17] turn off 16 LED ports
 Todo: [1000] = 3V output
 Todo: [2000] = 5V output
/
 [IO16#LED_ADDITIONAL_ON} / {@link berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor.LedStatusType](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/IO16.java) = all LEDs on/off
 
 [0] = all LEDs off
 
 [1] = all LEDs on
 
 [2 ... 18] turn on 16 LED ports
 
 [2 ... -18] turn off 16 LED ports
 Todo: [1000] = 3V output
 Todo: [2000] = 5V output
/
--- 

 Measures ambient light up to 900lux
 LIGHT_LUX[lx] = n / 100.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Ambient_Light.html)
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

 Measures UV light
 **Values**
 LIGHT_UV[index] = n / 10.0
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light.html)
--- 

 Basis to build stacks and has 4 Bricklet ports
--- 

 Passive infrared (PIR) motion sensor, 7m range with 100° angle
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

 Measures ambient temperature with 0.5°C accuracy
 **Values**
 TEMPERATURE[°C] = n / 100.0
--- 

 Detects inclination of Bricklet (tilt switch open/closed)
 **Values**
TILT[012] = closed/open/vibrating
 
[Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Tilt.html)
--- 
