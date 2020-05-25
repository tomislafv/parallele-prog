package sensor.loop;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

import java.util.concurrent.BlockingQueue;

//Umsetzung des dauerhaften Lesens mit dem Touchsensor
//(Aufbau wie in ColorSensor)
public class EV3TouchSensorRunner implements Runnable {

    private final EV3TouchSensor ev3TouchSensor;
    private final BlockingQueue<float[]> sensorQueue;
    private final long sleepMillis;

    public EV3TouchSensorRunner(Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.ev3TouchSensor = new EV3TouchSensor(port);
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    public EV3TouchSensorRunner(EV3TouchSensor ev3TouchSensor, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (ev3TouchSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.ev3TouchSensor = ev3TouchSensor;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        EV3TouchSensor ev3TouchSensor = this.ev3TouchSensor;
        while (!Thread.currentThread().isInterrupted()) {
            float[] nextValue = new float[ev3TouchSensor.sampleSize()];
            ev3TouchSensor.fetchSample(nextValue, 0);
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