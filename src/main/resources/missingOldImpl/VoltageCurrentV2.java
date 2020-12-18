package berlin.yuna.tinkerforgesensor.model.missing;

public class VoltageCurrentV2 {

//    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) {
//        BrickletVoltageCurrentV2 device = (BrickletVoltageCurrentV2) sensor.device;
//        registration.sensitivity(50, ENERGY);
//
//        device.addCurrentListener(send -> sendEventUnchecked( CURRENT, (long) send));
//        device.addPowerListener(send -> sendEventUnchecked( POWER, (long) send));
//        device.addVoltageListener(send -> sendEventUnchecked( VOLTAGE, (long) send));
//
//        sensor.hasStatusLed = true;
//        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
//                send -> {
//                    if (send == LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
//                    else if (send ==LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
//                    else if (send ==LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
//                    else if (send ==LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
//                }, ignore -> { }, ignore -> { }));
//    }
}
