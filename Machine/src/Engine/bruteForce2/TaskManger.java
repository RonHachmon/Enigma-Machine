package Engine.bruteForce2;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.BruteForce.DifficultyLevel;
import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.machineutils.MachineManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TaskManger {

    private static final int MAX_QUEUE_SIZE = 1000;
    private MachineManager machineManager;
    private DMData dmData;

    private Consumer<Runnable> onCancel;
    private CandidateList candidateList=new CandidateList();
    BlockingQueue<CodeConfiguration> blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);


    //
    private List<MachineManager> agentMachines=new ArrayList<>();

    public TaskManger(DMData dMData, MachineManager machineManager, Consumer<Runnable> onCancel) throws Exception {
        this.machineManager = machineManager;
        this.onCancel = onCancel;
        dmData = dMData;
    }


    private void setAgentMachine() throws CloneNotSupportedException {
    }


    public void start()
    {
        AssignmentProducer assignmentProducer = null;
        try {
            assignmentProducer = new AssignmentProducer(blockingQueue,dmData,machineManager.clone());
            for (int i = 0; i <dmData.getAmountOfAgents() ; i++) {
                Agent agent=new Agent(blockingQueue,machineManager.clone(),dmData,candidateList);
                new Thread(agent).start();
            }
            new Thread(assignmentProducer).start();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

/*    private void setAgentMachine() {
>>>>>>> 757172a (basic consumer producer)
        for (int i = 0; i <amountOfAgents ; i++) {
            agentMachines.add(machineManager.clone());
        }
    }*/

/*    public void run(){

        MachineManager machineManger;
        //loop with all possible code configuration
        candidatesPool.execute(()->{
                this.decrypt();

        });

    }*/
/*    synchronized void addCandidateToList(String foundWord,String codeSetting)
    {
        DecryptionCandidate decryptionCandidate = new DecryptionCandidate(parseThreadToId(), foundWord, codeSetting);
        decryptionCandidates.add(decryptionCandidate);
    }*/
/*    synchronized int parseThreadToId()
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

    }*/

}