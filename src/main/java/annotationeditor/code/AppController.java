package annotationeditor.code;

import annotationeditor.code.FileSystem.File_System;
import annotationeditor.code.ScriptEdit.codeData;
import annotationeditor.code.ScriptEdit.codeType;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.time.Clock;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;

public class AppController implements Initializable {
    // this is Jason commit test
    @FXML private TreeView<File> fileView_treeTable;
    @FXML private TextField RootPath_textField;
    @FXML private TextArea CodeView;
    @FXML private ComboBox<String> fileFilter_comboBox;
          private final ObservableList<String> ob = FXCollections.observableArrayList("All", ".java", ".cs", ".txt");
    @FXML private ComboBox<String> codeTypeFilter_comboBox;
          private ObservableList<String> ob2 = FXCollections.observableArrayList("codeType1","codeType2");
    @FXML private AnchorPane sideBar;
    @FXML private AnchorPane sideBar_View;
    @FXML private AnchorPane left_pane;
    @FXML private AnchorPane right_pane;

    @FXML private ImageView user_image;
    @FXML private ImageView file_image;
    @FXML private ImageView home_image;
    @FXML private ImageView settings_image;
    @FXML private VBox fileView_pane;
          private Image folder_icon = File_System.getImage("fileIcon.png",15,15);
          private Image java_icon = File_System.getImage("java.png",15,15);
          private Image cs_icon = File_System.getImage("c#.png",15,15);
          private Image txt_icon = File_System.getImage("txt.png",15,15);
          private boolean isVisible = false;
    @FXML private AnchorPane exit_bar;

    /** Initializing UI
     * */
    public codeData codeData = new codeData("C:/Users/jason/Desktop/CE1004-B/CE_homework/A8/A8_vscode/MySystem.java",new codeType("code.txt").getTypeList());
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //set codeDataPane
        InformationLayout p1 = new InformationLayout(codeData.getInfoList());
        p1.prefHeightProperty().bind(left_pane.heightProperty());
        p1.prefWidthProperty().bind(left_pane.widthProperty());
        left_pane.getChildren().add(new ScrollPane(p1));

        //set comboBox
        System.out.println(System.getProperty("user.dir"));
        fileFilter_comboBox.setItems(ob);
        fileFilter_comboBox.setValue("All");
        codeTypeFilter_comboBox.setItems(ob2);
        codeTypeFilter_comboBox.setValue("codeType1");

        //set sideBar
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),sideBar_View);
        translateTransition.setByX(-600);
        translateTransition.play();
        fileView_pane.setVisible(false);
        sideBar.setOnMouseClicked(event -> {
            if (isVisible) {
                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5),sideBar_View);
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
     */
    public void reloadTreeTable(){
        loadTreeTable();
    }

    /** fileView_treeTable 物件被選擇
     */
    private long userLastClick = 0;
    public void selectItem(){
        if(Math.abs(System.currentTimeMillis()-userLastClick) < 500) {
            userLastClick = System.currentTimeMillis();
            File selectedItem = fileView_treeTable.getSelectionModel().getSelectedItem().getValue();
            RootPath_textField.setText(selectedItem.toString());
            if(selectedItem.isDirectory()){
                reloadTreeTable();
            }
        }
        else userLastClick = System.currentTimeMillis();

    }

/*    private File newSelectedFile, oldSelectedFile;
    @Deprecated
    public void selectItem2() {
        try {
            this.newSelectedFile = (File)((TreeItem)this.fileView_treeTable.getSelectionModel().getSelectedItem()).getValue();
        } catch (NullPointerException var2) {
            System.out.println("Empty Selection Error");
        }

        if (this.newSelectedFile != null) {
            if (this.newSelectedFile.equals(this.oldSelectedFile)) {
                if (this.newSelectedFile.isDirectory() && !Objects.equals(this.RootPath_textField.getText(), this.newSelectedFile.getAbsolutePath())) {
                    this.RootPath_textField.setText(this.newSelectedFile.getAbsolutePath());
                    this.loadTreeTable();
                }
            } else {
                System.out.println(this.newSelectedFile);
            }
        }

        this.oldSelectedFile = this.newSelectedFile;
    }*/
    public String getSelectedItem(){
        return fileView_treeTable.getSelectionModel().getSelectedItem().getValue().toString();
    }
    /** 返回建觸發
     */
    public void backButtonOnClick(){
        System.out.println("Back");
        RootPath_textField.setText(new File(RootPath_textField.getText()).getParent());
        loadTreeTable();
    }

    //==================================================================================================================
    public void mouseEnterExit_bar(){
        exit_bar.setBlendMode(BlendMode.DARKEN);
    }
    public void mouseExitExit_bar(){
        exit_bar.setBlendMode(BlendMode.LIGHTEN);
    }
    public void exitButtonOnclick(){
        System.exit(0);
    }
    public void enlargeButtonOnclick(){
        System.exit(0);
    }
    public void smallButtonOnclick(){
        System.exit(0);
    }

    //==================================================================================================================
    /* 重整fileView_treeTable
    */
    private void loadTreeTable(){
        String inpath = RootPath_textField.getText();
        File rootFile = new File(inpath);
        if (rootFile.exists()){
            TreeItem<File> root = new TreeItem<>(rootFile);
            findInner(rootFile, root, !fileFilter_comboBox.getValue().equals("All"));
            root.setExpanded(true);

            fileView_treeTable.setRoot(root);
            fileView_treeTable.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
                public TreeCell<File> call(TreeView<File> param) {
                    return new TreeCell<File>() {
                        @Override
                        protected void updateItem(File item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                ImageView imageView = null;
                                if(item.isDirectory()){
                                    imageView = new ImageView(folder_icon);
                                }else if(item.getName().contains(".java")){
                                    imageView = new ImageView(java_icon);
                                }else if(item.getName().contains(".cs")){
                                    imageView = new ImageView(cs_icon);
                                }else if(item.getName().contains(".txt")){
                                    imageView = new ImageView(txt_icon);
                                }
                                setGraphic(imageView);
                                setText(item.getName());
                            } else {
                                setText(null);
                                setGraphic(null);
                            }
                        }
                    };
                }
            });
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

    private void findInner(File file, TreeItem<File> root, boolean fileFilter){
        File[] innerfiles = file.listFiles();
        for (File value : innerfiles) {
            if (value.isDirectory()) {
                TreeItem<File> innerRoot = new TreeItem<>(value);

                findInner(value, innerRoot,fileFilter);
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