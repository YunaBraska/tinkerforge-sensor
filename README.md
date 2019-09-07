Tinkerforge Sensor Library
------------------

This library simplifies Tinkerforge's sensor API.
It Removes the pain of sensor UIDs, sensor versions, ports and provides a generic API for every sensor.

#### Sensor documentation
##### A list of all sensors behaviors and more can be found in the [ReadmeDoc](readmeDoc/README.md)

#### Hackerschool sessions (berlin)
* [x] [24.11.18 (RatePAY)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_01_241118)
* [x] [22.02.19 (RatePAY)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_02_220219)
* [x] [18.05.19 (RatePAY)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_03_180519)
* [ ] *<s>20.07.19 (Trabrennbahn/Mariendorf)</s>*
* [ ] *<s>03.08.19 (Trabrennbahn/Mariendorf)</s>*
* [X] [07.09.19 (DKB)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_04_070919)
* [X] 24.11.19 (???)

#### Examples
* Examples can be found here: (https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/example)
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

#### How it works
* TinkerForge original [Device](https://www.tinkerforge.com/en/doc/Software/Device_Identifier.html)
* wrapped by Generic [Sensor](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/Sensor.java)
* handles input and sends [SensorEvent](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/type/SensorEvent.java)
* Auto closeable [Stack](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java) with addable event consumer
* Returns List of Sensors [Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Sensors.java)
* Supported devices [Bricks and Bricklets](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/main/java/berlin/yuna/tinkerforgesensor/model/sensor)
* Feel free for pull request as the wrapper classes are not so hard to implement, its just a mapping ;)


#### TODO
- [ ] 16IO manage also input
- [ ] Thread does not shutdown and keeps program alive :(
- [ ] Spring integration

* Sensors
- [ ] Get Additional Sensor information like "ChipTemperature"