Tinkerforge Sensor Library
------------------
#### [BETA] 90% done - testing this on hackerSchool / kids sessions

This library simplifies Tinkerforge's sensor API.
It Removes the pain of sensor UIDs, sensor version, ports and provides a generic API for every sensor.

#### How it works
* TinkerForge original [Device](https://www.tinkerforge.com/en/doc/Software/Device_Identifier.html)
* wrapped by Generic [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/Sensor.java)
* handles input and sends [SensorEvent](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/SensorEvent.java)
* Auto closeable [SensorListener](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/SensorListener.java) with addable event consumer
* Returns a returns a [SensorList](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/SensorList.java)
* Supported devices [Bricks](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/main/java/berlin/yuna/tinkerforgesensor/model/driver/brick) and [Bricklets](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/main/java/berlin/yuna/tinkerforgesensor/model/driver/bricklet)
* Feel free for pull request as the wrapper classes are not so hard to implement, its just a mapping ;)

#### Examples
* Examples can be found here: (https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/src/test/java/berlin/yuna/tinkerforgesensor/example)
* Connecting with auto closeable [SensorListener](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/SensorListener.java)
```java
try (SensorListener sensorListener = new SensorListener("host", 4223, "optionalPassword")) {
    //Getting [SensorList]
    SensorList<Sensor> sensorList = sensorListener.sensorList;
    //Add listener on any sensor and event getting the [SensorEvent]s
    sensorListener.sensorEventConsumerList.add(ths::onSensorEvent);
}
```

* event example
```java
private void onSensorEvent(final Sensor currentSensor, final ValueType valueType) {
    Sensor display = sensorList.getSegmentDisplay);
    
    if (valueType.isMotionDetected() and value == 1L) {
        console("Motion detected");
        console("A lux value from any sensor: " + sensorList.value(LIGHT_LUX));
        console("Current sensor is new motion detection sensor? " + currentSensor.is(BrickletMotionDetectorV2.class));
        currentSensor.ledStatusOn();
        display.ledAdditionalOn();
        display.value("Off");
    
    } else if (valueType.isMotionDetected()) {
        console("No motion detected");
        currentSensor.ledStatusOff();
        display.ledAdditionalOff;
        display.value("On");
    }
}
```

#### TODO
- [ ] Thread does not shutdown and keeps program alive :(
- [ ] Make testable
- [ ] Spring integration

* Sensors
- [ ] Add more sensors
- [ ] Autodetect TinkerForge sensor list changes
- [ ] Get Additional Sensor information like "ChipTemperature"
- [ ] Simple Color class without AWT and with predefined colorValues

* Sensor Display 20x4
- [ ] Center String ${center}

* SensorListener/Registration
- [ ] Stop a loop/program/thread by name

* Connections
- [ ] Tinkerforge Auto-reconnect is not working properly - reconnected devices are getting timeouts