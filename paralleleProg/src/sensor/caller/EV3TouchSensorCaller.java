package sensor.caller;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

import java.util.concurrent.Callable;

//Umsetzung des Lesen eines Einzelwertes mit dem Touchsensor (Aufbau wie in ColorSensor)
public class EV3TouchSensorCaller implements Callable<float[]> {

    private final EV3TouchSensor ev3TouchSensor;

    public EV3TouchSensorCaller(Port port) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.ev3TouchSensor = new EV3TouchSensor(port);
    }

    public EV3TouchSensorCaller(EV3TouchSensor ev3TouchSensor) {
        if (ev3TouchSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.ev3TouchSensor = ev3TouchSensor;
    }

    @Override
    public float[] call() {
        EV3TouchSensor ev3TouchSensor = this.ev3TouchSensor;
        float[] returnValue = new float[ev3TouchSensor.sampleSize()];
        ev3TouchSensor.fetchSample(returnValue, 0);
        return returnValue;
    }
}