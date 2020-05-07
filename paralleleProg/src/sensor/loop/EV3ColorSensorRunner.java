package sensor.loop;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;


public class EV3ColorSensorRunner implements Runnable {

    final Port port;
    final BlockingQueue<float[]> sensorQueue;
    final long sleepMillis;

    public EV3ColorSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        this.port = port;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            EV3ColorSensor ev3ColorSensor = new EV3ColorSensor(port);
            float[] nextValue = new float[ev3ColorSensor.sampleSize()];
            ev3ColorSensor.fetchSample(nextValue, 0);
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
