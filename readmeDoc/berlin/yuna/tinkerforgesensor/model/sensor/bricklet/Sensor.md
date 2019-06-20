
## berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### Sensor · 

---

 Generic wrapper for  to have generic methods and behavior on all sensors

 @param 
 List of  for getting all {@link Sensor
 @param consumer to notify  with {@link Sensor
 Creates new [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))

 @param device  to wrap with [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
 @param uid    for unique identifier
 @return [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
/
 Creates new [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))

 @param deviceIdentifier  identifier
 @param uid              for unique identifier like [DEVICE_IDENTIFIER](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
 @param connection        for 
 @return [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
/
 Constructor

 @param device connected original 
 @param uid    cached uid of 
/
 Tells if the sensor is real/present. The sensors are fake sensors as long as the connection is not established

 @return true if the sensor is present
/
 This method should not be called to often as this slows down the sensors
 Checks if the sensor is [isPresent()](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) and checks the connection by [port](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))

 @return true if the sensor is present and the port refresh was successfully as the refresh needs that the  is answering
/
 @return [Compare](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/Compare.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/builder/Compare.java)) with predefined compare methods
/
 For automation to know if its worth to call setLedStatus_Status functions. Value is taken from [initLedConfig()](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))

 @return true if the Sensor  has setLedStatus_Status
/
 For automation to know if its worth to call setLedAdditional functions. Value is taken from [initLedConfig()](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))

 @return true if the Sensor  has setLedAdditional
/
 Tells if the Sensor is a brick or bricklet calculated from {@link Sensor
 Same method as [valueMap](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) with limitation of 1 call per millisecond

 @param limitPerSec sets message limit per seconds (hast to be > 0 and < 1000000000) else default method [send(Object...)](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) will be called
 @param values      some objects like a "howdy", 123, Color.GREEN which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 @param value some object like a "howdy" string for [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 Sets the refresh rate for the sensor values (e.g. for power issues)

 @param perSec hast to be in range of 0 to 1000 (0 = listen only on changes)
               
 Some old  does't have the option 0 and will fall back to period callback
 @return current {@link Sensor
 Sets the refresh period directly to the  - its safer to use the method [refreshLimit(int)](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))

 @param milliseconds callBack period
 @return current {@link Sensor
 @param values some objects like a "howdy", "howdy2" string for [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 Loads the led configurations for setLedStatus_Status and setLedAdditional

 @return current {@link Sensor
 @return [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) state or null if setLedStatus_Status is not present
/
 @return [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) state or null if setLedAdditional is not present
/
 @param ledStatusType for status led like [LED_STATUS_HEARTBEAT](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 @param value for status led like [LED_STATUS_HEARTBEAT](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 @param ledStatusType additional led like [LED_ADDITIONAL_ON](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 @param value additional led like [LED_ADDITIONAL_ON](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it
 @return current {@link Sensor
 Status led try to show [LED_STATUS](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 Status led try to show [LED_STATUS_HEARTBEAT](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 Status led try to show [LED_STATUS_ON](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 Status led try to show [LED_STATUS_OFF](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 led try to show [LED_ADDITIONAL_ON](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the display of [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 led try to show [LED_ADDITIONAL_HEARTBEAT](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the flash led of [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 Additional led try to show [LED_ADDITIONAL_HEARTBEAT](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 Additional led try to show [LED_ADDITIONAL_STATUS](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 led try to show [LED_ADDITIONAL_ON](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) like display of [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) which the sensor could process - else it just should ignore it

 @return current {@link Sensor
 Gets the send of [ValueType](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))

 @param valueType to get from {@link Sensor
 Gets the send of [ValueType](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/ValueType.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/type/ValueType.java))

 @param valueType to get from {@link Sensor
 Connected port first number represents the brick and the second the connected port

 @return port starts at 0
/
 Refreshes/Searches the {@link Sensor
 Relink parent and sets port if given Sensor were the parent

 @param sensorList of possible parents
/
 Relink parent and sets port if given Sensor were the parent

 @param sensor to link as parent
/
 Internal api to send {@link Sensor
 Compares the sensor with 

 @return true if the is connected with class type of 
/
 Compares the sensor with {@link Sensor
 @return class type of 
/
 Flashing status led

 @return {@link Sensor
 This is needed for the [SensorRegistry](readmeDoc/berlin/yuna/tinkerforgesensor/model/SensorRegistry.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/SensorRegistry.java)) to instantiate [newInstance(Integer, String, IPConnection)](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) new [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
/
 Generic led function enum for all [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) types, can be used also in [setLedStatus(Integer)](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) or [setLedAdditional(Integer)](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
/
--- 
