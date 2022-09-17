package Engine.bruteForce2;

import DTO.DMData;
import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.CodeConfiguration;
import Engine.bruteForce2.utils.Dictionary;
import Engine.machineutils.MachineManager;
import utils.Utils;

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
    private static long totalCombinations=0;
    private static long doneCombinations=0;


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
        calcMissionSize();
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
    private void calcMissionSize() {
        long easy = (long) Math.pow(machineManager.getAvailableChars().length(), machineManager.getCurrentRotorsList().size());
        long medium = easy * machineManager.getMachineInformation().getAvailableReflectors();
        long hard = medium * Utils.factorial(machineManager.getCurrentRotorsList().size());
        long impossible = hard * Utils.binomial(machineManager.getCurrentRotorsList().size(), machineManager.getMachineInformation().getAmountOfRotors());

        switch (dmData.getDifficulty()) {
            case EASY:
                totalCombinations = easy;
                break;
            case MEDIUM:
                totalCombinations = medium;
                break;
            case HARD:
                totalCombinations = hard;
                break;
            case IMPOSSIBLE:
                totalCombinations = impossible;
                break;
        }
        System.out.println("Total combination"+totalCombinations);
    }
    public  long getTotalWork()
    {
        return totalCombinations;
    }
    public long getWorkDone()
    {
        return doneCombinations;
    }
    synchronized static public void addWorkDone(long number)
    {
        doneCombinations+=number;
    }



}