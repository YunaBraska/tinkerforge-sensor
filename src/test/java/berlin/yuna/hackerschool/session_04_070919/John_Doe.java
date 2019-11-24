package berlin.yuna.hackerschool.session_04_070919;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.type.SensorEvent;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

import java.util.Arrays;
import java.util.List;

public class John_Doe extends Helper {
    //lists
    static List<String> zahlen = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    // VARIABLES
    static int ergebnis = (0);

    static String eingabe = "0";

    static boolean plus = (false);

    private static Stack stack;

    static String text = ("");


    // CODE FUNCTION
    static void onSensorEvent(final SensorEvent event) {
        if (event.getValueType() == ValueType.BUTTON_TOUCH) {
            // Display hell schalten
            stack.sensors().display().ledAdditional_setOn();

            final List<Number> liste = event.getValues();

            for (int i = 0; i < zahlen.size(); i++) {
                if (liste.get(i).intValue() == 1) {
                    final String ziffer = zahlen.get(i);
                    eingabe = eingabe + ziffer;
                    plus = false;
                    text += zahlen.get(i);
                    break;
                }
            }

            if (liste.get(11).intValue() == 1 && plus == false) {
                text = text + "+";
                ergebnis = Integer.valueOf(eingabe) + ergebnis;
                eingabe = "0";
                plus = (true);
            }

        }

        zeige(text, ergebnis);

    }

    private static void zeige(String text, int ergebnis) {
        stack.sensors().display().send(text, false, 0);
        stack.sensors().display().send(("=") + String.valueOf(ergebnis), false, 2);
    }

    // START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.consumers.add(John_Doe::onSensorEvent);
    }

}
