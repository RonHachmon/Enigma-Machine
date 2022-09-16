package Engine.bruteForce2;

import DTO.DMData;
import Engine.machineutils.MachineManager;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Agent extends Thread {

    static int idRunningIndex = 0;
    private int id = 0;
    private final String agentID;
    private long encryptionTimeDurationInNanoSeconds;
    private MachineManager machineManager;
    private DMData dMdata;


    public Agent(MachineManager machineManager, DMData dMData, Runnable assignment){
        this.id = idRunningIndex;
        idRunningIndex++;
        this.agentID = Thread.currentThread().getName();
        try {
            this.machineManager = machineManager.clone(); // ?
        }
        catch (CloneNotSupportedException CNSE){
            //TODO: handle exception
        }
        this.dMdata = dMData;
    }

    @Override
    public void run() {
        Instant startTaskTime = Instant.now();
        List<Integer> currentRotorPositions = machineManager.getCurrentRotorsList();

        for (int i = 0; i < dMdata.getAssignmentSize(); i++) {
            try {
                Instant startingTime = Instant.now();
                String candidateMessage = machineManager.encryptSentence(dMdata.getEncryptedString().toUpperCase());
                encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public int getAgentId() {
        return id;
    }
}