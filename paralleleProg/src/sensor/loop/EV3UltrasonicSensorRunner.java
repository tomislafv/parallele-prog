package sensor.loop;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.concurrent.BlockingQueue;


public class EV3UltrasonicSensorRunner implements Runnable {

    private final Port port;
    private final EV3UltrasonicSensor ev3UltrasonicSensor;
    private final BlockingQueue<float[]> sensorQueue;
    private final long sleepMillis;

    public EV3UltrasonicSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        this.port = port;
        this.ev3UltrasonicSensor = null;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    public EV3UltrasonicSensorRunner(EV3UltrasonicSensor ev3UltrasonicSensor, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        this.port = null;
        this.ev3UltrasonicSensor = ev3UltrasonicSensor;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        EV3UltrasonicSensor ev3UltrasonicSensor = this.ev3UltrasonicSensor;
        if (ev3UltrasonicSensor == null) {
            ev3UltrasonicSensor = new EV3UltrasonicSensor(port);
        }
        while (!Thread.currentThread().isInterrupted()) {
            float[] nextValue = new float[ev3UltrasonicSensor.sampleSize()];
            ev3UltrasonicSensor.getDistanceMode().fetchSample(nextValue, 0);
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