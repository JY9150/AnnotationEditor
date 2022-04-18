package annotationeditor.code;

import annotationeditor.code.FileSystem.*;
import annotationeditor.code.ScriptEdit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test1 {

    public static void main(String[] args)  {
        saveDefaultCodeData();
    }

    private static void saveDefaultCodeData(){
        ArrayList<String> codeTypeList = new ArrayList<>();
        codeTypeList.add("void");
        codeTypeList.add("class");
        codeTypeList.add("int");
        codeTypeList.add("float");
        codeTypeList.add("double");
        codeTypeList.add("boolean");
        codeTypeList.add("String");
        codeTypeList.add("List<String>");
        codeTypeList.add("ArrayList<codeInformation>");
        codeTypeList.add("string");

        File_System.saveAsJson(codeTypeList,"codeTypeDefault.txt");
    }
}
