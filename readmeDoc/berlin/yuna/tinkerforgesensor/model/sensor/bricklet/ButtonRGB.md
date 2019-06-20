
## berlin.yuna.tinkerforgesensor.model.sensor.bricklet.ButtonRGB
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### ButtonRGB · 

---

### [ButtonRGB](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/ButtonRGB.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/ButtonRGB.java))

*Push button with built-in RGB LED*

### Values

* [BUTTON_PRESSED](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [0/1] = Released/Pressed
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
