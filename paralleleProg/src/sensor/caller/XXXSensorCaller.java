package sensor.caller;

import lejos.hardware.port.Port;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;


//Callable fuer beliebigen Sensor mit Reflection
//(-> hierdurch jedoch deutlich langsamer als die fuer konkrete Sensoren)
//(Aufbau wie in ColorSensor)
public class XXXSensorCaller implements Callable<float[]> {
    private final Object sensor;

    //Nutzt in diesem Fall ein Objekt des No-Args-Konstruktors der uebergebenen (Sensor-)Klasse
    public XXXSensorCaller(Class<?> sensorClass, Port port) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        try {
            this.sensor = sensorClass.getConstructor(Port.class).newInstance(port);
        } catch (NullPointerException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Given class can not be used as sensor!");
        }
    }

    //Nutzt in das uebergebenen Objekt der (Sensor-)Klasse
    public XXXSensorCaller(Object sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.sensor = sensor;
    }

    @Override
    public float[] call() {
        Object sensor = this.sensor;
        float[] returnValue;
        try {
            returnValue = new float[(int) sensor.getClass().getMethod("sampleSize").invoke(sensor)];
        } catch (ReflectiveOperationException | NullPointerException | SecurityException e) {
            throw new IllegalStateException("Can not access methods of sensor!");
        }
        try {
            sensor.getClass().getMethod("sampleSize", float[].class, Integer.class).invoke(sensor, returnValue, 0);
        } catch (ReflectiveOperationException | NullPointerException | SecurityException e) {
            throw new IllegalStateException("Can not access methods of sensor!");
        }
        return returnValue;
    }
}