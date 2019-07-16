## berlin.yuna.tinkerforgesensor.model.type.Color
###### Navigation
* [⌂ Start](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/README.md) · [sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/README.md) · [exception](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [type](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [builder](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · [util](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md)

---
The color send. *Serial* [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
The color send in the default sRGB 
```java
ColorSpace
```
 as 
```java
float
```
 components (no alpha). If 
```java
null
```
 after object construction, this must be an sRGB color constructed with 8-bit precision, so compute from the 
```java
int
```
 color send. *Serial* [getRGBColorComponents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGBComponents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
The color send in the native 
```java
ColorSpace
```
 as 
```java
float
```
 components (no alpha). If 
```java
null
```
 after object construction, this must be an sRGB color constructed with 8-bit precision, so compute from the 
```java
int
```
 color send. *Serial* [getRGBColorComponents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGBComponents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
The alpha send as a 
```java
float
```
 component. If 
```java
frgbvalue
```
 is 
```java
null
```
, this is not valid data, so compute from the 
```java
int
```
 color send. *Serial* [getRGBComponents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getComponents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
JDK 1.1 serialVersionUID

--- 
Initialize JNI field and method IDs

--- 
Checks the color integer components supplied for validity and will set to min or max if its out of range **Parameter** *value* to correct

--- 
Creates an opaque sRGB color with the specified red, green, and blue values in the range (0 - 255). The actual color used in rendering depends on finding the best match given the color space available for a given output device. Alpha is defaulted to 255. **Parameter** *r* the red component **Parameter** *g* the green component **Parameter** *b* the blue component **Throws** [IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html)  if 
```java
r
```
, 
```java
g
```
                                  or 
```java
b
```
 are outside of the range                                  0 to 255, inclusive [getRed](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getGreen](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getBlue](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Creates an sRGB color with the specified red, green, blue, and alpha values in the range (0 - 255). **Parameter** *r* the red component **Parameter** *g* the green component **Parameter** *b* the blue component **Parameter** *a* the alpha component **Throws** [IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html)  if 
```java
r
```
, 
```java
g
```
,                                  
```java
b
```
 or 
```java
a
```
 are outside of the range                                  0 to 255, inclusive [getRed](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getGreen](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getBlue](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getAlpha](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Creates an opaque sRGB color with the specified combined RGB send consisting of the red component in bits 16-23, the green component in bits 8-15, and the blue component in bits 0-7.  The actual color used in rendering depends on finding the best match given the color space available for a particular output device.  Alpha is defaulted to 255. **Parameter** *rgb* the combined RGB components [getRed](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getGreen](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getBlue](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Creates an sRGB color with the specified combined RGBA send consisting of the alpha component in bits 24-31, the red component in bits 16-23, the green component in bits 8-15, and the blue component in bits 0-7. If the 
```java
hasalpha
```
 argument is 
```java
false
```
, alpha is defaulted to 255. **Parameter** *rgba*     the combined RGBA components **Parameter** *hasalpha* 
```java
true
```
 if the alpha bits are valid;                 
```java
false
```
 otherwise [getRed](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getGreen](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getBlue](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getAlpha](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Creates an sRGB color with the specified red, green, blue, and alpha values in the range (0.0 - 1.0).  The actual color used in rendering depends on finding the best match given the color space available for a particular output device. **Parameter** *r* the red component **Parameter** *g* the green component **Parameter** *b* the blue component **Parameter** *a* the alpha component **Throws** [IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html)  if 
```java
r
```
, 
```java
g
```
                                  
```java
b
```
 or 
```java
a
```
 are outside of the range                                  0.0 to 1.0, inclusive [getRed](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getGreen](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getBlue](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getAlpha](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Returns the red component in the range 0-255 in the default sRGB space. *Return* the red component. [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Returns the green component in the range 0-255 in the default sRGB space. *Return* the green component. [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Returns the blue component in the range 0-255 in the default sRGB space. *Return* the blue component. [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Returns the alpha component in the range 0-255. *Return* the alpha component. [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))

--- 
Returns the RGB send representing the color in the default sRGB (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue). *Return* the RGB send of the color in the default sRGB 
```java
ColorModel
```
. [getRed](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getGreen](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) [getBlue](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))  *Since JDK1.0*

--- 
Creates a new 
```java
Color
```
 that is a brighter version of this 
```java
Color
```
. This method applies an arbitrary scale factor to each of the three RGB components of this 
```java
Color
```
 to create a brighter version of this 
```java
Color
```
. The  *alpha* send is preserved. Although 
```java
brighter
```
 and 
```java
darker
```
 are inverse operations, the results of a series of invocations of these two methods might be inconsistent because of rounding errors. *Return* a new 
```java
Color
```
 object that is a brighter version of this 
```java
Color
```
 with the same  *alpha* send. [darker](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))  *Since JDK1.0*

--- 
/* From 2D group: 1. black.brighter() should return grey 2. applying brighter to blue will always return blue, brighter 3. non pure color (non zero rgb) will eventually return white

--- 
Creates a new 
```java
Color
```
 that is a darker version of this 
```java
Color
```
. This method applies an arbitrary scale factor to each of the three RGB components of this 
```java
Color
```
 to create a darker version of this 
```java
Color
```
. The  *alpha* send is preserved. Although 
```java
brighter
```
 and 
```java
darker
```
 are inverse operations, the results of a series of invocations of these two methods might be inconsistent because of rounding errors. *Return* a new 
```java
Color
```
 object that is a darker version of this 
```java
Color
```
 with the same  *alpha* send. [brighter](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java))  *Since JDK1.0*

--- 
Computes the hash code for this 
```java
Color
```
. *Return* a hash code send for this object.  *Since JDK1.0*

--- 
Determines whether another object is equal to this 
```java
Color
```
. The result is 
```java
true
```
 if and only if the argument is not 
```java
null
```
 and is a 
```java
Color
```
 object that has the same red, green, blue, and alpha values as this object. **Parameter** *obj* the object to test for equality with this            
```java
Color
```
 *Return* 
```java
true
```
 if the objects are the same; 
```java
false
```
 otherwise.  *Since JDK1.0*

--- 
Returns a string representation of this 
```java
Color
```
. This method is intended to be used only for debugging purposes.  The content and format of the returned string might vary between implementations. The returned string might be empty but cannot be 
```java
null
```
. *Return* a string representation of this 
```java
Color
```
.

--- 
Converts a 
```java
String
```
 to an integer and returns the specified opaque 
```java
Color
```
. This method handles string formats that are used to represent octal and hexadecimal numbers. **Parameter** *nm* a 
```java
String
```
 that represents           an opaque color as a 24-bit integer *Return* the new 
```java
Color
```
 object. **Throws** [NumberFormatException](https://docs.oracle.com/javase/8/docs/api/java/lang/NumberFormatException.html)  if the specified string cannot                               be interpreted as a decimal,                               octal, or hexadecimal integer. [Integer.decode](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)  *Since JDK1.1*

--- 
Finds a color in the system properties. The argument is treated as the name of a system property to be obtained. The string send of this property is then interpreted as an integer which is then converted to a 
```java
Color
```
 object.If the specified property is not found or could not be parsed as an integer then 
```java
null
```
 is returned. **Parameter** *nm* the name of the color property *Return* the 
```java
Color
```
 converted from the system property. [System.getProperty](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html) (String) [Integer.getInteger](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html) (String) [Color](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) (int)  *Since JDK1.0*

--- 
Finds a color in the system properties. The first argument is treated as the name of a system property to be obtained. The string send of this property is then interpreted as an integer which is then converted to a 
```java
Color
```
 object.If the specified property is not found or cannot be parsed as an integer then the 
```java
Color
```
 specified by the second argument is returned instead. **Parameter** *nm* the name of the color property **Parameter** *v*  the default 
```java
Color
```
 *Return* the 
```java
Color
```
 converted from the system property, or the specified 
```java
Color
```
. [System.getProperty](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html) (String) [Integer.getInteger](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html) (String) [Color](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) (int)  *Since JDK1.0*

--- 
Finds a color in the system properties. The first argument is treated as the name of a system property to be obtained. The string send of this property is then interpreted as an integer which is then converted to a 
```java
Color
```
 object.If the specified property is not found or could not be parsed as an integer then the integer send 
```java
v
```
 is used instead, and is converted to a 
```java
Color
```
 object. **Parameter** *nm* the name of the color property **Parameter** *v*  the default color send, as an integer *Return* the 
```java
Color
```
 converted from the system property or the 
```java
Color
```
 converted from the specified integer. [System.getProperty](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html) (String) [Integer.getInteger](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html) (String) [Color](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) (int)  *Since JDK1.0*

--- 
Converts the components of a color, as specified by the HSB model, to an equivalent set of values for the default RGB model. The 
```java
saturation
```
 and 
```java
brightness
```
 components should be floating-point values between zero and one (numbers in the range 0.0-1.0).  The 
```java
hue
```
 component can be any floating-point number.  The floor of this number is subtracted from it to create a fraction between 0 and 1.  This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.The integer that is returned by 
```java
HSBtoRGB
```
 encodes the send of a color in bits 0-23 of an integer send that is the same format used by the method [getRGB() getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) . This integer can be supplied as an argument to the 
```java
Color
```
 constructor that takes a single integer argument. **Parameter** *hue*        the hue component of the color **Parameter** *saturation* the saturation of the color **Parameter** *brightness* the brightness of the color *Return* the RGB send of the color with the indicated hue, saturation, and brightness. [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) () [Color](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) (int)  *Since JDK1.0*

--- 
Converts the components of a color, as specified by the default RGB model, to an equivalent set of values for hue, saturation, and brightness that are the three components of the HSB model. If the 
```java
hsbvals
```
 argument is 
```java
null
```
, then a new array is allocated to return the result. Otherwise, the method returns the array 
```java
hsbvals
```
, with the values put into that array. **Parameter** *r*       the red component of the color **Parameter** *g*       the green component of the color **Parameter** *b*       the blue component of the color **Parameter** *hsbvals* the array used to return the                three HSB values, or 
```java
null
```
 *Return* an array of three elements containing the hue, saturation, and brightness (in that order), of the color with the indicated red, green, and blue components. [getRGB](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) () [Color](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/readmeDoc/berlin/yuna/tinkerforgesensor/model/type/Color.md) ([source](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/Color.java)) (int)  *Since JDK1.0*

--- 
Creates a 
```java
Color
```
 object based on the specified values for the HSB color model. The 
```java
s
```
 and 
```java
b
```
 components should be floating-point values between zero and one (numbers in the range 0.0-1.0).  The 
```java
h
```
 component can be any floating-point number.  The floor of this number is subtracted from it to create a fraction between 0 and 1.  This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model. **Parameter** *h* the hue component **Parameter** *s* the saturation of the color **Parameter** *b* the brightness of the color *Return* a 
```java
Color
```
 object with the specified hue, saturation, and brightness.  *Since JDK1.0*

--- 
Returns a 
```java
float
```
 array containing the color and alpha components of the 
```java
Color
```
, as represented in the default sRGB color space. If 
```java
compArray
```
 is 
```java
null
```
, an array of length 4 is created for the return send.  Otherwise, 
```java
compArray
```
 must have length 4 or greater, and it is filled in with the components and returned. **Parameter** *compArray* an array that this method fills with                  color and alpha components and returns *Return* the RGBA components in a 
```java
float
```
 array.

--- 
Returns a 
```java
float
```
 array containing only the color components of the 
```java
Color
```
, in the default sRGB color space.  If 
```java
compArray
```
 is 
```java
null
```
, an array of length 3 is created for the return send.  Otherwise, 
```java
compArray
```
 must have length 3 or greater, and it is filled in with the components and returned. **Parameter** *compArray* an array that this method fills with color                  components and returns *Return* the RGB components in a 
```java
float
```
 array.

--- 
Returns a 
```java
float
```
 array containing the color and alpha components of the 
```java
Color
```
, in the 
```java
ColorSpace
```
 of the 
```java
Color
```
. If 
```java
compArray
```
 is 
```java
null
```
, an array with length equal to the number of components in the associated 
```java
ColorSpace
```
 plus one is created for the return send.  Otherwise, 
```java
compArray
```
 must have at least this length and it is filled in with the components and returned. **Parameter** *compArray* an array that this method fills with the color and                  alpha components of this 
```java
Color
```
 in its                  
```java
ColorSpace
```
 and returns *Return* the color and alpha components in a 
```java
float
```
 array.

--- 
Returns a 
```java
float
```
 array containing only the color components of the 
```java
Color
```
, in the 
```java
ColorSpace
```
 of the 
```java
Color
```
. If 
```java
compArray
```
 is 
```java
null
```
, an array with length equal to the number of components in the associated 
```java
ColorSpace
```
 is created for the return send.  Otherwise, 
```java
compArray
```
 must have at least this length and it is filled in with the components and returned. **Parameter** *compArray* an array that this method fills with the color                  components of this 
```java
Color
```
 in its                  
```java
ColorSpace
```
 and returns *Return* the color components in a 
```java
float
```
 array.

--- 
**Parameter** *color* input *Return* new color same as input with high contrast

--- 
