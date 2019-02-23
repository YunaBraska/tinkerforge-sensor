package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

public class VoltageCurrentV2 {

//    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) {
//        BrickletVoltageCurrentV2 device = (BrickletVoltageCurrentV2) sensor.device;
//        registration.sensitivity(50, ENERGY);
//
//        device.addCurrentListener(value -> sendEvent( CURRENT, (long) value));
//        device.addPowerListener(value -> sendEvent( POWER, (long) value));
//        device.addVoltageListener(value -> sendEvent( VOLTAGE, (long) value));
//
//        sensor.hasStatusLed = true;
//        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
//                value -> {
//                    if (value == LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
//                    else if (value ==LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
//                    else if (value ==LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
//                    else if (value ==LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
//                }, ignore -> { }, ignore -> { }));
//    }
}
