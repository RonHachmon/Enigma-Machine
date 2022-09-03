import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class machineController {
    private SettingController settingController;
    private MachineManager machineManager=new MachineManager();
    private MachineInformation machineInformation;

    @FXML
    private Button loadXMLButton;

    @FXML
    private TextField currentPath;

    @FXML
    private Button setRandomCode;

    @FXML
    private Button loadXMLButton2;

    @FXML
    private Label amountOfRotors;

    @FXML
    private Label amountOfRequiredRotors;

    @FXML
    private Label amountOfReflectors;

    @FXML
    private Label amountOfProcessedInput;

    @FXML
    private Label currentCodeConfig;

    @FXML
    void loadXML(ActionEvent event) {
        machineManager.createMachineFromXML("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
        machineInformation=machineManager.getMachineInformation();
        this.currentPath.setText("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
        setMachineInformation();
/*        FileChooser fileChooser = configFileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);*//*
        if(selectedFile!=null)
        {
                new Thread(()->
                {
                    try {
*//*                        machineManager.createMachineFromXML(selectedFile.getAbsoluteFile().toString());
                        machineInformation=machineManager.getMachineInformation();
                        this.currentPath.setText(selectedFile.getAbsolutePath());*//*
                        machineManager.createMachineFromXML("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
                        Platform.runLater(()->
                        {
                            setMachineInformation();

                        });
                    }
                        catch (Exception e)
                        {
                            Platform.runLater(()->
                            {
                                Alert a=new Alert(AlertType.ERROR);
                                a.setContentText(e.getMessage());
                                a.setTitle("Invalid file");
                                a.show();
                            });
                        }

                }).start();

        }*/


    }

    @FXML
    void setCode(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/settingScene.fxml"));
            AnchorPane anchorPane = loader.load();
            settingController=loader.getController();
            settingController.setMachineInformation(machineInformation);
            settingController.setSetting();
            Scene scene = new Scene(anchorPane,800,600);
            Stage settingStage=new Stage();
            settingStage.setScene(scene);
            scene.getStylesheets().add("/resources/ConfigScene.css");
            settingStage.setTitle("Machine code");
            settingStage.initModality(Modality.WINDOW_MODAL);
            settingStage.showAndWait();
            if(settingController.getIsCodeValid())
            {
                this.setMachinceCode();
            }

            //Stage currentStage = (Stage) this.setRandomCode.getScene().getWindow();
            //currentStage.initModality(Modality.APPLICATION_MODAL);
            //stage.close();
            //settingStage.onCloseRequestProperty().addListener(e->this.setMachinceCode());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMachinceCode() {
        List<Integer> rotorsID = new ArrayList<>();
        StringBuilder staringIndexes = new StringBuilder("");
        StringBuilder switchPlugs = new StringBuilder("");

        this.settingController.getRotorIndexes().forEach(choiceBox -> rotorsID.add(choiceBox.getValue()-1));
        this.settingController.getRotorStartingIndexes().forEach(characterChoiceBox -> staringIndexes.append(characterChoiceBox.getValue()));
        this.settingController.getAllSwitchPlugChoiceBoxes().forEach(switchPlugsBox->switchPlugs.append(switchPlugsBox.getValue()));
        Collections.reverse(rotorsID);
        staringIndexes.reverse();
        machineManager.setSelectedRotors(rotorsID);
        machineManager.setStartingIndex( staringIndexes.reverse().toString());
        machineManager.setSelectedReflector(this.settingController.getSelectedReflector()-1);
        if(!switchPlugs.toString().isEmpty())
        {
            machineManager.setSwitchPlug(switchPlugs.toString());
        }

        this.currentCodeConfig.setText(machineManager.getCurrentCodeSetting());
    }

    @FXML
    void setRandomCode(ActionEvent event) {
        new Thread(()->
        {
            this.machineManager.autoZeroMachine();
            Platform.runLater(()->
            {
                this.currentCodeConfig.setText(machineManager.getCurrentCodeSetting());
            });

        }).start();


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

    private void setMachineInformation()
    {
        amountOfRotors.setText(String.valueOf(machineInformation.getAmountOfRotors()));
        amountOfRequiredRotors.setText(String.valueOf(machineInformation.getAmountOfRotorsRequired()));
        amountOfReflectors.setText(String.valueOf(machineInformation.getAvailableReflectors()) );
        amountOfProcessedInput.setText(String.valueOf(machineManager.getAmountOfProcessedInputs()));
    }

}





