package Engine.bruteForce2;

import DTO.DMData;
import Engine.bruteForce2.TaskManger;
import Engine.bruteForce2.utils.Dictionary;
import Engine.machineutils.MachineManager;

import java.util.concurrent.BlockingQueue;

public class DecryptManager {

    private long combination;
    private Dictionary dictionary;
    private MachineManager machineManager;
    private DMData DMdata;
    private TaskManger tasksManager;


    public DecryptManager(MachineManager machineManager,DMData dmData) {
        this.machineManager = machineManager;
/*        this.dictionary = dictionary;*/
        this.DMdata=dmData;
    }

    public void startDeciphering() throws Exception{
        tasksManager = new TaskManger(DMdata,machineManager,(stop) -> System.out.println("ff"));
        tasksManager.start();

    }


}