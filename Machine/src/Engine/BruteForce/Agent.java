package Engine.BruteForce;

import Engine.EnigmaException.WordNotValidInDictionaryException;
import Engine.machineparts.Machine;
import Engine.machineutils.MachineManager;
import jdk.nashorn.internal.runtime.regexp.joni.constants.internal.Arguments;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent implements Runnable {

    private final String agentID;
    private long encryptionTimeDurationInNanoSeconds;
    private AgentTask agentTask;
    private MachineManager machineManager;
    private String startingRotorPosition;
    private static int staticTaskID;
    private int taskID;
    private TasksManager tasksManager;

    public Agent(AgentTask agentTask, TasksManager tasksManager) {
        synchronized (this) {
            this.agentTask = agentTask;
            this.agentID = Thread.currentThread().getName();
            this.machineManager = agentTask.getMachineManager();
            this.startingRotorPosition = agentTask.getStartingRotorPositions();
            this.tasksManager = tasksManager;
            this.taskID = staticTaskID++;
        }
    }

    static {
        resetTaskID();
    }

    private static void resetTaskID() {
        staticTaskID = 1;
    }

    public static int getStaticTaskID() {
        return staticTaskID;
    }

    @Override
    public void run() {
        Instant startTaskTime = Instant.now();
        List<Integer> currentRotorPositions = machineManager.getCurrentRotorsList();

        for (int i = 0; i < agentTask.getTaskSize(); i++) {
            try {
                Instant startingTime = Instant.now();

                validateAndSetStartingRotorPositions(currentRotorPositions);

                String currentCodeConfigurationFormat = machineManager.getCurrentCodeSetting();
                String candidateMessage = machineManager.encryptSentence(agentTask.getEncryptedString().toUpperCase());
                //System.out.println("Agent id: " + Thread.currentThread().getName() + " code: " + currentCodeConfigurationFormat + "candidate: " + candidateMessage);

                try {
                    agentTask.validateWordsInDictionary(Arrays.asList(candidateMessage.split(" ")));
                    //System.out.println(candidateMessage + ": Agent " + Thread.currentThread().getName() + " " + enigmaMachine.getCurrentSettingsFormat().toString());//to check
                    encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();
                    agentTask.addDecryptionCandidateTaskToThreadPool(new DecryptionCandidateTaskHandler());
                }
                catch (WordNotValidInDictionaryException ignored) {}

                try {
                    //set keyboard changes
                }
                catch (Exception e) {
                    break;
                }
            }
            catch (Exception e) {
                break;
            }
        }

        long totalTaskTime = Duration.between(startTaskTime, Instant.now()).toMillis();
        //agentTask.getBruteForceUIAdapter().updateExistingAgentTaskTime(new AgentTaskData(taskId, totalTaskTime));
        tasksManager.agentTaskFinished(totalTaskTime);

    }

    private void validateAndSetStartingRotorPositions(List<Integer> currentRotorPositions) throws IllegalArgumentException, CloneNotSupportedException {
        machineManager.setSelectedRotors(currentRotorPositions);
        machineManager.commitChangesToMachine();
        machineManager.resetMachineCode();
    }
}
