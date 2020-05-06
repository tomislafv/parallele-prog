import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class Child implements Runnable {

    @Override
    public void run() {

        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
        colorSensor.setCurrentMode("RGB");

        float[] color = new float[colorSensor.sampleSize()];


        while (!Thread.currentThread().isInterrupted()) {
            colorSensor.fetchSample(color, 0);
            LCD.drawString("Red component: " + Float.toString(color[0]), 0, 0);
            LCD.drawString("Green component: " + Float.toString(color[1]), 0, 1);
            LCD.drawString("Blue component: " + Float.toString(color[2]), 0, 2);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException intExc) {
                Thread.currentThread().interrupt();
                break;
            }

        }

    }
}
