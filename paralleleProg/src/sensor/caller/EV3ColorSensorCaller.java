package sensor.caller;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

import java.util.concurrent.Callable;

public class EV3ColorSensorCaller implements Callable<float[]> {

    private final Port port;
    private final EV3ColorSensor ev3ColorSensor;

    public EV3ColorSensorCaller(Port port) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.port = port;
        this.ev3ColorSensor = null;
    }

    public EV3ColorSensorCaller(EV3ColorSensor ev3ColorSensor) {
        if (ev3ColorSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.port = null;
        this.ev3ColorSensor = ev3ColorSensor;
    }

    @Override
    public float[] call() {
        EV3ColorSensor ev3ColorSensor = this.ev3ColorSensor;
        if (ev3ColorSensor == null) {
            ev3ColorSensor = new EV3ColorSensor(port);
        }
        float[] returnValue = new float[ev3ColorSensor.sampleSize()];
        ev3ColorSensor.fetchSample(returnValue, 0);
        return returnValue;
    }
}