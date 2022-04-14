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

import java.util.ArrayList;

public class codeType
{

    private ArrayList<String> codeTypeList = new ArrayList<>();

    public codeType(String dataName){
        loadData(dataName);
        //do something
    }

    public void addNewType(String newType){
        codeTypeList.add(newType);
        saveData();
    }


    public boolean removeType(int targetTypeIndex){return removeType(codeTypeList.get(targetTypeIndex)); }
    public boolean removeType(String targetType)
    {
        saveData();
        if(true) return true;
        return false;
    }

    // if can , turn void to bool
    private void loadData(String dataName)
    {
        codeTypeList = File_System.loadFromJson(ArrayList.class,"codeTypeDefault.txt");
    }

    private void saveData()
    {
    }

    private void createData(String dataName)
    {

    }
    public ArrayList<String> getTypeList(){
        return codeTypeList;
    }
}