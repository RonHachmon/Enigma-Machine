package app;

import app.bodies.BruteForceController;
import app.bodies.ConfigurationController;
import app.bodies.EncryptController;
import app.bodies.absractScene.MainAppScene;
import app.bodies.interfaces.CodeHolder;
import app.header.HeaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import Engine.machineutils.MachineManager;

import java.net.URL;
import java.util.*;

public class MainAppController implements Initializable {

    private MachineManager machineManager=new MachineManager();
    @FXML private VBox headerComponent;
    @FXML private CheckMenuItem animationButton;
    @FXML private HeaderController headerComponentController;
    @FXML private GridPane configurationComponent;
    @FXML private ConfigurationController configurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private GridPane bruteForceComponent;
    @FXML private BruteForceController bruteForceComponentController;
    public static final String ARMY_CSS = "/resources/css/army.css";
    public static final String NORMAL_CSS = "/resources/css/app.css";


    private List<CodeHolder> codeHolders=new ArrayList<>();
    private final List<MainAppScene> mainAppScenes = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        addControllerToArray();

        encryptComponent.setVisible(false);
        bruteForceComponent.setVisible(false);

        headerComponentController.setMachineManager(machineManager);
        mainAppScenes.forEach(mainAppScene -> mainAppScene.setMainAppController(this));

    }
    @FXML
    void enabledAnimation(ActionEvent event)
    {
            bruteForceComponentController.enableAnimation(animationButton.isSelected());
            encryptComponentController.enableAnimation(animationButton.isSelected());
    }
    @FXML
    void armyCss(ActionEvent event)
    {
        encryptComponent.getScene().getStylesheets().clear();
        encryptComponent.getScene().getStylesheets().add(String.valueOf(getClass().getResource(ARMY_CSS)));

    }

    @FXML
    void plainCss(ActionEvent event)
    {
        encryptComponent.getScene().getStylesheets().clear();
        encryptComponent.getScene().getStylesheets().add(String.valueOf(getClass().getResource(NORMAL_CSS)));
    }

    private void addControllerToArray() {
        codeHolders.add(configurationComponentController);
        codeHolders.add(encryptComponentController);
        codeHolders.add(bruteForceComponentController);
        mainAppScenes.add(headerComponentController);
        mainAppScenes.add(configurationComponentController);
        mainAppScenes.add(encryptComponentController);
        mainAppScenes.add(bruteForceComponentController);
    }

    public void updateMachineInformation()
    {
        configurationComponentController.updateMachineInformation();
    }

    public void displayMachineConfigScene() {
        setAllPagesVisibilityToFalse();
        this.configurationComponent.setVisible(true);
    }
    public void displayEncrypt() {
        setAllPagesVisibilityToFalse();
        this.encryptComponent.setVisible(true);
    }

    public void bruteForce() {
        setAllPagesVisibilityToFalse();
        this.bruteForceComponent.setVisible(true);
    }

    private void setAllPagesVisibilityToFalse()
    {
        this.configurationComponent.setVisible(false);
        this.encryptComponent.setVisible(false);
        this.bruteForceComponent.setVisible(false);
    }

    public void updateAllControllers() {
        mainAppScenes.forEach(mainAppScene -> {
            mainAppScene.setMachineManager(machineManager);
            mainAppScene.setMachineInformation(machineManager.getMachineInformation());
        });
        this.updateMachineInformation();

        encryptComponentController.updateMachineKeyboard();

        this.bruteForceComponentController.updateInitialDictionaryTable();
        this.bruteForceComponentController.updateAmountOfAgent();
    }



    public void updateMachineCode(String currentCodeSetting) {
        codeHolders.forEach(codeHolder -> codeHolder.updateCode(currentCodeSetting));
    }
    public void setInitialCode(String code) {
        encryptComponentController.addCodeToComboBox(code);
        updateMachineCode(code);
    }

    public void enableEncrypt()
    {
        headerComponentController.enableEncrypt(true);
    }
    public void enableBruteForce() {
        headerComponentController.enableBruteForce(true);
    }

    //called when new machine is loaded
    public void resetAll() {
        this.displayMachineConfigScene();
        this.clearEncryptText();
        headerComponentController.enableBruteForce(false);
        headerComponentController.enableEncrypt(false);

        configurationComponentController.resetCode();
        configurationComponentController.resetInformation();
        configurationComponentController.disableConfigButtons();

        encryptComponentController.clearStats();

        bruteForceComponentController.clearScreen();
    }
    public void clearEncryptText()
    {
        this.encryptComponentController.clearText();
    }



    public void updateTotalWordEncrypted(int processedInputCounter) {
        this.configurationComponentController.updateTotalEncryptedWord(processedInputCounter);
    }
}
