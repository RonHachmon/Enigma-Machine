package Engine.BruteForce;

import DTO.DMData;
import Engine.machineutils.MachineManager;

import java.util.concurrent.BlockingQueue;

public class DecryptManager {

    private long combination;
    private Dictionary dictionary;
    private MachineManager machineManager;
    private DMData DMdata;
    private TasksManager tasksManager;


    public DecryptManager(MachineManager machineManager, Dictionary dictionary,DMData dmData) {
        this.machineManager = machineManager;
        this.dictionary = dictionary;
        this.DMdata=dmData;
    }

    public void startDeciphering() throws Exception{
        tasksManager = new TasksManager(DMdata,machineManager,(stop) -> System.out.println("ff"));

    }


}