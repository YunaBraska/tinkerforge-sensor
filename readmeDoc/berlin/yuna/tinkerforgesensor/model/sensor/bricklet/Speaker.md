
## berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Speaker
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### Speaker · 

---

### [Speaker](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Speaker.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Speaker.java))

*Creates beep with configurable frequency*

### Values

* [BEEP_ACTIVE](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [1 = active]
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
