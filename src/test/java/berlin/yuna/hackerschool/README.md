# TINKERFORGE SENSOR API

![logo](https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Tinkerforge_logo.svg/1599px-Tinkerforge_logo.svg.png "Pigeon aka The Pig")

![logo](pictureEx_01.jpg "pictureEx_01")
![logo](pictureEx_02.jpg "pictureEx_02")

### Description
This project is an example to get a feeling how the world of smart home works.
From putting sensors together up to programming the logic/behavior.

### Requirements
* Tinkerforge Elements (https://www.tinkerforge.com)
* Tinkerforge Brick Daemon (https://www.tinkerforge.com/en/doc/Software/Brickd.html#brickd)
* IntelliJ IDEA for programming (https://www.jetbrains.com/idea/download/)
* Java 8 or higher
* Maven
* Parent Project (https://github.com/YunaBraska/tinkerforge-sensor) 

### Examples
```java
private static TinkerForge tinkerForge;

    //INIT CONNECTION
    public static void main(String[] args) {
        TinkerForge tinkerForge = ConnectionAndPrintValues_Example.connect();
        tinkerForge.sensorEventConsumerList.add(event -> onSensorEvent(event.value, event.valueType));
    }

    //CODE
    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.isSoundIntensity()) {
            tinkerForge.sensors().displaySegment().value(value / 10 + "db");
        }
    }


```
* More examples like [SoundIntensity_to_DisplaySegment_Example](https://gitlab.com/hckrschl/berlin/tinkerforge/blob/master/src/main/java/examples/SoundIntensity_to_DisplaySegment_Example.java) can be found in the [example package](https://gitlab.com/hckrschl/berlin/tinkerforge/blob/master/src/main/java/examples/SoundIntensity_to_DisplaySegment_Example.java) and also from the other [sessions](https://gitlab.com/hckrschl/berlin/tinkerforge/blob/master/src/main/java/)


### Helper
* of curse there are some helping methods for faster programming and don.t thinking about it.
##### timePassed(milliseconds)
* Will return true if the last call at this lime has passed the time
```java
if(timePassed(1000)) {
    console("You were waiting long engouh");
} else {
    console("You still have to wait for it")
}
```

##### timePassed(textLabel, milliseconds)
* Will return true if the last call with this label has passed the time
```java
if("program 2", timePassed(1000)) {
    console("You were waiting long engouh");
} else {
    console("You still have to wait for it")
}
```

##### isEmpty(text)
* Will return true if text is null or empty otherwise false
```java
if(isEmpty("not empty text")) {
    console("This ist an empty text");
} else {
    console("This text is not empty")
}
```
* result: "This text is not empty"
##### console(text)
* print message to the console (including java.lang.String.format)
```java
console("Write something")
}
```
* result: "18.05.19 11:22:33 Write something"

##### error(text)
* same as console just with error (red line)

##### async(textLabel, run -> myFunction)
* Will start a program in a new thread/async/parallel 
```java
loop("my program 5", run -> myFunction())
myFunction() {
    console("my async program")
}
```

##### loop(textLabel, run -> myFunction)
* Will start and loop a program in a new thread thread/async/parallel 
```java
loop("my program 3", run -> myFunction())
myFunction() {
    console("Endless message")
}
```

##### loop(textLabel, run -> myFunction, 10)
* Will start and loop a program in a new thread thread/async/parallel 
```java
loop("my program 4", run -> myFunction(), 10)
myFunction() {
    console("message will be processed 10 times")
}
```

##### roundUp(value)
* Will round up and cut the number of decimal values to two  
```java
roundUp(1.55555555, run -> myFunction())
* result: 1.56
```

##### roundUp(value, decimals)
* Will round up and cut the number of the decimal values to the given ones  
```java
roundUp(1.55555555, run -> myFunction(), 4)
* result: 1.5556
```

##### sleep(millisecond)
* Will wait for the given milliseconds  
```java
sleep(1000)
* result: wait 1 Second
```

##### date()
* print returns a tex including the current date
```java
String date = date()
}
```
* result: "18.05.19"

##### time()
* print returns a tex including the current time
```java
String time = date()
}
```
* result: "11:22:33"

##### dateTime()
* print returns a tex including the current date and time
```java
String date = date()
}
```
* result: "18.05.19 11:22:33"