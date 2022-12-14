package app.bodies;

import Engine.machineutils.NewStatisticInput;
import app.bodies.absractScene.MainAppScene;
import app.bodies.interfaces.CodeHolder;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;


import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EncryptController extends MainAppScene implements Initializable, CodeHolder {
    public static final String KEYBOARD_BUTTON_FXML = "/app/smallComponent/keyboardButton.fxml";



    @FXML
    private Label currentCode;

    @FXML
    private FlowPane inputKeyboard;

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;

    @FXML
    private FlowPane outputKeyboard;

    @FXML
    private TableView<NewStatisticInput> statisticTable;

    @FXML
    private TableColumn<NewStatisticInput, String> inputColumn;

    @FXML
    private TableColumn<NewStatisticInput, String> outputColumn;

    @FXML
    private TableColumn<NewStatisticInput, Integer> timeColumn;

    @FXML
    private ComboBox<String> codeChooseBox;


    @FXML
    private CheckBox isManualButton;

    @FXML
    private Button sendToStaticsButton;
    @FXML
    private Button runButton;


    private static String newline = System.getProperty("line.separator");
    private Integer currentNanoDuration=0;
    SimpleBooleanProperty isManual = new SimpleBooleanProperty(false);
    boolean clearTextClicked=false;
    private boolean toAnimate=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputArea.textProperty().addListener((object, oldValue, newValue) -> {
            darkInputArea();
            if(!newValue.isEmpty()) {
                lightInputKeyboard(newValue.substring(newValue.length() - 1).toUpperCase());
            }
            if (isManual.get()) {
                if(!clearTextClicked)
                {
                    encryptOneByOne(oldValue, newValue);
                }
                else
                {
                    clearTextClicked=false;
                }

            }

        });

        setStatisticTable();
        setBindings();


    }

    private void setBindings() {
        isManual.bind(isManualButton.selectedProperty());

        sendToStaticsButton.visibleProperty().bind(isManual);
        runButton.visibleProperty().bind(isManual.not());

        sendToStaticsButton.managedProperty().bind(isManual);
        runButton.managedProperty().bind(isManual.not());
    }

    private void setStatisticTable() {
        inputColumn.setCellValueFactory(new PropertyValueFactory<NewStatisticInput, String>("input"));
        outputColumn.setCellValueFactory(new PropertyValueFactory<NewStatisticInput, String>("output"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<NewStatisticInput, Integer>("duration"));

        //set choose box to show only static relatable to the code
        codeChooseBox.valueProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    ArrayList<NewStatisticInput> currentArray = machineManager.getStatsPerCode(new_val);
                    if (currentArray != null) {
                        statisticTable.setItems(FXCollections.observableArrayList(machineManager.getStatsPerCode(new_val)));
                    }

                });
    }


    @FXML
    void clearTextClicked(ActionEvent event) {
        this.clearTextClicked=true;
        this.mainAppController.clearEncryptText();
    }

    @FXML
    void resetCodeClicked(ActionEvent event) {
        this.machineManager.resetMachineCode();
        this.mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
    }

    @FXML
    void resetAndClear(ActionEvent event) {
        this.mainAppController.clearEncryptText();
        this.resetCodeClicked(event);

    }

    @FXML
    void addToStatistic(ActionEvent event) {
        this.machineManager.sendToStats(inputArea.getText().toUpperCase(),outputArea.getText(),currentNanoDuration);
        currentNanoDuration=0;
        this.mainAppController.clearEncryptText();
        updateStatistic();

    }

    @FXML
    void runClicked(ActionEvent event) {
        try {
            String output = this.machineManager.encryptSentenceAndAddToStatistic(inputArea.getText().toUpperCase());
            outputArea.setText(output);
            updateStatistic();
            mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
        }
        catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }

    }

    private void updateStatistic() {
        this.mainAppController.updateTotalWordEncrypted( this.machineManager.getProcessedInputCounter());
        if(codeChooseBox.getValue()!=null)
        {
            if(codeChooseBox.getValue().equals(machineManager.getInitialFullMachineCode())) {
                statisticTable.getItems().add(machineManager.getLastStatsPerCode(codeChooseBox.getValue()));
            }
        }
    }

    public void clearStats()
    {
        codeChooseBox.getItems().clear();
        statisticTable.getItems().clear();
    }


    public void clearText() {
        inputArea.setText("");
        outputArea.setText("");
        darkOutputKeyboard();
        darkInputArea();
    }

    public void updateMachineKeyboard() {
        inputKeyboard.getChildren().clear();
        outputKeyboard.getChildren().clear();
        for (int i = 0; i < machineInformation.getAvailableChars().length(); i++) {
            Character currentCharacter = machineInformation.getAvailableChars().charAt(i);
            Label inputKeyLabel = loadFXML(KEYBOARD_BUTTON_FXML);
            Label outputKeyLabel = loadFXML(KEYBOARD_BUTTON_FXML);

            FlowPane.setMargin(inputKeyLabel, new Insets(2, 10, 2, 10));
            FlowPane.setMargin(outputKeyLabel, new Insets(2, 10, 2, 10));

            inputKeyLabel.setText(currentCharacter.toString());
            outputKeyLabel.setText(currentCharacter.toString());

            inputKeyboard.getChildren().add(inputKeyLabel);
            outputKeyboard.getChildren().add(outputKeyLabel);

        }
    }



    @Override
    public void updateCode(String code) {
        if (toAnimate) {
            scaleAnimation(code);
        }
        else {
            this.currentCode.setText(code);
        }
    }

    private void scaleAnimation(String code) {

            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setNode(this.currentCode);
            translateTransition.setDuration(javafx.util.Duration.millis(400));
            translateTransition.setFromY(0);
            translateTransition.setToY(-20);

            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setNode(this.currentCode);
            fadeTransition.setDuration(javafx.util.Duration.millis(300));
            fadeTransition.setFromValue(0.2);
            fadeTransition.setToValue(0);
            translateTransition.play();
            fadeTransition.play();
            fadeTransition.setOnFinished((actionEvent) ->
            {
                this.currentCode.setText(code);
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(this.currentCode);
                transition.setDuration(javafx.util.Duration.millis(400));
                transition.setFromY(20);
                transition.setToY(0);
                FadeTransition fade = new FadeTransition();
                fade.setNode(this.currentCode);
                fade.setDuration(javafx.util.Duration.millis(300));
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.play();
                transition.play();
            });
    }


    public void addCodeToComboBox(String code) {
 /*       updateCurrentCode();*/
        codeChooseBox.getItems().add(code);
    }

    private void lightInputKeyboard(String keyPressed) {
        inputKeyboard.getChildren().forEach(node -> {
            Label keyBoardLabel = (Label) node;
            if (keyPressed.equals(keyBoardLabel.getText())) {
                keyBoardLabel.setId("pressed-key");
                rotateAnimation(keyBoardLabel);
            }
        });

    }

    private void lightOutputKeyboard(String keyPressed) {
        outputKeyboard.getChildren().forEach(node -> {
            Label keyBoardLabel = (Label) node;
            if (keyPressed.equals(keyBoardLabel.getText())) {
                keyBoardLabel.setId("pressed-key");
                rotateAnimation(keyBoardLabel);
            }
        });

    }

    private void rotateAnimation(Label keyBoardLabel) {
            RotateTransition rotateTransition = new RotateTransition();
            rotateTransition.setNode(keyBoardLabel);
            rotateTransition.setDuration(javafx.util.Duration.millis(400));
            rotateTransition.setByAngle(360);
            rotateTransition.play();
    }


    private void darkInputArea() {
        inputKeyboard.getChildren().forEach(node -> {
            node.setId("");
        });
    }

    private void darkOutputKeyboard() {
        outputKeyboard.getChildren().forEach(node -> {
            node.setId("");
        });
    }


    //--------------------------------------------Encrypt related--------------------------------
    //listen to inputTextArea and differs between new text to old text
    private void encryptOneByOne(String oldValue, String newValue) {
        try {
            /*if (!newValue.isEmpty()) {*/
                if (newValue.length() > oldValue.length()) {
                    encryptNewText(oldValue, newValue);
                } else {
                    this.machineManager.resetMachineCode();
                    this.mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
                    this.outputArea.setText("");
                    encryptString(newValue);
                }
            /*}*/
        } catch (Exception e) {
            inputArea.setText(oldValue);
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }
    }

    //extract new characters
    private void encryptNewText(String oldValue, String newValue) {
        String addedCharacters = newValue.substring(oldValue.length());

        if (addedCharacters.contains(newline)) {
            outputArea.setText(outputArea.getText() + newline);
        }
        if (!addedCharacters.contains("\t")) {
            encryptString(addedCharacters);
        }
    }
    //send to encryption char by char
    private void encryptString(String addedChar) {
        for (int i = 0; i < addedChar.length(); i++) {
            sentToEncryptAndCalculateTime(addedChar.charAt(i));
        }
    }

    //for each char encrypt and sums time of encryption
    private void sentToEncryptAndCalculateTime(Character input) {
        Instant timeStart = Instant.now();
        String output = this.machineManager.encryptSentence(input.toString().toUpperCase());
        currentNanoDuration+=Duration.between(timeStart, Instant.now()).getNano();
        outputArea.setText(outputArea.getText() + output);
        mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
        darkOutputKeyboard();
        lightOutputKeyboard(output);
    }

    //--------------------------------------------End: Encrypt related--------------------------------

    private <T> T loadFXML(String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }

    public void enableAnimation(boolean selected) {
        this.toAnimate=selected;
    }
}

