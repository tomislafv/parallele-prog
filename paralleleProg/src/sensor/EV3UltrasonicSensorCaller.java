package sensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.concurrent.Callable;

public class EV3UltrasonicSensorCaller implements Callable<float[]> {

    final Port port;

    public EV3UltrasonicSensorCaller(Port port) {
        this.port = port;
    }

    @Override
    public float[] call() {
        EV3UltrasonicSensor ev3UltrasonicSensor = new EV3UltrasonicSensor(port);
        float[] returnValue = new float[ev3UltrasonicSensor.sampleSize()];
        ev3UltrasonicSensor.getDistanceMode().fetchSample(returnValue, 0);
        return returnValue;
    }
}
