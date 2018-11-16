package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.type.LedStatusType;
import berlin.yuna.tinkerforgesensor.model.type.ThrowingConsumer;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_ADDITIONAL_ON;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_CUSTOM;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;

public class SensorRequest extends TinkerForgeUtil {

    public final Object customValue;
    public final LedStatusType ledStatusType;

    public SensorRequest(final LedStatusType ledStatusType, final Object customValue) {
        this.ledStatusType = ledStatusType;
        this.customValue = customValue;
    }

    public void process(final ThrowingConsumer<Integer, Exception> functionStatus, final ThrowingConsumer<Integer, Exception> functionAdditional, final ThrowingConsumer<Object, Exception> custom) {
        try {
            Integer customInt = getIntFromCustomValue();
            switch (ledStatusType) {
                case LED_STATUS_ON:
                    functionStatus.accept(customInt != null ? customInt : LED_STATUS_ON.bit);
                    break;
                case LED_STATUS_OFF:
                    functionStatus.accept(customInt != null ? customInt : LED_STATUS_OFF.bit);
                    break;
                case LED_ADDITIONAL_ON:
                    functionAdditional.accept(customInt != null ? customInt : LED_ADDITIONAL_ON.bit);
                    break;
                case LED_ADDITIONAL_OFF:
                    functionAdditional.accept(customInt != null ? customInt : LED_ADDITIONAL_OFF.bit);
                    break;
                case LED_CUSTOM:
                    custom.accept(customValue != null ? customValue : (long) LED_CUSTOM.bit);
                    break;
            }
        } catch (Exception e) {
            error("[%s] [%s] [%s]", getClass().getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
        }

    }

    private Integer getIntFromCustomValue() {
        if (customValue instanceof Long) {
            return ((Long) customValue).intValue();
        } else if (customValue instanceof Integer) {
            return (Integer) customValue;
        }
        return null;
    }


}
