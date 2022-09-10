package app.bodies.absractScene;

import Engine.machineutils.MachineInformation;
import Engine.machineutils.MachineManager;
import app.MainAppController;

public abstract class MainAppScene {
    protected MachineManager machineManager;
    protected MainAppController mainAppController;
    protected MachineInformation machineInformation;
    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    public void setMainAppController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }
    public void setMachineInformation(MachineInformation machineInformation) {
        this.machineInformation = machineInformation;
    }
}
