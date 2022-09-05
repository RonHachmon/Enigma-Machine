package app;

import app.bodies.ConfigurationController;
import app.header.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import Engine.machineutils.MachineManager;

import java.net.URL;
import java.util.ResourceBundle;

public class MainAppController implements Initializable {

    private MachineManager machineManager=new MachineManager();
    @FXML private VBox headerComponent;
    @FXML private HeaderController headerComponentController;


    @FXML private GridPane configurationComponent;
    @FXML private ConfigurationController configurationComponentController;





    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        headerComponentController.setMachineManager(machineManager);
        configurationComponentController.setMachineManager(machineManager);
        headerComponentController.setMainAppController(this);


    }

    public void updateMachineInformation()
    {
        configurationComponentController.setMachineInformation(machineManager.getMachineInformation());
        configurationComponentController.updateMachineInformation();
    }
}
