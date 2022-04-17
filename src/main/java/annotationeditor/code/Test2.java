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
        codeData codeData = new codeData("test.txt",new codeType("code.txt").getTypeList());
        ArrayList<codeInformation> infoList = codeData.getInfoList();
        infoList.get(0).isDocCommentExist = true;
        infoList.get(0).docComment.description = "test";
        infoList.get(0).docComment.addComment("@param lineIndex","lineIndex");
        infoList.get(0).docComment.addComment("@param type","type");
        infoList.get(1).docComment.addComment("@return", "String");
        infoList.get(2).type = "value";
        infoList.get(3).type = "function";
        SecondPane secondPane = new SecondPane(infoList);
        ScrollPane scrollPane = new ScrollPane(secondPane);
        StackPane stackPane2 = new StackPane(new Label("2"));
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(stackPane1,scrollPane,stackPane2);
        splitPane.setDividerPosition(1,250);
        Scene scene = new Scene(splitPane,1000,600);
        primaryStage.setTitle("secondPane test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
class SecondPane extends Pane {
    VBox vBoxForInfoPane = new VBox(15);

    public SecondPane(ArrayList<codeInformation> informationArrayList) {
        for (codeInformation information : informationArrayList) {
            InfoPane infoPane = new InfoPane(information);
            vBoxForInfoPane.getChildren().add(infoPane);
        }
        getChildren().add(vBoxForInfoPane);
    }

    class InfoPane extends Pane {
        codeInformation information;
        HBox hBox = new HBox(10); // this hBox puts in button,label,radioButton.
        VBox vBox = new VBox(5);      // this vBox puts in hBox,textAreaInInfoPane.If radioButton is selected, docPane will be added,too.
        Label label = new Label();   // this label represents the type and the name in the information.
        RadioButton radioButton = new RadioButton("DocComment");
        TextArea textAreaInInfoPane = new TextArea(); // the content of the TextArea will be added to the annotation in information.
        ImageView close = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_close.png").toString()));
        ImageView open = new ImageView(new Image(App.class.getResource("/annotationeditor/image/button/arrow_open.png").toString()));
        Button button = new Button("", close); // this button will determine whether the textAreaInInfoPane will be showed or not.
        DocPane docPane;

        public InfoPane(codeInformation information) {
            this.information = information;
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
            vBox.setPadding(new Insets(5));
            this.textAreaChanged(information.annotation);
            this.setAction();
            this.getChild();
        }

        public void getChild(){
            hBox.getChildren().addAll(button, label, radioButton);
            vBox.getChildren().addAll(hBox);
            if (information.isDocCommentExist) {
                docPane = new DocPane(information.docComment);
                vBox.getChildren().add(docPane);
            }
            getChildren().add(vBox);
        }

        public void setColor() {
            if (information.type.contains("class")) {
                label.setTextFill(Color.ORANGE);
            } else if (information.type.contains("function")) {
                label.setTextFill(Color.YELLOW);
            } else if (information.type.contains("value")) {
                label.setTextFill(Color.INDIGO);
            }
        }

        public void textAreaChanged(String text){
            information.annotation = text;
            int newLine = 0;
            while (text.contains("\n")){
                newLine += 1;
                text = text.substring(text.indexOf("\n")+1);
            }
            textAreaInInfoPane.setMinHeight(30+20*newLine);
        }

        public void setAction(){
            textAreaInInfoPane.setOnKeyTyped(e->textAreaChanged(textAreaInInfoPane.getText()));
            radioButton.setSelected(information.isDocCommentExist);
            radioButton.setOnAction(e->{
                if (radioButton.isSelected()){
                    docPane = new DocPane(information.docComment);
                    vBox.getChildren().add(docPane);
                }
                else {
                    vBox.getChildren().remove(docPane);
                }
            });
            button.setOnAction(e -> {
                if (button.getGraphic() == close) {
                    button.setGraphic(open);
                    vBox.getChildren().add(1,textAreaInInfoPane);
                } else {
                    button.setGraphic(close);
                    vBox.getChildren().remove(textAreaInInfoPane);
                }
            });

        }

        public class DocPane extends Pane{
            docComment docComment;
            Label labelInDocPane = new Label("Description : ");
            TextArea textAreaInDocPane = new TextArea();           // the content in this textArea will be added to the description in docComment.
            VBox vBoxInDocPane = new VBox(10);                  // this vBox puts in the label,textArea and CommentPanes.

            public DocPane(docComment docComment){
                this.docComment = docComment;
                labelInDocPane.setFont(Font.font(15));
                textAreaInDocPane.setFont(Font.font(15));
                textAreaInDocPane.setText(docComment.description);
                textAreaInDocPane.setPrefRowCount(1);
                textAreaInDocPane.setMaxWidth(500);
                this.textAreaChanged(information.docComment.description);
                this.setAction();
                this.getChild();
            }

            public void getChild(){
                vBoxInDocPane.getChildren().addAll(labelInDocPane, textAreaInDocPane);
                for (int i=0;i<information.docComment.comment.size();i++){
                    CommentPane commentPane = new CommentPane(i, information.docComment.comment.get(i)[0]);
                    vBoxInDocPane.getChildren().add(commentPane);
                }
                getChildren().add(vBoxInDocPane);
            }

            public void textAreaChanged(String text){
                information.annotation = text;
                int newLine = 0;
                while (text.contains("\n")){
                    newLine += 1;
                    text = text.substring(text.indexOf("\n")+1);
                }
                textAreaInInfoPane.setMinHeight(30+20*newLine);
            }

            public void setAction(){
                textAreaInDocPane.setOnKeyTyped(e->textAreaChanged(information.docComment.description));
            }

            class CommentPane extends Pane{
                Label labelInCommentPane = new Label();             // this label will show the type in comment in docComment.
                TextField textFieldInCommentPane = new TextField(); // the content in textField will be added to description in comment in docComment.
                HBox hBoxInCommentPane = new HBox(20);           // this hBox puts in text and textField.
                int line;                                           // record the position in comment in docComment.

                public CommentPane(int line, String text){
                    this.line = line;
                    labelInCommentPane.setPadding(new Insets(0,0,0,10));
                    labelInCommentPane.setText(text);
                    labelInCommentPane.setFont(Font.font(15));
                    labelInCommentPane.setTextFill(Color.GREEN);
                    labelInCommentPane.setMinWidth(150);
                    textFieldInCommentPane.setMinWidth(330);
                    this.setAction();
                    this.getChild();
                }

                public void getChild(){
                    hBoxInCommentPane.getChildren().addAll(labelInCommentPane, textFieldInCommentPane);
                    getChildren().add(hBoxInCommentPane);
                }

                public void setAction(){
                    textFieldInCommentPane.setOnKeyTyped(e->
                            information.docComment.comment.get(line)[1] = textFieldInCommentPane.getText());
                }
            }
        }
    }
}