## berlin.yuna.tinkerforgesensor.model.sensor.LocalAudio
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
### [LocalAudio](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/LocalAudio.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/LocalAudio.java))

 *Mini audio player plays wav only*
 
### Technical Info
 
###### Play file (allowed = STRING/FILE/PATH/URL/URI)
 
```java
sensor.send("/Downloads/mySoundFile.wav");
```
 
###### Set volume to 50% (0-100)
 
```java
sensor.send(50);
```
 
###### Mute
 
```java
sensor.send(true);
```
 
###### Play file, volume 20%, unmuted
 
```java
sensor.send("/Downloads/mySoundFile.wav", 20, false, PLAY);
```
 
### Audio commands [AudioCmd](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/AudioCmd.java)
 *One can be added anytime at sending the commands from above*
 
```java
sensor.send(PLAY);
```
 
```java
sensor.send(REPLAY);
```
 
```java
sensor.send(PAUSE);
```
 
```java
sensor.send(STOP);
```
 
```java
sensor.send(MUTE);
```
 
```java
sensor.send(UNMUTE);
```
 
### Parallel sounds
 *Can be done by adding different playerIds at the startAsync of the command*
 
```java
sensor.send(1, "/Downloads/mySoundFile.wav");
```
 
```java
sensor.send(2, "/Downloads/mySoundFile.wav");
```

--- 
