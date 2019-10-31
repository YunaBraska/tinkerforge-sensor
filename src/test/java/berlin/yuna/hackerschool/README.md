# TINKERFORGE SENSOR API
![logo](pictureEx_01.jpg "pictureEx_01")

#### Hackerschool sessions (berlin)
* [x] [24.11.18 (RatePAY)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_01_241118)
* [x] [22.02.19 (RatePAY)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_02_220219)
* [x] [18.05.19 (RatePAY)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_03_180519)
* [ ] *<s>20.07.19 (Trabrennbahn/Mariendorf)</s>*
* [ ] *<s>03.08.19 (Trabrennbahn/Mariendorf)</s>*
* [X] [07.09.19 (DKB)](https://github.com/YunaBraska/tinkerforge-sensor/tree/master/src/test/java/berlin/yuna/hackerschool/session_04_070919)
* [ ] *<s>16.11.19 (BSR)</s>*
* [X] 30.11.19 (DKB)

### Hardware Requirements
* Tinkerforge Elements [https://www.tinkerforge.com](https://www.tinkerforge.com)
* Tinkerforge Driver [Brick Daemon](https://www.tinkerforge.com/en/doc/Software/Brickd.html#brickd)

### Installation (Mac) via [Brew](https://brew.sh/) \[Java, Maven, Git, Intellij]
* Install [Brew](https://brew.sh/)
* brew tap homebrew/cask-versions
* brew tap adoptopenjdk/openjdk
* brew install maven
* brew install git
* brew cask install intellij-idea-ce
* git clone [https://github.com/YunaBraska/tinkerforge-sensor.git](https://github.com/YunaBraska/tinkerforge-sensor.git)
* Now just open this downloaded folder with Intellij

### Installation (Windows) via [Chocolatey](https://chocolatey.org/) \[Java, Maven, Git, Intellij]
* Install [Chocolatey](https://chocolatey.org/install)
* choco install openjdk
* choco install maven
* choco install git
* choco install intellijidea-community
* git clone [https://github.com/YunaBraska/tinkerforge-sensor.git](https://github.com/YunaBraska/tinkerforge-sensor.git)
* Now just open this downloaded folder with Intellij

### Hackerschool Helper methods
#### timePassed(milliseconds)
* Will return true if the last call at this lime has passed the time
```java
if(timePassed(1000)) {
    console("You were waiting long engouh");
} else {
    console("You still have to wait for it")
}
```

#### timePassed(textLabel, milliseconds)
* Will return true if the last call with this label has passed the time
```java
if("program 2", timePassed(1000)) {
    console("You were waiting long engouh");
} else {
    console("You still have to wait for it")
}
```

#### isEmpty(text)
* Will return true if text is null or empty otherwise false
```java
if(isEmpty("not empty text")) {
    console("This ist an empty text");
} else {
    console("This text is not empty")
}
```
* result: "This text is not empty"
#### console(text)
* print message to the console (including java.lang.String.format)
```java
console("Write something")
```
* result: "18.05.19 11:22:33 Write something"

#### error(text)
* same as console just with error (red line)

#### async(textLabel, run -> myFunction)
* Will start a program in a new thread/async/parallel 
```java
loop("my program 5", run -> myFunction())
myFunction() {
    console("my async program")
}
```

#### loop(textLabel, run -> myFunction)
* Will start and loop a program in a new thread thread/async/parallel 
```java
loop("my program 3", run -> myFunction())
myFunction() {
    console("Endless message")
}
```

#### loop(textLabel, run -> myFunction, 10)
* Will start and loop a program in a new thread thread/async/parallel 
```java
loop("my program 4", run -> myFunction(), 10)
myFunction() {
    console("message will be processed 10 times")
}
```

#### roundUp(value)
* Will round up and cut the number of decimal values to two  
```java
roundUp(1.55555555, run -> myFunction())
* result: 1.56
```

#### roundUp(value, decimals)
* Will round up and cut the number of the decimal values to the given ones  
```java
roundUp(1.55555555, run -> myFunction(), 4)
* result: 1.5556
```

#### sleep(millisecond)
* Will wait for the given milliseconds  
```java
sleep(1000)
* result: wait 1 Second
```

#### date()
* print returns a text including the current date
```java
String date = date()
```
* result: "18.05.19"

#### time()
* print returns a text including the current time
```java
String time = date()
```
* result: "11:22:33"

#### dateTime()
* print returns a text including the current date and time
```java
String date = date()
```
* result: "18.05.19 11:22:33"

![logo](https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Tinkerforge_logo.svg/1599px-Tinkerforge_logo.svg.png "Tinkerforge logo")
