## berlin.yuna.tinkerforgesensor.util.Base58
* [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 
---
###### Base58 · 

---
A custom form of base58 is used to encode Bitcoin addresses. Note that this is not the same base58 as used by Flickr, which you may see reference to around the internet.Satoshi says: why base-58 instead of standard base-64 encoding?* Don't want 0OIl characters that look the same in some fonts and could be used to create visually identical looking account numbers.
 * A string with non-alphanumeric characters is not as easily accepted as an account number.
 * E-mail usually won't line-break if there's no punctuation to break at.
 * Double-clicking selects the whole number as one word if it's all alphanumeric. @author The bitcoinj developers, modified by Jonathan Coe

--- 
