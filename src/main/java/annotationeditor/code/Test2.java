package annotationeditor.code;

import annotationeditor.code.ScriptEdit.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Test2 extends Application {
    @Override
    public void start(Stage primaryStage){
        StackPane stackPane1 = new StackPane(new Label("0"));
        //codeData codeData = new codeData("test.txt",new codeType("code.txt").getTypeList());
        codeData codeData = new codeData("C:/Users/howar/Desktop/IDEA java/src/A4_110504513.java",new codeType("code.txt").getTypeList());
        ArrayList<codeInformation> infoList = codeData.getInfoList();
//        infoList.get(0).isDocCommentExist = true;
//        infoList.get(0).docComment.description = "test";
//        infoList.get(0).docComment.addComment("@param lineIndex","lineIndex");
//        infoList.get(0).docComment.addComment("@param type","type");
//        infoList.get(1).docComment.addComment("@return", "String");
//        infoList.get(2).type = "value";
//        infoList.get(3).type = "function";
        ScrollPane secondPane = new ScrollPane(new SecondPane(infoList));
        StackPane stackPane2 = new StackPane(new Label("2"));
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(stackPane1,secondPane,stackPane2);
        splitPane.setDividerPosition(1,250);
        Scene scene = new Scene(splitPane,1000,600);
        primaryStage.setTitle("secondPane test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
class SecondPane extends StackPane {
    VBox vBoxForInfoPane = new VBox(15);

    public SecondPane(ArrayList<codeInformation> informationArrayList) {
        this.setPadding(new Insets(11,12,13,14));
        this.setStyle("-fx-background-color: #111C26");
        for (codeInformation information : informationArrayList) {
            InfoPane infoPane = new InfoPane(information);
            vBoxForInfoPane.getChildren().add(infoPane);
        }
        this.getChildren().add(vBoxForInfoPane);
    }

    class InfoPane extends Pane {
        codeInformation information;
        HBox hBox = new HBox(10); // this hBox puts in button,label,radioButton.
        VBox vBox = new VBox(5);      // this vBox puts in hBox,textAreaInInfoPane.If radioButton is selected, docPane will be added,too.
        Label label = new Label();   // this label presents the type and the name in the information.
        RadioButton radioButton = new RadioButton("DocComment");
        TextArea textAreaInInfoPane = new TextArea(); // the content in the TextArea will be added to the annotation in information.
        ImageView close = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_close.png").toString()));
        ImageView open = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_open.png").toString()));
        Button button = new Button("", close); // this button will determine whether the textAreaInInfoPane will be showed or not.
        DocPane docPane;

        public InfoPane(codeInformation information) {
            this.information = new codeInformation(information.lineIndex, information.name, information.type ,information.annotation);
            this.information.isDocCommentExist = information.isDocCommentExist;
            this.information.docComment = information.docComment;
            setStyle("-fx-background-radius: 10;-fx-background-color: #38424D");
            close.setFitHeight(12);
            close.setFitWidth(12);
            open.setFitHeight(12);
            open.setFitWidth(12);
            label.setText(String.format("%10s  %10s", information.type, information.name));
            label.setMinWidth(350);
            label.setFont(new Font(15));
            this.setColor();
            textAreaInInfoPane.setFont(Font.font(15));
            textAreaInInfoPane.setPrefRowCount(1);
            textAreaInInfoPane.setMaxWidth(500);
            textAreaInInfoPane.setMaxHeight(100);
            textAreaInInfoPane.setText(information.annotation);
            this.textAreaChanged(information.annotation);
            radioButton.setSelected(information.isDocCommentExist);
            vBox.setPadding(new Insets(11,12,13,14));
            docPane = new DocPane();
            this.setAction();
            this.getChild();
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
            textAreaInInfoPane.setMinHeight(30+20*newLine);
        }

        private void setAction(){
            textAreaInInfoPane.setOnKeyTyped(e->textAreaChanged(textAreaInInfoPane.getText()));
            radioButton.setOnAction(e->{
                if (radioButton.isSelected()){
                    information.isDocCommentExist = true;
                    if (button.getGraphic() == open)
                        vBox.getChildren().add(docPane);
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
                    if (information.isDocCommentExist) {
                        vBox.getChildren().add(docPane);
                    }
                } else {
                    button.setGraphic(close);
                    vBox.getChildren().remove(textAreaInInfoPane);
                    if (information.isDocCommentExist)
                        vBox.getChildren().remove(docPane);
                }
            });
        }

        public class DocPane extends Pane{
            Label labelInDocPane = new Label("Description : ");
            TextArea textAreaInDocPane = new TextArea();           // the content in this textArea will be added to the description in docComment.
            VBox vBoxInDocPane = new VBox(10);                  // this vBox puts in the label,textArea and CommentPanes.

            public DocPane(){
                labelInDocPane.setFont(Font.font(15));
                textAreaInDocPane.setFont(Font.font(15));
                textAreaInDocPane.setPrefRowCount(1);
                textAreaInDocPane.setMaxWidth(500);
                textAreaInDocPane.setText(information.docComment.description);
                this.textAreaChanged(information.docComment.description);
                this.setAction();
                this.getChild();
            }

            private void getChild(){
                vBoxInDocPane.getChildren().addAll(labelInDocPane, textAreaInDocPane);
                for (int i=0;i<information.docComment.comment.size();i++){
                    vBoxInDocPane.getChildren().add(new CommentPane(i, information.docComment.comment.get(i)));
                }
                this.getChildren().add(vBoxInDocPane);
            }

            private void textAreaChanged(String text){
                information.docComment.description = text;
                int newLine = 0;
                while (text.contains("\n")){
                    newLine += 1;
                    text = text.substring(text.indexOf("\n")+1);
                }
                textAreaInDocPane.setMinHeight(30+20*newLine);
            }

            private void setAction(){
                textAreaInDocPane.setOnKeyTyped(e->textAreaChanged(textAreaInDocPane.getText()));
            }

            class CommentPane extends Pane{
                Label labelInCommentPane = new Label();             // this label will show the type in comment in docComment.
                TextField textFieldInCommentPane = new TextField(); // the content in textField will be added to description in comment in docComment.
                HBox hBoxInCommentPane = new HBox(20);           // this hBox puts in text and textField.
                int line;                                           // record the index in comment.

                public CommentPane(int line, String[] textArray){
                    this.line = line;
                    labelInCommentPane.setPadding(new Insets(0,0,0,10));
                    labelInCommentPane.setText(textArray[0]);
                    labelInCommentPane.setFont(Font.font(15));
                    labelInCommentPane.setTextFill(Color.GREEN);
                    labelInCommentPane.setMinWidth(150);
                    textFieldInCommentPane.setMinWidth(330);
                    textFieldInCommentPane.setText(textArray[1]);
                    this.setAction();
                    this.getChild();
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