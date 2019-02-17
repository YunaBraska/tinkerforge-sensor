package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

public class LightColor {

//    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) throws TimeoutException, NotConnectedException {
//        BrickletColor device = (BrickletColor) sensor.device;
//        registration.sensitivity(50, COLOR);
//
//        device.addColorListener((r, g, b, c) -> {
//            sendEvent( COLOR_R, (long) r / 256);
//            sendEvent( COLOR_G, (long) g / 256);
//            sendEvent( COLOR_B, (long) b / 256);
//            sendEvent( COLOR_LUX, (long) c / 256);
//        });
//        device.addColorTemperatureListener(value -> sendEvent( COLOR_TEMPERATURE, (long) value));
//
//        device.setColorCallbackPeriod(period);
//        device.setColorTemperatureCallbackPeriod(period);
//
//        sensor.hasStatusLed = true;
//        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(ignore -> { },
//                value -> {
//                    if (value ==LED_ADDITIONAL_ON.bit) {device.lightOn();}
//                    else if (value ==LED_ADDITIONAL_OFF.bit) {device.lightOff();}
//                },
//                ignore -> { }));
//    }
}
