TinkerForge Sensor Library
------------------
#### [BETA] testing this on hackerSchool / kids sessions

TinkerForge provides awesome Hardware and APIs. You don't have to care about Hardware and can just start programming.
This Library is simplifying the API usage in a pure Java 8 way without any Frameworks.
Removes pain of the Sensor UID, how to speak to the sensor and what values can i get from it etc. so that the focus is more on the logic

#### Features
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

#### Examples
```java
public class someExample extends TinkerforgeUtil {
    
    private final SensorList<Sensor> sensorList = new SensorList<>();
    
    public static void main(String args[]) {
            try (SensorListener sensorListener = new SensorListener("localhost", 4223)) {
                sensorList = sensorListener.sensorList;
                sensorListener.sensorEventConsumerList.add(ths::onSensorEvent);
                onStart();
                //...
            }
        }
        
    private void onStart() {
        console(readFile("hello.txt"));
        loop(EACH_SECOND, run -> showNumberOfLoops());
        loop(EACH_SECOND, run -> displayMoveMessage());
    }
    
    private void showNumberOfLoops() {
        console("[%s] Running programs", loopList.size());
    }
    
    private void onSensorEvent(final Sensor currentSensor, final ValueType valueType) {
        Sensor display = sensorList.first(BrickletSegmentDisplay4x7.class);
    
        if (valueType.is(MOTION_DETECTED_ON)) {
    
            console("Motion detected");
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
}
```

#### TODO
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