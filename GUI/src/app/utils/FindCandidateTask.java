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
    private int lastKnownIndex=0;
    private ScheduledExecutorService timedExecute;
    private DecryptManager decryptManager;
    ScheduledFuture<?> scheduledFuture;
    private long totalWork=0;


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
        updateMessage("starting to work");
        decryptManager.startDeciphering();
        totalWork=decryptManager.getTotalTaskSize();
        updateProgress(0,totalWork);
        startTimedTask();

        return null;

    }

    private void startTimedTask() {
        scheduledFuture = timedExecute.scheduleAtFixedRate(() -> update(), 500, 500, TimeUnit.MILLISECONDS);
    }




    public void pause()
    {
        scheduledFuture.cancel(true);
        decryptManager.pause();
    }

    public void resume()
    {
        decryptManager.resume();
        startTimedTask();
    }
    @Override
    protected void cancelled() {
        timedExecute.shutdownNow();
        decryptManager.stop();
        super.cancelled();
        this.cancel();
        this.done();
    }
    private void update()
    {

        long workDone = decryptManager.getWorkDone();
        System.out.println("work done: "+workDone);
        List<DecryptionCandidate> decryptionCandidates = decryptManager.getCandidateList().getList();
        if (decryptionCandidates.size()>lastKnownIndex)
        {
            for (int i = lastKnownIndex; i <decryptionCandidates.size() ; i++) {
                uiAdapter.addNewCandidate(decryptionCandidates.get(i));
            }
            lastKnownIndex=decryptionCandidates.size();
            uiAdapter.updateTotalFoundWords(lastKnownIndex);
        }
        /*System.out.println("total work done "+ decryptManager.getWorkDone());*/
        updateProgress(workDone,totalWork);
        if(workDone>=totalWork)
        {
            updateMessage("Done work");
            uiAdapter.done();
            this.cancelled();
        }

    }


    public void stop() {
        System.out.println("stopped");
        updateMessage("Cancelled ;/");
        this.cancelled();

    }

}
