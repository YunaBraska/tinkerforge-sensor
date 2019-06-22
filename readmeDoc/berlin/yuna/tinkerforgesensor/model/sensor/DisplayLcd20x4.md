## berlin.yuna.tinkerforgesensor.model.sensor.DisplayLcd20x4
* [⌂ Start](https:/github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md)

---
###### ### [DisplayLcd20x4](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/DisplayLcd20x4.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/DisplayLcd20x4.java))
 *20x4 character alphanumeric display with blue backlight* 
### Values
 * [BUTTON](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [10, 20, 30, 40] = Released
 * [BUTTON](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [11, 21, 31, 41] = Pressed
 * [BUTTON_PRESSED](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [0/1] = Released/Pressed 
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
display.setLedAdditional_On;
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
