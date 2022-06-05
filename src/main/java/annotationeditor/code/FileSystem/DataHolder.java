package annotationeditor.code.FileSystem;

import annotationeditor.code.ScriptEdit.*;
import annotationeditor.code.FileSystem.*;

public class DataHolder {
    //建立單例
    private static DataHolder instance  = new DataHolder();
    private DataHolder(){}

    public static DataHolder getInstance(){
        return instance;
    }

    //公有變數
    public codeType codeType;
    public codeData codeData;

}
