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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.*;
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
    @FXML private HBox exit_bar_content;

    @FXML private TextArea codeView;

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
    private String cssFile_path_abs = "src/main/resources/annotationeditor/css/"; //fix**
    private File selectedItem;

    //colorPicker
    @FXML private ColorPicker background_ColorPicker;
    @FXML private ColorPicker button_ColorPicker;
    @FXML private ColorPicker Border_ColorPicker;
    @FXML private ColorPicker Text_ColorPicker;
    private String backgroundColor_code;
    private String buttonColor_code;
    private String borderColor_code;
    private String textColor_code;
    private final String backgroundColor_code_default = "111c26";
    private final String buttonColor_code_default = "111c26";
    private final String borderColor_code_default = "33ffff";
    private final String textColor_code_default = "ffffff";

    //unsorted
    @FXML private AnchorPane mainScene;
    @FXML private Label msg;
    //================================================ todolist  =======================================================
    //TODO: 2022/5/5 rename everything

    //todo : use fileSystem

    //fixme : fileView 過度reload

    //================================================ initialize ======================================================

    /** Initializing UI
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        comboBoxInitialize();
        sideBarInitialize();
        colorPickerInitialize();
        changeColor();
        loadTreeTable();
        sideBar_View.setVisible(true);
        fileView_pane.setVisible(true);
        annotationEditor_view.setVisible(true);



//       App.mainStage.se
    }


// =============================================== Public Methods for UI ===============================================
    /**若有更改 rootPath,fileFilter 重設 fileView_treeTable
     */
    public void reloadTreeTable(){
        loadTreeTable();
    }

    /** 若有更改 codeType 重新 load informationLayout
     */
    public void reloadLeftRightPane(){
        if(selectedItem != null){
            loadInformationLayout(selectedItem.getAbsolutePath(), codeTypes_path_abs + codeTypeFilter_comboBox.getValue());
            codeView.setText("");
        }
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
                    reloadLeftRightPane();
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
        reloadTreeTable();
    }

    /** 將顏色設定回預設設定
     */
    public void resetColorPicker(){
        background_ColorPicker.setValue(Color.valueOf(backgroundColor_code_default));
        button_ColorPicker.setValue(Color.valueOf(buttonColor_code_default));
        Border_ColorPicker.setValue(Color.valueOf(borderColor_code_default));
        Text_ColorPicker.setValue(Color.valueOf(textColor_code_default));
        changeColor();
    }
    /** 修改顏色
     */
    public void changeColor(){
        System.out.println("change");
        try{
            File temp = new File("style_new.css");
            temp.delete();
            temp.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

            backgroundColor_code = background_ColorPicker.getValue().toString().substring(2);
            buttonColor_code = button_ColorPicker.getValue().toString().substring(2);
            borderColor_code = Border_ColorPicker.getValue().toString().substring(2);
            textColor_code = Text_ColorPicker.getValue().toString().substring(2);
            saveUserStyle_settings();

            bw.write("" +
                    ".mainBackgroundColor{\n" +
                    "    -fx-background-color: #"+backgroundColor_code+";\n" +
                    "}" +
                    ".myButton {\n" +
                    "    -fx-background-color: #"+buttonColor_code+";\n" +
                    "    -fx-border-color: #" +borderColor_code+";\n" +
                    "    -fx-text-fill: #"+textColor_code+";\n" +
                    "    -fx-highlight-fill: #ffffff;\n" +
                    "}" +
                    ".TreeView{\n" +
                    "    -fx-background-color:  #"+backgroundColor_code+";\n" +
                    "    -fx-border-color: #"+borderColor_code+";\n" +
                    "}" +
                    ".TreeView .tree-cell{\n" +
                    "   -fx-text-fill: #"+textColor_code+";\n" +
                    "}" +
                    ".text-field," +
                    ".text-area{\n" +
                    "    -fx-background-color: #"+backgroundColor_code+";\n" +
                    "    -fx-border-color: #"+borderColor_code+";\n" +
                    "    -fx-text-fill: #"+textColor_code+";\n" +
                    "    -fx-highlight-fill: #ffffff;\n" +
                    "    -fx-highlight-text-fill: #"+backgroundColor_code+";\n" +
                    "}\n" +
                    ".combo-box{\n" +
                    "    -fx-background-color: #"+buttonColor_code+";\n" +
                    "    -fx-border-color: #"+borderColor_code+";\n" +
                    "}\n" +
                    ".combo-box .cell{\n" +
                    "    -fx-text-fill: #"+textColor_code+";\n" +
                    "}\n" +
                    ".combo-box .list-view {\n" +
                    "    -fx-background-color: #"+buttonColor_code+";\n" +
                    "    -fx-border-color: #"+borderColor_code+";\n" +
                    "}\n" +
                    ".combo-box .list-view .list-cell{\n" +
                    "    -fx-text-fill: #"+textColor_code+";\n" +
                    "}"
                    );
            bw.close();
            //優化
            for(Node n : mainScene.getChildren()){
                if(n instanceof AnchorPane){
                    AnchorPane k = (AnchorPane) n;
                    k.getStylesheets().clear();
                    k.getStylesheets().add(temp.toURI().toString());
                }
            }
            mainScene.getStylesheets().add(temp.toURI().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        //fixme
        if(informationLayout_pane != null) informationLayout_pane.setBackgroundColor(backgroundColor_code);
    }

    public void save(){
//        //fixme saver save
//        try {
//            File saveFile = new File(selectedItem.getAbsolutePath());
//            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
//            bw.write(cd.OutputProcessedScript());
//            bw.flush();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            msg.setText("Save Successfully");
//        }
//        msg.setText("Save Successfully");
    }
    public void preview(){
        //fixme
        if(cd != null) codeView.setText(cd.OutputProcessedScript());
    }

    //================================================ 視窗按鍵 =========================================================
    public void mouseEnterExit_bar(){
        exit_bar_content.setVisible(true);
    }
    public void mouseExitExit_bar(){exit_bar_content.setVisible(false);}
    public void exitButtonOnclick(){
        App.mainStage.close();
    }
    public void enlargeButtonOnclick(){
        App.mainStage.setMaximized(true);
    }
    public void smallButtonOnclick(){
        App.mainStage.setIconified(true);
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
                            if (!empty && item != null) {
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
            fileView_treeTable.setCellFactory(fileTreeView -> new TreeCell<>());
        }
    }

    /* load informationLayout
    * */
    private codeData cd;
    private void loadInformationLayout(String filePath_abs, String codeType_abs){
        cd = new codeData(filePath_abs, new codeType(codeType_abs).getTypeList());
        informationLayout_pane = new InformationLayout(cd.getInfoList(),backgroundColor_code);
        left_pane.getChildren().clear();
        //fixme
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
                if(innerFiles.length < 10){
                    findInner(value, innerRoot,fileFilter);
                }
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

        sideBar_content.maxWidthProperty().bind(main_view.widthProperty().divide(3));
        sideBar_content.minWidthProperty().bind(main_view.widthProperty().divide(6));
        sideBar.setOnMouseEntered(event -> translateEnter.play());
        sideBar_View.setOnMouseExited(event -> {
            if(sideBar_View.getCursor() != Cursor.E_RESIZE && !codeTypeFilter_comboBox.showingProperty().getValue() && !fileFilter_comboBox.showingProperty().getValue()){
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
        });
        home_icon.setOnMouseClicked(event -> {
            setAllInvisible(sideBar_content);
            home_pane.setVisible(true);
        });
        settings_icon.setOnMouseClicked(event -> {
            setAllInvisible(main_view);
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
        //todo: rotate
        user_icon.setOnMouseEntered(event -> user_icon.setRotate(10));
        user_icon.setOnMouseExited(event -> user_icon.setRotate(0));
        settings_icon.setOnMouseEntered(event -> settings_icon.setRotate(10));
        settings_icon.setOnMouseExited(event -> settings_icon.setRotate(0));
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

    /* 儲存使用者顏色喜好設定
     */
    private void saveUserStyle_settings(){
        try{
            File userStyle_settings_file = new File(cssFile_path_abs + "userStyle_settings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(userStyle_settings_file));
            bw.write("backgroundColor_code = "+ backgroundColor_code +"\n" +
                        "buttonColor_code = "+ buttonColor_code +"\n" +
                        "borderColor_code = "+ borderColor_code +"\n" +
                        "textColor_code = "+ textColor_code +"\n"
                    );
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* initialize colorPicker
     */
    private void colorPickerInitialize(){
        try{
            File userStyle_settings_file = new File(cssFile_path_abs + "userStyle_settings.txt");
            BufferedReader bf = new BufferedReader(new FileReader(userStyle_settings_file));
            String line;
            while((line = bf.readLine() )!= null){
                switch (line.substring(0,line.indexOf(" = "))){
                    case "backgroundColor_code" -> backgroundColor_code = line.substring(line.indexOf(" = ")+3);
                    case "buttonColor_code" -> buttonColor_code = line.substring(line.indexOf(" = ")+3);
                    case "borderColor_code" -> borderColor_code = line.substring(line.indexOf(" = ")+3);
                    case "textColor_code" -> textColor_code = line.substring(line.indexOf(" = ")+3);
                    default -> System.out.println("Invalid Color code in userStyle_setting.text");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        background_ColorPicker.setValue(Color.valueOf(backgroundColor_code));
        button_ColorPicker.setValue(Color.valueOf(buttonColor_code));
        Border_ColorPicker.setValue(Color.valueOf(borderColor_code));
        Text_ColorPicker.setValue(Color.valueOf(textColor_code));
    }

    /* set all the other Panes invisible
    * */
    private void setAllInvisible(Pane parentPane){
        for (int i = 0; i < parentPane.getChildren().size(); i++) {
            parentPane.getChildren().get(i).setVisible(false);
        }
    }

}