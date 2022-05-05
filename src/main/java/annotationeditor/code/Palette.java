package annotationeditor.code;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Palette implements Initializable {
    @FXML AnchorPane anchorPane;
    @FXML private ColorPicker colorPicker;
    private Color color = Color.WHITE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colorPicker.getStyleClass().add("button");
    }

    public void onClicked() {
        color = colorPicker.getValue();
    }

    public Color getColor(){
        return color;
    }
}
