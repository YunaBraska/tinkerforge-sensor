package berlin.yuna.tinkerforgesensor.model.type;

public enum LedChipType {
    CHIP_TYPE_WS2801(2801, LedMapping.BGR),
    CHIP_TYPE_WS2811(2811, LedMapping.BGR),
    CHIP_TYPE_WS2812(2812, LedMapping.GRB),
    CHIP_TYPE_WS2812B(CHIP_TYPE_WS2812.value, CHIP_TYPE_WS2812.rawMapping()),
    CHIP_TYPE_SK6812(CHIP_TYPE_WS2812.value, CHIP_TYPE_WS2812.rawMapping()),
    CHIP_TYPE_SK6812RGBW(CHIP_TYPE_WS2812.value, CHIP_TYPE_WS2812.rawMapping()),
    CHIP_TYPE_LPD8806(8806, LedMapping.RGB),
    APA102(102, LedMapping.WBGR);

    private final int value;
    private final LedMapping ledMapping;

    LedChipType(final int value, final LedMapping ledMapping) {
        this.value = value;
        this.ledMapping = ledMapping;
    }

    public int value() {
        return value;
    }

    public char[] mapping() {
        return ledMapping.mapping();
    }

    public int channel() {
        return ledMapping.channel();
    }

    private LedMapping rawMapping() {
        return ledMapping;
    }
}

enum LedMapping {
    RGB(6),
    RBG(9),
    BRG(33),
    BGR(36),
    GRB(18),
    GBR(24),
    RGBW(27),
    RGWB(30),
    RBGW(39),
    RBWG(45),
    RWGB(54),
    RWBG(57),
    GRWB(78),
    GRBW(75),
    GBWR(108),
    GBRW(99),
    GWBR(120),
    GWRB(114),
    BRGW(135),
    BRWG(141),
    BGRW(147),
    BGWR(156),
    BWRG(177),
    BWGR(180),
    WRBG(201),
    WRGB(198),
    WGBR(216),
    WGRB(210),
    WBGR(228),
    WBRG(225);

    private final int channel;

    LedMapping(final int channel) {
        this.channel = channel;
    }

    public int channel() {
        return channel;
    }

    public char[] mapping() {
        return name().toCharArray();
    }
}