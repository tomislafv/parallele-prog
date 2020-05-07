package sensor.caller;

import java.util.concurrent.Callable;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class EV3TouchSensorCaller implements Callable<float[]> {
    private final Port port;

    public EV3TouchSensorCaller(Port port) {
        this.port = port;
    }
    @Override
    public float[] call() {
        EV3TouchSensor ev3TouchSensor = new EV3TouchSensor(port);
        float[] returnValue = new float[ev3TouchSensor.sampleSize()];
        ev3TouchSensor.fetchSample(returnValue, 0);
        return returnValue;
    }
}
