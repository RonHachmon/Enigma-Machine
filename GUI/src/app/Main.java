package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Engine.machineutils.MachineManager;

public class Main extends Application {
    public static final String APP_FXML = "/app/mainApp.fxml";
    public static final String APP_ARMY_CSS = "/resources/css/army.css";
    public static final String ROUTED_GOTHIC_FONT = "/resources/fonts/routed-gothic.ttf";
    public static final String COMMAND_PROMPT_FONT = "/resources/fonts/windows_command_prompt.ttf";
    Stage mainStage;
    private MachineManager machineManager = new MachineManager();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cracking The Enigma");
        Parent load = FXMLLoader.load(getClass().getResource(APP_FXML));
        Scene scene = new Scene(load,1100,670);
        loadingFonts();
        scene.getStylesheets().add(String.valueOf(getClass().getResource(APP_ARMY_CSS)));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadingFonts() {
        Font.loadFont(getClass().getResourceAsStream(ROUTED_GOTHIC_FONT),17);
        //.getName to get font name
        Font.loadFont(getClass().getResourceAsStream(COMMAND_PROMPT_FONT),17);
    }
}
