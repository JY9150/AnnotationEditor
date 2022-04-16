package annotationeditor.code;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    // this is Jason commit test
    @FXML
    private TreeView<String> fileView_treeTable;
    @FXML
    private TextField RootPath_textField;
    @FXML
    private TextArea Codeview;
    @FXML
    private ComboBox<String> fileFilter_comboBox;
    private final ObservableList<String> ob = FXCollections.observableArrayList("All", ".java", ".cs", ".txt");
    @FXML
    private ComboBox<String> codeTypeFilter_comboBox;
    private ObservableList<String> ob2 = FXCollections.observableArrayList("codeType1","codeType2");
    @FXML
    private VBox DDD;


    @Override
    /** Initializing UI
     * */
    public void initialize(URL url, ResourceBundle rb){
        fileFilter_comboBox.setItems(ob);
        fileFilter_comboBox.setValue("All");
        codeTypeFilter_comboBox.setItems(ob2);
        codeTypeFilter_comboBox.setValue("codeType1");
        loadTreeTable();
        //DDD.setVisible(false);
    }
    /**若有更改 rootPath,fileFilter 重設 fileView_treeTable
     *
     * */
    public void reloadTreeTable(){
        loadTreeTable();
    }
    /** fileView_treeTable 物件被選擇
     *
     * */
    public void selectItem(){
        TreeItem<String> item = fileView_treeTable.getSelectionModel().getSelectedItem();
        if (item != null){
            System.out.println(item.getValue());
        }
    }
    /** fileView_treeTable 物件被連點
     * */
    public void doubleClickSelectItem(){

    }
    /** 返回建觸發
     * */
    // TODO: 2022/4/16
    public void backButtonOnClick(){
        System.out.println("Back");
    }

    /* 重整fileView_treeTable
    * */
    private void loadTreeTable(){
        String inpath = RootPath_textField.getText();
        File rootFile = new File(inpath);
        if (rootFile.exists()){
            TreeItem<String> root = new TreeItem<>(rootFile.getName());
            findInner(rootFile, root, fileFilter_comboBox.getValue() != "All" );
            root.setExpanded(true);
            fileView_treeTable.setRoot(root);
        } else {
            fileView_treeTable.setRoot(new TreeItem<>("Invalidate Path"));
        }
    }
    @Deprecated
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
    /*
     */
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
    private void findInner2(File file, TreeItem<String> root, boolean fileFilter){
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
    /* Check if the File object is the file type we want
    * */
    private boolean passFileFilter(File file){
        return file.getName().contains(fileFilter_comboBox.getValue());
    }
}