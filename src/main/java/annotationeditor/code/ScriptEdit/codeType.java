package annotationeditor.code.ScriptEdit;
/**
 * public :
 * 
 * getTypeList
 * addType
 * removeType
 * 
 * getInfoList
 */ 

import annotationeditor.code.FileSystem.File_System;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class codeType
{

    private ArrayList<String> codeTypeList = new ArrayList<>();
    private String dataName = "";

    public codeType(String dataName){
        if(!loadData(dataName)){
            createData(dataName);
            loadData(dataName);
        }
    }

    public void addNewType(String newType){
        codeTypeList.add(newType);
        saveData(dataName);
    }


    public boolean removeType(int targetTypeIndex){return removeType(codeTypeList.get(targetTypeIndex)); }
    public boolean removeType(String targetType)
    {
        boolean removeSuccessfully = codeTypeList.remove(targetType);
        saveData(dataName);
        return removeSuccessfully;
    }

    // if can , turn void to bool
    private boolean loadData(String dataName)
    {
        if (!new File(dataName).exists())
            return false;
        codeTypeList = File_System.loadFromJson(ArrayList.class,dataName);
        return true;
    }

    private void saveData(String dataName)
    {
        File_System.saveAsJson(codeTypeList, dataName);
    }

    private void createData(String dataName)
    {
        File file = new File(dataName);
        try {
            file.createNewFile();
            Scanner scanner = new Scanner(new File("codeTypeDefault.txt"));
            PrintWriter printWriter = new PrintWriter(dataName);
            while (scanner.hasNext()) {
                printWriter.println(scanner.next());
                printWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("Invalid input dataName");
        }
    }
    public ArrayList<String> getTypeList(){
        return codeTypeList;
    }
}