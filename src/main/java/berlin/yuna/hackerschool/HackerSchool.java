package berlin.yuna.hackerschool;

import berlin.yuna.tinkerforgesensor.generator.SensorRegistry;
import berlin.yuna.tinkerforgesensor.model.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

import java.util.concurrent.ConcurrentHashMap;

import static berlin.yuna.hackerschool.Example.printAllValues;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ALL;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.DEVICE_STATUS;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.ROTARY;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_INTENSITY;
import static java.lang.String.format;

public class HackerSchool extends TinkerForgeUtil {

    public SensorList<Sensor> sensorList = new SensorList<>();


    private int port = 0;

    public void startup() {
        //CODE HERE
        console(readFile("start.txt"));
        console("Supported Sensors [%s]", SensorRegistry.getDeviceAvailableDevices().size());
        loop("printValues", run -> printAllValues(sensorList));
//        loop("soundIntensityLED", 90, run -> {
//            Sensor io16 = sensorList.getIO16().value(false);
//            port = port == 16 ? 0 : ++port;
//            io16.value(port);
//        });


//        loop("ledEffect", run -> Example.animateStatusLEDs(sensorList));


//        routines.add("Name", run -> {
//
//            if (sensorList.value(TEMPERATURE) > 20) {
//
//            }
//
//        });
//
//        routines.start("name");

//        loop("Button_0", run -> {
//            Sensor display = sensorList.first(BrickletLCD20x4.class);
//            Double temperature = sensorList.valueDecimal(TEMPERATURE);
//
//            display.ledAdditionalOn();
//            display.value("${0}ho ho Mary Christmas");
//            display.value("${1}light ;" + sensorList.value(LIGHT_LUX));
//            display.value("${2}Temperature" + roundUp(temperature / 100) + " °C");
//            display.value("${3}                     ");
//
//
//        });
    }


    private void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        //CODE HERE
        if (type.containsDeviceStatus()) {
            System.out.println(format("Sensor [%s] type [%s] value [%s]", sensor.name, type, value));
        }

        if (type.isRotary()) {
            sensorList.getDisplaySegment().value(value);
            sensorList.getSpeaker().value(value);


            sensorList.getLightAmbient().value("asdasds");
        }


        if (type.containsAll()) {
            if (hasWaited( 16)) {
                sensorList.getIO16().ledAdditionalOn();
            } else {
                sensorList.getIO16().ledAdditionalOff();
            }
        }



        if (type.is(SOUND_INTENSITY)) {
//            sensorList.getDisplaySegment().value(value.toString());
            sensorList.getDisplaySegment().value((value / 10) + "db");
        }

//        Sensor display = sensorList.first(BrickletLCD20x4.class);
//
//        display.ledAdditionalOn();
//        if (valueType.is(BUTTON_PRESSED)) {
//            if (value == 0) {
//                display.value("${clear}");
//                display.value((sensorList.valueDecimal(LIGHT_LUX) / 100) + " LX");
//
//            } else if (value == 1) {
//                display.value("${clear}");
//
//                // display.value("Button 2");
//                display.value((sensorList.valueDecimal(TEMPERATURE) / 100) - 5 + " °C");
//            } else if (value == 2) {
//                display.value("${clear}");
//                display.value(dateTime());
//            }
//
//        }
//
//        if (sensorList.valueDecimal(LIGHT_LUX) > 30000) {
//            display.value("${clear}");
//            display.value("This is the end of our Project we hope you enjoyd it");
//        }

//        else if (valueType.is(BUTTON_RELEASED)) {
//            display.value("Where are you? ${space}");
//        } display.value(25.11 + "${space}");
//        display.value("laba diena su vistiena ir su kalakutiena");

    }

    public void shutdown() {
        console(readFile("stop.txt"));
    }

    public void onSensorEvent(final SensorEvent sensorEvent) {
        onSensorEvent(sensorEvent.sensor, sensorEvent.value, sensorEvent.valueType);
    }
}