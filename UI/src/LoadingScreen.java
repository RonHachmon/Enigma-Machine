public class LoadingScreen implements Runnable {
    private  boolean exit = false;
    private String message;
    private Thread thread;
    public LoadingScreen(String message)
    {
        this.message=message;
        thread = new Thread(this, "loading screen");

    }
    @Override
    public void run() {
        System.out.print(message);
        String dots = ".";
        while (!exit) {
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void stop()
    {
        exit = true;
    }
    public void start()
    {
        thread.start();
    }
}
