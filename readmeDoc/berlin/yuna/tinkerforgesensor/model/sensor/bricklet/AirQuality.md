
## berlin.yuna.tinkerforgesensor.model.sensor.bricklet.AirQuality
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### AirQuality · 

---

### [AirQuality](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AirQuality.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/AirQuality.java))

*Measures acceleration in three axis*

### Values

* [IAQ_INDEX](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x = IAQ]
* [TEMPERATURE](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 100.0 = °C]
* [HUMIDITY](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 100.0 = %RH]
* [AIR_PRESSURE](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 1000.0 = mbar]
### Technical Info

* [Official documentation](https://www.tinkerforge.com/en/doc/Hardware/Bricklets/Air_Quality.html)
###### Getting air pressure examples
```java
stack.values().airPressure();
stack.values().airPressure_Avg();
stack.values().airPressure_Min();
stack.values().airPressure_Max();
stack.values().airPressure_Sum();
```

--- 
