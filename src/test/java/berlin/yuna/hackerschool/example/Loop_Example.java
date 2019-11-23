package berlin.yuna.hackerschool.example;

import berlin.yuna.tinkerforgesensor.model.type.Loop;

import static java.lang.String.format;

public class Loop_Example extends Helper {


    public static void main(final String[] args) {
        final var myProgram = new Loop(512, Loop_Example::myProgram);

        myProgram.start();
        final long startTimeMs = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTimeMs + 10000) {
            sleep(10);
        }
        myProgram.stop();


        loop("EachSecond", run -> System.out.println(format("EachSecond [%s]", run)));
    }

    private static void myProgram(final Long run) {
        System.out.println(format("myProgram [%s]", run));
    }


}
