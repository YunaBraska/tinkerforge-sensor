package berlin.yuna.hackerschool.session_03_180519;

import berlin.yuna.hackerschool.example.ConnectionAndPrintValues_Example;
import berlin.yuna.hackerschool.example.Helper;
import berlin.yuna.tinkerforgesensor.logic.Stack;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;

/**
 * @author Nathalie
 */
public class Nathalie extends Helper {

    private static Stack stack;

    //START FUNCTION
    public static void main(final String[] args) {
        stack = ConnectionAndPrintValues_Example.connect();
        stack.sensorEventConsumerList.add(event -> onSensorEvent(event.sensor, event.value, event.valueType));
    }

    //VARIABLES
    private static long soundMax = 1;

    //CODE FUNCTION
    private static void onSensorEvent(final Sensor sensor, final Long value, final ValueType type) {
//        try {
//            BufferedImage image = ImageIO.read(new File("/Users/morgenstern/Downloads/icon_temperature.png"));
//            final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//
//
//            int width = image.getWidth();
//            int height = image.getHeight();
//            int[][] result = new int[height][width];
//
//            for (int row = 0; row < height; row++) {
//                for (int col = 0; col < width; col++) {
//                    result[row][col] = image.getRGB(col, row);
//                }
//            }
//
//            //System.out.println(Arrays.toString(pixels));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //Get Sensor and Value
        final Sensor io16 = stack.sensors().iO16();
        final long decibel = stack.values().soundIntensity() + 1;

        //Dynamic max volume
        if (decibel > soundMax) {
            soundMax = decibel;
        }

        //every 250 milliseconds - for readable display
        if (timePassed(250)) {
            stack.sensors().displaySegment().send((decibel / 10) + "dB");
        }

        //every 50 milliseconds
        if (timePassed(50)) {
            final int ledCount = (int) (decibel / ((soundMax / 18) + 1));

            //Switch LEDs on
            for (int led = 1; led < ledCount; led++) {
                io16.send(led);
            }

            //Switch other LEDs off
            for (int led = ledCount; led < 16; led++) {
                io16.send(-led);
            }
        }

        //Fan temperature
        if (stack.values().temperature() > 2880) {
            io16.send(17);
        } else if (stack.values().temperature() < 2880) {
            io16.send(-17);
        }
    }
}
