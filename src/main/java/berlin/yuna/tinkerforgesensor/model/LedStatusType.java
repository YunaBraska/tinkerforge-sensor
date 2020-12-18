package berlin.yuna.tinkerforgesensor.model;

import static java.util.Arrays.asList;

public enum LedStatusType {
    LED_NONE(-1),
    LED_OFF(0),
    LED_ON(1),
    LED_HEARTBEAT(2),
    LED_STATUS(3);

    public final int bit;

    LedStatusType(final int bit) {
        this.bit = bit;
    }

    public static LedStatusType ledStatusTypeOf(final int bit) {
        for (LedStatusType status : asList(LED_NONE, LED_ON, LED_OFF, LED_HEARTBEAT, LED_STATUS)) {
            if (bit == status.bit) {
                return status;
            }
        }
        return LED_NONE;
    }

    public static boolean isLedOn(int value) {
        return value == LED_ON.bit || value == LED_STATUS.bit || value == LED_HEARTBEAT.bit;
    }
}
