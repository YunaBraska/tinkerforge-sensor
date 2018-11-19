package berlin.yuna.tinkerforgesensor;

import berlin.yuna.hackerschool.HackerSchool;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

import java.io.IOException;

public class Application extends TinkerForgeUtil {

    public static void main(String args[]) {
        new Application().run();
    }

    private void run() {
        console(readFile("start.txt"));
        try (SensorListener sensorListener = new SensorListener("localhost", 4223, true)) {
//        try (SensorListener sensorListener = new SensorListener("localhost", 4223)) {
//        try (SensorListener sensorListener = new SensorListener("hackerschool", 4223, "7576Simba")) {
            HackerSchool hackerSchool = new HackerSchool();
            hackerSchool.sensorList = sensorListener.sensorList;
            sensorListener.sensorEventConsumerList.add(hackerSchool::onSensorEvent);
            hackerSchool.startup();
            console("Press key to exit");
            console(System.in.read());
        } catch (IOException | NetworkConnectionException e) {
            throw new RuntimeException(e);
        } finally {
            console(readFile("stop.txt"));
        }
    }
}