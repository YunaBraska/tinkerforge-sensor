## berlin.yuna.tinkerforgesensor.model.sensor.Speaker
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
###### ### [Speaker](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/Speaker.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Speaker.java))

 *Creates beep with configurable frequency*
 
### Values
 * [BEEP_ACTIVE](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [1 = active] 
### Technical Info
 * [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/Piezo_Speaker.html)
 * [Morse generator](https://morsecode.scphillips.com/translator.html) 
###### Send 1 second beep
 
```java
speaker.send(1000)
```
 
###### Send 2 second beep with frequency (min 585 - max 7100)
 
```java
speaker.send(1000, 2000)
```
 
###### Send morse
 
```java
speaker.send("... --- ...")
```
 
###### Send morse with frequency (min 585 - max 7100)
 
```java
speaker.send("... --- ...", 3000)
```
 
###### Wait until sound is finished
 
```java
speaker.send(256, 4000, true)
```

--- 
