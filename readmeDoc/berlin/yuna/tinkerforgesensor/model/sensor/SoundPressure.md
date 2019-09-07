## berlin.yuna.tinkerforgesensor.model.sensor.SoundPressure
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
### [SoundPressure](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/SoundPressure.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/SoundPressure.java))

 *Measures Sound Pressure Level in dB(A/B/C/D/Z)*
 
### Values
 * [SOUND_DECIBEL](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [x / 10 = db]
 * [SOUND_INTENSITY](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [x / 100 = db]
 * [SOUND_SPECTRUM](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))  [x = x[]] 
### Technical Info
 * [Official documentation](https://www.tinkerforge.com/de/doc//Hardware/Bricklets/Sound_Pressure_Level.html) 
###### Get sound spectrum
 
```java
sensor.values().listSoundSpectrum();
```
 
###### Get sound spectrum with FFT index 20
 
```java
sensor.values().listSoundSpectrum(20);
```
 
###### Get sound spectrum list
 
```java
sensor.values().listSoundSpectrum_List();
```
 
###### Setting FFT to size of 256
 
```java
sensor.send(256)
```
 
###### Setting FFT to size of 256
 *Allowed: 128, 256, 512, 1024* 
```java
sensor.send(256)
```
 
###### Setting weighting
 *Allowed: A, B, C, D, Z* 
```java
sensor.send("A")
```
 
###### Setting FFT and weighting
 
```java
sensor.end("A", 256)
```

--- 
