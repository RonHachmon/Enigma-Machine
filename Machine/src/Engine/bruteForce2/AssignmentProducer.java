package Engine.bruteForce2;

import DTO.DMData;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.machineutils.MachineManager;
import utils.Permutation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AssignmentProducer implements Runnable {
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private CodeConfiguration codeConfiguration;
    private Permutation permutation;
    private MachineManager machineManager;
    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData, MachineManager machineManager) {
        this.queue = codeQueue;
        this.dmData = dmData;
        this.setInitialConfiguration();
        this.machineManager=machineManager;
        this.permutation=new Permutation(machineManager.getAvailableChars());

    }

    private void setInitialConfiguration() {
        //easy set up
        String startingIndexes="";
        List<Integer> startingRotors=machineManager.getCurrentRotorsList();
        int reflectorID=machineManager.getReflector();
        for (int i =0;i<machineManager.getMachineInformation().getAmountOfRotorsRequired();i++)
        {
            startingIndexes+=machineManager.getAvailableChars().charAt(0);
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

            queue.offer(this.codeConfiguration,1000, TimeUnit.MILLISECONDS);
            nextPermutation=permutation.increasePermutation(dmData.getAssignmentSize()+1,codeConfiguration.getCharIndexes());
            this.codeConfiguration.setCharIndexes(nextPermutation);
        }while (permutation.getOverFlow()==false);

    }
}
