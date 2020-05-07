package sensor.loop;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;


public class EV3TouchSensorRunner implements Runnable {

    final Port port;
    final BlockingQueue<float[]> sensorQueue;
    final long sleepMillis;

    public EV3TouchSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        this.port = port;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            EV3TouchSensor ev3TouchSensor = new EV3TouchSensor(port);
            float[] nextValue = new float[ev3TouchSensor.sampleSize()];
            ev3TouchSensor.fetchSample(nextValue, 0);
            sensorQueue.add(nextValue);
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException intExc) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}