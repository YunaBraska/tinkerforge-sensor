## berlin.yuna.tinkerforgesensor.model.sensor.LightUv
* [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 
---
###### LightUvV2 · LightUv · 

---
### [LightUvV2](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/LightUvV2.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/LightUvV2.java))

 *Measures UV-A, UV-B and UV index*
 
### Values
 * [LIGHT_UV](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 10.0 = index]
 * [LIGHT_UVA](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 10.0 = mW/m²]
 * [LIGHT_UVB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)) [x / 10.0 = mW/m²] 
### Technical Info
 * [Official documentation](https://www.tinkerforge.com/de/doc/Hardware/Bricklets/UV_Light.html_V2) 
###### Getting lightUv examples
 ```java
stack.values().lightUv(); stack.values().lightUv_Avg(); stack.values().lightUv_Min(); stack.values().lightUv_Max(); stack.values().lightUv_Sum();
```

--- 
