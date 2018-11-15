package berlin.yuna.hackerschool;

import berlin.yuna.hackerschool.a_program.Example;
import berlin.yuna.hackerschool.a_program.HackerSchool;
import berlin.yuna.hackerschool.logic.SensorListener;
import berlin.yuna.hackerschool.util.TinkerforgeUtil;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.CryptoException;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.io.IOException;

import static berlin.yuna.hackerschool.util.TinkerforgeUtil.RefreshType.CUSTOM_PROCESS;
import static javax.management.timer.Timer.ONE_MINUTE;

public class Application extends TinkerforgeUtil {

    public static void main(String args[]) {
        new Application().run();
    }

    private void run() {
        console(readFile("start.txt"));
        try (SensorListener sensorListener = new SensorListener("localhost", 4223)) {
//        try (SensorListener sensorListener = new SensorListener("hackerschool", 4223, "7576Simba")) {
            HackerSchool hackerSchool = new HackerSchool();
            hackerSchool.sensorList = sensorListener.sensorList;
            sensorListener.sensorEventConsumerList.add(hackerSchool::onSensorEvent);
            hackerSchool.startup();
            loop(CUSTOM_PROCESS, run -> Example.refreshBrickPorts(sensorListener.sensorList, ONE_MINUTE));
            loop(60000, run -> console("[INFO] RunningPrograms [%s]", loopList.size()));
            console("Press key to exit");
            console(System.in.read());
        } catch (IOException | AlreadyConnectedException | NotConnectedException | NetworkException | TimeoutException | CryptoException e) {
            throw new RuntimeException(e);
        } finally {
            console(readFile("stop.txt"));
        }
    }
}

//getLux().first()
//getLux().last()
//getLux().average()
//getLux().get(0)
//getLux().get(Device)

//get(Lux).get(0)
//get(Lux).first()
//get(Lux).last()
//get(Lux).average()
//get(Lux).get(Device)
//get(Lux).get(Device.class).get(0)
//get(Lux).get(Device.class).first()
//get(Lux).get(Device.class).last()
//get(Lux).get(Device.class).average()

//setButton()
//setButton(int delayMs)