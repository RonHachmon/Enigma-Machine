package app.header;

import app.MainAppController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import Engine.machineutils.MachineManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {




    private MachineManager machineManager;
    private MainAppController mainAppController;

    @FXML
    private Button machineButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button bruteForceButton;

    @FXML
    private TextField currentPath;
    @FXML
    void bruteForceClicked(ActionEvent event) {

    }

    @FXML
    void encryptClicked(ActionEvent event) {
        encryptButton.setDisable(true);
        machineButton.setDisable(false);
        mainAppController.displayEncrypt();

    }

    @FXML
    void machineClicked(ActionEvent event) {
        machineButton.setDisable(true);
        encryptButton.setDisable(false);
        mainAppController.displayMachineConfigScene();

    }


    @FXML
    void loadXML(ActionEvent event) {
/*        machineManager.createMachineFromXML("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
        this.currentPath.setText("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
        mainAppController.updateAllControllers();
        mainAppController.resetAll();*/

        FileChooser fileChooser = configFileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null)
        {
                new Thread(()->
                {
                    try {
                        System.out.println(selectedFile.getAbsoluteFile());
                        machineManager.createMachineFromXML(selectedFile.getAbsoluteFile().toString());
                        Platform.runLater(()->
                        {
                            this.encryptButton.setDisable(true);
                            this.currentPath.setText(selectedFile.getAbsolutePath());
                            mainAppController.updateAllControllers();
                            mainAppController.resetAll();
                        });
                    }
                        catch (Exception e)
                        {
                            Platform.runLater(()->
                            {
                                Alert a=new Alert(Alert.AlertType.ERROR);
                                a.setContentText(e.getMessage());
                                a.setTitle("Invalid file");
                                a.show();
                            });
                        }

                }).start();

        }


    }

    private FileChooser configFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("ALL FILES", "*.*")
        );
        return fileChooser;
    }

    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    public void setMainAppController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void enableEncrypt() {
        encryptButton.setDisable(false);
    }
}