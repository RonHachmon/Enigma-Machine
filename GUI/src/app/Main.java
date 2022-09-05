package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Engine.machineutils.MachineManager;

public class Main extends Application {
    Stage mainStage;
    private MachineManager machineManager = new MachineManager();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Niggas in Paris");
        Parent load = FXMLLoader.load(getClass().getResource("/app/mainApp.fxml"));
        Scene scene = new Scene(load,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
