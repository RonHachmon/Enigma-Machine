package app.utils;

import app.bodies.BruteForceController;
import app.utils.threads.DaemonThread;
import javafx.concurrent.Task;
import primeengine.PrimeFinder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FindCandidateTask extends Task<Boolean> {


    private final UIAdapter uiAdapter;
    private final Long totalNumbers;
    private final BruteForceController controller;

    private int foundNumbers=0;
    private PrimeFinder primeFinder =new PrimeFinder(20);
    private int lastKnownIndex=0;
    private ScheduledExecutorService timedExecute;
    ScheduledFuture<?> scheduledFuture;


 /*   private bruteForce;*/

    public FindCandidateTask(long totalNumbers, UIAdapter uiAdapter, BruteForceController bruteForceController) {
        this.uiAdapter = uiAdapter;
        this.totalNumbers = totalNumbers;
        this.controller=bruteForceController;
        controller.bindTaskToUIComponents(this);
        DaemonThread daemonThreadFactory = new DaemonThread();
        timedExecute = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);

    }
    
    
    @Override
    protected Boolean call() throws Exception {
        startTimedTask();
        System.out.println("test");
        updateProgress(0,20);

        primeFinder.findPrimes(this);


/*        System.out.println("starting task");
        Integer runningNumber=10000;

        while (foundNumbers!=20)
        {
            if(isPrime(runningNumber))
            {
                System.out.println("found number");
                foundNumbers++;
                updateProgress(foundNumbers,20);
                uiAdapter.addNewCandidate(new PrimeNumberData(runningNumber));
                Thread.sleep(1000);
            }

            runningNumber++;
        }*/


        return null;

    }

    private void startTimedTask() {
        scheduledFuture = timedExecute.scheduleAtFixedRate(() -> update(), 500, 1000, TimeUnit.MILLISECONDS);
    }

    private void update()
    {
        if (primeFinder.listSize()>lastKnownIndex)
        {
            System.out.println("in update");
            List<Integer> primeNumberList = primeFinder.getPrimeNumbers();
            System.out.println("last know "+lastKnownIndex);
            System.out.println("amount "+primeNumberList.size());
            for (int i = lastKnownIndex; i <primeNumberList.size() ; i++) {
                uiAdapter.addNewCandidate(new PrimeNumberData(primeNumberList.get(i)));
            }
            lastKnownIndex=primeNumberList.size();
            updateProgress(lastKnownIndex,20);
        }

    }

    public void pause()
    {
        scheduledFuture.cancel(true);
    }

    public void resume()
    {
        startTimedTask();
    }


    private boolean isPrime(Integer runningNumber) {
        for (int i=2;i<=Math.sqrt(runningNumber);i++)
        {
            if(runningNumber%i==0)
            {
                return false;
            }
        }
        return true;
    }
}
