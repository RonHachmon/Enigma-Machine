package Engine.bruteForce2;

import DTO.DMData;
import Engine.machineutils.MachineManager;

import java.util.concurrent.ThreadFactory;

public class AgentFactory implements ThreadFactory {
    MachineManager machineManager;
    DMData dmData;
    public AgentFactory(MachineManager machineManager,DMData dmData)
    {
        this.machineManager=machineManager;
        this.dmData=dmData;
    }

    @Override
    public Thread newThread(Runnable r) {
        Agent thread = new Agent(machineManager,dmData,r);
        thread.setName(String.valueOf(thread.getAgentId()));
        thread.setDaemon(true);
        return thread;
    }
}
