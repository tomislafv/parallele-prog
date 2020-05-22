import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import sensor.caller.EV3ColorSensorCaller;
import sensor.caller.EV3TouchSensorCaller;
import sensor.caller.EV3UltrasonicSensorCaller;
import sensor.loop.EV3ColorSensorRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Example implements AutoCloseable {

    private final ExecutorService executorService; //ExecutorService statt Thread direkt verwenden (besserer Stil und performanter)

    public Example() {
        executorService = Executors.newCachedThreadPool();
    }

    public void childExample() {
        Child child = new Child();
        Thread childThread = new Thread(child);
        childThread.start();
        Button.ESCAPE.waitForPress(); //Aktueller Thread wartet auf Button
        childThread.interrupt();
    }

    public void callerExample() {
        List<Callable<float[]>> sensorCallers = List.of(
                new EV3ColorSensorCaller(SensorPort.S1),
                new EV3TouchSensorCaller(SensorPort.S2),
                new EV3ColorSensorCaller(SensorPort.S3),
                new EV3UltrasonicSensorCaller(SensorPort.S4));
        List<Future<float[]>> futureList;
        try {
            futureList = executorService.invokeAll(sensorCallers);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        //Code der im Main-Thread ausgefuehrt wird
        for (Future<float[]> actFuture : futureList) {
            try {
                float[] sensorResult = actFuture.get();
                System.out.println("Sensor-Result: " + Arrays.toString(sensorResult));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void runnerExample() {
        BlockingQueue<float[]> blockingQueue = new LinkedBlockingQueue<>(1);
        EV3ColorSensorRunner ev3ColorSensorRunner = new EV3ColorSensorRunner(SensorPort.S1, blockingQueue, 10);
        executorService.submit(ev3ColorSensorRunner);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println(Arrays.toString(blockingQueue.take())); //Wartet bis ein Element verfügbar ist, Synchronisation wird durch BlockieQueue realisiert
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void close() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
