## berlin.yuna.tinkerforgesensor.model.sensor.LedStrip
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
### [LedStripV2](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/LedStripV2.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/LedStripV2.java))

 *16-channel digital input/output*
 
### Values
 *input values coming soon*
 
### Technical Info
 * [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/LED_Strip_V2.html) 
###### [Setup] Setting number of leds to 30 and chip type to "WS2812" [LedChipType](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/LedChipType.java)
 
```java
sensor.send(30, "WS2812");
```
 
```java
sensor.send(30, LED_TYPE_WS2812);
```
 
###### Set led 1 to magenta
 
```java
sensor.send(Color.Magenta);
```
 
###### Set led 1 to magenta and led 2 to green
 
```java
sensor.send(Color.Magenta, Color.Green);
```
 
###### Set led 4 to red
 
```java
sensor.send(4, Color.Red);
```
 
###### Set all led's to black/off
 
```java
sensor.send(-1, Color.Black);
```
 
###### Set all led's to Black but don't update
 
```java
sensor.send(false, -1, Color.Black);
```
 
###### [Config] Set the refresh period of the led's
 
```java
sensor.refreshLimit(30);
```

--- 
