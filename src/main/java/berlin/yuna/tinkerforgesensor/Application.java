package berlin.yuna.tinkerforgesensor;

import berlin.yuna.hackerschool.HackerSchool;
import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.IPConnectionBase;

import java.io.IOException;
import java.io.InputStream;

public class Application extends TinkerForgeUtil {

    public static void main(String[] args) {
        new Application().run();
    }

    private void run() {
        HackerSchool hackerSchool = new HackerSchool();
        try (SensorListener sensorListener = new SensorListener("localhost", 4223, true)) {
//        try (SensorListener sensorListener = new SensorListener("hackerschool", 4223, "7576Simba", true)) {
            hackerSchool.sensorList = sensorListener.sensorList;
            sensorListener.sensorEventConsumerList.add(hackerSchool::onSensorEvent);
            hackerSchool.startup();
            console("Press key to exit");
            console(System.in.read());
        } catch (IOException | NetworkConnectionException e) {
            throw new RuntimeException(e);
        } finally {
            hackerSchool.shutdown();
        }
    }


    static class TestClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name.equals("test.Test1")) {
                try {
                    InputStream is = IPConnectionBase.class.getClassLoader().getResourceAsStream("test/Test1.class");
                    byte[] buf = new byte[10000];
                    int len = is.read(buf);
                    return defineClass(name, buf, 0, len);
                } catch (IOException e) {
                    throw new ClassNotFoundException("", e);
                }
            }
            return getParent().loadClass(name);
        }
    }
}