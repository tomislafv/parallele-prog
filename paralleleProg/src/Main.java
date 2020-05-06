import sun.nio.ch.ThreadPool;

import lejos.hardware.Button;
import java.awt.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        Child child = new Child();
        ExecutorService tp = Executors.newFixedThreadPool(1);
        tp.execute(child);
        Button.ESCAPE.waitForPress();
        tp.shutdownNow();

    }



}
