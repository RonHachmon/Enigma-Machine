package Engine.bruteForce2;

import DTO.DMData;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.machineutils.MachineInformation;
import Engine.machineutils.MachineManager;
import utils.ObjectCloner;
import utils.Permutation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AssignmentProducer implements Runnable {
    public static boolean isDone=false;
    private final MachineInformation machineInformation;
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private CodeConfiguration codeConfiguration;
    private Permutation permutation;
    private MachineManager machineManager;
    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData, MachineManager machineManager) throws Exception {
        this.queue = codeQueue;
        this.dmData = dmData;
        this.machineManager= (MachineManager) ObjectCloner.deepCopy(machineManager);
        this.machineInformation=machineManager.getMachineInformation();
        this.setInitialConfiguration();
        this.permutation=new Permutation(machineManager.getAvailableChars());

    }

    private void setInitialConfiguration() {
        //easy set up
        String startingIndexes="";
        List<Integer> startingRotors=machineManager.getCurrentRotorsList();
        int reflectorID=machineManager.getReflector();
        for (int i =0;i<machineInformation.getAmountOfRotorsRequired();i++)
        {
            startingIndexes+=machineInformation.getAvailableChars().charAt(0);
        }
        codeConfiguration=new CodeConfiguration(startingIndexes,startingRotors,reflectorID);
    }

    @Override
    public void run() {
        try {
            generateCode();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
    private void generateCode() throws InterruptedException {
        String nextPermutation;
        System.out.println("starting push code");

        do{
            queue.offer(codeConfiguration.clone(codeConfiguration),1000, TimeUnit.MILLISECONDS);
            nextPermutation=permutation.increasePermutation(dmData.getAssignmentSize(),codeConfiguration.getCharIndexes());
            this.codeConfiguration.setCharIndexes(nextPermutation);
        }while (permutation.isOverFlow()==false);
        isDone=true;
        System.out.println("Producer Done");

    }
}
