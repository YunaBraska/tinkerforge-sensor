
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