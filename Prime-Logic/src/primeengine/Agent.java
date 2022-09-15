package primeengine;

public class Agent extends Thread{
    static int idRunningIndex=0;
    int id;
    Runnable runnable;
    public Agent(Runnable runnable)
    {

        this.id=idRunningIndex;
        idRunningIndex++;
        System.out.println("Agent"+ idRunningIndex);
        this.runnable=runnable;
    }

    @Override
    public void run() {
        System.out.println();
        runnable.run();
    }
}
