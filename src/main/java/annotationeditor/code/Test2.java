package annotationeditor.code;

import annotationeditor.code.ScriptEdit.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Test2 extends Application {
    @Override
    public void start(Stage primaryStage){
        StackPane stackPane1 = new StackPane(new Label("0"));
        //codeData codeData = new codeData("test.txt",new codeType("code.txt").getTypeList());
        codeData codeData = new codeData("C:/Users/howar/Desktop/IDEA java/src/A4_110504513.java",new codeType("code.txt").getTypeList());
        ArrayList<codeInformation> infoList = codeData.getInfoList();
//        infoList.get(0).isDocCommentExist = true;
//        infoList.get(0).docComment.description = "test";
//        infoList.get(0).docComment.addComment("@param lineIndex","lineIndex");
//        infoList.get(0).docComment.addComment("@param type","type");
//        infoList.get(1).docComment.addComment("@return", "String");
//        infoList.get(2).type = "value";
//        infoList.get(3).type = "function";
        InformationLayout informationLayout = new InformationLayout(infoList);
        StackPane stackPane2 = new StackPane(new Label("2"));
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(stackPane1,informationLayout,stackPane2);
        splitPane.setDividerPosition(1,250);
        Scene scene = new Scene(splitPane,1000,600);
        primaryStage.setTitle("secondPane test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
