package app.utils.candidate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import utils.Utils;

import java.awt.*;

public class CandidateController {

    @FXML
    private Label textLabel;

    @FXML
    private Label codeLabel;

    @FXML
    private Label agentLabel;

    private Tooltip fullText;
    public void setTextFont(Font font)
    {
        textLabel.setFont(font);
    }
    public void setText(String text)
    {
        fullText=new Tooltip(text);
        Utils.hackTooltipStartTiming(fullText,500);
        textLabel.setTooltip(fullText);
        textLabel.setText(text);
    }

}