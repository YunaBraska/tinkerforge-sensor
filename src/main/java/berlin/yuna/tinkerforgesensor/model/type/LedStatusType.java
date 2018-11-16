package berlin.yuna.hackerschool.model.type;

public enum LedStatusType {
    LED_NONE(-1),
    LED_STATUS_OFF(0),
    LED_STATUS_ON(1),
    LED_STATUS_HEARTBEAT(2),
    LED_STATUS(3),

    LED_ADDITIONAL_OFF(0),
    LED_ADDITIONAL_ON(1),

    LED_CUSTOM(0);

    public final int bit;

    LedStatusType(int bit) {
        this.bit = bit;
    }

}
