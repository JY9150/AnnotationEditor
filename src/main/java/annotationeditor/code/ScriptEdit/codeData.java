package annotationeditor.code.ScriptEdit;

import java.io.IOException;
import java.lang.ref.Reference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class codeData {

    private String filePath;
    private List<String> precessedScript;
    private ArrayList<String> codeTypeList = new ArrayList<>();
    private ArrayList<codeInformation> informationList = new ArrayList<>();

    public codeData(String filePath ,ArrayList<String> codeTypeList){
        this.filePath = filePath;
        this.codeTypeList = codeTypeList;
        process();
    }


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
    public ArrayList<codeInformation> getInfoList(String[] typeArray){return getInfoList(typeArray,null,false);}
    public ArrayList<codeInformation> getInfoList(String type){return getInfoList(null , type , false);}
    /**
     * 取得 information 格式之 ArrayList
     * @param typeArray     String[] 多 tags 過濾
     * @param type          String  單 tag 過濾
     * @param returnAll     Boolean  回傳所有
     * @return ArrayList
     * @see codeInformation infomation 之儲存格式
     */
    public ArrayList<codeInformation> getInfoList(){return getInfoList(null,null,true);}


    public List<String> getPrecessedScript(){return precessedScript;}

    /**
     * 處理輸入的腳本資料
     * 並儲存至 informationList 變數
     */
    private void process(){
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
                informationList.add(new codeInformation(i,line.substring(line.indexOf("class")+6, line.contains("{") ? line.indexOf("{") : line.length()),"class",""));
                isFound = true;
            }
            else if(! isFunction(line).equals("")){
                String type = isFunction(line);
                informationList.add(new codeInformation(i,line.substring(line.indexOf(type), line.contains("{") ? line.indexOf("{") : line.length()),"function",""));
                isFound = true;
            }

            //讀取註解  todo : 更優化的處理空格符號
            if(isFound){
                String temp;
                codeInformation information =  informationList.get(informationList.size()-1);
                boolean isDC,isAnno = false;

                index--;
                while (index >= 0){
                    temp = input.get(index);
                    while (temp.length()>0 && temp.charAt(0) == ' ')
                        temp = temp.substring(1);

                    try {isDC = temp.replaceAll("\\s","").charAt(0) == '*';}
                    catch (StringIndexOutOfBoundsException e) {isDC = false;}

                    if(temp.contains("@param")){
                        String t = "";
                        try {
                            t = temp.substring(temp.indexOf("@param")+7).split(" ")[0];
                        }
                        catch (StringIndexOutOfBoundsException e){

                        }
                        information.docComment.addComment("@param"+' '+t,temp.substring(temp.indexOf("@param")+8+t.length()));
                    }
                    else if(temp.contains("@return")){
                        information.docComment.addComment("@return",temp.substring(temp.indexOf("@return")+8));
                    }
                    else if(temp.contains("@see")){
                        information.docComment.addComment("@see",temp.substring(temp.indexOf("@see")+5));
                    }
                    else if(temp.contains("/**")){
                        isAnno = false;
                    }
                    else if(temp.contains("//")) {
                        if(temp.length() > 2)
                            information.annotation += temp.substring(2)+'\n';
                    }
                    else if(temp.contains("/*")){
                        if(temp.length() > 2)
                            information.annotation += temp.substring(2)+'\n';
                        isAnno = false;
                    }
                    else if(temp.contains("*/")){
                        isAnno = true;
                    }
                    else if(isDC){
                        information.docComment.description += temp.substring(temp.indexOf("*")+1)+'\n';
                    }
                    else if(isAnno){
                        information.annotation += temp+'\n';
                    }
                    else {
                        break;
                    }

                    input.remove(index);
                    index--;
                    removeCount++;
                }

                if(! isFunction(line).equals("")){
                    codeInformation thisInfo = informationList.get(informationList.size()-1);
                    List<String> comment = new ArrayList<>();
                    String type = isFunction(line);
                    if(!type.equals("void"))
                        comment.add("void");
                    String[] param = line.substring(line.indexOf("(")+1,line.indexOf(")")).split(",");

                    for (String p : param){
                        String name =  p.split(" ")[1];
                        boolean isExist = false;
                        for(String[] s : thisInfo.docComment.comment){
                            if(name.equals(s[0])) {
                                isExist = true;
                                break;
                            }
                        }

                        if(! isExist){
                            String[] newComment = new String[2];
                            newComment[0] = name;
                            thisInfo.docComment.comment.add(newComment);
                        }
                    }
                }

                isFound = false;
            }
        }
        this.precessedScript = input;
        System.out.print(OutputProcessedScript());
    }

    private boolean isClass(String input){
        return  input.contains("class") &&
                ! input.contains(".class") &&
                (! input.contains("(") && ! input.contains(")")) ;
    }

    private String isFunction(String input){
        String output = "";
        for(String i :codeTypeList){
            if (    input.contains(' '+i+' ') && (input.contains("public") || input.contains("private"))) {
                String temp = input.substring(input.indexOf(i)+i.length());
                if(temp.contains("(") && temp.contains(")")){
                    output = i;
                    break;
                }
            }
        }
        return output;
    }

    public String OutputProcessedScript(){
        List<String> output = new ArrayList<>(precessedScript);
        int lineOffset = 0;
        for(codeInformation information : informationList){
            if(!information.annotation.equals(null)){
                output.add(information.lineIndex+lineOffset,information.annotation.replace("\n","\n//"));
                lineOffset++;
            }
            if(!information.docComment.description.equals(null)){
                output.add(information.lineIndex+lineOffset,"/**\n*");
                lineOffset++;
                output.add(information.lineIndex+lineOffset,information.docComment.description.replace("\n","\n*"));
                lineOffset++;
                for(String[] i : information.docComment.comment){
                    String temp = "* @"+i[0]+" "+i[1];
                    output.add(information.lineIndex+lineOffset,temp);
                    lineOffset++;
                }
            }
            output.add(information.lineIndex+lineOffset,"*/");
            lineOffset++;
        }

        String outputStr = "";
        for(String i : output){
            outputStr+=i+"\n";
        }

        return outputStr;
    }
}
