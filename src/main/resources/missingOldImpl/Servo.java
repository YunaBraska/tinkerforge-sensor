package berlin.yuna.tinkerforgesensor.model.missing;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.ValueType;
import com.tinkerforge.BrickServo;
import com.tinkerforge.Device;
import com.tinkerforge.TinkerforgeException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_NONE;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_STATUS_OFF;
import static berlin.yuna.tinkerforgesensor.model.LedStatusType.LED_STATUS_ON;
import static berlin.yuna.tinkerforgesensor.model.ValueType.DEVICE_TIMEOUT;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MOTOR_POSITION;
import static berlin.yuna.tinkerforgesensor.model.ValueType.MOTOR_VELOCITY;
import static berlin.yuna.tinkerforgesensor.model.ValueType.UNDER_VOLTAGE;
import static berlin.yuna.tinkerforgesensor.model.ValueType.VOLTAGE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * <h3>{@link Servo}</h3>
 * <i>Drives up to 7 RC Servos with up to 3A</i>
 *
 * <h3>Values</h3>
 * <ul>
 * <li>{@link ValueType#MOTOR_POSITION} reached position</li>
 * <li>{@link ValueType#MOTOR_VELOCITY} reached velocity</li>
 * <li>{@link ValueType#UNDER_VOLTAGE} [x = mV] voltage too low</li>
 * <li>{@link ValueType#VOLTAGE} [x = mV] output voltage change</li>
 * </ul>
 * <h3>Technical Info</h3>
 * <ul>
 * <li><a href="href="https://www.tinkerforge.com/en/doc/Hardware/Bricks/Servo_Brick.html#servo-brick">Official documentation</a></li>
 * </ul>
 * <h6>Set servo voltage 5V</h6>
 * <code>sensor.send(-99, 5000);</code>
 * <h6>Set servo voltage 7V, minimal voltage 6V</h6>
 * <code>sensor.send(-99, 7000, 6000);</code>
 * <h6>Turn on servo 3</h6>
 * <code>sensor.send(2, true);</code>
 * <h6>Turn on servo 4 and invert position</h6>
 * <code>sensor.send(3, true, true);</code>
 * <h6>Turn on all Servos</h6>
 * <code>sensor.send(-1, true);</code>
 * <h6>Set servo 2 to position/degree 3000 (min -9000 max 9000)</h6>
 * <code>sensor.send(1, 3000);</code>
 * <h6>Set servo 3 to position 4000, velocity 10000</h6>
 * <code>sensor.send(2, 3000, 10000);</code>
 * <h6>Set servo 4 to position 5000, velocity 20000, acceleration 10000</h6>
 * <code>sensor.send(3, 5000, 20000, 10000);</code>
 * <h6>Set servo 5 to position 5000, velocity 20000, acceleration 10000. period 19500</h6>
 * <code>sensor.send(4, 5000, 20000, 10000, 19500);</code>
 */
public class Servo extends Sensor<BrickServo> {


    private final CopyOnWriteArrayList<ServoConfig> servoList = new CopyOnWriteArrayList<>(asList(new ServoConfig(0), new ServoConfig(1), new ServoConfig(2), new ServoConfig(3), new ServoConfig(4), new ServoConfig(5), new ServoConfig(6)));

    static class ServoConfig {
        private final int port;
        private boolean enabled = false;
        private boolean invert = false;
        private int position = 0;
        private int velocity = 60000;
        private int acceleration = 60000;
        private int period = 19500;

        public ServoConfig(final int port) {
            this.port = port;
        }

        public void set(final ServoConfig servoConfig) {
            enabled = servoConfig.enabled;
            invert = servoConfig.invert;
            position = servoConfig.position;
            velocity = servoConfig.velocity;
            acceleration = servoConfig.acceleration;
            period = servoConfig.period;
        }

        public ServoConfig copy() {
            final ServoConfig servoConfig = new ServoConfig(port);
            servoConfig.enabled = enabled;
            servoConfig.invert = invert;
            servoConfig.position = position;
            servoConfig.velocity = velocity;
            servoConfig.acceleration = acceleration;
            servoConfig.period = period;
            return servoConfig;
        }
    }

    public Servo(final Device device, final String uid) throws NetworkConnectionException {
        super((BrickServo) device, uid);
    }

    @Override
    protected Sensor<BrickServo> initListener() {
        //TODO: add setters and getters
        //TODO: send more than one value e.g. Buttons, 1,2,3 Servo 1,2,3
        device.addPositionReachedListener((servoNum, position) -> sendEvent(MOTOR_POSITION, (long) position));
        device.addVelocityReachedListener((servoNum, velocity) -> sendEvent(MOTOR_VELOCITY, (long) velocity));
        device.addUnderVoltageListener(underVoltage -> sendEvent(UNDER_VOLTAGE, (long) underVoltage));
        setMaxVoltage(5000);
        setMinVoltage(5000);
        refreshPeriod(10);
        return this;
    }

    //Fixme: -99 should be boolean - unique combination of (bool, int) and (bool, int, int)
    @Override
    public Sensor<BrickServo> send(final Object... values) {
        if (values.length > 0 && values[0] instanceof Number && ((Number) values[0]).intValue() == -99) {
            if (values.length > 1 && values[1] instanceof Number) {
                setMaxVoltage(((Number) values[1]).intValue());
            }
            if (values.length > 2 && values[2] instanceof Number) {
                setMinVoltage(((Number) values[2]).intValue());
            }
        } else if (values.length > 0 && values[0] instanceof Number) {
            final int port = ((Number) values[0]).intValue();
            final List<ServoConfig> servos = (port == -1 ? servoList.stream().map(ServoConfig::copy).collect(Collectors.toList()) : singletonList(servoList.get(port).copy()));

            if (values.length > 1 && values[1] instanceof Boolean) {
                servos.forEach(s -> s.enabled = (boolean) values[1]);
                if (values.length > 2 && values[2] instanceof Boolean) {
                    servos.forEach(s -> s.invert = (boolean) values[2]);
                }
            } else if (values.length > 1 && values[1] instanceof Number) {
                servos.forEach(s -> s.position = ((Number) values[1]).intValue());

                if (values.length > 2 && values[2] instanceof Number) {
                    servos.forEach(s -> s.velocity = ((Number) values[2]).intValue());
                }

                if (values.length > 3 && values[3] instanceof Number) {
                    servos.forEach(s -> s.acceleration = ((Number) values[3]).intValue());
                }

                if (values.length > 4 && values[4] instanceof Number) {
                    servos.forEach(s -> s.period = ((Number) values[4]).intValue());
                }
            }
            applyServoChanges(servos);
        }
        return this;
    }

    @Override
    public Sensor<BrickServo> send(final Object value) {
        return send(new Object[]{value});
    }

    @Override
    public Sensor<BrickServo> setLedStatus(final Integer value) {
        if (ledStatus.bit == value) return this;
        try {
            if (value == LED_STATUS_ON.bit) {
                ledStatus = LED_STATUS_ON;
                device.enableStatusLED();
            } else if (value == LED_STATUS_OFF.bit) {
                ledStatus = LED_STATUS_OFF;
                device.disableStatusLED();
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickServo> ledAdditional(final Integer value) {
        return this;
    }

    @Override
    public Sensor<BrickServo> initLedConfig() {
        try {
            ledStatus = device.isStatusLEDEnabled() ? LED_STATUS_ON : LED_OFF;
            ledAdditional = LED_NONE;
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
        return this;
    }

    @Override
    public Sensor<BrickServo> refreshPeriod(final int milliseconds) {
        return this;
    }

    private void applyServoChanges(final List<ServoConfig> servos) {
        try {
            for (ServoConfig servoConfig : servos) {
                final ServoConfig servoConfigOrg = servoList.get(servoConfig.port);
                if (servoConfigOrg.enabled != servoConfig.enabled) {
                    enable(servoConfig.port, servoConfig.enabled);
                }

                if (servoConfigOrg.position != servoConfig.position || servoConfigOrg.invert != servoConfig.invert) {
                    device.setPosition((short) servoConfig.port, (short) (servoConfig.invert ? servoConfig.position * -1 : servoConfig.position));
                }
                if (servoConfigOrg.velocity != servoConfig.velocity) {
                    device.setVelocity((short) servoConfig.port, servoConfig.velocity);
                }
                if (servoConfigOrg.acceleration != servoConfig.acceleration) {
                    device.setAcceleration((short) servoConfig.port, servoConfig.acceleration);
                }
                if (servoConfigOrg.period != servoConfig.period) {
                    device.setPeriod((short) servoConfig.port, servoConfig.period);
                }
                servoConfigOrg.set(servoConfig);
            }
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void enable(final int port, final boolean enable) throws TinkerforgeException {
        if (enable) {
            device.enable((short) port);
        } else {
            device.disable((short) port);
        }
    }

    private void setMinVoltage(final int minVoltage) {
        try {
            device.setMinimumVoltage(minVoltage);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }

    private void setMaxVoltage(final int maxVoltage) {
        try {
            device.setOutputVoltage(maxVoltage);
            sendEvent(VOLTAGE, (long) maxVoltage);
        } catch (TinkerforgeException ignored) {
            sendEvent(DEVICE_TIMEOUT, 404L);
        }
    }
}
