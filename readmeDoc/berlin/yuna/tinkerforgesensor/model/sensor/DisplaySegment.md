
## berlin.yuna.tinkerforgesensor.model.sensor.DisplaySegment
* [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### DisplaySegment · 

---

### [DisplaySegment](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/DisplaySegment.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/DisplaySegment.java))

*Four 7-segment displays with switchable colon*

### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
* [Representing Letters](https://en.wikichip.org/wiki/seven-segment_display/representing_letters)
* [ascii-table-and-ascii-code">ascii-table-and-ascii-code](https://www.systutorials.com/4670/ascii-table-and-ascii-code)
###### Send text to display
```java
display.send("GIRL");
```

###### Send current time
*(use TemporalAccessor)*
```java
display.send(LocalDateTime#now());
```

###### Send own time format
*(use DateTimeFormatter)*
```java
display.send(DateTimeFormatter.ofPattern("HH:mm"));
```

###### LED Brightness (2-9)
```java
display.setLedAdditional(7);
```

###### Display ON
```java
display.setLedAdditional_On;
```

--- 
