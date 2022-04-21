package annotationeditor.code;

import annotationeditor.code.FileSystem.File_System;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    // this is Jason commit test
    @FXML private TreeTableView<File> fileView_treeTable;
    @FXML private TreeTableColumn<File, String> col1;
    @FXML private TextField RootPath_textField;
    @FXML private TextArea CodeView;
    @FXML private ComboBox<String> fileFilter_comboBox;
          private final ObservableList<String> ob = FXCollections.observableArrayList("All", ".java", ".cs", ".txt");
    @FXML private ComboBox<String> codeTypeFilter_comboBox;
          private ObservableList<String> ob2 = FXCollections.observableArrayList("codeType1","codeType2");
    @FXML private AnchorPane sideBar;
    @FXML private AnchorPane sideBar_View;
    @FXML private ImageView user_image;
    @FXML private ImageView file_image;
    @FXML private ImageView home_image;
    @FXML private ImageView settings_image;
    @FXML private VBox fileView_pane;
          private Image folder_icon = File_System.getImage("fileIcon.png",15,15);
          private boolean isVisible = false;


    @Override
    /** Initializing UI
     * */
    public void initialize(URL url, ResourceBundle rb){
        System.out.println(System.getProperty("user.dir"));
        fileFilter_comboBox.setItems(ob);
        fileFilter_comboBox.setValue("All");
        codeTypeFilter_comboBox.setItems(ob2);
        codeTypeFilter_comboBox.setValue("codeType1");

        //sideBar
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),sideBar_View);
        translateTransition.setByX(-600);
        translateTransition.play();

        fileView_pane.setVisible(false);

        sideBar.setOnMouseClicked(event -> {
            if (isVisible) {
                TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),sideBar_View);
                translateTransition1.setByX(-600);
                translateTransition1.play();
                sideBar_View.getChildren().get(0).setVisible(false);
                isVisible = false;
            }else {
                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), sideBar_View);
                translateTransition1.setByX(+600);
                translateTransition1.play();
                isVisible = true;
            }
        });

        file_image.setOnMouseClicked(event -> {
            fileView_pane.setVisible(true);
        });
        loadTreeTable();
    }

    /**若有更改 rootPath,fileFilter 重設 fileView_treeTable
     * */
    public void reloadTreeTable(){
        loadTreeTable();
    }

    /** fileView_treeTable 物件被選擇
     * */
    public File oldSelectedFile, newSelectedFile;
    public void selectItem(){
        try{
            newSelectedFile = fileView_treeTable.getSelectionModel().getSelectedItem().getValue();
        }catch (NullPointerException e){
            System.out.println("Empty Selection Error");
        }

        if (newSelectedFile != null){
            if(newSelectedFile.equals(oldSelectedFile)){
                if(newSelectedFile.isDirectory() && !Objects.equals(RootPath_textField.getText(), newSelectedFile.getAbsolutePath())) {
                    RootPath_textField.setText(newSelectedFile.getAbsolutePath());
                    loadTreeTable();
                }
            }else {
                System.out.println(newSelectedFile);
            }
        }
        oldSelectedFile = newSelectedFile;
    }
    
    /** 返回建觸發
     * */
    public void backButtonOnClick(){
        System.out.println("Back");
        RootPath_textField.setText(new File(RootPath_textField.getText()).getParent());
        loadTreeTable();
    }

    /* 重整fileView_treeTable
    * */
    private void loadTreeTable(){
        String inpath = RootPath_textField.getText();
        File rootFile = new File(inpath);
        if (rootFile.exists()){
            TreeItem<File> root = new TreeItem<>(rootFile, new ImageView(folder_icon));
            findInner2(rootFile, root, !fileFilter_comboBox.getValue().equals("All"));
            root.setExpanded(true);
            col1.setCellValueFactory(new Callback<>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> fileStringCellDataFeatures) {
                    return new SimpleStringProperty(fileStringCellDataFeatures.getValue().getValue().getName());
                }
            });

            fileView_treeTable.setRoot(root);
        } else {
            fileView_treeTable.setRoot(new TreeItem<>());
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

/*    private void findInner(File file, TreeItem<File> root, boolean fileFilter){
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
    }*/
    private void findInner2(File file, TreeItem<File> root, boolean fileFilter){
        File[] innerfiles = file.listFiles();
        for (File value : innerfiles) {
            if (value.isDirectory()) {
                TreeItem<File> innerRoot = new TreeItem<>(value, new ImageView(folder_icon));

                findInner2(value, innerRoot,fileFilter);
                root.getChildren().add(innerRoot);
            }
        }
        for (File value : innerfiles) {
            if (fileFilter){
                if (value.isFile() && passFileFilter(value)) {
                    root.getChildren().add((new TreeItem<>(value)));
                }
            } else {
                if (value.isFile()) {
                    root.getChildren().add((new TreeItem<>(value)));
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