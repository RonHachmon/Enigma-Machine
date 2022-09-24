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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BruteForceController extends MainAppScene implements Initializable, CodeHolder {

    public static final String WORD_CANDIDATE_FXML = "/app/smallComponent/wordCandidate.fxml";
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
    @FXML
    private Label encryptTimeLabel;

    @FXML
    private Label averageTimeLabel;

    @FXML
    private Label taskDone;

    @FXML
    private FontAwesomeIconView pauseFontAwesome;

    private SimpleIntegerProperty totalFoundCandidate=new SimpleIntegerProperty();
    private SimpleStringProperty amountDone=new SimpleStringProperty();
    private boolean validAssignment=false;
    private boolean pauseFlag = false;
    private  FindCandidateTask currentRunningTask;

    private Tooltip toolTipError;
    private DMData dmData = new DMData();
    private Dictionary dictionary;
    private DecryptManager decryptManager;
    private Instant startTaskClock;
    private boolean toAnimate =false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Arrays.stream(DifficultyLevel.values()).sequential().forEach(eDifficulty -> difficultyComboBox.getItems().add(eDifficulty));
        amountOfCandidateFound.textProperty().bind(Bindings.format("%,d", totalFoundCandidate));

        setInitialDictionaryTable();

        setToolTip();

        bindListenersToInputButtons();


        searchBar.textProperty().
                addListener((object, oldValue, newValue) -> filterDictionaryTable(newValue));

    }

    private void bindListenersToInputButtons() {
        assignmentSizeText.textProperty().
                addListener((object, oldValue, newValue) -> allDataValid());
        difficultyComboBox.valueProperty().addListener((object, oldValue, newValue) -> allDataValid());
        amountOfAgentsChoiceBox.valueProperty().addListener((object, oldValue, newValue) -> allDataValid());
        outputArea.visibleProperty().addListener((object, oldValue, newValue) -> allDataValid());
    }

    public void updateAmountOfAgent() {
        amountOfAgentsChoiceBox.getItems().clear();
        for (int j = 0; j < machineManager.getBruteForceData().getMaxAmountOfAgent(); j++) {
            amountOfAgentsChoiceBox.getItems().add(j + 1);

        }
    }

    private void setToolTip() {
        toolTipError = new Tooltip("input must be a positive number");
        toolTipError.setId("error-tool-tip");
        //changes duration until tool tip is shown
        Utils.hackTooltipStartTiming(toolTipError, 100);
        assignmentSizeText.setOnMouseEntered(event -> showToolTip());
        assignmentSizeText.setOnMouseExited(event -> toolTipError.hide());
    }

    @FXML
    void runClicked(ActionEvent event) {
        try {

            String input = dictionary.cleanWord(inputArea.getText());
            if (!dictionary.isAtDictionary(input.toUpperCase())) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("word not in dictionary");
                a.setTitle("Invalid word");
                a.show();
                return;
            }
            String output = this.machineManager.encryptSentence(input.toUpperCase());
            outputArea.setText(output);
            mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
            allDataValid();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }
    }

    @FXML
    void pauseClicked(ActionEvent event) {
        pauseFlag = !pauseFlag;
        if (pauseFlag) {
            pauseFontAwesome.setGlyphName("PLAY");
            currentRunningTask.pause();
        }
        else {
            pauseFontAwesome.setGlyphName("PAUSE");
            currentRunningTask.resume();
        }
    }

    @FXML
    void stopClicked(ActionEvent event) {
        enableOrDisableInputButton(false);
        long avgTime = currentRunningTask.getAvgTime();
        this.averageTimeLabel.setText(String.valueOf(avgTime));
        long encryptionTimeInNanoSeconds = Duration.between(startTaskClock, Instant.now()).toMillis();
        this.encryptTimeLabel.setText(String.valueOf(encryptionTimeInNanoSeconds));
        currentRunningTask.stop();

        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        this.pauseFlag=false;
        pauseFontAwesome.setGlyphName("PAUSE");


    }


    @FXML
    void startBruteForce(ActionEvent event) {
        this.resetAll();

         this.startTaskClock = Instant.now();
        pullDmData();
        currentRunningTask = new FindCandidateTask(dmData, createUIAdapter(), this, this.machineManager);
        new Thread(currentRunningTask).start();

        pauseButton.setDisable(false);
        stopButton.setDisable(false);


        enableOrDisableInputButton(true);


    }

    private void resetAll() {
        candidatesFlowPane.getChildren().clear();
        this.totalFoundCandidate.set(0);
        this.encryptTimeLabel.setText("");
        this.averageTimeLabel.setText("");
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
                //on done
                () -> {
                    enableOrDisableInputButton(false);
                    this.currentRunningTask.stop();
                    this.stopButton.setDisable(true);
                    this.pauseButton.setDisable(true);
                    long avgTime = currentRunningTask.getAvgTime();
                    this.averageTimeLabel.setText(avgTime+" ns");
                    long encryptionTimeInNanoSeconds = (Duration.between(startTaskClock, Instant.now()).toMillis())/1_000;
                    this.encryptTimeLabel.setText(encryptionTimeInNanoSeconds+" sec");
                    scaleAnimation();

                },
                (string) ->{
                    this.taskDone.setText(string);
                }



        );
    }
    private void scaleAnimation() {
        if (toAnimate) {
            ScaleTransition scaleTransition = new ScaleTransition();
            scaleTransition.setNode(amountOfCandidateFound);
            scaleTransition.setDuration(javafx.util.Duration.millis(800));
            scaleTransition.setByX(2.0);
            scaleTransition.setByY(2.0);
            scaleTransition.setCycleCount(2);
            scaleTransition.setAutoReverse(true);
            scaleTransition.play();
        }
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
        loader.setLocation(getClass().getResource(WORD_CANDIDATE_FXML));
        Node wordCandidate = loader.load();
        wordCandidate.setFocusTraversable(false);

        CandidateController wordCandidateController = loader.getController();
        wordCandidateController.setTextFont();
        wordCandidateController.setText(decryptionCandidate.getDecryptedString());
        wordCandidateController.setAgent(String.valueOf(decryptionCandidate.getAgentID()));
        wordCandidateController.setCode(decryptionCandidate.getCodeConfiguration());
        return wordCandidate;
    }

    public void bindTaskToUIComponents(FindCandidateTask aTask) {


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

    public void updateInitialDictionaryTable() {
        decryptManager = new DecryptManager(machineManager, dmData);
        dictionary = decryptManager.getDictionary();
        dictionaryTable.getItems().clear();
        dictionary.getDictionary().forEach(s -> dictionaryTable.getItems().add(s));
    }

    private void showToolTip() {
        String currentInput = assignmentSizeText.getText();

        if (!currentInput.isEmpty() && !validAssignment) {
            renderToolTip();
        } else {
            toolTipError.hide();
        }
    }

    private void allDataValid() {
        if (assignmentInputValid(assignmentSizeText.getText())) {
            if (difficultyComboBox.getValue() != null && amountOfAgentsChoiceBox.getValue() != null) {
                if (!outputArea.getText().isEmpty()) {
                    startButton.setDisable(false);
                    return;
                }
            }
        }
        startButton.setDisable(true);
    }

    private boolean assignmentInputValid(String newValue) {
        if (newValue.isEmpty()) {
            assignmentSizeText.setId(null);
            toolTipError.hide();
        }
        else {
            if (Utils.isNumeric(newValue)) {
                int number = Integer.parseInt(newValue);
                if(number>0) {
                    dmData.setAssignmentSize(Integer.parseInt(newValue));
                    assignmentSizeText.setId(null);
                    validAssignment = true;
                    toolTipError.hide();
                    return true;
                }
                else {
                    validAssignment = false;
                    renderToolTip();
                    assignmentSizeText.setId("error-text-field");
                }
            } else {
                validAssignment = false;
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
        if (inputText.isEmpty() || inputText.charAt(inputText.length() - 1) == ' ') {
            inputArea.setText(inputText + selectedWord);
        } else {
            inputArea.setText(inputText + " " + selectedWord);
        }
    }

    private void filterDictionaryTable(String newValue) {
        dictionaryTable.getItems().clear();
        if (newValue.isEmpty()) {
            dictionary.getDictionary().forEach(s -> dictionaryTable.getItems().add(s));

        } else {
            dictionary.getDictionary().stream().filter(s -> s.startsWith(newValue.toUpperCase())).
                    forEach(s -> dictionaryTable.getItems().add(s));
        }
    }

    public void enableAnimation(boolean selected) {
        this.toAnimate=selected;
    }

    public void clearScreen() {
        this.candidatesFlowPane.getChildren().clear();
        inputArea.clear();
        outputArea.clear();
        this.resetAll();
        this.assignmentSizeText.setText("");
        amountOfAgentsChoiceBox.setValue(null);
        difficultyComboBox.setValue(null);
        if(this.currentRunningTask!=null)
        {
            this.currentRunningTask.reset();
        }

    }

    //--------------------------------------------End: inner Logic button related--------------------------------
}
