module finalproject.finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires json.simple;

    opens annotationeditor.code to javafx.fxml;
    exports annotationeditor.code;
    exports annotationeditor.code.ScriptEdit;
    opens annotationeditor.code.ScriptEdit to javafx.fxml;
    exports annotationeditor.code.FileSystem;
    opens annotationeditor.code.FileSystem to javafx.fxml;
}