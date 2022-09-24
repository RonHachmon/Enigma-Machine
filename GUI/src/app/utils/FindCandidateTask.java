package app.utils;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.bruteForce2.DecryptManager;
import Engine.bruteForce2.TaskManger;
import Engine.machineutils.MachineManager;
import app.bodies.BruteForceController;
import app.utils.threads.DaemonThread;
import com.sun.javafx.binding.StringFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import utils.Utils;


import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FindCandidateTask extends Task<Boolean> {
    private final UIAdapter uiAdapter;
    private final BruteForceController controller;
    private DecryptManager decryptManager;
    private int lastKnownIndex = 0;
    private ScheduledExecutorService timedExecute;

    ScheduledFuture<?> scheduledFuture;
    private SimpleStringProperty totalWork=new SimpleStringProperty();

    private long totalWorkSize = 0;

    private long totalTime=0;
    private long avgTime=0;

    public FindCandidateTask(DMData dmData, UIAdapter uiAdapter, BruteForceController bruteForceController, MachineManager machineManager) {
        this.uiAdapter = uiAdapter;
        this.controller = bruteForceController;
        controller.bindTaskToUIComponents(this);
        DaemonThread daemonThreadFactory = new DaemonThread();
        timedExecute = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);
        decryptManager =new DecryptManager(machineManager,dmData);

    }


    @Override
    protected Boolean call() throws Exception {
        TaskManger.resetStaticMembers();
        updateMessage("starting to work");
        decryptManager.startDeciphering();




        totalWorkSize = decryptManager.getTotalTaskSize();
        updateProgress(0, totalWorkSize);
        uiAdapter.updateProgress("permutation: "+0+"/"+Utils.formatToIntWithCommas(totalWorkSize));
        startTimedTask();

        return null;

    }

    private void startTimedTask() {
        scheduledFuture = timedExecute.scheduleAtFixedRate(() -> update(), 100, 100, TimeUnit.MILLISECONDS);
    }


    public void pause() {

        scheduledFuture.cancel(true);
        decryptManager.pause();
    }

    public void resume() {
        decryptManager.resume();
        startTimedTask();
    }
    public void stop() {
        setTime();
        updateMessage("Cancelled ;/");
        this.cancelled();
    }

    @Override
    protected void cancelled() {
        timedExecute.shutdownNow();
        decryptManager.stop();
        super.cancelled();
        this.cancel();
        this.done();
    }

    private void update() {
        long workDone = decryptManager.getWorkDone();

        List<DecryptionCandidate> decryptionCandidates = decryptManager.getCandidateList().getList();
        if (decryptionCandidates.size() > lastKnownIndex) {
            for (int i = lastKnownIndex; i < decryptionCandidates.size(); i++) {
                uiAdapter.addNewCandidate(decryptionCandidates.get(i));
            }
            lastKnownIndex = decryptionCandidates.size();
            uiAdapter.updateTotalFoundWords(lastKnownIndex);
        }


        updateProgress(workDone,totalWorkSize);
        uiAdapter.updateProgress("permutation: "+Utils.formatToIntWithCommas(workDone)+"/"+Utils.formatToIntWithCommas(totalWorkSize));

        checkIfDone(workDone);
    }

    private void checkIfDone(long workDone) {
        if(workDone >=totalWorkSize)
        {
            setTime();
            updateMessage("Done work");
            uiAdapter.done();
            this.cancelled();
        }
    }


    private void setTime() {
        this.totalTime= decryptManager.getTotalTaskDurationInNanoSeconds();
        this.avgTime=decryptManager.getAvgTaskDuration();
    }

    public long getAvgTime () {
        if(avgTime==0)
        {
            avgTime=decryptManager.getAvgTaskDuration();
        }
        return avgTime;
    }


    public void reset() {
        uiAdapter.updateProgress("");
        updateProgress(0,100);
    }
}
