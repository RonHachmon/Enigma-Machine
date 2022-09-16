package Engine.BruteForce;


import DTO.DMData;
import Engine.EnigmaException.TaskIsCanceledException;
import Engine.machineparts.*;

import Engine.machineutils.MachineInformation;
import Engine.machineutils.MachineManager;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TasksManager extends Task<Boolean> {
    private static final int  MAX_QUEUE_SIZE = 1000;
    private ThreadPoolExecutor tasksPool;
    private DecodeStatistics decodeStatistics;
    private Double totalCombinations;
    //private BruteForceUIAdapter bruteForceUIAdapter;
    private String encryptedString;
    private Engine.BruteForce.Dictionary dictionary;
    private Integer amountOfAgents;
    private ExecutorService candidatesPool;
    private Integer taskSize;
    private MachineManager machineManager;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private long totalTaskSize;
    private long currentTaskSize;
    private long totalAgentTasksTime;
    private long totalAgentTasksAverageTime;
    private String settingsFormat;
    private BlockingQueue<Runnable> blockingQueue;
    private Consumer<Runnable> onCancel;
    private Boolean isPaused;
    private MachineInformation machineInformation;


    /*BruteForceTask bruteForceTask*/
    public TasksManager(DMData dMData, MachineManager machineManager, Consumer<Runnable> onCancel) throws Exception {
        this.machineManager = machineManager;
        machineInformation = machineManager.getMachineInformation();
        this.difficultyLevel = dMData.getDifficulty();
        this.amountOfAgents = dMData.getAmountOfAgents();
        this.taskSize = dMData.getAssignmentSize();
        this.encryptedString = dMData.getEncryptedString();
        this.dictionary = machineManager.getBruteForceData().getDictionary();
        this.candidatesPool = Executors.newFixedThreadPool(1);
//        this.settingsFormat = decryptedSettingsFormat;
        this.onCancel = onCancel;
        this.blockingQueue = new ArrayBlockingQueue<Runnable>(MAX_QUEUE_SIZE);
        this.tasksPool = new ThreadPoolExecutor(amountOfAgents,
                amountOfAgents,
                5000,
                TimeUnit.SECONDS,
                blockingQueue ,
                new AgentThreadFactory(amountOfAgents), new ThreadPoolExecutor.CallerRunsPolicy());
        decodeStatistics = new DecodeStatistics();

        initializeMachineSettings();
        initializeTaskData();
        calcMissionSize();
    }

    private void initializeMachineSettings() {
        machineManager.resetMachineCode();
        //TODO: initializeMachineSettings
    }

    private void initializeTaskData() {
        this.totalAgentTasksTime = 0;
        this.totalAgentTasksAverageTime = 0;
        this.currentTaskSize = 0;
//        this.bruteForceUIAdapter.updateTotalProcessedAgentTasks(currentTaskSize);
//        this.bruteForceUIAdapter.updateAverageTaskTime(totalAgentTasksAverageTime);
//        this.bruteForceUIAdapter.updateMissionTotalTime(totalAgentTasksTime);
    }

    private List<List<Integer>> getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist() {
        List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = new ArrayList<>();
        List<Integer> allPossibleRotors = new ArrayList<>();
        calculateAllPossibleRotorsIDsCombinations(machineInformation.getAmountOfRotors(),1, machineInformation.getAmountOfRotorsRequired(), allPossibleRotors, allPossibleRotorsCombinationsFromAllRotors);
        return allPossibleRotorsCombinationsFromAllRotors;
    }

    public void calculateAllPossibleRotorsIDsCombinations(int totalNumberOfRotors, int left, int numberOfRotorsInUse, List<Integer> combination, List<List<Integer>> combinations) {
        // Pushing this vector to a vector of vector
        if (numberOfRotorsInUse == 0) {
            combinations.add(new ArrayList<>(combination.stream().collect(Collectors.toList())));
            return;
        }

        // i iterates from left to n. First time
        // left will be 1
        for (int i = left; i <= totalNumberOfRotors; ++i)
        {
            combination.add(i);
            calculateAllPossibleRotorsIDsCombinations(totalNumberOfRotors, i + 1, numberOfRotorsInUse - 1, combination, combinations);

            // Popping out last inserted element from the vector
            combination.remove(combination.size() - 1);
        }
    }

    private Integer binomial(int numOfActiveRotors, int numOfAllRotors) {
        return factorial(numOfAllRotors) / (factorial(numOfActiveRotors) * factorial(numOfAllRotors - numOfActiveRotors));
    }

    private Integer factorial(int size) {
        if(size == 0) {
            return 1;
        }
        return size * factorial(size - 1);
    }

    private void calcMissionSize(){
        Double easy = Math.pow(machineManager.getAvailableChars().length(), machineManager.getCurrentRotorsList().size());
        Double medium = easy * machineManager.getMachineInformation().getAmountOfRotors();
        Double hard = medium * factorial(machineManager.getCurrentRotorsList().size());
        Double impossible = hard * binomial(machineManager.getCurrentRotorsList().size(),machineManager.getMachineInformation().getAmountOfRotors());

        switch (difficultyLevel) {
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

        totalTaskSize = (long) (totalCombinations / taskSize);
        //bruteForceUIAdapter.updateTotalAgentsTasks(totalTaskSize);
    }

    public void setImpossibleTasks() throws Exception {

    }

    public void setHardTasks() throws Exception {

    }

    public void setMediumTasks() throws Exception {

    }

    public void setEasyTasks() throws Exception, TaskIsCanceledException {
        int numOfPossibleRotorsPositions = (int)  Math.pow(machineManager.getAvailableChars().length(), machineManager.getCurrentRotorsList().size());
        List<Integer> currentStartingRotorsPositions = machineManager.getCurrentRotorsList();

        if (isCancelled()) {
            throw new TaskIsCanceledException();
        }

        if(Thread.currentThread().isInterrupted()) {
            pauseMission();
        }

        while(numOfPossibleRotorsPositions > 0) {
            MachineManager clonedEnigmaMachine = machineManager.cloneMachine();
            AgentTask agentTask = new AgentTask(taskSize, machineManager.getInitialFullMachineCode(), clonedEnigmaMachine , encryptedString, dictionary, candidatesPool);
            Agent agent = new Agent(agentTask,this);

            blockingQueue.put(agent);
            numOfPossibleRotorsPositions -= taskSize;
            try {
                //get input from keyboard
            }
            catch (Exception e) {
                break;
            }

        }
    }

    synchronized private void pauseMission() {
        isPaused = true;

        while (isPaused) {
            try{
                this.wait();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    synchronized public void resumeMission() {
        isPaused = false;

        while(!isPaused) {

        }
        this.notify();
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            tasksPool.prestartAllCoreThreads();
            difficultyLevel.setTask(this);
        } catch (Exception  e) {
            //System.out.println(e.getMessage());
        }

        try {
            tasksPool.awaitTermination(8, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return Boolean.TRUE;
    }

    synchronized public void agentTaskFinished(long agentTaskTimeDuration) {
        totalAgentTasksTime += agentTaskTimeDuration;
        //bruteForceUIAdapter.updateTotalProcessedAgentTasks(++currentTaskSize);
        totalAgentTasksAverageTime = totalAgentTasksTime * 1000 / currentTaskSize ;
        //bruteForceUIAdapter.updateAverageTaskTime(totalAgentTasksAverageTime);
        //bruteForceUIAdapter.updateMissionTotalTime(totalAgentTasksTime);
        updateProgress(currentTaskSize, totalTaskSize);
    }
}
