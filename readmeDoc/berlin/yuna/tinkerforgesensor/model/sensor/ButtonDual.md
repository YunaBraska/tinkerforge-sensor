## berlin.yuna.tinkerforgesensor.model.sensor.ButtonDual
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
### [ButtonDual](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/ButtonDual.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/ButtonDual.java))
 *Two tactile buttons with built-in blue LEDs* 
### Values
 * [BUTTON_PRESSED](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [1] = Pressed
 * [BUTTON_RELEASED](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [0] = Released
 * [BUTTON](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [0/1,0/1] = 2x Button Released/Pressed 
### Technical Info
 * [Official documentation](href=) 
###### Getting button state from second button (0=Released, 1= pressed)
 
```java
values().button(1);
```
 
###### Getting button state list of 0/1 (0=Released, 1= pressed) value for each button
 
```java
values().button_List();
```
 
###### Switch first led on
 
```java
button.send(1);
```
 
###### Switch first led off
 
```java
button.send(-1);
```
 
###### Switch first led on and second led off
 
```java
button.send(1, -2);
```
 
```java
button.send(true, false);
```
 
###### (Auto) Set LEDs on
 
```java
button.ledAdditional_setOn();
```
 
###### (Auto) Set LEDs off
 
```java
button.ledAdditional_setOff();
```
 
###### (Auto) Set LEDs active on press
 
```java
button.setLedAdditional_Status();
```
 
###### (Auto) Set LEDs active on release
 
```java
button.setLedAdditional_Heartbeat();
```

--- 
