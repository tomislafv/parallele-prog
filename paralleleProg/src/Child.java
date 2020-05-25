import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

//Erstes Beispiel fuer paralelle Programmierung mit lejos hardware, Einfach gehalten
public class Child implements Runnable {

    //Staendiges Ausgeben der Sensorwerte am Display
    @Override
    public void run() {

        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1); //Sensor-Initialisierung
        colorSensor.setCurrentMode("RGB"); //Beispielhaftes verwenden des RGB-Modes

        float[] color = new float[colorSensor.sampleSize()]; //Initialisieren des Rueckgabewertes in passender Array-Laenge


        while (!Thread.currentThread().isInterrupted()) { //Dauerhaftes Auslesen und Ausgeben der Komponenten, solange Thread nicht unterbrochen wird
            colorSensor.fetchSample(color, 0);
            LCD.drawString("Red component: " + color[0], 0, 0);
            LCD.drawString("Green component: " + color[1], 0, 1);
            LCD.drawString("Blue component: " + color[2], 0, 2);

            try {
                Thread.sleep(1000); //Mit Sleep von einer Sekunde, da so performanter und angezeigte Werte aktuell genug
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
