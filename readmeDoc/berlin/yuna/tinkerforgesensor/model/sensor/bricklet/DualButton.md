
## berlin.yuna.tinkerforgesensor.model.sensor.bricklet.DualButton
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### DualButton · 

---

### [DualButton](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DualButton.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/DualButton.java))
*Two tactile buttons with built-in blue LEDs*
### Values

* [BUTTON](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [10, 20] = Released
* [BUTTON](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [11, 21] = Pressed
* [BUTTON_PRESSED](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [0/1] = Released/Pressed
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
button.setLedAdditional_On();
```

###### Set LEDs off
```java
button.setLedAdditional_Off();
```

###### Set LEDs active on press
```java
button.setLedAdditional_Status();
```

###### Set LEDs active on release
```java
button.setLedAdditional_Heartbeat();
```

--- 
