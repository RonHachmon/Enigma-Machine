package Engine.bruteForce2;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.bruteForce2.utils.Dictionary;
import Engine.machineutils.MachineManager;
import utils.ObjectCloner;
import utils.Permutation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


//Consumer
public class Agent implements Runnable {
    private BlockingQueue<CodeConfiguration> queue;
    static int idRunningIndex = 1;
    private int id = 0;
    private MachineManager machineManager;
    private DMData dMdata;
    private String currentCharConfiguration;
    private Permutation permutation;


    private CandidateList candidateList;
    private Dictionary dictionary;

    public Agent(BlockingQueue<CodeConfiguration> queue, MachineManager machineManager, DMData dMData, CandidateList candidateList, Dictionary dictionary) throws Exception {
        this.id = idRunningIndex;
        idRunningIndex++;
        this.machineManager = (MachineManager) ObjectCloner.deepCopy(machineManager); // ?
        this.dMdata = dMData;
        this.candidateList=candidateList;
        this.queue=queue;
        permutation=new Permutation(machineManager.getMachineInformation().getAvailableChars());
        this.dictionary =dictionary;
    }
    @Override
    public void run() {
        try {
            System.out.println("starting pull code");
            while (true) {
                CodeConfiguration codeConfiguration = queue.poll(10000, TimeUnit.MILLISECONDS);
                if (codeConfiguration != null) {

                    this.setInitialMachine(codeConfiguration);
                    String encryptionOutput;
                    for (int i = 0; i < dMdata.getAssignmentSize() - 1; i++) {
                        encryptionOutput = machineManager.encryptSentence(dMdata.getEncryptedString());
                        if(dictionary.isAtDictionary(encryptionOutput))
                        {
                            candidateList.addCandidate(createCandidate(encryptionOutput));
                        }

                        increaseCodePermutation(codeConfiguration);
                    }
                    TaskManger.addWorkDone(dMdata.getAssignmentSize());
                }
                else
                {
                    if(AssignmentProducer.isDone)
                    {
                        System.out.println("Agent "+getAgentId()+" done");
                        return;
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void increaseCodePermutation(CodeConfiguration codeConfiguration) {
        /*System.out.println("before pemutation"+this.machineManager.getInitialFullMachineCode());*/
         currentCharConfiguration=permutation.increasePermutation(1,currentCharConfiguration);
       /* System.out.println(currentCharConfiguration);*/

        this.machineManager.setStartingIndex(currentCharConfiguration);
      /*  System.out.println(this.machineManager.getInitialFullMachineCode());*/
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