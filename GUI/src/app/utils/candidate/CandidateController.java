package app.utils.candidate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.awt.*;

public class CandidateController {

    @FXML
    private Label textLabel;

    @FXML
    private Label codeLabel;

    @FXML
    private Label agentLabel;

    public void setTextFont(Font font)
    {
        textLabel.setFont(font);
    }

}