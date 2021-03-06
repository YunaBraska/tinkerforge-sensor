package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.ValueType;

import java.net.URL;

public class Keys_Mouse_to_Sound_Display_Example {

    private static Stack stack;
    private static final URL sound = Keys_Mouse_to_Sound_Display_Example.class.getClassLoader().getResource("cheer.wav");

    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(event -> onSensorEvent(event.getValue(), event.getValueType()));

        //Activate localControl
        stack.sensors().localControl().ledAdditional_setOn();
    }

    private static void onSensorEvent(final Long value, final ValueType type) {
        if (type.containsHumanInput()) {
            //Write on display
            stack.sensors().display().ledAdditional_setOn();
            stack.sensors().display().send(value + " [" + type + "]", true);
        }

        if (type.isButtonPressed() || (type.is().cursorClickCount() && value >= 2)) {
            //Play sound
            stack.sensors().localAudio().send(sound);
        }

        if (type.isRotary()) {
            //Set volume
            stack.sensors().localAudio().send(value);
        }
    }
}
