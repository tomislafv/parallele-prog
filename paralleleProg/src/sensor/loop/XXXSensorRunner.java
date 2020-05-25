package sensor.loop;

import lejos.hardware.port.Port;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

//Runnable fuer beliebigen Sensor mit Reflection
//(-> hierdurch jedoch deutlich langsamer als die fuer konkrete Sensoren)
//(Aufbau wie in ColorSensor)
public class XXXSensorRunner implements Runnable {
    private final Object sensor;
    private final BlockingQueue<float[]> sensorQueue;
    private final long sleepMillis;

    //Nutzt in diesem Fall ein Objekt des No-Args-Konstruktors der uebergebenen (Sensor-)Klasse
    public XXXSensorRunner(Class<?> sensorClass, Port port, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        try {
            this.sensor = sensorClass.getConstructor(Port.class).newInstance(port);
        } catch (NullPointerException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Given class can not be used as sensor!");
        }
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    //Nutzt in das uebergebenen Objekt der (Sensor-)Klasse
    public XXXSensorRunner(Object sensor, BlockingQueue<float[]> sensorQueue, long sleepMillis) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.sensor = sensor;
        this.sensorQueue = sensorQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        Object sensor = this.sensor;
        while (!Thread.currentThread().isInterrupted()) {
            float[] nextValue;
            try {
                nextValue = new float[(int) sensor.getClass().getMethod("sampleSize").invoke(sensor)];
            } catch (ReflectiveOperationException | NullPointerException | SecurityException e) {
                throw new IllegalStateException("Can not access methods of sensor!");
            }
            try {
                sensor.getClass().getMethod("sampleSize", float[].class, Integer.class).invoke(sensor, nextValue, 0);
            } catch (ReflectiveOperationException | NullPointerException | SecurityException e) {
                throw new IllegalStateException("Can not access methods of sensor!");
            }
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