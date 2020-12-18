package berlin.yuna.tinkerforgesensor.util;

import berlin.yuna.tinkerforgesensor.model.AudioDevice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AudioControl {

    private final List<AudioDevice> outputDevices = new ArrayList<>();
    private final List<AudioDevice> inputDevices = new ArrayList<>();
    private final AudioDevice defaultDevice;

    public AudioControl() {
        final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            addAudioDevices(mixer, mixer.getTargetLineInfo(), true);
            addAudioDevices(mixer, mixer.getSourceLineInfo(), false);
        }
        defaultDevice = getMaster();
    }

    private void addAudioDevices(final Mixer mixer, final Line.Info[] lineInfos, final boolean output) {
        for (Line.Info lineInfo : lineInfos) {
            final Line line = getLine(mixer, lineInfo);
            if (line != null) {
                if (output) {
                    outputDevices.add(new AudioDevice(line, mixer, output));
                } else {
                    inputDevices.add(new AudioDevice(line, mixer, output));
                }
            }
        }
    }

    private Line getLine(final Mixer mixer, final Line.Info lineInfo) {
        try {
            if (mixer.isLineSupported(lineInfo)) {
                return mixer.getLine(lineInfo);
            }
        } catch (LineUnavailableException ignored) {
        }
        return null;
    }

    public void pauseAll() {
        outputDevices.forEach(AudioDevice::pause);
        inputDevices.forEach(AudioDevice::pause);
    }

    public void playAll() {
        outputDevices.forEach(AudioDevice::play);
        inputDevices.forEach(AudioDevice::play);
    }

    public int getVolume() {
        return defaultDevice != null ? defaultDevice.getVolume() : -1;
    }

    public void setVolume(final int volume) {
        if (defaultDevice != null) {
            defaultDevice.setVolume(volume);
        }
    }

    public void setAllVolumes(final int volume) {
        outputDevices.forEach(audioDevice -> audioDevice.setVolume(volume));
    }

    public boolean getMute() {
        return defaultDevice != null && defaultDevice.getMute();
    }

    public void setMute(final boolean mute) {
        if (defaultDevice != null) {
            defaultDevice.setMute(mute);
        }
    }

    public void setAllMute(final boolean mute) {
        outputDevices.forEach(audioDevice -> audioDevice.setMute(mute));
    }

    public AudioDevice getDefaultDevice() {
        return defaultDevice;
    }

    private AudioDevice getMaster() {
        final List<AudioDevice> volumeDevices = outputDevices.stream().filter(audioDevice -> audioDevice.getVolume() != -1).collect(Collectors.toList());
        final List<AudioDevice> masterVolumes = volumeDevices.stream()
                .filter(audioDevice ->
                        {
                            String lineInfo = audioDevice.lineInfo.toString().toLowerCase();
                            return !lineInfo.contains("interface")
                                    || lineInfo.contains("master")
                                    || lineInfo.contains("default");
                        }
                ).collect(Collectors.toList());
        if (!masterVolumes.isEmpty()) {
            return masterVolumes.get(0);
        } else if (!volumeDevices.isEmpty()) {
            return volumeDevices.get(0);
        } else if (!outputDevices.isEmpty()) {
            return outputDevices.get(0);
        }
        return null;
    }

}
