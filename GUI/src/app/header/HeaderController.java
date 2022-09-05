package app.header;

import app.MainAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import Engine.machineutils.MachineManager;

public class HeaderController {

    @FXML
    private Button loadXMLButton;

    @FXML
    private TextField currentPath;

    private MachineManager machineManager;
    private MainAppController mainAppController;


    @FXML
    void loadXML(ActionEvent event) {
        machineManager.createMachineFromXML("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
        this.currentPath.setText("C:\\Users\\97254\\IdeaProjects\\Enigma-Machine\\test files\\ex1-sanity-paper-enigma.xml");
        mainAppController.updateMachineInformation();

/*        FileChooser fileChooser = configFileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null)
        {
                new Thread(()->
                {
                    try {
                        System.out.println("Check 0");
                        System.out.println(selectedFile.getAbsoluteFile());
                        machineManager.createMachineFromXML(selectedFile.getAbsoluteFile().toString());
                        System.out.println("Check 1");
                        Platform.runLater(()->
                        {
                            this.currentPath.setText(selectedFile.getAbsolutePath());
                            mainAppController.updateMachineInformation();

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

        }*/


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
}