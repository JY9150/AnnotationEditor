package annotationeditor.code;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Palette {
    PalettePane palettePane = new PalettePane();
    public Stage getStage(){
        Scene scene = palettePane.getScene();
        if (scene==null){
            scene = new Scene(palettePane,500,500);
        }
        Stage stage = new Stage();
        scene.setRoot(palettePane);
        stage.setTitle("調色盤");
        stage.setScene(scene);
        return stage;
    }

    /*
    public String getCurrentRGB(){
        String RGBinHex = "#";
        for (int i=0;i<3;i++){
            String temp = String.format("%x",(int)slider[i].getValue());
            RGBinHex += temp.length()<2?"0"+temp:temp;
        }
        return RGBinHex;
    }
    */

    public String getCurrentRGB(){
        String RGBinHex = "#";
        for (int i=0;i<3;i++){
            String temp = String.format("%x",palettePane.sliderPanes[i].value);
            RGBinHex += temp.length()<2?"0"+temp:temp;
        }
        return RGBinHex;
    }
    class PalettePane extends Pane{
        VBox vBox = new VBox(20);
        SliderPane[] sliderPanes = new SliderPane[3];
        Rectangle rectangle = new Rectangle(300,300);
        String[] text = {"R : ","G : ","B : "};
        public PalettePane(){
            vBox.setAlignment(Pos.CENTER);
            rectangle.setFill(Color.rgb(0,0,0));
            vBox.getChildren().add(rectangle);
            for (int i=0;i<3;i++){
                sliderPanes[i] = new SliderPane(text[i]);
                vBox.getChildren().add(sliderPanes[i]);
            }
            getChildren().add(vBox);
        }
        public String getCurrentRGB(){
            String RGBinHex = "#";
            for (int i=0;i<3;i++){
                String temp = String.format("%x",sliderPanes[i].value);
                RGBinHex += temp.length()<2?"0"+temp:temp;
            }
            return RGBinHex;
        }
        class SliderPane extends Pane{
            HBox hBox = new HBox(10);
            Label label = new Label();
            TextField textField = new TextField();
            Slider slider = new Slider(0,255,0);
            int value = 0;

            public SliderPane(String text) {
                hBox.setPadding(new Insets(0, 0, 0, 10));
                hBox.setAlignment(Pos.BASELINE_LEFT);
                label.setMinWidth(20);
                label.setFont(Font.font(15));
                label.setText(text);
                textField.setAlignment(Pos.BASELINE_RIGHT);
                textField.setMaxWidth(40);
                textField.setText(value + "");
                textField.setOnAction(e ->{
                    if (value<0) {value = 0;}
                    else if (value>255) {value = 255;}
                    try {
                        value = Integer.parseInt(textField.getText());
                    }catch (NumberFormatException ex){
                        System.out.println("invalid input");
                        textField.setText(value+"");
                        return;
                    }
                    slider.setValue(value);
                });
                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);
                slider.setMajorTickUnit(16);
                slider.setMinorTickCount(16);
                slider.setMinWidth(400);
                slider.valueProperty().addListener(ov -> {
                    value = (int)slider.getValue();
                    rectangle.setFill(Color.rgb(sliderPanes[0].value,sliderPanes[1].value,sliderPanes[2].value));
                    textField.setText(value + "");
                    System.out.println(getCurrentRGB());
                });
                hBox.getChildren().addAll(label, textField, slider);
                getChildren().add(hBox);
            }
        }
    }
}
