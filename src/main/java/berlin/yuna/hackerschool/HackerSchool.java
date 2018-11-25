package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.model.Sensor;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;
import com.tinkerforge.BrickletLCD20x4;

import static berlin.yuna.hackerschool.Example.printAllValues;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DISTANCE_IR;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.TEMPERATURE;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();

    public void startup() {
        //CODE HERE
        console(readFile("start.txt"));
        loop("printValues", run -> printAllValues(sensorList));

        loop("testProgram", run -> {
            Sensor display = sensorList.first(BrickletLCD20x4.class);
            Long distance = sensorList.value(DISTANCE_IR);
            Double temperature = sensorList.valueDecimal(TEMPERATURE);


            display.ledAdditionalOn();
            display.value("${0}Ho ho ho ${space}");
            display.value("${1}Mary Christmas ${space}");
            display.value("${2}Temperature ${space}" + roundUp(temperature / 100));


//            String text = format("Ho ho ho\n");
//            text += format("Mary Christmas\n");
//            text += format("Temperature ${space} %s %%\n", roundUp((double) temperature / 100.0, 2));
//            text += format("Humidity${space}${space}${space} N/A %%\n");
//            text += format("AirPress${space} N/A mb\n");
//            text += format("Temperature${space} N/A °C\n");
//            display.value(text);


        });
    }

    private void onSensorEvent(final Sensor sensor, final long value, final ValueType valueType) {
        //CODE HERE
    }

    public void shutdown() {
        console(readFile("stop.txt"));
    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}