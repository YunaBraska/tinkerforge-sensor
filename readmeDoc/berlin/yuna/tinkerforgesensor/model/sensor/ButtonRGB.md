## berlin.yuna.tinkerforgesensor.model.sensor.ButtonRGB
* [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 
---
###### ButtonRGB · 

---
### [ButtonRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/ButtonRGB.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/ButtonRGB.java))

 *Push button with built-in RGB LED*
 
### Values
 * [BUTTON_PRESSED](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [0/1] = Released/Pressed 
### Technical Info
 * [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/RGB_LED_Button.htm) 
###### Getting button pressed example
 
```java
stack.values().buttonPressed();
```

--- 
### Send
 
###### Set LED color
 
```java
sensor.send(Color.MAGENTA); sensor.send(new Color(255, 128, 64)); sensor.send(12367);
```
 
###### Set auto contrast on=true off=false
 
```java
sensor.send(true);
```

--- 
