package berlin.yuna.hackerschool.a_program;

import berlin.yuna.hackerschool.model.Sensor;
import berlin.yuna.hackerschool.model.SensorEvent;
import berlin.yuna.hackerschool.model.SensorList;
import berlin.yuna.hackerschool.model.type.ValueType;
import berlin.yuna.hackerschool.util.TinkerForgeUtil;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.Device;

import static berlin.yuna.hackerschool.model.type.ValueType.BUTTON_PRESSED;
import static berlin.yuna.hackerschool.model.type.ValueType.LIGHT_LUX;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();

    public void startup() {
//        loop(run -> Example.animateStatusLEDs(sensorList));
//        loop(run -> Example.displayTimeoutMessage(sensorList, 2500));
//        loop(EACH_SECOND, run -> Example.displayWeather(sensorList));
//        loop(run -> Example.displayMoveMessage(sensorList, 500));
//        loop(run -> Example.displayAlphabet(sensorList, 256));
    }

    public void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType, final Class<? extends Device> sensorType) {
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
//        console("[%10s] [%25s] [%s]", sensorEvent.value, sensorEvent.sensorValueType, sensorEvent.sensor);
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.sensorValueType, sensorEvent.sensorType);
    }
}