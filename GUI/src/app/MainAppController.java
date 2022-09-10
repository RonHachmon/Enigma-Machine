package app;

import app.bodies.ConfigurationController;
import app.bodies.EncryptController;
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

    @FXML private GridPane encryptComponent;

    @FXML private EncryptController encryptComponentController;





    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        encryptComponent.setVisible(false);
        headerComponentController.setMachineManager(machineManager);
        encryptComponentController.setMainAppController(this);
        headerComponentController.setMainAppController(this);
        configurationComponentController.setMainAppController(this);
    }

    public void updateMachineInformation()
    {
        configurationComponentController.updateMachineInformation();
    }

    public void displayMachineConfigScene() {
        this.configurationComponent.setVisible(true);
        this.encryptComponent.setVisible(false);
    }
    public void displayEncrypt() {
        this.configurationComponent.setVisible(false);
        this.encryptComponent.setVisible(true);
    }

    public void updateAllControllers() {
        configurationComponentController.setMachineManager(machineManager);
        configurationComponentController.setMachineInformation(machineManager.getMachineInformation());
        encryptComponentController.setMachineManager(machineManager);
        encryptComponentController.setMachineInformation(machineManager.getMachineInformation());
        this.updateMachineInformation();
        this.updateMachineKeyBoard();
    }

    private void updateMachineKeyBoard() {
        encryptComponentController.updateMachineKeyboard();
    }

    public void updateMachineCode() {
        encryptComponentController.updateCurrentCode();
        configurationComponentController.updateMachineCode();

    }
    public void setInitialCode() {
        encryptComponentController.setInitalCodeConfig();
        configurationComponentController.updateMachineCode();

    }

    public void enableEncrypt()
    {
        headerComponentController.enableEncrypt();
    }

    //called when new machine is loaded
    public void resetAll() {
        this.displayMachineConfigScene();
        this.clearEncryptText();
        configurationComponentController.resetCode();
        configurationComponentController.disableConfigButtons();
        encryptComponentController.clearStats();


    }
    public void clearEncryptText()
    {
        this.encryptComponentController.clearText();
    }
}
