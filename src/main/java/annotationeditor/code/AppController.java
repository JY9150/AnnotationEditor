package annotationeditor.code;

import annotationeditor.code.FileSystem.File_System;
import annotationeditor.code.ScriptEdit.codeData;
import annotationeditor.code.ScriptEdit.codeType;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    //Main_view
    @FXML private AnchorPane main_view;
    @FXML private AnchorPane annotationEditor_view;
        //annotationEditor_view
        @FXML private AnchorPane left_pane;
        @FXML private AnchorPane right_pane;
    @FXML private AnchorPane readMeMdEditor_view;
    @FXML private AnchorPane exit_bar;

    @FXML private AnchorPane sideBar;
        //sideBar
        @FXML private ImageView user_icon;
        @FXML private ImageView file_icon;
        @FXML private ImageView home_icon;
        @FXML private ImageView settings_icon;
        @FXML private StackPane readMeMdEditor_icon;
        @FXML private StackPane annotationEditor_icon;
    @FXML private AnchorPane sideBar_View;
    @FXML private StackPane sideBar_content;
        //sideBar_content
        @FXML private Pane user_pane;
        @FXML private Pane fileView_pane;
        @FXML private Pane home_pane;
        @FXML private Pane settings_pane;
        //fileView_Pane
        @FXML private TreeView<File> fileView_treeTable;
        @FXML private TextField RootPath_textField;
        @FXML private ComboBox<String> fileFilter_comboBox;
              private ObservableList<String> ob;
        @FXML private ComboBox<String> codeTypeFilter_comboBox;
              private ObservableList<String> ob2;

    //Others
    private Image folder_icon = File_System.getImage("fileIcon.png",15,15);
    private Image java_icon = File_System.getImage("java.png",15,15);
    private Image cs_icon = File_System.getImage("c#.png",15,15);
    private Image txt_icon = File_System.getImage("txt.png",15,15);

    private InformationLayout informationLayout_pane;
    private String codeTypes_path_abs = "src/main/resources/annotationeditor/CodeTypes"; //fix**
    private File selectedItem;

    //================================================ initialize ======================================================

    /** Initializing UI
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        comboBoxInitialize();
        sideBarInitialize();
        loadTreeTable();
    }


// TODO: 2022/5/5 rename everything
// =============================================== Public Methods for UI ===============================================
    /**若有更改 rootPath,fileFilter 重設 fileView_treeTable
     */
    public void reloadTreeTable(){
        loadTreeTable();
    }

    /** 若有更改 codeType 重新 load informationLayout
     */
    public void reloadInformationLayout(){
        loadInformationLayout(selectedItem.getAbsolutePath(), codeTypes_path_abs + codeTypeFilter_comboBox.getValue());
    }

    /** fileView_treeTable 物件被選擇
     */
    private long userLastClick = 0;
    public void selectItem(){
        if(Math.abs(System.currentTimeMillis() - userLastClick) < 500) {
            userLastClick = System.currentTimeMillis();
            try{
                selectedItem = fileView_treeTable.getSelectionModel().getSelectedItem().getValue();
                if(selectedItem.isDirectory()){
                    reloadTreeTable();
                    RootPath_textField.setText(selectedItem.toString());
                }else{
                    loadInformationLayout(selectedItem.getAbsolutePath(), codeTypes_path_abs + codeTypeFilter_comboBox.getValue());
                }
            }catch (NullPointerException e){
                System.out.println("empty selection!");
            }
        }
        else userLastClick = System.currentTimeMillis();
    }

    /** 返回建觸發
     */
    public void backButtonOnClick(){
        RootPath_textField.setText(new File(RootPath_textField.getText()).getParent());
        loadTreeTable();
    }

    //================================================ 視窗按鍵 =========================================================
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

// =============================================== Private Methods =====================================================
    /* 重整fileView_treeTable
    */
    private void loadTreeTable(){
        File rootFile = new File(RootPath_textField.getText());
        if (rootFile.exists()){
            TreeItem<File> root = new TreeItem<>(rootFile);
            findInner(rootFile, root, !fileFilter_comboBox.getValue().equals("All"));
            root.setExpanded(true);

            fileView_treeTable.setRoot(root);
            fileView_treeTable.setCellFactory(new Callback<>() {
                public TreeCell<File> call(TreeView<File> param) {
                    return new TreeCell<>() {
                        @Override
                        protected void updateItem(File item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                ImageView imageView = null;
                                if (item.isDirectory()) {
                                    imageView = new ImageView(folder_icon);
                                } else if (item.getName().contains(".java")) {
                                    imageView = new ImageView(java_icon);
                                } else if (item.getName().contains(".cs")) {
                                    imageView = new ImageView(cs_icon);
                                } else if (item.getName().contains(".txt")) {
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

    /* load informationLayout
    * */
    private void loadInformationLayout(String filePath_abs, String codeType_abs){
        informationLayout_pane = new InformationLayout(new codeData(filePath_abs,new codeType(codeType_abs).getTypeList()).getInfoList());
        left_pane.getChildren().clear();
        informationLayout_pane.prefHeightProperty().bind(left_pane.heightProperty());
        informationLayout_pane.prefWidthProperty().bind(left_pane.widthProperty());
        left_pane.getChildren().add(informationLayout_pane);
    }

    /* find files and folder in the path and check whether pass teh fileFilter
    * */
    private void findInner(File file, TreeItem<File> root, boolean fileFilter){
        File[] innerFiles = file.listFiles();
        for (File value : innerFiles) {
            if (value.isDirectory()) {
                TreeItem<File> innerRoot = new TreeItem<>(value);
                //fixme: OverLoad
                findInner(value, innerRoot,fileFilter);
                root.getChildren().add(innerRoot);
            }
        }
        for (File value : innerFiles) {
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

    /* initialize sideBar
    * */
    private void sideBarInitialize(){

        //set sideBar
        TranslateTransition translateEnter = new TranslateTransition(Duration.seconds(0.5),sideBar_View);
        TranslateTransition translateExit = new TranslateTransition(Duration.seconds(0.5),sideBar_View);
        translateEnter.setFromX(-500);
        translateEnter.setToX(0);
        translateExit.setToX(-500);
        translateExit.play();

        sideBar_content.maxWidthProperty().bind(main_view.widthProperty().divide(3));//fixme: sidebar width
        sideBar.setOnMouseEntered(event -> translateEnter.play());
        sideBar_View.setOnMouseExited(event -> {
            if(sideBar_View.getCursor() != Cursor.E_RESIZE){
                translateExit.play();
            }
        });
        sideBar_View.setOnMouseMoved(event -> {
            if( Math.abs(event.getX() - sideBar_content.getWidth()) < 5){
                sideBar_View.setCursor(Cursor.E_RESIZE);
            }else {
                sideBar_View.setCursor(Cursor.DEFAULT);
            }
        });
        sideBar_View.setOnMouseDragged(event -> {
            if (sideBar_View.getCursor() == Cursor.E_RESIZE) {
                sideBar_content.setPrefWidth(event.getX());
            }
        });

        user_icon.setOnMouseClicked(event -> {
            setAllInvisible(sideBar_content);
            user_pane.setVisible(true);
        });
        file_icon.setOnMouseClicked(event -> {
            setAllInvisible(sideBar_content);
            fileView_pane.setVisible(true);
            loadTreeTable();
        });
        home_icon.setOnMouseClicked(event -> {
            setAllInvisible(sideBar_content);
            home_pane.setVisible(true);
        });
        settings_icon.setOnMouseClicked(event -> {
            setAllInvisible(sideBar_content);
            settings_pane.setVisible(true);
        });
        readMeMdEditor_icon.setOnMouseClicked(event -> {
            setAllInvisible(main_view);
            readMeMdEditor_view.setVisible(true);
        });
        annotationEditor_icon.setOnMouseClicked(event -> {
            setAllInvisible(main_view);
            annotationEditor_view.setVisible(true);
        });
    }

    /* initialize comboBox
    * */
    private void comboBoxInitialize(){
        ob = FXCollections.observableArrayList("All", ".java", ".cs", ".txt");
        ob2 = FXCollections.observableArrayList();
        for(File f : new File(codeTypes_path_abs).listFiles()){
            ob2.add(f.getName());
        }

        //set comboBox
        System.out.println(System.getProperty("user.dir"));
        fileFilter_comboBox.setItems(ob);
        fileFilter_comboBox.setValue(ob.get(0));
        codeTypeFilter_comboBox.setItems(ob2);
        codeTypeFilter_comboBox.setValue(ob2.get(0));
    }

    /* set all the other Panes invisible
    * */
    private void setAllInvisible(Pane parentPane){
        for (int i = 0; i < parentPane.getChildren().size(); i++) {
            parentPane.getChildren().get(i).setVisible(false);
        }
    }
}