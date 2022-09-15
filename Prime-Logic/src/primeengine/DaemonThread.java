package primeengine;

import java.util.concurrent.ThreadFactory;

public class DaemonThread implements ThreadFactory {
    @Override
    public Agent newThread(Runnable r) {
        Agent thread = new Agent(r);
        thread.setDaemon(true);
        return thread;
    }
}
