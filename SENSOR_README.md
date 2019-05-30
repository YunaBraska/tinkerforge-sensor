
## Accelerometer

### [Accelerometer](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Accelerometer.java)

*Measures acceleration in three axis*

### Values

* [ValueType#ACCELERATION_X](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ValueType#ACCELERATION_Y](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ValueType#ACCELERATION_Z](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
### Technical Info

* [Official doku](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Accelerometer.html)
## AirQuality

### [AirQuality](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AirQuality.java)

*Measures acceleration in three axis*

### Values

* [ValueType#IAQ_INDEX](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)[x = IAQ]
* [ValueType#TEMPERATURE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)[x / 1000.0 = mbar]
* [ValueType#HUMIDITY [x / 100.0 = °C]](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* [ValueType#AIR_PRESSURE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)[x / 100.0 = %RH]
### Technical Info

* [Official doku](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
## Barometer

### [Barometer](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Barometer.java)

*Measures air pressure and altitude changes*

### Values

* [ValueType#AIR_PRESSURE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)[x / 1000.0 = mbar]
* [ValueType#ALTITUDE](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)[x / 1000.0 = m]
### Technical Info

* [Official doku](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.htm)
## ButtonRGB

### [ButtonRGB](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/ButtonRGB.java)

*Push button with built-in RGB LED*

### Values

* [ValueType#BUTTON_PRESSED](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)[0/1]
### Technical Info

* [Official doku](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/RGB_LED_Button.htm)@param value
[Color#getRGB()](src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)[] RGB send *
[Color#getRGB()](src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)[Number] RGB send  *
[Sensor](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)[Boolean] activate highContrast * @return  */// +100% brightness
