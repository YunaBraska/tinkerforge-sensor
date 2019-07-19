## berlin.yuna.tinkerforgesensor.model.sensor.LocalAudio
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
### [LocalAudio](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/LocalAudio.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/LocalAudio.java))

 *Mini audio player plays wav only*
 *Under construction*
 
### Technical Info
 
###### Play file (allowed = STRING/FILE/PATH/URL/URI)
 
```java
localAudio.send("/Downloads/mySoundFile.wav");
```
 
###### Set volume to 50% (0-100)
 
```java
localAudio.send(50);
```
 
###### Mute
 
```java
localAudio.send(true);
```
 
###### Play file, volume 20%, unmuted
 
```java
localAudio.send("/Downloads/mySoundFile.wav", 20, false, PLAY);
```
 
### Audio commands [AudioCmd](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/AudioCmd.java)
 *One can be added anytime at sending the commands from above*
 
```java
localAudio.send(PLAY);
```
 
```java
localAudio.send(REPLAY);
```
 
```java
localAudio.send(PAUSE);
```
 
```java
localAudio.send(STOP);
```
 
```java
localAudio.send(MUTE);
```
 
```java
localAudio.send(UNMUTE);
```
 
### Parallel sounds
 *Can be done by adding different playerIds at the start of the command*
 
```java
localAudio.send(1, "/Downloads/mySoundFile.wav");
```
 
```java
localAudio.send(2, "/Downloads/mySoundFile.wav");
```

--- 
