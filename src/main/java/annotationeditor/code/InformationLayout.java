package annotationeditor.code;

import annotationeditor.code.ScriptEdit.codeInformation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class InformationLayout extends ScrollPane{
    private SecondPane secondPane;

    public InformationLayout(ArrayList<codeInformation> informationArrayList){
        secondPane = new SecondPane(informationArrayList);
        setContent(secondPane);
        this.listen();
    }

    private synchronized void listen(){
        this.widthProperty().addListener(ov->secondPane.setMinWidth(this.getWidth()));
        this.heightProperty().addListener(ov->secondPane.setMinHeight(this.getHeight()));
    }

    class SecondPane extends StackPane {
        public codeInformation SelectedInformation;
        private VBox vBoxForInfoPane = new VBox(15);

        public SecondPane(ArrayList<codeInformation> informationArrayList) {
            this.setPadding(new Insets(11,12,13,14));
            this.setStyle("-fx-background-color: #111C26");
            for (codeInformation information : informationArrayList) {
                InfoPane infoPane = new InfoPane(information);
                vBoxForInfoPane.widthProperty().addListener(ov->infoPane.setMinWidth(this.getMinWidth()-60));
                vBoxForInfoPane.getChildren().add(infoPane);
            }
            this.setPadding(new Insets(0,20,0,0));
            this.widthProperty().addListener(ov->vBoxForInfoPane.setMinWidth(this.getMinWidth()-50));
            this.getChildren().add(vBoxForInfoPane);
        }

        public codeInformation getSelectedInformation(){
            return SelectedInformation;
        }

        class InfoPane extends Pane {
            protected codeInformation information;
            private HBox hBox = new HBox(); // this hBox puts in button,label,radioButton.
            private VBox vBox = new VBox(5);      // this vBox puts in hBox,textAreaInInfoPane.If radioButton is selected, docPane will be added,too.
            private Label label = new Label();   // this label presents the type and the name in the information.
            private RadioButton radioButton = new RadioButton("DocComment");
            private TextArea textAreaInInfoPane = new TextArea(); // the content in the TextArea will be added to the annotation in information.
            private ImageView close = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_close.png").toString()));
            private ImageView open = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_open.png").toString()));
            private Button button = new Button("", close); // this button will determine whether the textAreaInInfoPane will be showed or not.
            private DocPane docPane;

            public InfoPane(codeInformation information) {
                this.information = new codeInformation(information.lineIndex, information.name, information.type ,information.annotation);
                this.information.isDocCommentExist = information.isDocCommentExist;
                this.information.docComment = information.docComment;
                setStyle("-fx-background-radius: 10;-fx-background-color: #38424D");
                close.setFitHeight(12);
                close.setFitWidth(12);
                open.setFitHeight(12);
                open.setFitWidth(12);
                label.setText(String.format("   %-10s  %-10s", information.type, information.name));
                label.setFont(new Font(15));
                this.setColor();
                textAreaInInfoPane.setFont(Font.font(15));
                textAreaInInfoPane.setPrefRowCount(1);
                textAreaInInfoPane.setText(information.annotation);
                this.textAreaChanged(information.annotation);
                radioButton.setSelected(information.isDocCommentExist);
                hBox.setAlignment(Pos.BASELINE_LEFT);
                vBox.setPadding(new Insets(11,12,13,14));
                docPane = new DocPane();
                this.setAction();
                this.getChild();
                this.setMouseClicked();
                this.widthProperty().addListener(ov->this.listen());
            }

            private synchronized void listen(){
                this.setMinHeight(50+this.getWidth()/100);
                hBox.setSpacing(this.getWidth()/25);
                vBox.setMinWidth(this.getWidth()-10);
                textAreaInInfoPane.setMinWidth(this.getWidth()-40);
                textAreaInInfoPane.setMaxWidth(this.getWidth()-40);
                docPane.setMinWidth(this.getWidth()-30);
                label.setFont(Font.font(10+this.getWidth()/100));
                label.setMinWidth(this.getWidth()*3/5);
                radioButton.setFont(Font.font(8+this.getWidth()/100));
            }

            private void setMouseClicked(){
                Node node = this;
                Queue<Node> nodes = new LinkedList<>();
                nodes.add(node);
                while (!nodes.isEmpty()){
                    node = nodes.poll();
                    if (node instanceof Parent){
                        Parent parent = (Parent) node;
                        nodes.addAll(parent.getChildrenUnmodifiable());
                    }
                    //System.out.println(node);
                    node.setOnMouseClicked(e -> SelectedInformation = this.information);
                }
            }

            private void getChild(){
                hBox.getChildren().addAll(button, label, radioButton);
                vBox.getChildren().addAll(hBox);
                if (button.getGraphic() == open && information.isDocCommentExist) {
                    vBox.getChildren().add(docPane);
                }
                this.getChildren().add(vBox);
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

            private void textAreaChanged(String text){
                information.annotation = text;
                int newLine = 0;
                while (text.contains("\n")){
                    newLine += 1;
                    text = text.substring(text.indexOf("\n")+1);
                }
                textAreaInInfoPane.setMinHeight(50+20*newLine);
            }

            private void setAction(){
                textAreaInInfoPane.setOnKeyTyped(e->textAreaChanged(textAreaInInfoPane.getText()));
                textAreaInInfoPane.setOnMouseClicked(e-> SelectedInformation = information);
                radioButton.setOnAction(e->{
                    if (radioButton.isSelected()){
                        information.isDocCommentExist = true;
                        if (button.getGraphic() == open){
                            vBox.getChildren().add(docPane);
                            docPane.listenAll(this.getWidth());
                        }
                    }
                    else {
                        information.isDocCommentExist = false;
                        vBox.getChildren().remove(docPane);
                    }
                });
                button.setOnAction(e -> {
                    if (button.getGraphic() == close) {
                        button.setGraphic(open);
                        vBox.getChildren().add(1,textAreaInInfoPane);
                        if (information.isDocCommentExist){
                            vBox.getChildren().add(docPane);
                        }
                        docPane.listenAll(this.getWidth());
                    } else {
                        button.setGraphic(close);
                        vBox.getChildren().remove(textAreaInInfoPane);
                        if (information.isDocCommentExist)
                            vBox.getChildren().remove(docPane);
                    }
                });
            }

            class DocPane extends Pane{
                private Label labelInDocPane = new Label("Description : ");
                private TextArea textAreaInDocPane = new TextArea();           // the content in this textArea will be added to the description in docComment.
                private VBox vBoxInDocPane = new VBox(10);                  // this vBox puts in the label,textArea and CommentPanes.
                private CommentPane[] commentPanes;

                public DocPane(){
                    this.initialCommentPanes();
                    labelInDocPane.setFont(Font.font(15));
                    textAreaInDocPane.setFont(Font.font(15));
                    textAreaInDocPane.setPrefRowCount(1);
                    textAreaInDocPane.setText(information.docComment.description);
                    this.textAreaChanged(information.docComment.description);
                    this.setAction();
                    this.getChild();
                    this.setMouseClicked();
                    this.widthProperty().addListener(ov->this.listen());
                }

                private void initialCommentPanes(){
                    commentPanes = new CommentPane[information.docComment.comment.size()];
                    for (int i=0;i<information.docComment.comment.size();i++){
                        commentPanes[i] = new CommentPane(i, information.docComment.comment.get(i));
                    }
                }

                private void setMouseClicked(){
                    Node node = this;
                    Queue<Node> nodes = new LinkedList<>();
                    nodes.add(node);
                    while (!nodes.isEmpty()){
                        node = nodes.poll();
                        if (node instanceof Parent){
                            Parent parent = (Parent) node;
                            nodes.addAll(parent.getChildrenUnmodifiable());
                        }
                        //System.out.println(node);
                        node.setOnMouseClicked(e -> SelectedInformation = information);
                    }
                }

                private void getChild(){
                    vBoxInDocPane.getChildren().addAll(labelInDocPane, textAreaInDocPane);
                    for (int i=0;i<information.docComment.comment.size();i++){
                        CommentPane commentPane = new CommentPane(i, information.docComment.comment.get(i));
                        vBoxInDocPane.getChildren().add(commentPane);
                    }
                    this.getChildren().add(vBoxInDocPane);
                }

                protected void listenAll(double width){
                    this.listen(width);
                    for (CommentPane commentPane : commentPanes)
                        commentPane.listen();
                }

                private synchronized void listen(){
                    listen(this.getWidth());
                }
                private synchronized void listen(double width){
                    vBoxInDocPane.setMaxWidth(width-10);
                    labelInDocPane.setFont(Font.font(10+width/100));
                    textAreaInDocPane.setMaxWidth(width-10);
                    textAreaInDocPane.setMinWidth(width-10);
                }

                private void textAreaChanged(String text){
                    information.docComment.description = text;
                    int newLine = 0;
                    while (text.contains("\n")){
                        newLine += 1;
                        text = text.substring(text.indexOf("\n")+1);
                    }
                    textAreaInDocPane.setMinHeight(50+20*newLine);
                }

                private void setAction(){
                    textAreaInDocPane.setOnKeyTyped(e->textAreaChanged(textAreaInDocPane.getText()));
                }

                class CommentPane extends Pane{
                    private Label labelInCommentPane = new Label();             // this label will show the type in comment in docComment.
                    private TextField textFieldInCommentPane = new TextField(); // the content in textField will be added to description in comment in docComment.
                    private HBox hBoxInCommentPane = new HBox(20);           // this hBox puts in text and textField.
                    private int line;                                           // record the index in comment.

                    public CommentPane(int line, String[] textArray){
                        this.line = line;
                        hBoxInCommentPane.setAlignment(Pos.BASELINE_LEFT);
                        labelInCommentPane.setPadding(new Insets(0,0,0,10));
                        labelInCommentPane.setText(textArray[0]);
                        labelInCommentPane.setTextFill(Color.GREEN);
                        textFieldInCommentPane.setText(textArray[1]);
                        this.setAction();
                        this.getChild();
                        this.widthProperty().addListener(ov->this.listen());
                    }

                    protected synchronized void listen(){
                        labelInCommentPane.setFont(Font.font(10+this.getWidth()/100));
                        labelInCommentPane.setMinWidth(105+this.getWidth()/25);
                        textFieldInCommentPane.setMinWidth(this.getWidth()-125-this.getWidth()/25);
                        textFieldInCommentPane.setMaxWidth(this.getWidth()-125-this.getWidth()/25);
                        textFieldInCommentPane.setFont(Font.font(10+this.getWidth()/100));
                    }

                    private void getChild(){
                        hBoxInCommentPane.getChildren().addAll(labelInCommentPane, textFieldInCommentPane);
                        getChildren().add(hBoxInCommentPane);
                    }

                    private void setAction(){
                        textFieldInCommentPane.setOnKeyTyped(e-> information.docComment.comment.get(line)[1] = textFieldInCommentPane.getText());
                    }
                }
            }
        }
    }
}

