public class Main {

    public static void main(String[] args) {
        try (Example example = new Example()) {
            example.childExample();
            example.callerExample();
            example.runnerExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
