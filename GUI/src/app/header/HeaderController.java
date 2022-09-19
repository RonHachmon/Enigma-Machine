package app.header;

import app.MainAppController;
import app.bodies.absractScene.MainAppScene;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import Engine.machineutils.MachineManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController extends MainAppScene implements Initializable   {

    @FXML
    private Button machineButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button bruteForceButton;

    @FXML
    private TextField currentPath;

    @FXML
    private Label titleLabel;
    @FXML
    void bruteForceClicked(ActionEvent event) {
        bruteForceButton.setDisable(true);
        encryptButton.setDisable(false);
        machineButton.setDisable(false);
        mainAppController.bruteForce();

    }

    @FXML
    void encryptClicked(ActionEvent event) {
        encryptButton.setDisable(true);
        machineButton.setDisable(false);
        bruteForceButton.setDisable(false);
        mainAppController.displayEncrypt();

    }

    @FXML
    void machineClicked(ActionEvent event) {
        machineButton.setDisable(true);
        encryptButton.setDisable(false);
        bruteForceButton.setDisable(false);
        mainAppController.displayMachineConfigScene();

    }


    @FXML
    void loadXML(ActionEvent event) {

  /*      machineManager.createMachineFromXML("test_files/ex2-basic.xml");
        this.currentPath.setText("test_files/ex2-basic.xml");

        mainAppController.resetAll();
        mainAppController.updateAllControllers();*/


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
                            mainAppController.resetAll();
                            mainAppController.updateAllControllers();

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

    public void enableEncrypt(boolean toEnable) {
        encryptButton.setDisable(!toEnable);
    }

    public void enableBruteForce(boolean toEnable) {
        bruteForceButton.setDisable(!toEnable);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font font = Font.loadFont("file:resources/fonts/windows_command_prompt.ttf", 26);
        titleLabel.setFont(font);
    }
}