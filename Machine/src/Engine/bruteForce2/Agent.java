package Engine.bruteForce2;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.machineutils.MachineManager;
import utils.Permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;


//Consumer
public class Agent implements Runnable {
    private BlockingQueue<CodeConfiguration> queue;
    static int idRunningIndex = 1;
    private int id = 0;
    private MachineManager machineManager;
    private DMData dMdata;
    private String currentCharConfiguration;
    Permutation permutation;
    private CandidateList candidateList;

    /*    private final String agentID;*/
    /*    private long encryptionTimeDurationInNanoSeconds;*/

    public Agent(BlockingQueue<CodeConfiguration> queue,MachineManager machineManager, DMData dMData, CandidateList candidateList) {
        this.id = idRunningIndex;
        idRunningIndex++;
        this.machineManager = machineManager; // ?
        this.dMdata = dMData;
        this.candidateList=candidateList;
        this.queue=queue;
        permutation=new Permutation(machineManager.getMachineInformation().getAvailableChars());
    }
    @Override
    public void run() {
        try {
            System.out.println("starting pull code");
            while (true) {
                CodeConfiguration codeConfiguration = queue.take();
                this.setInitialMachine(codeConfiguration);
                String encryptionOutput;
                for (int i=0;i<dMdata.getAssignmentSize();i++)
                {
                    encryptionOutput =machineManager.encryptSentence(dMdata.getEncryptedString());
                    System.out.println(machineManager.getInitialFullMachineCode());
                    //check with dictionary
 /*                   if(true)
                    {
                        candidateList.addCandidate(createCandidate(encryptionOutput));
                    }*/
                    Thread.sleep(1000);
                    increaseCodePermutation(codeConfiguration);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void increaseCodePermutation(CodeConfiguration codeConfiguration) {
        String newCodeConfiguration=permutation.increasePermutation(1,currentCharConfiguration);

        this.machineManager.setStartingIndex(newCodeConfiguration);
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