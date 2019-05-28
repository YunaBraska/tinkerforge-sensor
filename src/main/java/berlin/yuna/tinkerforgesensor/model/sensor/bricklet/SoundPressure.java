package berlin.yuna.tinkerforgesensor.model.sensor.bricklet;

public class SoundPressure {

//    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) {
//        BrickletSoundPressureLevel device = (BrickletSoundPressureLevel) sensor.device;
//        registration.sensitivity(50, SOUND);
//
//        device.addDecibelListener(send -> sendEvent( SOUND_DECIBEL, (long) send));
//        device.addSpectrumLowLevelListener((spectrumLength, spectrumChunkOffset, spectrumChunkData) ->
//        {
//            sendEvent( SOUND_SPECTRUM_LENGTH, (long) spectrumLength);
//            sendEvent( SOUND_SPECTRUM_OFFSET, (long) spectrumChunkOffset);
//            //TODO: Spectrum sendEvent( SOUND_DECIBEL, (long) spectrumChunkData);
//        });
//
//        sensor.hasStatusLed = true;
//        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
//                send -> {
//                    if (send == LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
//                    else if (send ==LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
//                    else if (send ==LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
//                    else if (send ==LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
//                },
//                ignore -> { }, ignore -> { }));
//
//        try {
//            device.setDecibelCallbackConfiguration(period, false, 'x', 0, 0);
//            device.setSpectrumCallbackConfiguration(period);
//        } catch (TimeoutException | NotConnectedException ignore) {
//        }
//    }
}
