package sensor.loop;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class EV3UltrasonicSensorRunner implements Runnable {

    final Port port;
    final BlockingQueue<float[]> sensorQueue;
    final long sleepMillis;

    public EV3UltrasonicSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        this.port = port;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            EV3UltrasonicSensor ev3UltrasonicSensor = new EV3UltrasonicSensor(port);
            float[] nextValue = new float[ev3UltrasonicSensor.sampleSize()];
            ev3UltrasonicSensor.getDistanceMode().fetchSample(nextValue, 0);
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
