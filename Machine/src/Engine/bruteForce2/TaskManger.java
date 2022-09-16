package Engine.bruteForce2;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.BruteForce.DifficultyLevel;
import Engine.machineutils.MachineManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TaskManger {

    private static final int MAX_QUEUE_SIZE = 1000;
    private MachineManager machineManager;
    private DifficultyLevel difficultyLevel;
    private int amountOfAgents;
    private DMData dmData;
    private ExecutorService candidatesPool;
    private Consumer<Runnable> onCancel;
    private List<DecryptionCandidate> decryptionCandidates=new ArrayList<>();

    //
    private List<MachineManager> agentMachines=new ArrayList<>();

    public TaskManger(DMData dMData, MachineManager machineManager, Consumer<Runnable> onCancel) throws Exception {
        this.machineManager = machineManager;
        this.onCancel = onCancel;
        this.difficultyLevel = dMData.getDifficulty();
        this.amountOfAgents=dMData.getAmountOfAgents();
        dmData = dMData;
        this.candidatesPool = Executors.newFixedThreadPool(dmData.getAmountOfAgents());
        this.setAgentMachine();
    }

    private void setAgentMachine() {
        for (int i = 0; i <amountOfAgents ; i++) {
            agentMachines.add(machineManager.cloneMachine());

        }
    }

    public void run(){

        MachineManager machineManger;
        //loop with all possible code configuration
        candidatesPool.execute(()->{
                this.decrypt();

        });

    }
    synchronized void addCandidateToList(String foundWord,String codeSetting)
    {
        DecryptionCandidate decryptionCandidate = new DecryptionCandidate(parseThreadToId(), foundWord, codeSetting);
        decryptionCandidates.add(decryptionCandidate);
    }
    synchronized int parseThreadToId()
    {
        return Integer.parseInt(Thread.currentThread().getName());
    }
     synchronized MachineManager getMachine()
    {
        return agentMachines.get(parseThreadToId()-1);
    }

    private void decrypt() {
        MachineManager currentAgentMachine=getMachine();
        //loop over code
        currentAgentMachine.encryptSentence(dmData.getEncryptedString());
        //Dictinary
        //add to list addCandidateToList()

    }

}