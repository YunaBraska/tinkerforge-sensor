package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.Device;

import static berlin.yuna.tinkerforgesensor.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.LIGHT_LUX;
import static berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil.RefreshType.EACH_SECOND;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();

    public void startup() {
//        loop("animateLeds",run -> Example.animateStatusLEDs(sensorList));
//        loop("TimeoutMessage", run -> Example.displayTimeoutMessage(sensorList, 2500));
//        loop("moveMessage", run -> Example.displayMoveMessage(sensorList, 500));
//        loop("alphabet", run -> Example.displayAlphabet(sensorList, 256));
        loop("weather", EACH_SECOND, run -> Example.displayWeather(sensorList));
        sleep(5000);
    }

    public void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType) {
        //DEFINE VARIABLES CODING HERE
        Sensor lightSensor = sensorList.first(BrickletAmbientLightV2.class);
        Long lightValue_1 = sensor.value(LIGHT_LUX);
        Long lightValue_2 = sensorList.value(LIGHT_LUX);

        //START CODING HERE

        if (valueType.is(BUTTON_PRESSED)) {
            console(value);
        }

    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        if (sensorEvent.valueType.parent == DEVICE_STATUS) {
            console("[%10s] [%25s] [%s]", sensorEvent.value, sensorEvent.valueType, sensorEvent.sensor.name);
        }
//        console("[%10s] [%25s] [%s]", sensorEvent.value, sensorEvent.valueType, sensorEvent.sensor.name);
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}