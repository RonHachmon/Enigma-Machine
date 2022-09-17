package Engine.bruteForce2;

import DTO.DMData;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.machineutils.MachineInformation;
import Engine.machineutils.MachineManager;
import utils.ListPermutation;
import utils.ObjectCloner;
import utils.Permutation;

import java.util.ArrayList;
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
    private ListPermutation listPermutation;
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
            chooseDifficulty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private void chooseDifficulty() throws InterruptedException {
    /*    System.out.println("starting push code");*/
        switch (dmData.getDifficulty()) {
            case EASY:
                this.generateCode();
                break;
            case MEDIUM:
                this.goOverAllReflector();
                break;
            case HARD:
                this.goOverAllKnownRotor();
                break;
            case IMPOSSIBLE:
                this.goOverAllKRotor();
                break;
        }
        isDone=true;
        System.out.println("Producer Done");
    }
    private void generateCode() throws InterruptedException {
        String nextPermutation;
        do{
            queue.offer(codeConfiguration.clone(codeConfiguration),10000, TimeUnit.MILLISECONDS);
            nextPermutation=permutation.increasePermutation(dmData.getAssignmentSize(),codeConfiguration.getCharIndexes());
            this.codeConfiguration.setCharIndexes(nextPermutation);
        }while (permutation.isOverFlow()==false);
        permutation.cleanOverFLow();
    }
    private void goOverAllReflector() throws InterruptedException {
        for (int i = 0; i <machineInformation.getAvailableReflectors() ; i++) {
            codeConfiguration.setReflectorID(i);
            this.generateCode();
        }

    }

    private void goOverAllKnownRotor() throws InterruptedException {
        this.listPermutation=new ListPermutation(codeConfiguration.getRotorsID(),codeConfiguration.getRotorsID());
        List<Integer> currentIDs;
        while (!listPermutation.done())
        {
            currentIDs=listPermutation.increasePermutation();
            System.out.println();
            currentIDs.forEach(integer -> System.out.print(integer));
            System.out.println();
            codeConfiguration.setRotorsID(currentIDs);
            goOverAllReflector();
        }
    }
    private void goOverAllKRotor() throws InterruptedException {
        List<Integer> allRotors=new ArrayList<>();
        for (int i = 0; i <machineInformation.getAmountOfRotors() ; i++) {
            allRotors.add(i);

        }
        this.listPermutation=new ListPermutation(codeConfiguration.getRotorsID(),allRotors);
        List<Integer> currentIDs;
        int count=0;
        while (!listPermutation.done())
        {
            currentIDs=listPermutation.increasePermutation();
/*            System.out.println();
            currentIDs.forEach(integer -> System.out.print(integer));
            System.out.println();*/
            codeConfiguration.setRotorsID(currentIDs);
            count++;
            goOverAllReflector();
        }
       /* System.out.println("amount of Permut = "+count);*/
    }




}
