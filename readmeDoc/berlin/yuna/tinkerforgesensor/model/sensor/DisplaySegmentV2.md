## berlin.yuna.tinkerforgesensor.model.sensor.DisplaySegment
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
######  *([V1](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/DisplaySegment.java), [V2](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/DisplaySegmentV2.java))*


---
### [DisplaySegmentV2](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/DisplaySegmentV2.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/DisplaySegmentV2.java))

 *Four 7-segment displays with switchable colon*
 
### Technical Info
 * [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Segment_Display_4x7_V2.html)
 * [Representing Letters](https://en.wikichip.org/wiki/seven-segment_display/representing_letters) 
###### Send text to display
 
```java
sensor.send("1.2.:3.‘4.");
```
 
###### Send current time
 *(use [TemporalAccessor](https://docs.oracle.com/javase/8/docs/api/java/time/temporal/TemporalAccessor.html) )*
 
```java
sensor.send(LocalDateTime#now());
```
 
###### Send own time format
 *(use [DateTimeFormatter](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html) )*
 
```java
sensor.send(DateTimeFormatter.ofPattern("HH:mm"));
```
 
###### LED Brightness (2-9)
 
```java
sensor.ledAdditional(7);
```
 
###### Display ON
 
```java
sensor.ledAdditional_setOn;
```

--- 
