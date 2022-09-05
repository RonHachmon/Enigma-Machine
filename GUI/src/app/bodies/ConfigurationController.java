package app.bodies;

import app.settings.SettingController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Engine.machineutils.MachineInformation;
import Engine.machineutils.MachineManager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigurationController {
    public static final String SETTING_SCENE_FXML = "/app/settings/settingScene.fxml";
    public static final String SETTING_CSS = "/app/settings/setting.css";
    private MachineManager machineManager;
    private MachineInformation machineInformation;

    private SettingController settingController;

    @FXML
    private Button setRandomCode;

    @FXML
    private Button setCode;

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

    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    @FXML
    void setCode(ActionEvent event) {

        try {
            Stage settingStage = loadSettingStage();
            settingStage.initModality(Modality.WINDOW_MODAL);
            settingStage.showAndWait();
            if(settingController.getIsCodeValid())
            {
                this.setMachinceCode();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Stage loadSettingStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SETTING_SCENE_FXML));

        AnchorPane anchorPane = loader.load();
        settingController=loader.getController();
        settingController.setSetting(machineInformation);
        Scene scene = new Scene(anchorPane,800,600);
        Stage settingStage=new Stage();
        settingStage.setScene(scene);
        scene.getStylesheets().add(SETTING_CSS);
        settingStage.setTitle("machine setting");
        return settingStage;
    }
    private void setMachinceCode() {
        List<Integer> rotorsID = new ArrayList<>();
        StringBuilder staringIndexes = new StringBuilder("");
        StringBuilder switchPlugs = new StringBuilder("");

        this.settingController.getRotorIndexes().forEach(choiceBox -> rotorsID.add(choiceBox.getValue()-1));
        this.settingController.getRotorStartingIndexes().forEach(characterChoiceBox -> staringIndexes.append(characterChoiceBox.getValue()));
        this.settingController.getAllSwitchPlugChoiceBoxes().forEach(switchPlugsBox->switchPlugs.append(switchPlugsBox.getValue()));

        Collections.reverse(rotorsID);

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

    public void updateMachineInformation()
    {
        amountOfRotors.setText(String.valueOf(machineInformation.getAmountOfRotors()));
        amountOfRequiredRotors.setText(String.valueOf(machineInformation.getAmountOfRotorsRequired()));
        amountOfReflectors.setText(String.valueOf(machineInformation.getAvailableReflectors()) );
        amountOfProcessedInput.setText(String.valueOf(machineManager.getAmountOfProcessedInputs()));
    }


    public void setMachineInformation(MachineInformation machineInformation) {
        this.machineInformation = machineInformation;
    }
}
