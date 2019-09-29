package berlin.yuna.tinkerforgesensor.model.sensor;

import berlin.yuna.tinkerforgesensor.logic.AudioControl;
import berlin.yuna.tinkerforgesensor.logic.HumanInput;
import berlin.yuna.tinkerforgesensor.model.AudioCmd;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.AudioDevice;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.tinkerforge.Device;
import com.tinkerforge.DummyDevice;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static berlin.yuna.tinkerforgesensor.model.AudioCmd.REPLAY;
import static berlin.yuna.tinkerforgesensor.model.AudioCmd.UNKNOWN;
import static java.util.Arrays.asList;

/**
 * <h3>{@link LocalControl}</h3><br>
 * <i>Displays a tiny human input board on activation</i><br>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#KEY_PRESSED}</li>
 * <li>{@link ValueType#KEY_CHAR}</li>
 * <li>{@link ValueType#CURSOR_PRESSED}</li>
 * <li>{@link ValueType#CURSOR_CLICK_COUNT}</li>
 * <li>{@link ValueType#CURSOR_MOVE_X}</li>
 * <li>{@link ValueType#CURSOR_MOVE_Y}</li>
 * </ul>
 *
 * <h3>Technical Info</h3>
 * <h6>Activation</h6>
 * <code>sensor.ledAdditional_setOn()</code>
 * <h6>Key code</h6>
 * <code>sensor.values().keyCode()</code>
 * <h6>Readable Key Character</h6>
 * <code>Char char = (char) stack.values().keyChar().intValue()</code>
 */
public class LocalControl extends Sensor<DummyDevice> {

    private static HumanInput humanInput;
    private final HashMap<Integer, AudioDevice> players = new HashMap<>();

    public LocalControl(final Device device, final String uid) throws NetworkConnectionException {
        super((DummyDevice) device, uid);
    }

    @Override
    protected Sensor<DummyDevice> initListener() {
        return this;
    }

    private AudioDevice getPlayer(final int playerId) {
        if (playerId == -1) {
            return new AudioControl().getDefaultDevice();
        } else if (players.size() - 1 >= playerId) {
            return players.get(playerId);
        }
        final AudioDevice player = new AudioDevice();
        players.put(playerId, player);
        return player;
    }

    @Override
    public Sensor<DummyDevice> send(final Object... valuesArray) {
        List<Object> values = asList(valuesArray);
        try {
            int playerId = 0;
            if (values.size() == 1 && values.get(0) instanceof Number) {
                getPlayer(playerId).setVolume(((Number) values.get(0)).intValue());
            } else if (values.size() > 1 && values.get(0) instanceof Number) {
                playerId = ((Number) values.get(0)).intValue();
                values = values.subList(1, values.size());
            }

            final AudioDevice player = getPlayer(playerId);
            AudioCmd audioCmd = (values.size() == 1 &&
                    values.get(0) instanceof URI
                    || values.get(0) instanceof URL
                    || values.get(0) instanceof File
                    || values.get(0) instanceof Path
                    || values.get(0) instanceof String) ? REPLAY : UNKNOWN;

            for (Object value : values) {
                if (value instanceof Number) {
                    new AudioControl().getDefaultDevice().setVolume(((Number) value).intValue());
                } else if (value instanceof Boolean) {
                    new AudioControl().getDefaultDevice().setMute((Boolean) value);
                } else if (value instanceof URL) {
                    setAudioFile(playerId, (URL) value);
                } else if (value instanceof URI) {
                    setAudioFile(playerId, ((URI) value).toURL());
                } else if (value instanceof String) {
                    final File file = new File((String) value);
                    if (file.exists()) {
                        setAudioFile(playerId, file.toURI().toURL());
                    }
                } else if (value instanceof File) {
                    final File file = (File) value;
                    if (file.exists()) {
                        setAudioFile(playerId, file.toURI().toURL());
                    }
                } else if (value instanceof Path) {
                    final Path path = (Path) value;
                    if (Files.exists(path)) {
                        setAudioFile(playerId, path.toUri().toURL());
                    }
                } else if (value instanceof AudioCmd) {
                    audioCmd = (AudioCmd) value;
                }
            }
            switch (audioCmd) {
                case PLAY:
                    player.play();
                    break;
                case PAUSE:
                    player.pause();
                    break;
                case REPLAY:
                    player.replay();
                    break;
                case STOP:
                    player.stop();
                    break;
                case MUTE:
                    new AudioControl().getDefaultDevice().setMute(true);
                    break;
                case UNMUTE:
                    new AudioControl().getDefaultDevice().setMute(false);
                    break;
            }
        } catch (MalformedURLException ignored) {
        }
        return this;
    }

    private void setAudioFile(final int playerId, final URL url) {
        getPlayer(playerId).setSource(url);
    }

    @Override
    public Sensor<DummyDevice> send(final Object value) {
        return send(new Object[]{value});
    }

    @Override
    public Sensor<DummyDevice> setLedStatus(final Integer value) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> ledAdditional(final Integer value) {
        if(humanInput == null){
            humanInput = new HumanInput();
            humanInput.sensorEventConsumerList.add(this::sendEventUnchecked);
        }
        return this;
    }

    @Override
    public Sensor<DummyDevice> refreshPeriod(final int milliseconds) {
        return this;
    }

    @Override
    public Sensor<DummyDevice> initLedConfig() {
        return this;
    }
}
