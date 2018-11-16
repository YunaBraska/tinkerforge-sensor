TinkerForge Sensor Library
------------------
#### [BETA] testing this on hackerSchool / kids sessions

TinkerForge provides awesome Hardware and APIs. You don't have to care about Hardware and can just start programming.
This Library is simplifying the API usage in a pure Java 8 way without any Frameworks.
Removes pain of the Sensor UID, how to speak to the sensor and what values can i get from it etc. so that the focus is more on the logic

#### How it works
* Connecting is done simply with the auto closeable [SensorListener](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/SensorListener.java) which returns a [SensorList](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/SensorList.java) (Generic [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/Sensor.java))
* The Generic [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/Sensor.java)s (containing TinkerForge original [Device](com.tinkerforge.Device)) are extending [SensorRegistration](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/SensorRegistration.java) which contains the generic mapped actions/driver and gets automatically callbacks as [SensorEvent](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/SensorEvent.java)s
* The [SensorEvent](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/SensorEvent.java) contains the source [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/Sensor.java), Tinkerforge [DeviceClassType](https://www.tinkerforge.com/de/doc/Software/Device_Identifier.html), [Value](https://docs.oracle.com/javase/7/docs/api/java/lang/Long.html) (long), and [ValueType](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java)
* Supported devices [Bricks](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/main/java/berlin/yuna/tinkerforgesensor/model/driver/brick) and [Bricklets](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/main/java/berlin/yuna/tinkerforgesensor/model/driver/bricklet) - feel free for pull request as the driver classes are not so hard to implement, its just a mapping ;)
```java
    device.addStackCurrentListener(value -> registration.sendEvent(consumerList, CURRENT, sensor, (long) value));
```

#### Examples
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
    Sensor display = sensorList.first(BrickletSegmentDisplay4x7.class);
    
    if (valueType.is(MOTION_DETECTED_ON)) {
    
        console("Motion detected");
        console("A lux value from any sensor: " + sensorList.value(LIGHT_LUX));
        console("Current sensor is new motion detection sensor? " + currentSensor.is(BrickletMotionDetectorV2.class));
        currentSensor.ledStatusOn();
        display.ledBrightness(7);
        display.value("M=Off");
    
    } else if (valueType.is(MOTION_DETECTED_OFF)) {
    
        console("No motion detected");
        currentSensor.ledStatusOff();
        display.ledBrightness(0);
        display.value("M=On");
    }
}
```

* simple loop (thread) with auto start for subprogram. (The loop returns the original thread)
```java
//With enum
loop(EACH_SECOND, run -> myFunction());
//With custom time ms
loop(1000, run -> myFunction());
//With default time 1000 ms
loop(run -> myFunction());
```

#### Features done
* SensorListener/Registration
- [X] Find all sensors from connection
- [X] Connect/Disconnect event
- [X] AutoCloseable
- [X] Register and map sensor actions
- [X] Provides sendEvent api and provides listener api
- [X] Simple thread-loop generation called [loops] for fast coding with subprograms

* Sensors are generalized
- [X] Sensors are sending generic events
- [X] compare sensors 
- [X] provide a [sensor-list] 
- [X] sensors have parent 
- [X] generic listener function for sensors 
- [X] sensors have port number for [sensor-list] (e.g. night rider effect on status LEDs) 
- [X] get first, second, third, ... sensor with [sensor-type] ordered by [sensor-port] 
- [X] dummy sensor implementation as placeholder while sensor is connecting, offline or for testing 
- [X] isPresent() to check if its dummy sensor or real sensor 
- [X] is(sensorType) to check the sensor equals specific sensor type
- [X] set brightness
- [X] set status led
- [X] set additional led
- [X] set value [Object] - sensor will know how to handle input

* Values are generalized
- [X] compare [valueTypes] 
- [X] provide a [value-list] (rollingList) for each [sensor] separated by [value-type]
- [X] get first, second, third,... value with [value-type] ordered by [sensor-port] 

#### TODO
- [ ] Examples in test resources
- [ ] Spring integration

* Sensors
- [ ] Add more sensors
- [ ] Autodetect TinkerForge sensor list changes
- [ ] Get Additional Sensor information like "ChipTemperature"
- [ ] Simple Color class without AWT and with predefined colorValues

* Sensor Display 20x4
- [ ] Parse line setter ${1}, ${2}, ${3},...
- [ ] Center String ${center}
- [ ] dynamic space ${space}
* Sensor Display Segment
- [ ] Fix character 'P' and 'M'
- [ ] Parse brightness ${1}, ${2}, ${3},...
- [ ] Parse brightness ${1}, ${2}, ${3},...

* SensorListener/Registration
- [ ] Stop a loop/program/thread by name
- [ ] Better sensor sensibly configuration - sensors are floating currently the event stream (could overload connection when reacting on each micro change)

* Connections
- [ ] WLAN Enumeration
- [ ] Auto-reconnect
- [ ] Connect to multiple hosts
- [ ] Don't disrupt when others are connecting