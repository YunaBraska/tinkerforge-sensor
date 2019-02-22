package berlin.yuna.tinkerforgesensor.example;

import berlin.yuna.tinkerforgesensor.logic.SensorListener;
import berlin.yuna.tinkerforgesensor.model.Color;
import berlin.yuna.tinkerforgesensor.model.SensorList;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import berlin.yuna.tinkerforgesensor.util.TinkerForgeUtil;

public class Hackerschool_Example {

    private static SensorList<Sensor> sensorList;

    public static void main(String[] args) {
        SensorListener sensorListener = ConnectionAndPrintValues_Example.connect();
        sensorList = sensorListener.sensorList;
        sensorListener.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //TODO: sensor.type.is
    //TODO: value.type.is
    //TODO: sensorList.searchValue.is
    //TODO: sensorList.searchType.is

    static int counter = 0;
    Sensor meinSensor = sensorList.getButtonRGB();
    Color meineFarbe = new Color(125, 125, 25);
    static int nummer = 1;

    static int blink = 0;

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        if (sensorList.getButtonRGB().isPresent()) {
            counter++;
//            sensorList.getButtonRGB().value(new Color(0, counter, 0));

            if (TinkerForgeUtil.timePassed(1000)) {
                if (blink == 0) {
                    sensorList.getButtonRGB().value(Color.GREEN);
                    blink = 1;
                } else {
                    sensorList.getButtonRGB().value(Color.BLACK);
                    blink = 0;
                }
            }
        }

//        if (timePassed(1000)) {
//            sensorList.getDisplaySegment().value(counter);
//        }
//
//        if (type.isMotionDetected()) {
//            if (value == nummer) {
//                sensorList.getSpeaker().value(200);
//            }
//        }

//        sensorList.getButtonRGB().value(new Color(125, 125, 25));
//        sensorList.getSpeaker().value("F1000");
//        sensorList.getIO16().value(1);
//        sensorList.getDisplaySegment().value(7);

    }


}
