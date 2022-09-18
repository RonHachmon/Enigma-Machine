package app.utils;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.bruteForce2.DecryptManager;
import Engine.machineutils.MachineManager;
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
    private final BruteForceController controller;

    private int foundNumbers=0;
    private PrimeFinder primeFinder =new PrimeFinder(20);
    private int lastKnownIndex=0;
    private ScheduledExecutorService timedExecute;
    private DecryptManager decryptManager;
    ScheduledFuture<?> scheduledFuture;
    private long totalWorkSize = 0;
    //private long totalWorkDuration;


    /*   private bruteForce;*/

    public FindCandidateTask(DMData dmData, UIAdapter uiAdapter, BruteForceController bruteForceController, MachineManager machineManager) {
        this.uiAdapter = uiAdapter;
        this.controller=bruteForceController;
        controller.bindTaskToUIComponents(this);
        DaemonThread daemonThreadFactory = new DaemonThread();
        timedExecute = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);

        decryptManager =new DecryptManager(machineManager,dmData);

    }
    
    
    @Override
    protected Boolean call() throws Exception {
        decryptManager.startDeciphering();
        totalWorkSize = decryptManager.getTotalTaskSize();
        //totalWorkDuration = decryptManager.getTotalTaskDurationInNanoSeconds();
        updateProgress(0, totalWorkSize);
        startTimedTask();
/*        System.out.println("test");


        primeFinder.findPrimes(this);*/


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
        scheduledFuture = timedExecute.scheduleAtFixedRate(() -> update(), 500, 500, TimeUnit.MILLISECONDS);
    }
    private void update() {
        if (decryptManager.getSizeOfCandidateList() > lastKnownIndex) {
            List<DecryptionCandidate> decryptionCandidates = decryptManager.getCandidateList().getList();
            for (int i = lastKnownIndex; i < decryptionCandidates.size(); i++) {
                uiAdapter.addNewCandidate(decryptionCandidates.get(i));
            }
            lastKnownIndex = decryptionCandidates.size();
            uiAdapter.updateTotalFoundWords(lastKnownIndex);

        }
        /*System.out.println("total work done "+ decryptManager.getWorkDone());*/
        updateProgress(decryptManager.getWorkDone(), totalWorkSize);
        if (decryptManager.getWorkDone() >= totalWorkSize) {
            pause();
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


}
