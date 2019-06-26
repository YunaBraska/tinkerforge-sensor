package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

public class Distance_Display_Left_Right_Example extends Helper {

    private static Stack localStack;
    private static Stack remoteStack;

    public static void main(final String[] args) {
        try {
            remoteStack = new Stack("100.100.0.2", 4223, "hackerschool");
            while (remoteStack.isConnecting()) {
                sleep(128);
            }
        } catch (NetworkConnectionException e) {
            throw new RuntimeException(e);
        }


        localStack = ConnectionAndPrintValues_Example.connect();
        localStack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    private static boolean invert = false;

    private static long leftMax = 1;
    private static long rightMax = 1;

    private static long leftLimit = 1;
    private static long rightLimit = 1;

    private static long leftValue = 1;
    private static long rightValue = 1;

    private static long leftMotor = 1;
    private static long rightMotor = 1;

    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
        final Sensor leftDistance = localStack.sensors().distanceIR(0);
        final Sensor rightDistance = localStack.sensors().distanceIR(1);
        final Sensor display = localStack.sensors().displayLcd128x64();
        final Sensor servo = remoteStack.sensors().servo();

        //init
        if (timePassed(128)) {
            servo.send(0, true, invert);
            servo.send(1, true, invert);
            servo.send(2, true, invert);

            servo.send(4, true);
            servo.send(5, true);
            servo.send(6, true);


            display.ledAdditional_setOn();
            display.send("leftValue ${s}" + (leftValue / 10) + "cm", 0, 0);
            display.send("leftMotor ${s}" + (leftMotor), 0, 2);

            display.send("rightValue ${s}" + (rightValue / 10) + "cm", 0, 4);
            display.send("rightMotor ${s}" + (rightMotor), 0, 6);
        }


        if (sensor.compare().is(leftDistance)) {
            leftLimit = value > leftLimit ? value : leftLimit;
//            leftMax = leftMaxRaw > rightMaxRaw ? rightMaxRaw : leftMaxRaw;
            leftMax = leftLimit;

//            leftStart = leftValue == leftMax && value < leftMax ? value : leftStart;
            leftValue = value >= leftMax ? leftMax : value;
        }

        if (sensor.compare().is(rightDistance)) {
            rightLimit = value > rightLimit ? value : rightLimit;
//            rightMax = rightMaxRaw > leftMaxRaw ? leftMaxRaw : rightMaxRaw;
            rightMax = rightLimit;

//            rightStart = rightValue == rightMax && value < rightMax ? value : rightStart;
            rightValue = value >= rightMax ? rightMax : value;
        }

        if (timePassed(10)) {
            rightMotor = rightValue >= rightMax ? 0 : (((rightValue * 100) / rightMax) - 50) * 100;
            leftMotor = leftValue >= leftMax ? 0 : (((leftValue * 100) / leftMax) - 50) * 100;
            servo.send(0, rightMotor);
            servo.send(1, rightMotor);
            servo.send(2, rightMotor);

            servo.send(4, leftMotor);
            servo.send(5, leftMotor);
            servo.send(6, leftMotor);
        }


        if (sensor.compare().isRotary()) {
            if (type.isButtonPressed() && value == 1) {
                invert = !invert;
                servo.send(0, true, invert);
                servo.send(1, true, invert);
                servo.send(2, true, invert);
            } else if (type.isRotary()) {
                servo.send(-1, value * 500);
            }
        }
    }
}
