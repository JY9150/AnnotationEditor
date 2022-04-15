package annotationeditor.code;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    // this is Jason commit test
    @FXML
    private TreeView<String> treeTable;
    @FXML
    private TextField RootPath_textField;
    @FXML
    private TextArea Codeview;
    @FXML
    private ComboBox<String> fileFilter_comboBox;
    private final ObservableList<String> ob = FXCollections.observableArrayList("All", ".java", ".cs", ".txt");


    @Override
    public void initialize(URL url, ResourceBundle rb){
        fileFilter_comboBox.setItems(ob);
        fileFilter_comboBox.setValue("All");
        loadTreeTable();
    }

    public void Changed(){
        loadTreeTable();
    }

    public void selectItem(){
        //???
        TreeItem<String> item = treeTable.getSelectionModel().getSelectedItem();
        if (item != null){
            System.out.println(item.getValue());
        }
    }


    private void loadTreeTable(){
        String inpath = RootPath_textField.getText();
        File rootFile = new File(inpath);
        if (rootFile.exists()){
            TreeItem<String> root = new TreeItem<>(rootFile.getName());
            findInner(rootFile, root, fileFilter_comboBox.getValue() != "All" );
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

    private void findInner(File file, TreeItem<String> root, boolean fileFilter){
        File[] innerfiles = file.listFiles();
        for (File value : innerfiles) {
            if (value.isDirectory()) {
                TreeItem<String> innerRoot = new TreeItem<>(value.getName());
                findInner(value, innerRoot,fileFilter);
                root.getChildren().add(innerRoot);
            }
        }
        for (File value : innerfiles) {
            if (fileFilter){
                if (value.isFile() && passFileFilter(value)) {
                    root.getChildren().add((new TreeItem<>(value.getName())));
                }
            } else {
                if (value.isFile()) {
                    root.getChildren().add((new TreeItem<>(value.getName())));
                }
            }
        }

    }
    private boolean passFileFilter(File file){
        return file.getName().contains(fileFilter_comboBox.getValue());

//        String[] filters = new String[4];
//        for (String s : filters) {
//            if (s.equals(file.getName())){
//                return true;
//            }
//        }
//        return false;
    }
}