package app.bodies;

import DTO.DMData;
import DTO.DecryptionCandidate;
import Engine.bruteForce2.DecryptManager;
import Engine.bruteForce2.utils.Dictionary;
import Engine.bruteForce2.utils.DifficultyLevel;
import app.bodies.absractScene.MainAppScene;
import app.bodies.interfaces.CodeHolder;
import app.utils.FindCandidateTask;
import app.utils.UIAdapter;
import app.utils.candidate.CandidateController;
import app.utils.eDifficulty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BruteForceController extends MainAppScene implements Initializable, CodeHolder {

    @FXML
    private Label currentCode;

    @FXML
    private FlowPane candidatesFlowPane;

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;
    @FXML
    private ChoiceBox<Integer> amountOfAgentsChoiceBox;

    @FXML
    private ComboBox<DifficultyLevel> difficultyComboBox;
    @FXML
    private TextField assignmentSizeText;

    @FXML
    private TableView<String> dictionaryTable;

    @FXML
    private TableColumn<String, String> wordsColumn;

    @FXML
    private TextField searchBar;
    @FXML
    private Button runButton;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button pauseButton;
    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label amountOfCandidateFound;

    @FXML
    private Label percentageLabel;


    private SimpleIntegerProperty totalFoundCandidate=new SimpleIntegerProperty();
    private boolean validAssignment=false;
    private  FindCandidateTask currentRunningTask;
    private Tooltip toolTipError;
    private DMData dmData=new DMData();
    private Dictionary dictionary;
    private DecryptManager decryptManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

       Arrays.stream(DifficultyLevel.values()).sequential().forEach(eDifficulty ->difficultyComboBox.getItems().add(eDifficulty));
        amountOfCandidateFound.textProperty().bind(Bindings.format("%,d", totalFoundCandidate));

        setInitialDictionaryTable();

        setToolTip();

        bindListenersToInputButtons();


        searchBar.textProperty().
                addListener((object, oldValue, newValue)->filterDictionaryTable(newValue));

    }

    private void bindListenersToInputButtons() {
        assignmentSizeText.textProperty().
                addListener((object, oldValue, newValue)->allDataValid());
        difficultyComboBox.valueProperty().addListener((object, oldValue, newValue)->allDataValid());
        amountOfAgentsChoiceBox.valueProperty().addListener((object, oldValue, newValue)->allDataValid());
        outputArea.visibleProperty().addListener((object, oldValue, newValue)->allDataValid());
    }

    public void updateAmountOfAgent() {
        for (int j = 0; j < machineManager.getBruteForceData().getMaxAmountOfAgent(); j++) {
            amountOfAgentsChoiceBox.getItems().add(j+1);

        }
    }

    private void setToolTip() {
        toolTipError=new Tooltip("input must be a number");
        toolTipError.setId("error-tool-tip");
        //changes duration until tool tip is shown
        Utils.hackTooltipStartTiming(toolTipError,100);
        assignmentSizeText.setOnMouseEntered(event -> showToolTip());
        assignmentSizeText.setOnMouseExited(event ->toolTipError.hide());
    }

    @FXML
    void runClicked(ActionEvent event) {
        try {

            String input =dictionary.cleanWord(inputArea.getText());
            System.out.println(input);
            if(!dictionary.isAtDictionary(input))
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("word not in dictionary");
                a.setTitle("Invalid word");
                a.show();
            }
            String output = this.machineManager.encryptSentence(input.toUpperCase());
            outputArea.setText(output);
            mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
            allDataValid();
        }
        catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }
    }
    @FXML
    void pauseClicked(ActionEvent event) {
        Button pauseAndResume= (Button) event.getSource();
        if(pauseAndResume.getText().equals("pause"))
        {
            pauseAndResume.setText("resume");
            currentRunningTask.pause();

        }
        else
        {
            pauseAndResume.setText("pause");
            currentRunningTask.resume();
        }

    }
    @FXML
    void stopClicked(ActionEvent event) {
        enableOrDisableInputButton(false);

        currentRunningTask.stop();

        pauseButton.setDisable(true);
        stopButton.setDisable(true);


    }




    @FXML
    void startBruteForce(ActionEvent event) {
        candidatesFlowPane.getChildren().clear();

        pullDmData();
        currentRunningTask = new FindCandidateTask(dmData, createUIAdapter(),this,this.machineManager);
        new Thread(currentRunningTask).start();

        pauseButton.setDisable(false);
        stopButton.setDisable(false);


        enableOrDisableInputButton(true);


    }

    private void enableOrDisableInputButton(boolean disable) {
        startButton.setDisable(disable);
        difficultyComboBox.setDisable(disable);
        amountOfAgentsChoiceBox.setDisable(disable);
        assignmentSizeText.setDisable(disable);
        inputArea.setDisable(disable);
    }


    @FXML
    void selectedWord(MouseEvent event) {
        if (event.getClickCount() == 2) {
            this.addWordToInput(dictionaryTable.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void updateCode(String code) {
        currentCode.setText(code);
    }

    //--------------------------------------------Task related--------------------------------

    private void pullDmData() {
        dmData.setDifficulty(difficultyComboBox.getValue());
        dmData.setAmountOfAgents(amountOfAgentsChoiceBox.getValue());
        dmData.setEncryptedString(outputArea.getText());
    }
    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                decryptionCandidateData -> {
                    /*createWordCandidate(DecryptionCandidate);*/
                    createWordCandidate(decryptionCandidateData);
                },
                (delta) -> {
                    this.totalFoundCandidate.set(delta);
                },
                () -> {
                    this.totalFoundCandidate.set(totalFoundCandidate.get() + 1);
                },
                ()->{
                    enableOrDisableInputButton(false);
                    this.currentRunningTask.stop();
                    this.stopButton.setDisable(true);
                    this.pauseButton.setDisable(true);

                }

        );
    }

    private void createWordCandidate(DecryptionCandidate decryptionCandidate) {

        try {
            Node wordCandidate = loadCandidate(decryptionCandidate);

            FlowPane.setMargin(wordCandidate, new Insets(2, 10, 2, 10));
            this.candidatesFlowPane.getChildren().add(wordCandidate);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node loadCandidate(DecryptionCandidate decryptionCandidate) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/app/smallComponent/wordCandidate.fxml"));
        Node wordCandidate = loader.load();
        wordCandidate.setFocusTraversable(false);

        CandidateController wordCandidateController = loader.getController();
        wordCandidateController.setTextFont();
        wordCandidateController.setText(decryptionCandidate.getDecryptedString());
        wordCandidateController.setAgent(String.valueOf(decryptionCandidate.getAgentID()));
        wordCandidateController.setCode(decryptionCandidate.getCodeConfiguration());
        return wordCandidate;
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask) {

        // task message
 /*       taskMessageLabel.textProperty().bind(aTask.messageProperty());*/

        // task progress bar
        taskProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        percentageLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

/*        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });*/
    }


    //--------------------------------------------inner Logic button related--------------------------------
    private <T> T loadFXML(String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }
    private void setInitialDictionaryTable() {
        wordsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        /*String[] splitStringsOne = loremIpsumText.split(" ");*/

        /*Arrays.stream(splitStringsOne).sequential().forEach(s ->dictionaryTable.getItems().add(s));*/
    }
    public void updateInitialDictionaryTable(){
        decryptManager=new DecryptManager(machineManager,dmData);
        dictionary =decryptManager.getDictionary();
        dictionary.getDictionary().forEach(s ->dictionaryTable.getItems().add(s) );
    }
    private void showToolTip() {
        String currentInput = assignmentSizeText.getText();

        if(!currentInput.isEmpty()&&!validAssignment)
        {
            renderToolTip();
        }
        else {
            toolTipError.hide();
        }
    }
    private void allDataValid()
    {
        if(assignmentInputValid(assignmentSizeText.getText()))
        {
            if(difficultyComboBox.getValue()!=null &&amountOfAgentsChoiceBox.getValue()!=null)
            {
                if(!outputArea.getText().isEmpty()) {
                    startButton.setDisable(false);
                    return;
                }
            }
        }
        startButton.setDisable(true);
    }
    private boolean assignmentInputValid(String newValue) {
        if(newValue.isEmpty())
        {
            assignmentSizeText.setId(null);
            toolTipError.hide();
        }
        else {
            if (Utils.isNumeric(newValue)) {
                dmData.setAssignmentSize(Integer.parseInt(newValue));
                assignmentSizeText.setId(null);
                validAssignment=true;
                toolTipError.hide();
                return true;
            } else {
                validAssignment=false;
                renderToolTip();
                assignmentSizeText.setId("error-text-field");
            }
        }
        return false;
    }
    private void renderToolTip() {
        Bounds boundsInScene = assignmentSizeText.localToScreen(assignmentSizeText.getBoundsInLocal());
        toolTipError.show(assignmentSizeText, boundsInScene.getMaxX(), boundsInScene.getMaxY() + 15);
    }
    private void addWordToInput(String selectedWord) {
        String inputText = inputArea.getText();
        if(inputText.isEmpty()||inputText.charAt(inputText.length()-1)==' ')
        {
            inputArea.setText(inputText+selectedWord);
        }
        else
        {
            inputArea.setText(inputText+" "+selectedWord);
        }
    }
    private void filterDictionaryTable(String newValue) {
        dictionaryTable.getItems().clear();
        if(newValue.isEmpty())
        {
            dictionary.getDictionary().forEach(s ->dictionaryTable.getItems().add(s));

        }
        else {
            dictionary.getDictionary().stream().filter(s -> s.startsWith(newValue.toUpperCase())).
                    forEach(s ->dictionaryTable.getItems().add(s));
        }
    }

    //--------------------------------------------End: inner Logic button related--------------------------------
}
