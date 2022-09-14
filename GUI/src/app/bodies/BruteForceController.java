package app.bodies;

import app.bodies.absractScene.MainAppScene;
import app.bodies.interfaces.CodeHolder;
import app.utils.FindCandidateTask;
import app.utils.UIAdapter;
import app.utils.eDifficulty;
import app.utils.threads.DMData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import utils.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
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

    private List<String> dictWords=new ArrayList<>();
    private  FindCandidateTask currentRunningTask;
    private Tooltip toolTipError;
    private DMData dmData=new DMData();
    private String loremIpsumText ="Lorem ipsum dolor sit amet consectetur adipisicing elit." +
            " Adipisci quisquam suscipit magni dicta, repellat, quod illo harum libero" +
            " esse quidem veniam dolor dolores! Optio rerum atque maxime vero at voluptatibus!";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

       Arrays.stream(eDifficulty.values()).sequential().forEach(eDifficulty ->difficultyComboBox.getItems().add(eDifficulty));
        amountOfCandidateFound.textProperty().bind(Bindings.format("%,d", totalFoundCandidate));
        wordsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        String[] splitStringsOne = loremIpsumText.split(" ");
        Arrays.stream(splitStringsOne).sequential().forEach(s ->dictionaryTable.getItems().add(s));
        toolTipError=new Tooltip("input must be a number");
        toolTipError.setId("error-tool-tip");
        //changes duration until tool tip is shown
        Utils.hackTooltipStartTiming(toolTipError);
        assignmentSizeText.setOnMouseEntered(event ->toolTipError.show(assignmentSizeText, event.getScreenX(), event.getScreenY() + 15));
        assignmentSizeText.setOnMouseExited(event ->toolTipError.hide());

        assignmentSizeText.textProperty().addListener((object, oldValue, newValue)->{
            if(newValue.isEmpty())
            {
                assignmentSizeText.setTooltip(null);
                assignmentSizeText.setId(null);
            }
            else {
                if (Utils.isNumeric(newValue)) {
                    dmData.setAssignmentSize(Integer.parseInt(newValue));
                    assignmentSizeText.setTooltip(null);
                    assignmentSizeText.setId(null);

                } else {
                    assignmentSizeText.setId("error-text-field");
                    assignmentSizeText.setTooltip(toolTipError);
                }
            }

        } );
        searchBar.textProperty().addListener((object, oldValue, newValue)->{
            String[] splitStrings = loremIpsumText.split(" ");
            dictionaryTable.getItems().clear();
            if(newValue.isEmpty())
            {
                Arrays.stream(splitStrings).sequential().forEach(s ->dictionaryTable.getItems().add(s));
            }
            else {
                Arrays.stream(splitStrings).filter(s -> s.startsWith(newValue)).forEach(s ->dictionaryTable.getItems().add(s));
            }
        } );
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
        if (event.getClickCount() == 2) //Checking double click
        {
            System.out.println(dictionaryTable.getSelectionModel().getSelectedItem());
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
                    createCandidate(PrimeNumberData.getInteger());
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

    //--------------------------------------------End: Task related--------------------------------
    private <T> T loadFXML(String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }
}
