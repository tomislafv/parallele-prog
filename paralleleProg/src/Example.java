import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import sensor.caller.EV3ColorSensorCaller;
import sensor.caller.EV3TouchSensorCaller;
import sensor.caller.EV3UltrasonicSensorCaller;
import sensor.loop.EV3ColorSensorRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

//Beispiel-Klasse, die unsere Implementierungen nutzt
public class Example implements AutoCloseable {

    private final ExecutorService executorService; //ExecutorService, anstatt Thread direkt verwenden (besserer Stil und performanter)

    public Example() {
        executorService = Executors.newCachedThreadPool();
    } //Verwenden eines Cached ThreadPool -> Gute Performanz und Nutzbarkeit

    //Minimalistisches erstes Beispiel mit einem zusätzlichen parallelen Ablauf
    public void childExample() {
        Child child = new Child(); //Erstellen des Child-Runnable
        Thread childThread = new Thread(child); //Uebergeben an einen Thread (Hier direktes nutzen eines Threads, um nicht den gesamten Threadpool stoppen zu muessen)
        childThread.start(); //Ausfuehren
        Button.ESCAPE.waitForPress(); //Aktueller Main-Thread wartet auf Button
        childThread.interrupt(); //Sobald Button gedrueckt, stoppen des Childs mit interrupt (besserer Stil als Thread.stop, da hierbei keine laufenden Aktionen gestoppt werden)
    }

    //Beispiel fuer Sensor-Einzelwerte
    public void callerExample() {
        List<Callable<float[]>> sensorCallers = List.of(
                new EV3ColorSensorCaller(SensorPort.S1),
                new EV3TouchSensorCaller(SensorPort.S2),
                new EV3ColorSensorCaller(SensorPort.S3),
                new EV3UltrasonicSensorCaller(SensorPort.S4)); //Erstellen der Callable<float[]> Tasks
        List<Future<float[]>> futureList; //Liste-Deklarieren fuer Rueckgabewerte (Future<float[]>)
        try {
            futureList = executorService.invokeAll(sensorCallers); //Abgabe an Thread-Pool, werden hiermit gestartet
        } catch (InterruptedException e) {
            throw new IllegalStateException(e); //Darf im Main-Thread nicht nicht interrupted werden
        }
        //Code der im Main-Thread ausgefuehrt wird, koennte hier stehen -> weitere Parallelitaet
        for (Future<float[]> actFuture : futureList) {
            try {
                float[] sensorResult = actFuture.get(); //get auf ein Future stellt eine Tread-join-Operation dar
                System.out.println("Sensor-Result: " + Arrays.toString(sensorResult)); //Beispielhaftes verwenden des Sensorwertes
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    //Beispiel fuer einen in einer Schleife
    public void runnerExample() {
        BlockingQueue<float[]> blockingQueue = new LinkedBlockingQueue<>(1); //Synchronistation erfolgt ueber BlockingQueue, Laenge 1 fuer zeitnahen Wert
        EV3ColorSensorRunner ev3ColorSensorRunner = new EV3ColorSensorRunner(SensorPort.S1, blockingQueue, 10);
        executorService.submit(ev3ColorSensorRunner); //Task submiten, wird hierdurch gestartet
        while (!Thread.currentThread().isInterrupted()) { //Hier koennte eine Schleifenbedingung
            try {
                System.out.println(Arrays.toString(blockingQueue.take())); //Wartet bis ein Element verfügbar ist, Synchronisation wird durch BlockieQueue realisiert
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //erneuter Interrupt, da das Auffanger der Exception das Bit zuruecksetzt
                break;
            }
        }
    }

    //Implementiert Autocloseable, um den ThreadPool wieder ordnungsgemaeß freizugeben
    @Override
    public void close() throws InterruptedException {
        executorService.shutdown(); //schliesst den Threadpool
        executorService.awaitTermination(1, TimeUnit.SECONDS); //Blockiert/Thread join (max 1 Sekunde bzw. bis Threadpool geschlossen ist)
    }
}
