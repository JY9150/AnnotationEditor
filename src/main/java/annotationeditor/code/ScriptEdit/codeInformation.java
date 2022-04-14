package annotationeditor.code.ScriptEdit;

public class codeInformation {
    public int lineIndex;
    public String name;
    public String type;
    public String annotation;

    public codeInformation(int lineIndex,String name,String type,String annotation){
        this.lineIndex = lineIndex;
        this.name = name;
        this.type = type;
        this.annotation = annotation;
    }
}
