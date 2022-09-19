package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Engine.machineutils.MachineManager;

public class Main extends Application {
    public static final String APP_FXML = "/app/mainApp.fxml";
    public static final String APP_ARMY_CSS = "/app/army.css";
    Stage mainStage;
    private MachineManager machineManager = new MachineManager();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cracking The Enigma");
        Parent load = FXMLLoader.load(getClass().getResource(APP_FXML));
        Scene scene = new Scene(load,1100,600);
        scene.getStylesheets().add(String.valueOf(getClass().getResource(APP_ARMY_CSS)));
   /*     scene.getStylesheets().add("/app/army.css");*/
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
