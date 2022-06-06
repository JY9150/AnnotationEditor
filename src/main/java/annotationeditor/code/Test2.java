package annotationeditor.code;

import annotationeditor.code.ScriptEdit.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Test2 extends Application {
    @Override
    public void start(Stage primaryStage){
        StackPane stackPane1 = new StackPane(new Label("0"));
        //codeData codeData = new codeData("test.txt",new codeType("code.txt").getTypeList());
        codeData codeData = new codeData("C:/Users/howar/Desktop/IDEA java/src/main/java/A4_110504513.java",new codeType("code.txt").getTypeList());
        ArrayList<codeInformation> infoList = codeData.getInfoList();
        InformationLayout informationLayout = new InformationLayout(infoList);
        StackPane stackPane2 = new StackPane(new Label("2"));
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(stackPane1,informationLayout,stackPane2);
        splitPane.setDividerPosition(1,250);
        FXMLLoader fxmlLoader = new FXMLLoader(Test2.class.getResource("Palette.fxml"));
        Scene scene = new Scene(splitPane,1000,900);
        primaryStage.setTitle("Palette test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
