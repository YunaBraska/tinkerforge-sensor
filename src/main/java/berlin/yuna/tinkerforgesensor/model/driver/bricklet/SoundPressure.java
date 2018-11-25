package berlin.yuna.tinkerforgesensor.model.driver.bricklet;

import berlin.yuna.tinkerforgesensor.logic.SensorRegistration;
import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.driver.Driver;
import com.tinkerforge.BrickletSoundPressureLevel;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.util.List;
import java.util.function.Consumer;

import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_HEARTBEAT;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.type.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_DECIBEL;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM_LENGTH;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM_OFFSET;

public class SoundPressure extends Driver {

    public static void register(final SensorRegistration registration, final Sensor sensor, final List<Consumer<SensorEvent>> consumerList, final int period) {
        BrickletSoundPressureLevel device = (BrickletSoundPressureLevel) sensor.device;
        registration.sensitivity(50, SOUND);

        device.addDecibelListener(value -> registration.sendEvent(consumerList, SOUND_DECIBEL, sensor, (long) value));
        device.addSpectrumLowLevelListener((spectrumLength, spectrumChunkOffset, spectrumChunkData) ->
        {
            registration.sendEvent(consumerList, SOUND_SPECTRUM_LENGTH, sensor, (long) spectrumLength);
            registration.sendEvent(consumerList, SOUND_SPECTRUM_OFFSET, sensor, (long) spectrumChunkOffset);
            //TODO: Spectrum registration.sendEvent(consumerList, SOUND_DECIBEL, sensor, (long) spectrumChunkData);
        });

        sensor.hasStatusLed = true;
        registration.ledConsumer.add(sensorLedEvent -> sensorLedEvent.process(
                i -> {
                    if (i ==  LED_STATUS_ON.bit) {device.setStatusLEDConfig(LED_STATUS_ON.bit);}
                    else if (i == LED_STATUS_HEARTBEAT.bit) {device.setStatusLEDConfig(LED_STATUS_HEARTBEAT.bit);}
                    else if (i == LED_STATUS.bit) {device.setStatusLEDConfig(LED_STATUS.bit);}
                    else if (i == LED_STATUS_OFF.bit) {device.setStatusLEDConfig(LED_STATUS_OFF.bit);}
                },
                ignore -> { }, ignore -> { }));

        try {
            device.setDecibelCallbackConfiguration(period, false, 'x', 0, 0);
            device.setSpectrumCallbackConfiguration(period);
        } catch (TimeoutException | NotConnectedException ignore) {
        }
    }
}
