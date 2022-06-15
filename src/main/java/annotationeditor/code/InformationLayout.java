package annotationeditor.code;

import annotationeditor.code.ScriptEdit.codeInformation;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class InformationLayout extends ScrollPane{
    protected codeInformation selectedInformation;
    private SecondPane secondPane;
    private Rectangle[] rectangles;

    public InformationLayout(ArrayList<codeInformation> informationArrayList){
        this(informationArrayList, "111C26FF");
    }

    public InformationLayout(ArrayList<codeInformation> informationArrayList, String backgroundColor){
        rectangles = new Rectangle[informationArrayList.size()];
        secondPane = new SecondPane(informationArrayList, backgroundColor);
        setContent(secondPane);
        this.widthProperty().addListener(ov-> secondPane.setMinWidth(this.getWidth()));
        this.heightProperty().addListener(ov-> secondPane.setMinHeight(this.getHeight()));
    }

    public codeInformation getSelectedInformation(){
        return selectedInformation;
    }

    public void setBackgroundColor(String backgroundColor){
        secondPane.setStyle("-fx-background-color: #" + backgroundColor);
        for (Rectangle rectangle : rectangles) {
            rectangle.setFill(Color.valueOf(backgroundColor));
        }
    }

    class SecondPane extends StackPane {
        private VBox vBoxForInfoPane = new VBox();

        public SecondPane(ArrayList<codeInformation> informationArrayList, String backgroundColor) {
            this.setStyle("-fx-background-color: #" + backgroundColor);
            vBoxForInfoPane.setPadding(new Insets(11,12,13,14));
            for (int i=0;i<informationArrayList.size();i++) {
                InfoPane infoPane = new InfoPane(informationArrayList.get(i));
                rectangles[i] = new Rectangle(1000,15,Color.valueOf(backgroundColor)); // 放置在infoPane之間,防止跑動畫的時候docPane外露
                int finalI = i;
                vBoxForInfoPane.widthProperty().addListener(ov->{
                    infoPane.setMinWidth(this.getMinWidth()-60);
                    rectangles[finalI].setWidth(this.getMinWidth()-60);
                });
                vBoxForInfoPane.getChildren().addAll(infoPane,rectangles[i]);
                if (i == informationArrayList.size()-1){
                    infoPane.layoutYProperty().addListener(ov->rectangles[finalI].setHeight(getScene().getHeight()-rectangles[finalI].getLayoutY()-40));
                    vBoxForInfoPane.heightProperty().addListener(ov->rectangles[finalI].setHeight(getScene().getHeight()-rectangles[finalI].getLayoutY()-40));
                }
            }
            this.setPadding(new Insets(0,20,0,0));
            this.widthProperty().addListener(ov->vBoxForInfoPane.setMinWidth(this.getMinWidth()-50));
            this.getChildren().add(vBoxForInfoPane);
        }

        class InfoPane extends Pane {
            protected codeInformation information;
            private VBox vBox = new VBox(5);      // this vBox puts in hBox.If button is selected, docPane will be added.
            private HBox hBox = new HBox();          // this hBox puts in button,label,radioButton.
            private Label label = new Label();       // this label presents the type and the name in the information.
            private ImageView close = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_close.png").toString()));
            //private ImageView open = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_open.png").toString()));
            private Button button = new Button("", close); // this button will determine whether the docPane will be showed or not.
            private RadioButton radioButton = new RadioButton("DocComment");
            private DocPane docPane;
            private Timeline animation1; //控制infoPane的高度,達到慢慢展開的動畫效果
            private RotateTransition animation2 = new RotateTransition(Duration.millis(500),button);//控制button的旋轉動畫

            public InfoPane(codeInformation information) {
                this.information = information;
                this.setStyle("-fx-background-radius: 10;-fx-background-color: #38424D");
                close.setFitHeight(12);
                close.setFitWidth(12);
                label.setText(String.format("   %-10s  %-10s", information.type, information.name));
                label.setFont(new Font(15));
                this.setColor();
                radioButton.setSelected(information.isDocCommentExist);
                radioButton.setTextFill(Color.GREENYELLOW);
                hBox.setAlignment(Pos.BASELINE_LEFT);
                vBox.setPadding(new Insets(11,12,13,14));
                docPane = new DocPane();
                this.getChild();
                this.setAction();
                this.widthProperty().addListener(ov->this.listen());
            }

            private void startAnimation(double initialHeight, boolean isExtend, boolean requestFromRadioButton){
                if (isExtend) extend(initialHeight, requestFromRadioButton);
                else small(initialHeight, requestFromRadioButton);
            }
            private void extend(double initialHeight, boolean requestFromRadioButton){
                this.setMaxHeight(initialHeight);
                double finalHeight = this.getFinalHeight(true);
                animation1 = new Timeline(new KeyFrame(Duration.millis(20), event->this.setMaxHeight(this.getMaxHeight()+(finalHeight-initialHeight)/20)));
                animation1.setCycleCount(25);
                animation2.setByAngle(90);
                if (!requestFromRadioButton)
                    animation2.play();
                animation1.play();
            }
            private void small(double initialHeight, boolean requestFromRadioButton){
                this.setMinHeight(initialHeight);
                double finalHeight = this.getFinalHeight(false);
                animation1 = new Timeline(new KeyFrame(Duration.millis(20), event->this.setMinHeight(this.getMinHeight()+(finalHeight-initialHeight)/20)));
                animation1.setCycleCount(25);
                animation2.setByAngle(-90);
                if (!requestFromRadioButton)
                    animation2.play();
                animation1.play();
            }

            private double getFinalHeight(boolean isExtend){
                if (button.getRotate()==90&&!isExtend)
                    return hBox.getHeight();
                double finalHeight = hBox.getHeight() + docPane.textAreaForAnnotation.getMinHeight() + 20;
                if (radioButton.isSelected()) {
                    finalHeight += docPane.labelInDocPane.getMinHeight() + docPane.textAreaInDocPane.getMinHeight() + 20;
                    for (DocPane.CommentPane commentPane : docPane.commentPanes){
                        finalHeight += commentPane.labelInCommentPane.getMinHeight() + 10;
                    }
                }
                return finalHeight;
            }

            private void getChild(){
                hBox.getChildren().addAll(button, label, radioButton);
                vBox.getChildren().add(hBox);
                this.getChildren().add(vBox);
            }

            private void listen(){
                this.setMinHeight(50+this.getWidth()/100);
                docPane.setMaxWidth(super.getMinWidth()-60);
                hBox.setSpacing(this.getMinWidth()/25);
                vBox.setMinWidth(this.getMinWidth()-10);
                docPane.setMinWidth(this.getMinWidth()-30);
                label.setFont(Font.font(10+this.getMinWidth()/100));
                label.setMinWidth(this.getMinWidth()*3/5<200?200:this.getMinWidth()*3/5);
                radioButton.setFont(Font.font(8+this.getMinWidth()/100));
            }

            private void setAction(){
                button.setOnAction(e -> {
                    if (button.getRotate()==0) {
                        vBox.getChildren().add(docPane);
                        this.startAnimation(this.getHeight(),true, false);
                    } else if (button.getRotate()==90){
                        this.startAnimation(this.getHeight(),false, false);
                        vBox.getChildren().remove(docPane);
                    }
                });
            }

            private void setColor() {
                if (information.type.contains("class")) {
                    label.setTextFill(Color.ORANGE);
                } else if (information.type.contains("function")) {
                    label.setTextFill(Color.YELLOW);
                } else if (information.type.contains("value")) {
                    label.setTextFill(Color.INDIGO);
                }
            }

            private void resetHeight(double changeValue){
                this.setMinHeight(this.getMinHeight()+changeValue);
                this.setMaxHeight(this.getMaxHeight()+changeValue);
            }

            class DocPane extends VBox{
                private TextArea textAreaForAnnotation = new TextArea();       // the content in the TextArea will be added to the annotation in information.
                private VBox vBoxInDocPane = new VBox(10);   // this vBox puts in the textAreaForAnnotation.If radioButton is selected,label,textArea and CommentPanes will be added.
                private Label labelInDocPane = new Label("Description : ");
                private TextArea textAreaInDocPane = new TextArea();           // the content in this textArea will be added to the description in docComment.
                private CommentPane[] commentPanes;

                public DocPane(){
                    this.setStyle("-fx-background-color: #38424D");
                    textAreaForAnnotation.setFont(Font.font(15));
                    textAreaForAnnotation.setPrefRowCount(1);
                    textAreaForAnnotation.setText(information.annotation);
                    labelInDocPane.setFont(Font.font(15));
                    labelInDocPane.setTextFill(Color.GREENYELLOW);
                    textAreaInDocPane.setFont(Font.font(15));
                    textAreaInDocPane.setPrefRowCount(1);
                    textAreaInDocPane.setText(information.docComment.description);
                    commentPanes = new CommentPane[information.docComment.comment.size()];
                    this.getChild();
                    this.textAreaChanged(information.annotation, textAreaForAnnotation);
                    this.textAreaChanged(information.docComment.description, textAreaInDocPane);
                    this.setAction();
                    this.setMouseClicked();
                    this.listen();
                    this.widthProperty().addListener(ov->this.listen());
                }

                private void getChild(){
                    this.getChildren().add(textAreaForAnnotation);
                    vBoxInDocPane.getChildren().addAll(labelInDocPane, textAreaInDocPane);
                    for (int i=0;i<information.docComment.comment.size();i++){
                        commentPanes[i] = new CommentPane(i, information.docComment.comment.get(i));
                        vBoxInDocPane.getChildren().add(commentPanes[i]);
                    }
                    if (radioButton.isSelected())
                        this.getChildren().add(vBoxInDocPane);
                }

                private void listen(){
                    vBoxInDocPane.setMaxWidth(this.getWidth());
                    labelInDocPane.setFont(Font.font(10+this.getWidth()/100));
                    labelInDocPane.setMinHeight(25+this.getWidth()/100);
                    textAreaForAnnotation.setMinWidth(this.getWidth());
                    textAreaForAnnotation.setMaxWidth(this.getWidth());
                    textAreaForAnnotation.setFont(Font.font(15+this.getWidth()/200));
                    this.textAreaChanged(textAreaForAnnotation.getText(),textAreaForAnnotation);
                    textAreaInDocPane.setMaxWidth(this.getWidth());
                    textAreaInDocPane.setMinWidth(this.getWidth());
                    textAreaInDocPane.setFont(Font.font(15+this.getWidth()/200));
                    this.textAreaChanged(textAreaInDocPane.getText(),textAreaInDocPane);
                }

                private void setAction(){
                    radioButton.setOnAction(e->{
                        if (radioButton.isSelected()){
                            information.isDocCommentExist = true;
                            this.getChildren().add(vBoxInDocPane);
                            if (button.getRotate()==90)
                                startAnimation(super.getHeight()+this.getHeight(),true, true);
                        }
                        else {
                            information.isDocCommentExist = false;
                            if (button.getRotate()==90)
                                startAnimation(super.getHeight(),false, true);
                            this.getChildren().remove(vBoxInDocPane);
                        }
                    });
                    textAreaForAnnotation.setOnKeyPressed(e->{
                        information.annotation = textAreaForAnnotation.getText();
                        textAreaChanged(textAreaForAnnotation.getText(), textAreaForAnnotation);
                    });
                    textAreaInDocPane.setOnKeyPressed(e->{
                        information.docComment.description = textAreaInDocPane.getText();
                        textAreaChanged(textAreaInDocPane.getText(),textAreaInDocPane);
                    });
                }

                private void setMouseClicked(){
                    if (!radioButton.isSelected())
                        this.getChildren().add(vBoxInDocPane);
                    vBox.getChildren().add(this);
                    Queue<Node> nodes = new LinkedList<>();
                    nodes.add(vBox);
                    while (!nodes.isEmpty()){
                        Node node = nodes.poll();
                        if (node instanceof Parent){
                            Parent parent = (Parent) node;
                            nodes.addAll(parent.getChildrenUnmodifiable());
                        }
                        //System.out.println(node);
                        node.setOnMouseClicked(e -> selectedInformation = information);
                    }
                    if (!radioButton.isSelected())
                        this.getChildren().remove(vBoxInDocPane);
                    vBox.getChildren().removeAll(this);
                }

                private void textAreaChanged(String text, TextArea textArea){
                    int newLine = 0;
                    while (text.contains("\n")){
                        newLine += 1;
                        text = text.substring(text.indexOf("\n")+1);
                    }
                    double oldHeight = textArea.getMinHeight();
                    textArea.setMinHeight(50+(textArea.getFont().getSize()*4/3)*newLine);
                    oldHeight = textArea.getMinHeight()-oldHeight;
                    resetHeight(oldHeight);
                }

                class CommentPane extends Pane{
                    private Label labelInCommentPane = new Label();             // this label will show the type in comment in docComment.
                    private TextField textFieldInCommentPane = new TextField(); // the content in textField will be added to description in comment in docComment in information.
                    private HBox hBoxInCommentPane = new HBox(20);           // this hBox puts in text and textField.
                    private int line;                                           // record the index in comment.

                    public CommentPane(int line, String[] textArray){
                        this.line = line;
                        hBoxInCommentPane.setAlignment(Pos.CENTER);
                        labelInCommentPane.setPadding(new Insets(0,0,0,10));
                        labelInCommentPane.setText(textArray[0]);
                        labelInCommentPane.setTextFill(Color.GREENYELLOW);
                        textFieldInCommentPane.setText(textArray[1]);
                        this.setAction();
                        this.getChild();
                        this.listen();
                        this.widthProperty().addListener(ov->this.listen());
                    }

                    private void getChild(){
                        hBoxInCommentPane.getChildren().addAll(labelInCommentPane, textFieldInCommentPane);
                        getChildren().add(hBoxInCommentPane);
                    }

                    private void listen(){
                        labelInCommentPane.setFont(Font.font(10+this.getWidth()/100));
                        labelInCommentPane.setMinWidth(105+this.getWidth()/25);
                        labelInCommentPane.setMinHeight(25+this.getWidth()/100);
                        textFieldInCommentPane.setMinWidth(this.getWidth()-125-this.getWidth()/25);
                        textFieldInCommentPane.setMaxWidth(this.getWidth()-125-this.getWidth()/25);
                        textFieldInCommentPane.setFont(Font.font(10+this.getWidth()/100));
                    }

                    private void setAction(){
                        textFieldInCommentPane.setOnKeyPressed(e-> information.docComment.comment.get(line)[1] = textFieldInCommentPane.getText());
                    }
                }
            }
        }
    }
}