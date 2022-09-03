import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SettingController implements Initializable {


    public static final String SELECTED_SWITCH_PLUG = "selected-switch-plug";
    private boolean isSettingValid = false;
    private List<ChoiceBox<Integer>> rotorIndexes=new ArrayList<>();
    private List<ChoiceBox<Character>> rotorStartingIndexes=new ArrayList<>();
    private List<Button> reflectors=new ArrayList<>();

    private List<Character> availableCharacters =new ArrayList<>();
    private MachineInformation machineInformation;


    private Integer selectedReflector=null;
    private HBox selectedSwitchPlug=null;



    @FXML
    private Button confirmSetting;

    @FXML
    private GridPane selectingRotors;

    @FXML
    private HBox rotorsSetting;

    @FXML
    private Label rotorsErrorLabel;

    @FXML
    private GridPane selectingStartingIndex;

    @FXML
    private HBox rotorsStartingIndexSetting;

    @FXML
    private GridPane selectingReflectors;

    @FXML
    private VBox availableReflectors;

    @FXML
    private GridPane switchPlugGrid;

    @FXML
    private HBox allSwitchPlugs;

    @FXML
    private Label switchPlugError;

    @FXML
    void setReflectors(ActionEvent event) {
        selectingRotors.setVisible(false);
        selectingReflectors.setVisible(true);
        selectingStartingIndex.setVisible(false);
        switchPlugGrid.setVisible(false);

    }

    @FXML
    void setRotors(ActionEvent event) {
        selectingRotors.setVisible(true);
        selectingReflectors.setVisible(false);
        selectingStartingIndex.setVisible(false);
        switchPlugGrid.setVisible(false);

    }

    @FXML
    void setStartingIndexes(ActionEvent event) {
        selectingRotors.setVisible(false);
        selectingReflectors.setVisible(false);
        selectingStartingIndex.setVisible(true);
        switchPlugGrid.setVisible(false);


    }


    @FXML
    void setSwitchPlug(ActionEvent event) {
        selectingRotors.setVisible(false);
        selectingReflectors.setVisible(false);
        selectingStartingIndex.setVisible(false);
        switchPlugGrid.setVisible(true);

    }
    @FXML
    void confirmAllSetting(ActionEvent event) {
        String errorMsg="";
        boolean isValid=false;
        if(this.isAllElementAreUniqueInListOfChoiceBox(this.getRotorIndexes()))
        {
            if(this.isAllElementAreUniqueInListOfChoiceBox(this.getAllSwitchPlugChoiceBoxes()))
            {
                if(this.arrayDoesntContainNull())
                {
                    isValid=true;
                }
                else
                {
                    errorMsg="not all field are set";
                }

            }
            else
            {
                errorMsg="not all switch plugs keys are distinct";
            }

        }
        else
        {
            errorMsg="not all rotor index are distinct";
        }

        if(isValid)
        {
            this.isSettingValid=true;
            Stage stage = (Stage) confirmSetting.getScene().getWindow();
            stage.close();

        }
        else
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText(errorMsg);
            a.setTitle("Invalid setting");
            a.show();
        }
    }

    private boolean arrayDoesntContainNull() {
        boolean allInitalized=false;
        for (int i = 0; i < rotorIndexes.size(); i++) {
            if(rotorIndexes.get(i).getValue()==null) {

                return allInitalized;

            }
        }
        for (int i = 0; i < this.rotorStartingIndexes.size(); i++) {
            if(rotorStartingIndexes.get(i).getValue()==null) {

                return allInitalized;

            }
        }
        if(selectedReflector==null)
        {
            return allInitalized;
        }

        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMachineInformation(MachineInformation machineInformation)
    {
        this.machineInformation=machineInformation;
    }

    @FXML
    void addSwitchPlug(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/smallComponent/switchPlug.fxml"));
        try {
            HBox switchPlug = loader.load();
            switchPlug.setOnMouseClicked(hboxEvent ->
            {
                HBox selectedHbox = (HBox) hboxEvent.getSource();
                selectedSwitchPlug=selectedHbox;
                this.unSelectAllSwitchPlug();
                selectedHbox.setId(SELECTED_SWITCH_PLUG);
            });

            for (int i = 0; i <switchPlug.getChildren().size() ; i++) {
                if (switchPlug.getChildren().get(i) instanceof ChoiceBox)
                {
                    ChoiceBox<Character> choiceBox=(ChoiceBox<Character>) switchPlug.getChildren().get(i);
                    choiceBox.valueProperty().addListener(
                            (ObservableValue<? extends Character> ov, Character old_val, Character new_val) -> {
                                if(isAllElementAreUniqueInListOfChoiceBox(this.getAllSwitchPlugChoiceBoxes()))
                                {
                                    this.switchPlugError.setText("");

                                }
                                else
                                {
                                    this.switchPlugError.setText("Error, each key can only once ");
                                }

                            });
                    choiceBox.setItems(FXCollections.observableArrayList(this.availableCharacters));
                }

            }
            HBox.setMargin(switchPlug,new Insets(0,12,0,12));
            this.allSwitchPlugs.getChildren().add(switchPlug);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
/*    private <T> boolean  isAllSwitchPlugCharacterUnique(List<ChoiceBox<T>> listOfChoiceBoxed) {

        Set<T> characters = new HashSet<>();
        List<ChoiceBox<Character>> allSwitchPlugChoiceBoxes = getAllSwitchPlugChoiceBoxes();
        for (int i = 0; i <allSwitchPlugChoiceBoxes.size() ; i++) {
            if (allSwitchPlugChoiceBoxes.get(i).getValue() != null) {
                if (!characters.add(allSwitchPlugChoiceBoxes.get(i).getValue())) {

                    return false;
                }
            }
        }

        return true;
    }*/

    private <T> boolean  isAllElementAreUniqueInListOfChoiceBox(List<ChoiceBox<T>> listOfChoiceBoxes) {

        Set<T> characters = new HashSet<>();
        for (int i = 0; i <listOfChoiceBoxes.size() ; i++) {
            if (listOfChoiceBoxes.get(i).getValue() != null) {
                if (!(characters.add(listOfChoiceBoxes.get(i).getValue()))) {

                    return false;
                }
            }
        }

        return true;
    }

    private void unSelectAllSwitchPlug() {
        this.allSwitchPlugs.getChildren().forEach(node -> node.setId(""));
    }

    public void setSetting( )
    {
        for (int i = 0; i < machineInformation.getAvailableChars().length(); i++) {
            this.availableCharacters.add(machineInformation.getAvailableChars().charAt(i));
        }
        setRotorsIndexesChoiceBox();

        setRotorStartingIndexChoiceBox();

        setReflectors();

    }

    private void setReflectors() {
        for (int i =0 ;i<this.machineInformation.getAvailableReflectors();i++)
        {

            Button reflector=new Button();
            reflector.setOnAction((e)->
            {
                Button selectedReflector=((Button)e.getSource());
                this.selectedReflector=convertIntToRoman((selectedReflector.getText()));

            });
            reflector.setText(convertIntToRoman(i+1));
            reflector.setId("reflector");
            availableReflectors.getChildren().add(reflector);
            reflectors.add(reflector);
            VBox.setMargin(reflector,new Insets(12,0,0,0));
        }
    }

    private void setRotorStartingIndexChoiceBox() {
        List<Character> rotors=new ArrayList<>();
        for (int j =0 ;j<machineInformation.getAvailableChars().length();j++)
        {
            rotors.add(machineInformation.getAvailableChars().charAt(j));

        }

        for (int i =0 ;i<machineInformation.getAmountOfRotorsRequired();i++)
        {
            ChoiceBox<Character> rotorStartingIndex=new ChoiceBox<>(FXCollections.observableArrayList(rotors));
            rotorsStartingIndexSetting.getChildren().add(rotorStartingIndex);
            rotorStartingIndexes.add(rotorStartingIndex);
            HBox.setMargin(rotorStartingIndex,new Insets(0,12,0,12));
        }
    }

    private void setRotorsIndexesChoiceBox() {
        List<Integer> rotors=new ArrayList<>();
        for (int j =0 ;j<machineInformation.getAmountOfRotors();j++)
        {
            rotors.add(j+1);

        }

        for (int i =0 ;i<machineInformation.getAmountOfRotorsRequired();i++)
        {

            ChoiceBox<Integer> rotorIndex=new ChoiceBox<>(FXCollections.observableArrayList(rotors));

            rotorIndex.valueProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                System.out.println("new val: " +new_val);
                if(this.isAllElementAreUniqueInListOfChoiceBox(this.rotorIndexes))
                {
                    this.rotorsErrorLabel.setText("");

                }
                else
                {
                    this.rotorsErrorLabel.setText("Error, each id must only appear once");
                }


            });
            rotorsSetting.getChildren().add(rotorIndex);
            rotorIndexes.add(rotorIndex);
            HBox.setMargin(rotorIndex,new Insets(0,12,0,12));
        }
    }
    @FXML
    void removeSwitchPlug(ActionEvent event) {
        for (int i = 0; i <this.allSwitchPlugs.getChildren().size() ; i++) {
            if(allSwitchPlugs.getChildren().get(i).getId()==SELECTED_SWITCH_PLUG)
            {
                allSwitchPlugs.getChildren().remove(i);
                break;
            }
        }
        if(isAllElementAreUniqueInListOfChoiceBox(this.getAllSwitchPlugChoiceBoxes()))
        {
            this.switchPlugError.setText("");
        }
        else
        {
            this.rotorsErrorLabel.setText("Error, each id must only appear once");

        }


    }

    private static String convertIntToRoman(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");
        }
    }
    private static Integer convertIntToRoman(String number)
    {
        switch (number) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 3;
            case "V":
                return 5;
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");
        }

    }


    public List<ChoiceBox<Integer>> getRotorIndexes() {
        return rotorIndexes;
    }

    public List<ChoiceBox<Character>> getRotorStartingIndexes() {
        return rotorStartingIndexes;
    }

    public Integer getSelectedReflector() {
        return selectedReflector;
    }
    public List<ChoiceBox<Character>> getAllSwitchPlugChoiceBoxes() {
        List<ChoiceBox<Character>> switchPlugsChoiceBoxes = new ArrayList<>();
        for (int i = 0; i <allSwitchPlugs.getChildren().size() ; i++) {
            HBox currentHbox = (HBox) allSwitchPlugs.getChildren().get(i);
            for (int j = 0; j < currentHbox.getChildren().size(); j++) {
                Node currentNode = currentHbox.getChildren().get(j);
                if (currentNode instanceof ChoiceBox) {
                    switchPlugsChoiceBoxes.add((ChoiceBox<Character>) currentNode);
                }
            }
        }

    return switchPlugsChoiceBoxes;
    }
    public boolean getIsCodeValid() {
        return isSettingValid;
    }

}