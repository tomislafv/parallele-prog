public class Main {

    //Main fuehrt einzelne Examples aus
    public static void main(String[] args) throws InterruptedException {
        try (Example example = new Example()) { //Nutzen im Try-With-Ressources-Statement um Resourcen (Threadpool) automatisiert freizugeben
            example.childExample();
            example.callerExample();
            example.runnerExample();
        }
    }
}
