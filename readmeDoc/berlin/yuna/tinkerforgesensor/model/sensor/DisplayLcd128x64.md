## berlin.yuna.tinkerforgesensor.model.sensor.DisplayLcd128x64
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
### [DisplayLcd128x64](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/DisplayLcd128x64.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/DisplayLcd128x64.java))
 *7.1cm (2.8") display with 128x64 pixel and touch screen* *GUI elements are coming soon* 
### Values
 * TouchPosition (coming soon)
 * TouchGesture (coming soon)
 * GuiTabSelected (coming soon)
 * GuiSliver (coming soon)
 * GuiButton (coming soon) 
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
 
###### Send text on position 9 and row 4 with font (0-9)
 
```java
display.send("Line4 posX=8 font=2", 8, 3, 2);
```
 
###### Send text with dynamic spaces between)
 
```java
display.send("H ${s} O ${s} W ${s} D ${s} Y");
```
 
###### LED Brightness (2-100)
 
```java
display.setLedAdditional(7);
```
 
###### Display ON
 
```java
display.setLedAdditional_On;
```

--- 
