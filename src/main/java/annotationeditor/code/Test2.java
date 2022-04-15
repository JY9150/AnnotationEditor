package annotationeditor.code;

import annotationeditor.code.ScriptEdit.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Test2 extends Application {
    @Override
    public void start(Stage primaryStage){
        StackPane stackPane1 = new StackPane(new Label("0"));
        codeData codeData = new codeData("test.txt",new codeType("code.txt").getTypeList());
        SecondPane secondPane = new SecondPane(codeData.getInfoList());
        ScrollPane scrollPane = new ScrollPane(secondPane);
        StackPane stackPane2 = new StackPane(new Label("2"));
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(stackPane1,scrollPane,stackPane2);
        splitPane.setDividerPosition(1,250);
        Scene scene = new Scene(splitPane,1000,600);
        primaryStage.setTitle("secondPane test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
class SecondPane extends Pane{
    VBox vBox = new VBox(15);
    public SecondPane(ArrayList<codeInformation> informationArrayList){
        for (codeInformation information : informationArrayList){
            InfoPane infoPane = new InfoPane(information);
            vBox.getChildren().add(infoPane);
        }
        getChildren().add(vBox);
    }
    public void addPane(codeInformation information){
        getChildren().add(new InfoPane(information));
    }
    public void paint(){

    }
    static class InfoPane extends Pane{
        HBox hBox = new HBox(10);
        VBox vBox = new VBox();
        Text text = new Text();
        TextArea textArea = new TextArea();
        Button button = new Button("V");
        public InfoPane(codeInformation information){
            text.setText(String.format("%10s  %10s",information.type,information.name));
            text.setFont(new Font(15));
            button.setOnAction(e -> {
                if (button.getText().equals("V")) {
                    button.setText("^");
                    vBox.getChildren().add(textArea);
                }
                else{
                    button.setText("V");
                    vBox.getChildren().remove(1);
                }
            });
            hBox.getChildren().addAll(text,button);
            vBox.setPadding(new Insets(5));
            textArea.setMaxWidth(500);
            textArea.setMaxHeight(100);
            vBox.getChildren().addAll(hBox);
            getChildren().add(vBox);
        }
    }
}
