import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import sensor.caller.EV3ColorSensorCaller;
import sensor.caller.EV3TouchSensorCaller;
import sensor.caller.EV3UltrasonicSensorCaller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Child child = new Child();
        ExecutorService tp = Executors.newFixedThreadPool(1);
        tp.execute(child);
        Button.ESCAPE.waitForPress();
        tp.shutdownNow();
    }

    private static void callerExample() {
        ExecutorService tp = Executors.newCachedThreadPool();
        List<Callable<float[]>> sensorCallers = List.of(
                new EV3ColorSensorCaller(SensorPort.S1),
                new EV3TouchSensorCaller(SensorPort.S2),
                new EV3ColorSensorCaller(SensorPort.S3),
                new EV3UltrasonicSensorCaller(SensorPort.S4));
        List<Future<float[]>> futureList = sensorCallers.stream().map(tp::submit).collect(Collectors.toList());
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
}
