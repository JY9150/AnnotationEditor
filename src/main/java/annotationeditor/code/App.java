package annotationeditor.code;

import annotationeditor.code.FileSystem.*;
import annotationeditor.code.ScriptEdit.*;

import javafx.application.Application;
import javafx.event.EventDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class App extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        DataHolder DH = DataHolder.getInstance();
        DH.codeType = new codeType("");
        DH.codeData = new codeData("",DH.codeType.getTypeList());

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("AppLayout.fxml"));
        System.out.println(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setTitle("註解編輯器");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}