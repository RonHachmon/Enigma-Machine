package app.bodies;

import DTO.DMData;
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
    private ComboBox<eDifficulty> difficultyComboBox;
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
    private Set<String> dictionary;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

       Arrays.stream(eDifficulty.values()).sequential().forEach(eDifficulty ->difficultyComboBox.getItems().add(eDifficulty));
        amountOfCandidateFound.textProperty().bind(Bindings.format("%,d", totalFoundCandidate));

        setInitialDictionaryTable();

        setToolTip();

        assignmentSizeText.textProperty().
                addListener((object, oldValue, newValue)->validateInput(newValue));

        searchBar.textProperty().
                addListener((object, oldValue, newValue)->filterDictionaryTable(newValue));

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
            String output = this.machineManager.encryptSentence(inputArea.getText().toUpperCase());
            outputArea.setText(output);
            mainAppController.updateMachineCode(machineManager.getCurrentCodeSetting());
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
    void selectedWord(MouseEvent event) {
        if (event.getClickCount() == 2) {
            this.addWordToInput(dictionaryTable.getSelectionModel().getSelectedItem());
        }
    }



    @FXML
    void startBruteForce(ActionEvent event) {
        currentRunningTask = new FindCandidateTask(0, createUIAdapter(),this);
        new Thread(currentRunningTask).start();
        startButton.setDisable(true);


        pauseButton.setDisable(false);
        stopButton.setDisable(false);


    }
    @FXML
    void stopClicked(ActionEvent event) {
        startButton.setDisable(false);

    }

    @Override
    public void updateCode(String code) {
        currentCode.setText(code);
    }

    //--------------------------------------------Task related--------------------------------

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                PrimeNumberData -> {
                    createWordCandidate(PrimeNumberData.getInteger());
                   /* createCandidate(PrimeNumberData.getInteger());*/
                },
                (delta) -> {
               /*     HistogramsUtils.log("EDT: INCREASE total processed words");
                    totalProcessedWords.set(totalProcessedWords.get() + delta);*/
                },
                () -> {
                    this.totalFoundCandidate.set(totalFoundCandidate.get() + 1);
                }

        );
    }
    private void createCandidate(Integer number) {

            TextField candidate = loadFXML("/app/smallComponent/candidate.fxml");
            candidate.setText(number.toString());
            FlowPane.setMargin(candidate, new Insets(2, 10, 2, 10));
            this.candidatesFlowPane.getChildren().add(candidate);

    }
    private void createWordCandidate(Integer integer) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/app/smallComponent/wordCandidate.fxml"));
            Node wordCandidate = loader.load();
            wordCandidate.setFocusTraversable(false);
            CandidateController wordCandidateController = loader.getController();

            wordCandidateController.setTextFont();
            wordCandidateController.setText("HAIL HITLER");
            FlowPane.setMargin(wordCandidate, new Insets(2, 10, 2, 10));
            this.candidatesFlowPane.getChildren().add(wordCandidate);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        dictionary =machineManager.getBruteForceData().getDictionary().getDictionary();
        dictionary.forEach(s ->dictionaryTable.getItems().add(s) );
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
    private void validateInput(String newValue) {
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
            } else {
                validAssignment=false;
                renderToolTip();
                assignmentSizeText.setId("error-text-field");
            }
        }
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
            dictionary.forEach(s ->dictionaryTable.getItems().add(s));

        }
        else {
            dictionary.stream().filter(s -> s.startsWith(newValue.toUpperCase())).
                    forEach(s ->dictionaryTable.getItems().add(s));
        }
    }

    //--------------------------------------------End: inner Logic button related--------------------------------
}
