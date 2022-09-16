package Engine.bruteForce2;

import DTO.DMData;
import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.bruteForce2.utils.Dictionary;
import Engine.machineutils.MachineManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TaskManger {

    private static final int MAX_QUEUE_SIZE = 1000;
    private final Dictionary dictionary;
    private MachineManager machineManager;
    private DMData dmData;

    private Consumer<Runnable> onCancel;
    private CandidateList candidateList=new CandidateList();
    BlockingQueue<CodeConfiguration> blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);


    //
    private List<MachineManager> agentMachines=new ArrayList<>();

    public CandidateList getCandidateList() {
        return candidateList;
    }

    public TaskManger(Dictionary dictionary, DMData dMData, MachineManager machineManager, Consumer<Runnable> onCancel) throws Exception {
        this.machineManager = machineManager;
        this.onCancel = onCancel;
        dmData = dMData;
        this.dictionary=dictionary;
    }


    private void setAgentMachine() throws CloneNotSupportedException {
    }


    public void start()
    {
        AssignmentProducer assignmentProducer = null;
        System.out.println("starting task");
        try {
            assignmentProducer = new AssignmentProducer(blockingQueue,dmData,machineManager);
            new Thread(assignmentProducer).start();
            for (int i = 0; i <dmData.getAmountOfAgents() ; i++) {
                Agent agent=new Agent(blockingQueue,machineManager,dmData,candidateList,dictionary);
                new Thread(agent).start();
            }
            System.out.println("producer should have start ");
        } catch (Exception e) {
            System.out.println("execption");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
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