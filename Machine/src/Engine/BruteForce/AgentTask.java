package Engine.BruteForce;

import Engine.EnigmaException.WordNotValidInDictionaryException;
import Engine.machineparts.Machine;
import Engine.machineutils.MachineManager;

import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AgentTask {

    private final Integer taskSize;
    private final MachineManager machineManager;
    private final String encryptedString;
    private final Engine.BruteForce.Dictionary dictionary;
    private final ExecutorService candidatesThreadPoolExecutor;
    private  int maxAmountOfAgents;
//    private final BruteForceUIAdapter bruteForceUIAdapter;
//    private final DecipherStatistics decipherStatistics;
    private String startingRotorPositions;

    public AgentTask(Integer taskSize, String startingRotorPositionSector, MachineManager machineManager, String encryptedString, Engine.BruteForce.Dictionary dictionary, ExecutorService candidatesThreadPoolExecutor){
        this.taskSize = taskSize;
        this.machineManager = machineManager;
        this.encryptedString = encryptedString;
        this.dictionary = dictionary;
        this.candidatesThreadPoolExecutor = candidatesThreadPoolExecutor;
//        this.bruteForceUIAdapter = bruteForceUIAdapter;
//        this.decipherStatistics = decipherStatistics;
        this.startingRotorPositions = startingRotorPositionSector;
    }

    public Integer getTaskSize() {
        return taskSize;
    }

    public MachineManager getMachineManager() {
        return machineManager;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public Engine.BruteForce.Dictionary getDictionary() {
        return dictionary;
    }

    public ExecutorService getCandidatesThreadPoolExecutor() {
        return candidatesThreadPoolExecutor;
    }

//    public BruteForceUIAdapter getBruteForceUIAdapter() {
//        return bruteForceUIAdapter;
//    }
//
//    public DecipherStatistics getDecipherStatistics() {
//        return decipherStatistics;
//    }

    public String getStartingRotorPositions() {
        return startingRotorPositions;
    }

    public void setStartingRotorPositions(String startingRotorPositions) {
        this.startingRotorPositions = startingRotorPositions;
    }

    public void validateWordsInDictionary(List<String> wordsToCheck) throws WordNotValidInDictionaryException {
        dictionary.validateWords(wordsToCheck);
    }

    public void addDecryptionCandidateTaskToThreadPool(Runnable decryptionCandidateTask) {
        candidatesThreadPoolExecutor.execute(decryptionCandidateTask);
    }
    public void setMaxAmountOfAgents(int maxAmountOfAgents)
    {
        this.maxAmountOfAgents=maxAmountOfAgents;
    }
    public int getMaxAmountOfAgents()
    {
        return maxAmountOfAgents;
    }
}

