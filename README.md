# tinkerforge-sensor
*This library simplifies Tinkerforge's sensor API.*
*It Removes the pain of sensor UIDs, sensor versions, ports and provides a generic API for every sensor.*

[![Build][build_shield]][build_link]
[![Maintainable][maintainable_shield]][maintainable_link]
[![Coverage][coverage_shield]][coverage_link]
[![Issues][issues_shield]][issues_link]
[![Commit][commit_shield]][commit_link]
[![Dependencies][dependency_shield]][dependency_link]
[![License][license_shield]][license_link]
[![Central][central_shield]][central_link]
[![Tag][tag_shield]][tag_link]
[![Javadoc][javadoc_shield]][javadoc_link]
[![Size][size_shield]][size_shield]
![Label][label_shield]

[build_shield]: https://github.com/YunaBraska/tinkerforge-sensor/workflows/JAVA_CI/badge.svg
[build_link]: https://github.com/YunaBraska/tinkerforge-sensor/actions?query=workflow%3AJAVA_CI
[maintainable_shield]: https://img.shields.io/codeclimate/maintainability/YunaBraska/tinkerforge-sensor?style=flat-square
[maintainable_link]: https://codeclimate.com/github/YunaBraska/tinkerforge-sensor/maintainability
[coverage_shield]: https://img.shields.io/codeclimate/coverage/YunaBraska/tinkerforge-sensor?style=flat-square
[coverage_link]: https://codeclimate.com/github/YunaBraska/tinkerforge-sensor/test_coverage
[issues_shield]: https://img.shields.io/github/issues/YunaBraska/tinkerforge-sensor?style=flat-square
[issues_link]: https://github.com/YunaBraska/tinkerforge-sensor/commits/master
[commit_shield]: https://img.shields.io/github/last-commit/YunaBraska/tinkerforge-sensor?style=flat-square
[commit_link]: https://github.com/YunaBraska/tinkerforge-sensor/issues
[license_shield]: https://img.shields.io/github/license/YunaBraska/tinkerforge-sensor?style=flat-square
[license_link]: https://github.com/YunaBraska/tinkerforge-sensor/blob/master/LICENSE
[dependency_shield]: https://img.shields.io/librariesio/github/YunaBraska/tinkerforge-sensor?style=flat-square
[dependency_link]: https://libraries.io/github/YunaBraska/tinkerforge-sensor
[central_shield]: https://img.shields.io/maven-central/v/berlin.yuna/tinkerforge-sensor?style=flat-square
[central_link]:https://search.maven.org/artifact/berlin.yuna/tinkerforge-sensor
[tag_shield]: https://img.shields.io/github/v/tag/YunaBraska/tinkerforge-sensor?style=flat-square
[tag_link]: https://github.com/YunaBraska/tinkerforge-sensor/releases
[javadoc_shield]: https://javadoc.io/badge2/berlin.yuna/tinkerforge-sensor/javadoc.svg?style=flat-square
[javadoc_link]: https://javadoc.io/doc/berlin.yuna/tinkerforge-sensor
[size_shield]: https://img.shields.io/github/repo-size/YunaBraska/tinkerforge-sensor?style=flat-square
[label_shield]: https://img.shields.io/badge/Yuna-QueenInside-blueviolet?style=flat-square
[gitter_shield]: https://img.shields.io/gitter/room/YunaBraska/nats-streaming-server-embedded?style=flat-square
[gitter_link]: https://gitter.im/nats-streaming-server-embedded/Lobby

#### Sensor documentation
* A list of all sensors behaviors and more can be found in the [ReadmeDoc](readmeDoc/README.md)
* A list of all supported [Bricks and Bricklets](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor)

#### How it works
* Connect to a [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java) object
* Generic [Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java) objects will be initialized as child's of the stack *(ordered by port)*
* [Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java) are wrapping the TinkerForge original [Devices](https://www.tinkerforge.com/en/doc/Software/Device_Identifier.html) *(can be also used natively)*
* [Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java) send handles input and sends [SensorEvent](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/SensorEvent.java) to the [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)
* [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java) can have consumer to consume the [SensorEvents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/SensorEvent.java)
* [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java) will send [SensorEvents](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/SensorEvent.java) to consumer
* *Feel free for pull request as the wrapper classes are not so hard to implement, its just a mapping ;)*

#### Examples
* Examples can be found here: [Hackerschool Examples](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/example)
* Example getting display from [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java).[Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Sensors.java) and send text
```java
final Sensor display = stack.sensors().displaySegment();
display.send("GIRL");
display.sendLimit(2, "YOU"); //Sends only two messages in a second (useful for loops)
```

* Example getting multiple buttonRGB from [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java).[Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Sensors.java) <- (The order comes from the connected port in the stack - higher port/stack = higher orderNumber)
```java
final Sensor button_01 = stack.sensors().buttonRGB(0);
final Sensor button_02 = stack.sensors().buttonRGB(1);
final Sensor button_03 = stack.sensors().buttonRGB(2);
```

* [Compare](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Compare.java) a [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java)
```java
final Sensor display = stack.sensors().displaySegment();
display.compare().is(display); //returns true - UID is the same
display.compare().is(DisplaySegment.class); //returns true - ClassType is the same
display.compare().isDisplaySegment(); //returns true - obviously
display.compare().isLightAmbient(); //returns false - obviously
```

* Example getting [Values](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Values.java) from stack
```java
stack.values().temperature();
stack.values().temperature_Min();
stack.values().temperature_Max();
stack.values().temperature_Avg();
```

* Example getting [Values](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Values.java) of specific [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java)
```java
final Sensor distanceIR = stack.sensors().distanceIR();
distanceIR.values().distance();
distanceIR.values().distance_Min(); //This is only a measure of a short time;
distanceIR.values().distance_Max(); //This is only a measure of a short time;
distanceIR.values().distance_Avg(); //This is only a measure of a short time;
```

* Example other [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java) functions - all functions are safe to use and does not produce any user exceptions even if a Sensor were never connected 
```java
final Sensor distanceIR = stack.sensors().distanceIR();
distanceIR.isPort(); //returns stack-order-number (higher stack/port = higher number)
distanceIR.isBrick(); //returns false 'DistanceIR' is a Bricklet
distanceIR.isHasLedStatus(); //returns true if the sensor has a status LED
distanceIR.isPresent(); //true if a sensor is connected
distanceIR.ledStatusOn(); //Switch status LED on (On/Off/Heartbeat/Status)
distanceIR.ledAdditionalOn(); //Switch other LEDs on - can be Display-backLight, color-FlashLight, IMU-orientation-LEDs,... (On/Off/Heartbeat/Status)
distanceIR.refreshLimit(6); //(callback period 6/sec) sets the refresh value rate in a second - e.g. for power issues
// [...]
```

* event example
```java
private void onSensorEvent(final Sensor currentSensor, final ValueType valueType, final Long value) {
    Sensor display = stack.sensors().getSegmentDisplay();
    
    if (valueType.isMotionDetected() and value == 1) {
        console("Motion detected");
        console("A lux value from any sensor: " + stack.values().lightLux());
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

* Connecting with auto closeable [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)
```java
try (Stack stack = new Stack("host", 4223, "optionalPassword")) {
    //Getting [list of sensors]
    Sensors sensors = stack.sensors;
    //Add listener on any sensor and event getting the [SensorEvent]s
    tinkerForge.sensorEventConsumerList.add(ths::onSensorEvent);
}
```


#### TODO
- [ ] 16IO manage also input
- [ ] Thread does not shutdown and keeps program alive :(
- [ ] Spring integration

* Sensors
- [ ] Get Additional Sensor information like "ChipTemperature"