
## berlin.yuna.tinkerforgesensor.logic.Stack
* [exception](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/README.md) · [bricklet](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/README.md) · [brick](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/brick/README.md) · [type](readmeDoc/berlin/yuna/tinkerforgesensor/model/type/README.md) · [logic](readmeDoc/berlin/yuna/tinkerforgesensor/logic/README.md) · [model](readmeDoc/berlin/yuna/tinkerforgesensor/model/README.md) · [util](readmeDoc/berlin/yuna/tinkerforgesensor/util/README.md) · [builder](readmeDoc/berlin/yuna/tinkerforgesensor/model/builder/README.md) · 

---
###### Stack · 

---

 IPConnection connection of the [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) and for enumerating available [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))
/
 List of  for getting all [SensorEvent](src/main/java/berlin/yuna/tinkerforgesensor/model/type/SensorEvent.java)
/
 SensorList holds all connected [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) managing that list
/
 Dummy Sensorlist, wont connect to any [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) as the host is not set

 @throws NetworkConnectionException should never happen
/
 Auto connects and auto  [close()](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))s and manages the [sensorList](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) by creating 

 @param host for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param port for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @throws NetworkConnectionException if connection fails due/contains  [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
 Auto connects and auto  [close()](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))s and manages the [sensorList](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) by creating 

 @param host                  for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param port                  for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param ignoreConnectionError ignores any [NetworkConnectionException](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/NetworkConnectionException.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/exception/NetworkConnectionException.java)) and tries to auto reconnect
 @throws NetworkConnectionException if connection fails due/contains  [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
 Auto connects and auto  [close()](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))s and manages the [sensorList](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) by creating 

 @param host     for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param port     for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param password for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @throws NetworkConnectionException if connection fails due/contains  [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
 Auto connects and auto  [close()](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java))s and manages the [sensorList](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) by creating 

 @param host                  for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param port                  for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param password              for [connection](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
 @param ignoreConnectionError ignores any [NetworkConnectionException](readmeDoc/berlin/yuna/tinkerforgesensor/model/exception/NetworkConnectionException.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/exception/NetworkConnectionException.java)) and tries to auto reconnect
 @throws NetworkConnectionException if connection fails due/contains  [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
 connects to given host - this method will be called from [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) constructor

 @throws NetworkConnectionException if connection fails due/contains  [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java)) [Stack](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
 disconnects all [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) from the given host see [close()](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
 disconnects all [Sensor](readmeDoc/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/model/sensor/bricklet/Sensor.java)) from the given host and removes the sensors from [sensorList](readmeDoc/berlin/yuna/tinkerforgesensor/logic/Stack.md) ([source](src/main/java/berlin/yuna/tinkerforgesensor/logic/Stack.java))
/
--- 
