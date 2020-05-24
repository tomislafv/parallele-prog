package sensor.caller;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

import java.util.concurrent.Callable;

//Umsetzung des Lesen eines Einzelwertes mit dem Farbsensor
public class EV3ColorSensorCaller implements Callable<float[]> {

    private final EV3ColorSensor ev3ColorSensor;

    //Konstruktor initialisiert Sensor standardmaessig am angegebenen Port
    public EV3ColorSensorCaller(Port port) {
        if (port == null) {
            throw new IllegalArgumentException("Port can not be null!");
        }
        this.ev3ColorSensor = new EV3ColorSensor(port);
    }

    //Konstruktor nutzt uebergebenen Sensor, hierdurch kann der Betriebsmodus gewaehlt werden
    public EV3ColorSensorCaller(EV3ColorSensor ev3ColorSensor) {
        if (ev3ColorSensor == null) {
            throw new IllegalArgumentException("Sensor can not be null!");
        }
        this.ev3ColorSensor = ev3ColorSensor;
    }

    //Paraleller Callable-Task fuehrt das einmalige Auslesen durch
    @Override
    public float[] call() {
        EV3ColorSensor ev3ColorSensor = this.ev3ColorSensor;
        float[] returnValue = new float[ev3ColorSensor.sampleSize()];
        ev3ColorSensor.fetchSample(returnValue, 0);
        return returnValue;
    }
}