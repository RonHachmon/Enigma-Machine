package Engine.BruteForce;

import DTO.DMData;
import Engine.machineutils.MachineManager;
import app.MainAppController;

import java.util.concurrent.BlockingQueue;

public class DecryptionManager {

    private long combination;
    private Dictionary dictionary;
    private MachineManager machineManager;
    private DMData DMdata;
    private TasksManager tasksManager;


    public DecryptionManager(BlockingQueue queue, MachineManager machineManager, Dictionary dictionary) {
        this.machineManager = machineManager;
        this.dictionary = dictionary;
    }

    public void startDeciphering() throws Exception{
        tasksManager = new TasksManager(DMdata,machineManager,(stop) -> System.out.println("ff"));

    }


}