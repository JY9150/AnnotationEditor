package annotationeditor.code.ScriptEdit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class codeData {

    private String filePath;
    private List<String> precessedScript;
    private ArrayList<String> codeTypeList = new ArrayList<>();
    private ArrayList<codeInformation> informationList = new ArrayList<>();

    public codeData(String filePath ,ArrayList<String> codeTypeList){
        this.filePath = filePath;
        this.codeTypeList = codeTypeList;
        processInputScript();
    }

    public ArrayList<codeInformation> getInfoList(String[] typeArray){return getInfoList(typeArray,null,false);}
    public ArrayList<codeInformation> getInfoList(String type){return getInfoList(null , type , false);}
    public ArrayList<codeInformation> getInfoList(){return getInfoList(null,null,true);}
    public ArrayList<codeInformation> getInfoList(String[] typeArray , String type , Boolean returnAll)
    {
        if(returnAll){
            return informationList; // return All result
        }
        else if( type != null)
        {
            //do something that return result same with type
        }
        else if( typeArray != null)
        {
            //do something that return result same with type
        }
        else {
            System.out.println("Function getInfoList() : input value not correct");
            return null;
        }

        // remove when you finish it.
        return new ArrayList<>();
    }
    public List<String>



    getPrecessedScript(){return precessedScript;}

    //處理輸入的腳本資料
    private void processInputScript(){
        Scanner file = null;
        try {file = new Scanner(new FileReader(filePath));}
        catch (FileNotFoundException e) {e.printStackTrace();}

        List<String> input = new ArrayList<>();
        try {
            input = Files.readAllLines(Path.of(filePath));
        }catch (IOException e){
            System.out.println("Fail to process "+filePath);}

        int removeCount = 0;
        int index=0;
        boolean isFound = false;
        for(int i=0;index<input.size();index=++i - removeCount)
        {
            String line = input.get(index);
            // 判斷是否為 Class
            if(isClass(line)){
                informationList.add(new codeInformation(i,line.substring(line.indexOf("class")+6, line.contains("{") ? line.indexOf("{") : line.length())," class",""));
                isFound = true;
            }// 判斷是否為 ......
//                else if(input.get(index).contains(codeType))
//                {
//                    informationList.add(new codeInformation(i,input.get(index),codeType,"",""));
//                    input.remove(index);
//                    removeCount++;
//                }

            //讀取註解
            if(isFound){
                String annotation = "";
                index--;
                if(index >= 0){
                    String end;
                    if(input.get(index).contains("//"))
                        end = "//";
                    else if (input.get(index).contains("*/"))
                        end = "/**";
                    else end = null;

                    if(end != null){
                        while (index >= 0? ! input.get(index).contains(end) : false){
                            annotation = input.get(index) + '\n' + annotation;
                            input.remove(index);
                            index--;
                            removeCount++;
                        }
                        annotation = input.get(index) + '\n' + annotation;
                        input.remove(index);
                        removeCount++;
                    }
                    var temp = informationList.size()-1;
                    informationList.get(temp).annotation = annotation;
                    informationList.get(temp).lineIndex-=removeCount;
                }
                isFound = false;
            }
        }
        this.precessedScript = input;
    }

    private boolean isClass(String input){
        return  input.contains("class") &&
                ! input.contains(".class") &&
                (! input.contains("(") && ! input.contains(")")) ;
    }

}
