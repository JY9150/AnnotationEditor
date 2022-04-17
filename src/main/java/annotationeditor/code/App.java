package annotationeditor.code;

import annotationeditor.code.FileSystem.*;
import annotationeditor.code.ScriptEdit.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("AppLayout.fxml"));
        System.out.println(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("註解編輯器");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
        System.out.println("");
    }
}