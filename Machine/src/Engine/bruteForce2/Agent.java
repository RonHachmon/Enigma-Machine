package Engine.bruteForce2;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.bruteForce2.utils.Dictionary;
import Engine.bruteForce2.utils.QueueLock;
import Engine.machineutils.MachineManager;
import utils.ObjectCloner;
import utils.Permutation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


//Consumer
public class Agent implements Runnable {
    private final QueueLock lock;
    private BlockingQueue<CodeConfiguration> queue;

    private int id = 0;
    private MachineManager machineManager;
    private DMData dMdata;
    private String currentCharConfiguration;
    private Permutation permutation;


    private CandidateList candidateList;
    private Dictionary dictionary;

    private int amountOfPermutaionIWent=0;
    private boolean toStop=false;

    public Agent(BlockingQueue<CodeConfiguration> queue, MachineManager machineManager, DMData dMData, CandidateList candidateList, Dictionary dictionary, int ID, QueueLock queueLock) throws Exception {
        this.id = ID+1;
        this.machineManager = (MachineManager) ObjectCloner.deepCopy(machineManager);
        this.dMdata = dMData;
        this.candidateList=candidateList;
        this.queue=queue;
        permutation=new Permutation(machineManager.getMachineInformation().getAvailableChars());
        this.dictionary =dictionary;
        this.lock=queueLock;
    }
    @Override
    public void run() {
        try {
            /*System.out.println("starting pull code");*/
            while (true) {
                CodeConfiguration codeConfiguration = queue.poll(3000, TimeUnit.MILLISECONDS);
                if (codeConfiguration != null) {
                    this.setInitialMachine(codeConfiguration);
                    int amountOfPermutationIwentInTheLoop=0;
                    String encryptionOutput;
                    for (int i = 0; i < dMdata.getAssignmentSize() ; i++) {
                        lock.checkIfLocked();
                        if(toStop)
                        {
                            System.out.println("Agent stopped :)");
                            return;
                        }
                       /* System.out.println("Agent working");*/
                        /*System.out.println(machineManager.getInitialFullMachineCode());*/

                        encryptionOutput = machineManager.encryptSentence(dMdata.getEncryptedString());

                        amountOfPermutaionIWent++;
                        amountOfPermutationIwentInTheLoop++;

                        if(dictionary.isAtDictionary(encryptionOutput))
                        {
                            candidateList.addCandidate(createCandidate(encryptionOutput));
                        }

                        increaseCodePermutation(codeConfiguration);
                        if(permutation.isOverFlow())
                        {
                            break;
                        }
                    }

                    /*System.out.println("amount i went in the loop ");*/
                    permutation.cleanOverFLow();
                    TaskManger.addWorkDone(amountOfPermutationIwentInTheLoop);
                    if(this.isDone())
                    {
                        return;
                    }
                }
                else
                {
                    if (isDone()) {
                        return;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Agent got exception");
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private boolean isDone() {
        if(TaskManger.getWorkDone()>=TaskManger.getTotalWork())
        {
            System.out.println("Agent "+getAgentId()+" done");
            System.out.println("amount i went "+amountOfPermutaionIWent);
            return true;
        }
        return false;
    }

    private void increaseCodePermutation(CodeConfiguration codeConfiguration) {
        currentCharConfiguration=permutation.increasePermutation(1,currentCharConfiguration);
        this.machineManager.setStartingIndex(currentCharConfiguration);
    }

    private DecryptionCandidate createCandidate(String encryptionOutput) {
        return new DecryptionCandidate(this.getAgentId(), encryptionOutput,machineManager.getInitialFullMachineCode());
    }

    private void setInitialMachine(CodeConfiguration codeConfiguration)
    {

        this.machineManager.setSelectedRotors(codeConfiguration.getRotorsID());
        this.machineManager.setStartingIndex(codeConfiguration.getCharIndexes());
        this.machineManager.setSelectedReflector(codeConfiguration.getReflectorID());
        currentCharConfiguration=codeConfiguration.getCharIndexes();

    }
    public int getAgentId() {
        return id;
    }
    public void stop() {
        this.toStop=true;
    }



/*    @Override
>>>>>>> 757172a (basic consumer producer)
    public void run() {
        Instant startTaskTime = Instant.now();
        List<Integer> currentRotorPositions = machineManager.getCurrentRotorsList();

        for (int i = 0; i < dMdata.getAssignmentSize(); i++) {
            try {
                Instant startingTime = Instant.now();
                String candidateMessage = machineManager.encryptSentence(dMdata.getEncryptedString().toUpperCase());
                encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }*/



}