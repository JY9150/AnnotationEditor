package annotationeditor.code.ScriptEdit;

/**
 * lineIndex  程式碼所在的行數<BR/>
 * ------------------------<BR/>
 * name       程式碼之敘述<BR/>
 * ------------------------<BR/>
 * type       程式碼之類型<BR/>
 * &nbsp;&nbsp;&nbsp;&nbsp;class<BR/>
 * &nbsp;&nbsp;&nbsp;&nbsp;function<BR/>
 * &nbsp;&nbsp;&nbsp;&nbsp;value<BR/>
 * ------------------------<BR/>
 * annotation 程式碼之註解
 * ------------------------<BR/>
 */

public class codeInformation {
    public int lineIndex;
    public String name;
    public String type;
    public String annotation;
    public boolean isDocCommentExist = false;
    public docComment docComment = new docComment();

    public codeInformation(int lineIndex,String name,String type,String annotation){
        this.lineIndex = lineIndex;
        this.name = name;
        this.type = type;
        this.annotation = annotation;
    }
}
