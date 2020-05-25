package sensor.caller;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.concurrent.Callable;

//Umsetzung des Lesen eines Einzelwertes mit dem Ultraschallsensor
//(Aufbau wie in ColorSensor)
public class EV3UltrasonicSensorCaller implements Callable<float[]> {

    private final EV3UltrasonicSensor ev3UltrasonicSensor;

    public EV3UltrasonicSensorCaller(Port port) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.ev3UltrasonicSensor = new EV3UltrasonicSensor(port);
    }

    public EV3UltrasonicSensorCaller(EV3UltrasonicSensor ev3UltrasonicSensor) {
        if (ev3UltrasonicSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.ev3UltrasonicSensor = ev3UltrasonicSensor;
    }

    @Override
    public float[] call() {
        EV3UltrasonicSensor ev3UltrasonicSensor = this.ev3UltrasonicSensor;
        float[] returnValue = new float[ev3UltrasonicSensor.sampleSize()];
        ev3UltrasonicSensor.getDistanceMode().fetchSample(returnValue, 0);
        return returnValue;
    }
}