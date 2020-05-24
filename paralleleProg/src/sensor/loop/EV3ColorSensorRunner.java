package sensor.loop;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

import java.util.concurrent.BlockingQueue;

//Umsetzung des dauerhaften Lesens mit dem Farbsensor
public class EV3ColorSensorRunner implements Runnable {

    private final EV3ColorSensor ev3ColorSensor;
    private final BlockingQueue<float[]> sensorQueue;
    private final long sleepMillis;

    //Konstruktor initialisiert Sensor standardmaessig am angegebenen Port, Queue zur Synchronisation und Zeit zwischen den Messungen
    public EV3ColorSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.ev3ColorSensor = new EV3ColorSensor(port);
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    //Konstruktor nutzt uebergebenen Sensor, hierdurch kann der Betriebsmodus gewaehlt werden, Queue zur Synchronisation und Zeit zwischen den Messungen
    public EV3ColorSensorRunner(EV3ColorSensor ev3ColorSensor, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (ev3ColorSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.ev3ColorSensor = ev3ColorSensor;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    //Paraleller Runnable-Task fuehrt das staendige Auslesen durch, bis der Tasks unterbrochen wird
    @Override
    public void run() {
        EV3ColorSensor ev3ColorSensor = this.ev3ColorSensor;
        while (!Thread.currentThread().isInterrupted()) { //Solange lesen und in Queue pushen, bis der Tasks unterbrochen wird
            float[] nextValue = new float[ev3ColorSensor.sampleSize()];
            ev3ColorSensor.fetchSample(nextValue, 0);
            try {
                sensorQueue.put(nextValue); //Synchronisation findet ueber die Queue statt, diese nutzt blockierende Operationen fuer das Einlesen und Auslsen
                Thread.sleep(sleepMillis); //Schlafen um nicht dauerhaft Messungen durchzufuehren (Kann bei Task-Initialisierung spezifiziert werden
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //Erneut interrupten, da Bit beim Auffangen der Exception geflippt wird
                break;
            }
        }
    }
}