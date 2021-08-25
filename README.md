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

### Requirements
* Install [Brick Daemon](https://www.tinkerforge.com/en/doc/Downloads.html#tools)
* Install and open [IntelliJ](https://www.jetbrains.com/idea/download) (IDE / EDITOR) 
* Install Java via IntelliJ: File -> Project Structure -> SDKs -> `+` Download JDK -> `AdoptOpenJdk` -> Version `11`
* Compile / Initialize Project: Right click on `tinkerforge-senor` -> Maven -> `Reload Project` and `Generate Sources`

### Sensor documentation
* A list of all supported Sensors (Bricks/Bricklets): [Sensors](https://github.com/YunaBraska/tinkerforge-sensor/blob/master/src/main/java/berlin/yuna/tinkerforgesensor/model/handler)

### Example
```java
final Stack stack = new Stack();
stack.addListener(this::listen).connect();

boolean ledStatusToggle = true;
private void listen(final SensorEvent event) {
    if (event.isValueType().buttonPressed() ) {
        ledStatusToggle = !ledStatusToggle;
        if (ledStatusToggle) {
            sensor.setStatusLedOn();
            stack.get().buttonRGBList().forEach(button -> button.setColor(Color.GREEN));
        } else {
            sensor.setStatusLedOff();
            stack.get().buttonRGBList().forEach(button -> button.setColor(Color.RED));
        }
    }
}
```


#### TODO
- [ ] Handle Authentication
- [ ] Handle connection errors (currently slows API down)
- [ ] Import Sensors from v0.0.6
- [ ] 16IO manage also input
- [ ] Spring integration

[build_shield]: https://github.com/YunaBraska/tinkerforge-sensor/workflows/JAVA_CI_DAILY/badge.svg
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