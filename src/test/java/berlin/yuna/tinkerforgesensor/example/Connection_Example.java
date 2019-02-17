package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.io.IOException;

class Connection_Example {

    static SensorListener connect() {
        try {
            return new SensorListener("localhost", 4223, true);
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }
    }
}
