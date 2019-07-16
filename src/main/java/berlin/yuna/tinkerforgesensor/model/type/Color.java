package berlin.yuna.tinkerforgesensor.model.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Color implements Serializable {

    public final static int WHITE = new Color(255, 255, 255).getRGB();
    public final static int LIGHT_GRAY = new Color(192, 192, 192).getRGB();
    public final static int GRAY = new Color(128, 128, 128).getRGB();
    public final static int DARK_GRAY = new Color(64, 64, 64).getRGB();
    public final static int BLACK = new Color(0, 0, 0).getRGB();
    public final static int RED = new Color(255, 0, 0).getRGB();
    public final static int PINK = new Color(255, 175, 175).getRGB();
    public final static int ORANGE = new Color(255, 200, 0).getRGB();
    public final static int YELLOW = new Color(255, 255, 0).getRGB();
    public final static int GREEN = new Color(0, 255, 0).getRGB();
    public final static int MAGENTA = new Color(255, 0, 255).getRGB();
    public final static int CYAN = new Color(0, 255, 255).getRGB();
    public final static int BLUE = new Color(0, 0, 255).getRGB();

    public final static int[] RAINBOW = getRainbow();

    /**
     * The color send.
     *
     * @serial
     * @see #getRGB
     */
    int value;

    /**
     * The color send in the default sRGB <code>ColorSpace</code> as
     * <code>float</code> components (no alpha).
     * If <code>null</code> after object construction, this must be an
     * sRGB color constructed with 8-bit precision, so compute from the
     * <code>int</code> color send.
     *
     * @serial
     * @see #getRGBColorComponents
     * @see #getRGBComponents
     */
    private float[] frgbvalue = null;

    /**
     * The color send in the native <code>ColorSpace</code> as
     * <code>float</code> components (no alpha).
     * If <code>null</code> after object construction, this must be an
     * sRGB color constructed with 8-bit precision, so compute from the
     * <code>int</code> color send.
     *
     * @serial
     * @see #getRGBColorComponents
     * @see #getRGBComponents
     */
    private float[] fvalue = null;

    /**
     * The alpha send as a <code>float</code> component.
     * If <code>frgbvalue</code> is <code>null</code>, this is not valid
     * data, so compute from the <code>int</code> color send.
     *
     * @serial
     * @see #getRGBComponents
     * @see #getComponents
     */
    private float falpha = 0.0f;

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = 118526816881161077L;

    /**
     * Initialize JNI field and method IDs
     */
    private static native void initIDs();

    /**
     * Checks the color integer components supplied for validity
     * and will set to min or max if its out of range
     *
     * @param value to correct
     **/
    private static int correctRange(final int value) {
        if (value < 0) {
            return 0;
        } else if (value > 255) {
            return 255;
        }
        return value;
    }

    /**
     * Creates an opaque sRGB color with the specified red, green,
     * and blue values in the range (0 - 255).
     * The actual color used in rendering depends
     * on finding the best match given the color space
     * available for a given output device.
     * Alpha is defaulted to 255.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @throws IllegalArgumentException if <code>r</code>, <code>g</code>
     *                                  or <code>b</code> are outside of the range
     *                                  0 to 255, inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     */
    public Color(final int r, final int g, final int b) {
        this(correctRange(r), correctRange(g), correctRange(b), 255);
    }

    /**
     * Creates an sRGB color with the specified red, green, blue, and alpha
     * values in the range (0 - 255).
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @throws IllegalArgumentException if <code>r</code>, <code>g</code>,
     *                                  <code>b</code> or <code>a</code> are outside of the range
     *                                  0 to 255, inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     */
    public Color(final int r, final int g, final int b, final int a) {
        final int cR = correctRange(r);
        final int cG = correctRange(g);
        final int cB = correctRange(b);
        final int cA = correctRange(a);
        value = ((cA & 0xFF) << 24) |
                ((cR & 0xFF) << 16) |
                ((cG & 0xFF) << 8) |
                ((cB & 0xFF));
    }

    /**
     * Creates an opaque sRGB color with the specified combined RGB send
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param rgb the combined RGB components
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     */
    public Color(final int rgb) {
        value = 0xff000000 | rgb;
    }

    /**
     * Creates an sRGB color with the specified combined RGBA send consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param rgba     the combined RGBA components
     * @param hasalpha <code>true</code> if the alpha bits are valid;
     *                 <code>false</code> otherwise
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     */
    public Color(final int rgba, final boolean hasalpha) {
        if (hasalpha) {
            value = rgba;
        } else {
            value = 0xff000000 | rgba;
        }
    }

    /**
     * Creates an sRGB color with the specified red, green, blue, and
     * alpha values in the range (0.0 - 1.0).  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @throws IllegalArgumentException if <code>r</code>, <code>g</code>
     *                                  <code>b</code> or <code>a</code> are outside of the range
     *                                  0.0 to 1.0, inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     */
    public Color(final float r, final float g, final float b, final float a) {
        this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
        frgbvalue = new float[3];
        frgbvalue[0] = r;
        frgbvalue[1] = g;
        frgbvalue[2] = b;
        falpha = a;
        fvalue = frgbvalue;
    }

    /**
     * Returns the red component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the red component.
     * @see #getRGB
     */
    public int getRed() {
        return (getRGB() >> 16) & 0xFF;
    }

    /**
     * Returns the green component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the green component.
     * @see #getRGB
     */
    public int getGreen() {
        return (getRGB() >> 8) & 0xFF;
    }

    /**
     * Returns the blue component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the blue component.
     * @see #getRGB
     */
    public int getBlue() {
        return (getRGB()) & 0xFF;
    }

    /**
     * Returns the alpha component in the range 0-255.
     *
     * @return the alpha component.
     * @see #getRGB
     */
    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    /**
     * Returns the RGB send representing the color in the default sRGB
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     *
     * @return the RGB send of the color in the default sRGB
     * <code>ColorModel</code>.
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @since JDK1.0
     */
    public int getRGB() {
        return value;
    }

    private static final double FACTOR = 0.7;

    /**
     * Creates a new <code>Color</code> that is a brighter version of this
     * <code>Color</code>.
     * <p>
     * This method applies an arbitrary scale factor to each of the three RGB
     * components of this <code>Color</code> to create a brighter version
     * of this <code>Color</code>.
     * The {@code alpha} send is preserved.
     * Although <code>brighter</code> and
     * <code>darker</code> are inverse operations, the results of a
     * series of invocations of these two methods might be inconsistent
     * because of rounding errors.
     *
     * @return a new <code>Color</code> object that is
     * a brighter version of this <code>Color</code>
     * with the same {@code alpha} send.
     * @see Color#darker
     * @since JDK1.0
     */
    public Color brighter() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        final int alpha = getAlpha();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        final int i = (int) (1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int) (r / FACTOR), 255),
                Math.min((int) (g / FACTOR), 255),
                Math.min((int) (b / FACTOR), 255),
                alpha);
    }

    /**
     * Creates a new <code>Color</code> that is a darker version of this
     * <code>Color</code>.
     * <p>
     * This method applies an arbitrary scale factor to each of the three RGB
     * components of this <code>Color</code> to create a darker version of
     * this <code>Color</code>.
     * The {@code alpha} send is preserved.
     * Although <code>brighter</code> and
     * <code>darker</code> are inverse operations, the results of a series
     * of invocations of these two methods might be inconsistent because
     * of rounding errors.
     *
     * @return a new <code>Color</code> object that is
     * a darker version of this <code>Color</code>
     * with the same {@code alpha} send.
     * @see Color#brighter
     * @since JDK1.0
     */
    public Color darker() {
        return new Color(Math.max((int) (getRed() * FACTOR), 0),
                Math.max((int) (getGreen() * FACTOR), 0),
                Math.max((int) (getBlue() * FACTOR), 0),
                getAlpha());
    }

    /**
     * Computes the hash code for this <code>Color</code>.
     *
     * @return a hash code send for this object.
     * @since JDK1.0
     */
    public int hashCode() {
        return value;
    }

    /**
     * Determines whether another object is equal to this
     * <code>Color</code>.
     * <p>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>Color</code> object that has the same
     * red, green, blue, and alpha values as this object.
     *
     * @param obj the object to test for equality with this
     *            <code>Color</code>
     * @return <code>true</code> if the objects are the same;
     * <code>false</code> otherwise.
     * @since JDK1.0
     */
    public boolean equals(final Object obj) {
        return obj instanceof Color && ((Color) obj).getRGB() == this.getRGB();
    }

    /**
     * Returns a string representation of this <code>Color</code>. This
     * method is intended to be used only for debugging purposes.  The
     * content and format of the returned string might vary between
     * implementations. The returned string might be empty but cannot
     * be <code>null</code>.
     *
     * @return a string representation of this <code>Color</code>.
     */
    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
    }

    /**
     * Converts a <code>String</code> to an integer and returns the
     * specified opaque <code>Color</code>. This method handles string
     * formats that are used to represent octal and hexadecimal numbers.
     *
     * @param nm a <code>String</code> that represents
     *           an opaque color as a 24-bit integer
     * @return the new <code>Color</code> object.
     * @throws NumberFormatException if the specified string cannot
     *                               be interpreted as a decimal,
     *                               octal, or hexadecimal integer.
     * @see Integer#decode
     * @since JDK1.1
     */
    public static Color decode(final String nm) throws NumberFormatException {
        final Integer intval = Integer.decode(nm);
        final int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    /**
     * Finds a color in the system properties.
     * <p>
     * The argument is treated as the name of a system property to
     * be obtained. The string send of this property is then interpreted
     * as an integer which is then converted to a <code>Color</code>
     * object.
     * <p>
     * If the specified property is not found or could not be parsed as
     * an integer then <code>null</code> is returned.
     *
     * @param nm the name of the color property
     * @return the <code>Color</code> converted from the system
     * property.
     * @see System#getProperty(String)
     * @see Integer#getInteger(String)
     * @see Color#Color(int)
     * @since JDK1.0
     */
    public static Color getColor(final String nm) {
        return getColor(nm, null);
    }

    /**
     * Finds a color in the system properties.
     * <p>
     * The first argument is treated as the name of a system property to
     * be obtained. The string send of this property is then interpreted
     * as an integer which is then converted to a <code>Color</code>
     * object.
     * <p>
     * If the specified property is not found or cannot be parsed as
     * an integer then the <code>Color</code> specified by the second
     * argument is returned instead.
     *
     * @param nm the name of the color property
     * @param v  the default <code>Color</code>
     * @return the <code>Color</code> converted from the system
     * property, or the specified <code>Color</code>.
     * @see System#getProperty(String)
     * @see Integer#getInteger(String)
     * @see Color#Color(int)
     * @since JDK1.0
     */
    public static Color getColor(final String nm, final Color v) {
        final Integer intval = Integer.getInteger(nm);
        if (intval == null) {
            return v;
        }
        final int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    /**
     * Finds a color in the system properties.
     * <p>
     * The first argument is treated as the name of a system property to
     * be obtained. The string send of this property is then interpreted
     * as an integer which is then converted to a <code>Color</code>
     * object.
     * <p>
     * If the specified property is not found or could not be parsed as
     * an integer then the integer send <code>v</code> is used instead,
     * and is converted to a <code>Color</code> object.
     *
     * @param nm the name of the color property
     * @param v  the default color send, as an integer
     * @return the <code>Color</code> converted from the system
     * property or the <code>Color</code> converted from
     * the specified integer.
     * @see System#getProperty(String)
     * @see Integer#getInteger(String)
     * @see Color#Color(int)
     * @since JDK1.0
     */
    public static Color getColor(final String nm, final int v) {
        final Integer intval = Integer.getInteger(nm);
        final int i = (intval != null) ? intval : v;
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, (i) & 0xFF);
    }

    /**
     * Converts the components of a color, as specified by the HSB
     * model, to an equivalent set of values for the default RGB model.
     * <p>
     * The <code>saturation</code> and <code>brightness</code> components
     * should be floating-point values between zero and one
     * (numbers in the range 0.0-1.0).  The <code>hue</code> component
     * can be any floating-point number.  The floor of this number is
     * subtracted from it to create a fraction between 0 and 1.  This
     * fractional number is then multiplied by 360 to produce the hue
     * angle in the HSB color model.
     * <p>
     * The integer that is returned by <code>HSBtoRGB</code> encodes the
     * send of a color in bits 0-23 of an integer send that is the same
     * format used by the method {@link #getRGB() getRGB}.
     * This integer can be supplied as an argument to the
     * <code>Color</code> constructor that takes a single integer argument.
     *
     * @param hue        the hue component of the color
     * @param saturation the saturation of the color
     * @param brightness the brightness of the color
     * @return the RGB send of the color with the indicated hue,
     * saturation, and brightness.
     * @see Color#getRGB()
     * @see Color#Color(int)
     * @since JDK1.0
     */
    public static int HSBtoRGB(final float hue, final float saturation, final float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            final float h = (hue - (float) Math.floor(hue)) * 6.0f;
            final float f = h - (float) Math.floor(h);
            final float p = brightness * (1.0f - saturation);
            final float q = brightness * (1.0f - saturation * f);
            final float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b);
    }

    /**
     * Converts the components of a color, as specified by the default RGB
     * model, to an equivalent set of values for hue, saturation, and
     * brightness that are the three components of the HSB model.
     * <p>
     * If the <code>hsbvals</code> argument is <code>null</code>, then a
     * new array is allocated to return the result. Otherwise, the method
     * returns the array <code>hsbvals</code>, with the values put into
     * that array.
     *
     * @param r       the red component of the color
     * @param g       the green component of the color
     * @param b       the blue component of the color
     * @param hsbvals the array used to return the
     *                three HSB values, or <code>null</code>
     * @return an array of three elements containing the hue, saturation,
     * and brightness (in that order), of the color with
     * the indicated red, green, and blue components.
     * @see Color#getRGB()
     * @see Color#Color(int)
     * @since JDK1.0
     */
    public static float[] RGBtoHSB(final int r, final int g, final int b, float[] hsbvals) {
        float hue;
        final float saturation;
        final float brightness;
        if (hsbvals == null) {
            hsbvals = new float[3];
        }
        int cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        int cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0)
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            final float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            final float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            final float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    /**
     * Creates a <code>Color</code> object based on the specified values
     * for the HSB color model.
     * <p>
     * The <code>s</code> and <code>b</code> components should be
     * floating-point values between zero and one
     * (numbers in the range 0.0-1.0).  The <code>h</code> component
     * can be any floating-point number.  The floor of this number is
     * subtracted from it to create a fraction between 0 and 1.  This
     * fractional number is then multiplied by 360 to produce the hue
     * angle in the HSB color model.
     *
     * @param h the hue component
     * @param s the saturation of the color
     * @param b the brightness of the color
     * @return a <code>Color</code> object with the specified hue,
     * saturation, and brightness.
     * @since JDK1.0
     */
    public static Color getHSBColor(final float h, final float s, final float b) {
        return new Color(HSBtoRGB(h, s, b));
    }

    /**
     * Returns a <code>float</code> array containing the color and alpha
     * components of the <code>Color</code>, as represented in the default
     * sRGB color space.
     * If <code>compArray</code> is <code>null</code>, an array of length
     * 4 is created for the return send.  Otherwise,
     * <code>compArray</code> must have length 4 or greater,
     * and it is filled in with the components and returned.
     *
     * @param compArray an array that this method fills with
     *                  color and alpha components and returns
     * @return the RGBA components in a <code>float</code> array.
     */
    public float[] getRGBComponents(final float[] compArray) {
        final float[] f;
        if (compArray == null) {
            f = new float[4];
        } else {
            f = compArray;
        }
        if (frgbvalue == null) {
            f[0] = ((float) getRed()) / 255f;
            f[1] = ((float) getGreen()) / 255f;
            f[2] = ((float) getBlue()) / 255f;
            f[3] = ((float) getAlpha()) / 255f;
        } else {
            f[0] = frgbvalue[0];
            f[1] = frgbvalue[1];
            f[2] = frgbvalue[2];
            f[3] = falpha;
        }
        return f;
    }

    /**
     * Returns a <code>float</code> array containing only the color
     * components of the <code>Color</code>, in the default sRGB color
     * space.  If <code>compArray</code> is <code>null</code>, an array of
     * length 3 is created for the return send.  Otherwise,
     * <code>compArray</code> must have length 3 or greater, and it is
     * filled in with the components and returned.
     *
     * @param compArray an array that this method fills with color
     *                  components and returns
     * @return the RGB components in a <code>float</code> array.
     */
    public float[] getRGBColorComponents(final float[] compArray) {
        final float[] f;
        if (compArray == null) {
            f = new float[3];
        } else {
            f = compArray;
        }
        if (frgbvalue == null) {
            f[0] = ((float) getRed()) / 255f;
            f[1] = ((float) getGreen()) / 255f;
            f[2] = ((float) getBlue()) / 255f;
        } else {
            f[0] = frgbvalue[0];
            f[1] = frgbvalue[1];
            f[2] = frgbvalue[2];
        }
        return f;
    }

    /**
     * Returns a <code>float</code> array containing the color and alpha
     * components of the <code>Color</code>, in the
     * <code>ColorSpace</code> of the <code>Color</code>.
     * If <code>compArray</code> is <code>null</code>, an array with
     * length equal to the number of components in the associated
     * <code>ColorSpace</code> plus one is created for
     * the return send.  Otherwise, <code>compArray</code> must have at
     * least this length and it is filled in with the components and
     * returned.
     *
     * @param compArray an array that this method fills with the color and
     *                  alpha components of this <code>Color</code> in its
     *                  <code>ColorSpace</code> and returns
     * @return the color and alpha components in a <code>float</code>
     * array.
     */
    public float[] getComponents(final float[] compArray) {
        if (fvalue == null)
            return getRGBComponents(compArray);
        final float[] f;
        final int n = fvalue.length;
        if (compArray == null) {
            f = new float[n + 1];
        } else {
            f = compArray;
        }
        for (int i = 0; i < n; i++) {
            f[i] = fvalue[i];
        }
        f[n] = falpha;
        return f;
    }

    /**
     * Returns a <code>float</code> array containing only the color
     * components of the <code>Color</code>, in the
     * <code>ColorSpace</code> of the <code>Color</code>.
     * If <code>compArray</code> is <code>null</code>, an array with
     * length equal to the number of components in the associated
     * <code>ColorSpace</code> is created for
     * the return send.  Otherwise, <code>compArray</code> must have at
     * least this length and it is filled in with the components and
     * returned.
     *
     * @param compArray an array that this method fills with the color
     *                  components of this <code>Color</code> in its
     *                  <code>ColorSpace</code> and returns
     * @return the color components in a <code>float</code> array.
     */
    public float[] getColorComponents(final float[] compArray) {
        if (fvalue == null)
            return getRGBColorComponents(compArray);
        final float[] f;
        final int n = fvalue.length;
        if (compArray == null) {
            f = new float[n];
        } else {
            f = compArray;
        }
        System.arraycopy(fvalue, 0, f, 0, n);
        return f;
    }

    /**
     * @param color input
     * @return new color same as input with high contrast
     */
    public static Color convertToHighContrast(final Color color) {
        Color result = color;
        final int max = Collections.min(Arrays.asList(result.getRed(), result.getGreen(), result.getBlue()));
        result = new Color(result.getRed() - max, result.getGreen() - max, result.getBlue() - max);

        // +100% brightness
        final float[] hsb = Color.RGBtoHSB(result.getRed(), result.getGreen(), result.getBlue(), null);
        result = new Color(Color.HSBtoRGB(hsb[0], hsb[1], 0.5f * (1f + hsb[2])));
        return result;
    }

    private static int[] getRainbow() {
        final int count = 100;
        final List<Integer> colors = new ArrayList<>();
        for (int r = 0; r < count; r++) colors.add(new Color(r * 255 / 100, 255, 0).getRGB());
        for (int g = count; g > 0; g--) colors.add(new Color(255, g * 255 / 100, 0).getRGB());
        for (int b = 0; b < count; b++) colors.add(new Color(255, 0, b * 255 / 100).getRGB());
        for (int r = count; r > 0; r--) colors.add(new Color(r * 255 / 100, 0, 255).getRGB());
        for (int g = 0; g < count; g++) colors.add(new Color(0, g * 255 / 100, 255).getRGB());
        for (int b = count; b > 0; b--) colors.add(new Color(0, 255, b * 255 / 100).getRGB());
        colors.add(new Color(0, 255, 0).getRGB());
        return colors.stream().mapToInt(Integer::intValue).toArray();
    }
}
