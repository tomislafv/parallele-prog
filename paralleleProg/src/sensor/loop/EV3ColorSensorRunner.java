package sensor.loop;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

import java.util.concurrent.BlockingQueue;


public class EV3ColorSensorRunner implements Runnable {

    private final EV3ColorSensor ev3ColorSensor;
    private final BlockingQueue<float[]> sensorQueue;
    private final long sleepMillis;

    public EV3ColorSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.ev3ColorSensor = new EV3ColorSensor(port);
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    public EV3ColorSensorRunner(EV3ColorSensor ev3ColorSensor, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (ev3ColorSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.ev3ColorSensor = ev3ColorSensor;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        EV3ColorSensor ev3ColorSensor = this.ev3ColorSensor;
        while (!Thread.currentThread().isInterrupted()) {
            float[] nextValue = new float[ev3ColorSensor.sampleSize()];
            ev3ColorSensor.fetchSample(nextValue, 0);
            try {
                sensorQueue.put(nextValue);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}