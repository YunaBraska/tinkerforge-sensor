package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

public class ButtonRGB {

//    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList) {
//        BrickletRGBLEDButton device = (BrickletRGBLEDButton) sensor.device;
//        registration.sensitivity(100, BUTTON);
//        device.addButtonStateChangedListener(value -> {
//            sendEvent( BUTTON, (long) value);
//            sendEvent( (value == BUTTON_STATE_PRESSED) ? BUTTON_PRESSED : BUTTON_RELEASED, (long) value);
//        });
////            current.getChipTemperature()
////            current.getColor()
//        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
//                value -> {
//                    if (value ==LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
//                    else if (value ==LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
//                    else if (value ==LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
//                    else if (value ==LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
//                }, ignored -> { }, customValue -> {
//                    if (customValue instanceof Number) {
//                        Color color = new Color(((Number) customValue).intValue());
//                        device.setColor(color.getRed(), color.getGreen(), color.getBlue());
//                    } else {
//                        device.setColor(0, 0, 0);
//                    }
//                }));
//
//        sensor.hasStatusLed = true;
//    }
}
