package annotationeditor.code;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    // this is Jason commit test
    @FXML
    private TreeView treeTable;
    @FXML
    private TextField RootPath_textField;
    @FXML
    private TextArea Codeview;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadTreeTable();
    }

    public void textChanged(){
        loadTreeTable();
    }

    public void selectItem(){
        //???
        TreeItem<String> item = (TreeItem<String>) treeTable.getSelectionModel().getSelectedItem();
        if (item != null){
            System.out.println(item.getValue());
        }
    }


    private void loadTreeTable(){
        String inpath = RootPath_textField.getText();
        File rootFile = new File(inpath);
        if (rootFile.exists()){
            TreeItem<String> root = new TreeItem<>(rootFile.getName());
            findInner(rootFile, root);
            root.setExpanded(true);
            treeTable.setRoot(root);
        } else {
            treeTable.setRoot(new TreeItem<>("Invalidate Path"));
        }
    }

    private void findInner(File file, TreeItem<String> root){
        File[] innerfiles = file.listFiles();
        for (File value : innerfiles) {
            if (value.isDirectory()) {
                TreeItem<String> innerRoot = new TreeItem<>(value.getName());
                findInner(value, innerRoot);
                root.getChildren().add(innerRoot);
            }
        }
        for (File value : innerfiles) {
            if (value.isFile()) {
                root.getChildren().add((new TreeItem<>(value.getName())));
            }
        }

    }
}