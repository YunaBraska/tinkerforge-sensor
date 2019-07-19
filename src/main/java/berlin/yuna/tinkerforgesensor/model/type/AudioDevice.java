package berlin.yuna.tinkerforgesensor.model.type;

import berlin.yuna.tinkerforgesensor.logic.AudioControl;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AudioDevice {
    public final Mixer mixer;
    public final Line line;
    public final Line.Info lineInfo;
    public final Mixer.Info mixerInfo;
    public final boolean output;
    public final boolean playback;
    public long microsecondPosition;
    public URL source;

    public AudioDevice() {
        try {
            final AudioDevice audioDevice = createAudioDevice();
            this.mixer = audioDevice.mixer;
            this.line = audioDevice.line;
            this.lineInfo = audioDevice.lineInfo;
            this.mixerInfo = audioDevice.mixerInfo;
            this.output = audioDevice.output;
            this.playback = audioDevice.playback;
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public AudioDevice(final Line line, final Mixer mixer, final boolean output) {
        this(mixer, line, line.getLineInfo(), mixer.getMixerInfo(), output, line instanceof Clip);
    }

    public AudioDevice(final Mixer mixer, final Line line, final Line.Info lineInfo, final Mixer.Info mixerInfo, final boolean output, final boolean playback) {
        this.mixer = mixer;
        this.line = line;
        this.lineInfo = lineInfo;
        this.mixerInfo = mixerInfo;
        this.output = output;
        this.playback = playback;
    }

    public int getVolume() {
        final AtomicInteger volume = new AtomicInteger(-1);
        openLine(open -> {
            final FloatControl volCtrl = (FloatControl) findControl(FloatControl.Type.VOLUME);
            if (volCtrl != null) {
                volume.set((int) Math.round(volCtrl.getValue() * 100.0));
            }
        });
        return volume.get();
    }

    public void setVolume(final int volume) {
        openLine(open -> {
            final FloatControl volCtrl = (FloatControl) findControl(FloatControl.Type.VOLUME);
            if (volCtrl != null) {
                volCtrl.setValue(volume / 100.0f);
            }
        });
    }

    public int getBalance() {
        final AtomicInteger volume = new AtomicInteger(-1);
        openLine(open -> {
            final FloatControl volCtrl = (FloatControl) findControl(FloatControl.Type.BALANCE);
            if (volCtrl != null) {
                volume.set((int) Math.round(volCtrl.getValue() * 100.0));
            }
        });
        return volume.get();
    }

    public void setBalance(final int volume) {
        openLine(open -> {
            final FloatControl volCtrl = (FloatControl) findControl(FloatControl.Type.BALANCE);
            if (volCtrl != null) {
                volCtrl.setValue(volume / 100.0f);
            }
        });
    }

    public boolean getMute() {
        final AtomicBoolean mute = new AtomicBoolean(false);
        openLine(open -> {
            final BooleanControl muteCtrl = (BooleanControl) findControl(BooleanControl.Type.MUTE);
            mute.set(muteCtrl != null && muteCtrl.getValue());
        });
        return mute.get();
    }

    public void setMute(final boolean mute) {
        openLine(open -> {
            if(open) {
                final BooleanControl muteCtrl = (BooleanControl) findControl(BooleanControl.Type.MUTE);
                if (muteCtrl != null) {
                    muteCtrl.setValue(mute);
                }
            }
        });
    }

    public void setSource(final URL url) {
        source = url;
        microsecondPosition = 0;
    }

    public void play() {
        if (playback) {
            openClip(clip -> {
                clip.setFramePosition(0);
                clip.setMicrosecondPosition(microsecondPosition);
                clip.start();
            });
        }
    }

    public void pause() {
        if (playback) {
            openLine(open -> {
                final Clip clip = (Clip) line;
                microsecondPosition = clip.getMicrosecondPosition();
                clip.stop();
            });
        }
    }

    public void replay() {
        if (playback) {
            microsecondPosition = 0;
//                clip.setFramePosition(0);  // Must always rewind!
            play();
        }
    }

    public void stop() {
        if (playback) {
            openLine(open -> {
                final Clip clip = (Clip) line;
                clip.stop();
                microsecondPosition = 0;
            });
        }
    }

    public boolean isRunning() {
        final AtomicBoolean isRunning = new AtomicBoolean(false);
        if (playback) {
            openLine(open -> {
                final Clip clip = (Clip) line;
                isRunning.set(clip.isRunning());
            });
        }
        return isRunning.get();
    }

    private void openClip(final Consumer<Clip> consumer) {
        final Clip clip = (Clip) this.line;
        try {
            final boolean opened = clip.isOpen();
            if (!opened) {
                clip.open(AudioSystem.getAudioInputStream(source));
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ignored) {
        }
        consumer.accept(clip);
    }

    private void openLine(final Consumer<Boolean> consumer) {
        try {
            final boolean opened = line.isOpen() || line instanceof Clip;
            if (!opened) {
                line.open();
            }
        } catch (LineUnavailableException ignored) {
        }
        consumer.accept(line.isOpen());
    }

    private Control findControl(final Control.Type type) {
        if(line.isOpen()) {
            return findControl(type, line.getControls());
        }
        return null;
    }

    private Control findControl(final Control.Type type, final Control... controls) {
        if (controls == null || controls.length == 0) return null;
        for (Control control : controls) {
            if (control.getType().equals(type)) return control;
            if (control instanceof CompoundControl) {
                final CompoundControl compoundControl = (CompoundControl) control;
                final Control member = findControl(type, compoundControl.getMemberControls());
                if (member != null) return member;
            }
        }
        return null;
    }

    private static AudioDevice createAudioDevice() throws LineUnavailableException {
        final AudioDevice device = new AudioControl().getDefaultDevice();
        final Mixer mixer = device != null ? device.mixer : AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
        return new AudioDevice(AudioSystem.getClip(), mixer, false);
    }

    @Override
    public String toString() {
        return "AudioDevice{" +
                "lineInfo=" + (lineInfo != null ? lineInfo.toString() : mixer.getMixerInfo()) +
                '}';
    }
}
