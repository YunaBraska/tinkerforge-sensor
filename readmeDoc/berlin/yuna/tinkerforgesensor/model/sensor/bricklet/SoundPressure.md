
## berlin.yuna.tinkerforgesensor.model.sensor.bricklet.SoundPressure
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### SoundPressure · 

---

### [SoundPressure](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/SoundPressure.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/SoundPressure.java))

*Measures Sound Pressure Level in dB(A/B/C/D/Z)*

### Values

* [SOUND_INTENSITY](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 10 = db]
* [SOUND_SPECTRUM_OFFSET](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))
* [SOUND_SPECTRUM_LENGTH](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))
* [SOUND_SPECTRUM](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x = x[]]
* [SOUND_SPECTRUM_CHUNK](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x = x[]]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/de/doc//Hardware/Bricklets/Sound_Pressure_Level.html)
###### Getting sound spectrum examples
```java
stack.values().listSoundSpectrum();
```
```java
stack.values().listSoundSpectrumChunk();
```

--- 
