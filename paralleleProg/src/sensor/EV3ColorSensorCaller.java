package sensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.concurrent.Callable;

public class EV3ColorSensorCaller implements Callable<float[]> {

    final Port port;

    public EV3ColorSensorCaller(Port port) {
        this.port = port;
    }

    @Override
    public float[] call() {
        EV3ColorSensor ev3ColorSensor = new EV3ColorSensor(port);
        float[] returnValue = new float[ev3ColorSensor.sampleSize()];
        ev3ColorSensor.fetchSample(returnValue, 0);
        return returnValue;
    }
}
