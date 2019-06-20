
## berlin.yuna.tinkerforgesensor.model.type.Color
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### Color · 

---

 The color send.

 @serial
 @see #getRGB
/
 The color send in the default sRGB ```java
ColorSpace
```
 as
 ```java
float
```
 components (no alpha).
 If ```java
null
```
 after object construction, this must be an
 sRGB color constructed with 8-bit precision, so compute from the
 ```java
int
```
 color send.

 @serial
 @see #getRGBColorComponents
 @see #getRGBComponents
/
 The color send in the native ```java
ColorSpace
```
 as
 ```java
float
```
 components (no alpha).
 If ```java
null
```
 after object construction, this must be an
 sRGB color constructed with 8-bit precision, so compute from the
 ```java
int
```
 color send.

 @serial
 @see #getRGBColorComponents
 @see #getRGBComponents
/
 The alpha send as a ```java
float
```
 component.
 If ```java
frgbvalue
```
 is ```java
null
```
, this is not valid
 data, so compute from the ```java
int
```
 color send.

 @serial
 @see #getRGBComponents
 @see #getComponents
/
 JDK 1.1 serialVersionUID
/
 Initialize JNI field and method IDs
/
 Checks the color integer components supplied for validity
 and will set to min or max if its out of range

 @param value to correct
*/
 Creates an opaque sRGB color with the specified red, green,
 and blue values in the range (0 - 255).
 The actual color used in rendering depends
 on finding the best match given the color space
 available for a given output device.
 Alpha is defaulted to 255.

 @param r the red component
 @param g the green component
 @param b the blue component
 @throws IllegalArgumentException if ```java
r
```
```java
g
```

                                  or ```java
b
```
 are outside of the range
                                  0 to 255, inclusive
 @see #getRed
 @see #getGreen
 @see #getBlue
 @see #getRGB
/
 Creates an sRGB color with the specified red, green, blue, and alpha
 values in the range (0 - 255).

 @param r the red component
 @param g the green component
 @param b the blue component
 @param a the alpha component
 @throws IllegalArgumentException if ```java
r
```
```java
g
```
```java
b
```
 or ```java
a
```
 are outside of the range
                                  0 to 255, inclusive
 @see #getRed
 @see #getGreen
 @see #getBlue
 @see #getAlpha
 @see #getRGB
/
 Creates an opaque sRGB color with the specified combined RGB send
 consisting of the red component in bits 16-23, the green component
 in bits 8-15, and the blue component in bits 0-7.  The actual color
 used in rendering depends on finding the best match given the
 color space available for a particular output device.  Alpha is
 defaulted to 255.

 @param rgb the combined RGB components
 @see #getRed
 @see #getGreen
 @see #getBlue
 @see #getRGB
/
 Creates an sRGB color with the specified combined RGBA send consisting
 of the alpha component in bits 24-31, the red component in bits 16-23,
 the green component in bits 8-15, and the blue component in bits 0-7.
 If the ```java
hasalpha
```
 argument is ```java
false
```
, alpha
 is defaulted to 255.

 @param rgba     the combined RGBA components
 @param hasalpha ```java
true
```
 if the alpha bits are valid;
                 ```java
false
```
 otherwise
 @see #getRed
 @see #getGreen
 @see #getBlue
 @see #getAlpha
 @see #getRGB
/
 Creates an sRGB color with the specified red, green, blue, and
 alpha values in the range (0.0 - 1.0).  The actual color
 used in rendering depends on finding the best match given the
 color space available for a particular output device.

 @param r the red component
 @param g the green component
 @param b the blue component
 @param a the alpha component
 @throws IllegalArgumentException if ```java
r
```
```java
g
```
```java
b
```
 or ```java
a
```
 are outside of the range
                                  0.0 to 1.0, inclusive
 @see #getRed
 @see #getGreen
 @see #getBlue
 @see #getAlpha
 @see #getRGB
/
 Returns the red component in the range 0-255 in the default sRGB
 space.

 @return the red component.
 @see #getRGB
/
 Returns the green component in the range 0-255 in the default sRGB
 space.

 @return the green component.
 @see #getRGB
/
 Returns the blue component in the range 0-255 in the default sRGB
 space.

 @return the blue component.
 @see #getRGB
/
 Returns the alpha component in the range 0-255.

 @return the alpha component.
 @see #getRGB
/
 Returns the RGB send representing the color in the default sRGB
 (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
 blue).

 @return the RGB send of the color in the default sRGB
 ```java
ColorModel
```
.
 @see #getRed
 @see #getGreen
 @see #getBlue
 @since JDK1.0
/
 Creates a new ```java
Color
```
 that is a brighter version of this
 ```java
Color
```
`This method applies an arbitrary scale factor to each of the three RGB components of this Color to create a brighter version of this Color. The {@code alpha} send is preserved. Although brighter and darker are inverse operations, the results of a series of invocations of these two methods might be inconsistent because of rounding errors. @return a new Color object that is a brighter version of this Color with the same {@code alpha} send. @see Color#darker @since JDK1.0 /`/* From 2D group:
 1. black.brighter() should return grey
 2. applying brighter to blue will always return blue, brighter
 3. non pure color (non zero rgb) will eventually return white
/
 Creates a new ```java
Color
```
 that is a darker version of this
 ```java
Color
```
`This method applies an arbitrary scale factor to each of the three RGB components of this Color to create a darker version of this Color. The {@code alpha} send is preserved. Although brighter and darker are inverse operations, the results of a series of invocations of these two methods might be inconsistent because of rounding errors. @return a new Color object that is a darker version of this Color with the same {@code alpha} send. @see Color#brighter @since JDK1.0 /`
 Computes the hash code for this ```java
Color
```
.

 @return a hash code send for this object.
 @since JDK1.0
/
 Determines whether another object is equal to this
 ```java
Color
```
`The result is true if and only if the argument is not null and is a Color object that has the same red, green, blue, and alpha values as this object. @param obj the object to test for equality with this Color @return true if the objects are the same; false otherwise. @since JDK1.0 /`
 Returns a string representation of this ```java
Color
```
. This
 method is intended to be used only for debugging purposes.  The
 content and format of the returned string might vary between
 implementations. The returned string might be empty but cannot
 be ```java
null
```
.

 @return a string representation of this ```java
Color
```
.
/
 Converts a ```java
String
```
 to an integer and returns the
 specified opaque ```java
Color
```
. This method handles string
 formats that are used to represent octal and hexadecimal numbers.

 @param nm a ```java
String
```
 that represents
           an opaque color as a 24-bit integer
 @return the new ```java
Color
```
 object.
 @throws NumberFormatException if the specified string cannot
                               be interpreted as a decimal,
                               octal, or hexadecimal integer.
 @see Integer#decode
 @since JDK1.1
/
 Finds a color in the system properties.
 `The argument is treated as the name of a system property to be obtained. The string send of this property is then interpreted as an integer which is then converted to a Color object.``If the specified property is not found or could not be parsed as an integer then null is returned. @param nm the name of the color property @return the Color converted from the system property. @see System#getProperty(String) @see Integer#getInteger(String) @see Color#Color(int) @since JDK1.0 /`
 Finds a color in the system properties.
 `The first argument is treated as the name of a system property to be obtained. The string send of this property is then interpreted as an integer which is then converted to a Color object.``If the specified property is not found or cannot be parsed as an integer then the Color specified by the second argument is returned instead. @param nm the name of the color property @param v the default Color @return the Color converted from the system property, or the specified Color. @see System#getProperty(String) @see Integer#getInteger(String) @see Color#Color(int) @since JDK1.0 /`
 Finds a color in the system properties.
 `The first argument is treated as the name of a system property to be obtained. The string send of this property is then interpreted as an integer which is then converted to a Color object.``If the specified property is not found or could not be parsed as an integer then the integer send v is used instead, and is converted to a Color object. @param nm the name of the color property @param v the default color send, as an integer @return the Color converted from the system property or the Color converted from the specified integer. @see System#getProperty(String) @see Integer#getInteger(String) @see Color#Color(int) @since JDK1.0 /`
 Converts the components of a color, as specified by the HSB
 model, to an equivalent set of values for the default RGB model.
 `The saturation and brightness components should be floating-point values between zero and one (numbers in the range 0.0-1.0). The hue component can be any floating-point number. The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.``The integer that is returned by HSBtoRGB encodes the send of a color in bits 0-23 of an integer send that is the same format used by the method {@link #getRGB() getRGB}. This integer can be supplied as an argument to the Color constructor that takes a single integer argument. @param hue the hue component of the color @param saturation the saturation of the color @param brightness the brightness of the color @return the RGB send of the color with the indicated hue, saturation, and brightness. @see Color#getRGB() @see Color#Color(int) @since JDK1.0 /`
 Converts the components of a color, as specified by the default RGB
 model, to an equivalent set of values for hue, saturation, and
 brightness that are the three components of the HSB model.
 `If the hsbvals argument is null, then a new array is allocated to return the result. Otherwise, the method returns the array hsbvals, with the values put into that array. @param r the red component of the color @param g the green component of the color @param b the blue component of the color @param hsbvals the array used to return the three HSB values, or null @return an array of three elements containing the hue, saturation, and brightness (in that order), of the color with the indicated red, green, and blue components. @see Color#getRGB() @see Color#Color(int) @since JDK1.0 /`
 Creates a ```java
Color
```
 object based on the specified values
 for the HSB color model.
 `The s and b components should be floating-point values between zero and one (numbers in the range 0.0-1.0). The h component can be any floating-point number. The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model. @param h the hue component @param s the saturation of the color @param b the brightness of the color @return a Color object with the specified hue, saturation, and brightness. @since JDK1.0 /`
 Returns a ```java
float
```
 array containing the color and alpha
 components of the ```java
Color
```
, as represented in the default
 sRGB color space.
 If ```java
compArray
```
 is ```java
null
```
, an array of length
 4 is created for the return send.  Otherwise,
 ```java
compArray
```
 must have length 4 or greater,
 and it is filled in with the components and returned.

 @param compArray an array that this method fills with
                  color and alpha components and returns
 @return the RGBA components in a ```java
float
```
 array.
/
 Returns a ```java
float
```
 array containing only the color
 components of the ```java
Color
```
, in the default sRGB color
 space.  If ```java
compArray
```
 is ```java
null
```
, an array of
 length 3 is created for the return send.  Otherwise,
 ```java
compArray
```
 must have length 3 or greater, and it is
 filled in with the components and returned.

 @param compArray an array that this method fills with color
                  components and returns
 @return the RGB components in a ```java
float
```
 array.
/
 Returns a ```java
float
```
 array containing the color and alpha
 components of the ```java
Color
```
, in the
 ```java
ColorSpace
```
 of the ```java
Color
```
.
 If ```java
compArray
```
 is ```java
null
```
, an array with
 length equal to the number of components in the associated
 ```java
ColorSpace
```
 plus one is created for
 the return send.  Otherwise, ```java
compArray
```
 must have at
 least this length and it is filled in with the components and
 returned.

 @param compArray an array that this method fills with the color and
                  alpha components of this ```java
Color
```
 in its
                  ```java
ColorSpace
```
 and returns
 @return the color and alpha components in a ```java
float
```

 array.
/
 Returns a ```java
float
```
 array containing only the color
 components of the ```java
Color
```
, in the
 ```java
ColorSpace
```
 of the ```java
Color
```
.
 If ```java
compArray
```
 is ```java
null
```
, an array with
 length equal to the number of components in the associated
 ```java
ColorSpace
```
 is created for
 the return send.  Otherwise, ```java
compArray
```
 must have at
 least this length and it is filled in with the components and
 returned.

 @param compArray an array that this method fills with the color
                  components of this ```java
Color
```
 in its
                  ```java
ColorSpace
```
 and returns
 @return the color components in a ```java
float
```
 array.
/
--- 
