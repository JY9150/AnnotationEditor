package annotationeditor.code.ScriptEdit;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ScriptEdit_System 
{
    private static ArrayList<codeInformation> informationList = new ArrayList<>();
    private static codeData codeData;
    private static ArrayList<String> codeTypeList = new codeType("codeTypeDefault.txt").getTypeList();

    public static void main(String[] args)
    {
        codeTypeList.add("PrintWriter");
        codeTypeList.add("TextField");
        codeTypeList.add("HBox");
        codeData = new codeData("H:\\demo\\AnnotationEditor\\src\\main\\java\\annotationeditor\\code\\ScriptEdit\\codeData.java",codeTypeList);
        informationList = codeData.getInfoList();

        debug.showProcessedScript();
        debug.showInformationList();

        System.out.println(informationList.get(5).name);
        System.out.print(informationList.get(5).docComment.description);
        for (String[] i : informationList.get(5).docComment.comment){
            System.out.println("   "+i[0]+"  "+i[1]);
        }

        debug.descriptionSample(5,0.5f);
    }

    private static class debug{
         static void showInformationList(){
            System.out.println("\nThe result of process Data in informationList :\n-------------------------------------");
            for (codeInformation i:
                    codeData.getInfoList()){

                if(! i.annotation.equals("")){
                    System.out.print(i.annotation);
                }
                System.out.println("line "+i.lineIndex+" | "+i.type+" "+i.name);
                System.out.println("-------------------------------------");
            }
        }
        static  void showProcessedScript(){
             System.out.println("\nThe result of processed Script :\n-------------------------------------");
             for(int i=0;i<codeData.getPrecessedScript().size();i++)
                 System.out.println(String.format("% "+(String.valueOf(codeData.getPrecessedScript().size())).length()+"d" ,i)+" | "+codeData.getPrecessedScript().get(i));
        }

        /**
         * @param a is your magic Number.
         * @param b don't do anything.
         * @see codeInformation
         */
        static codeInformation descriptionSample(int a,float b){
             return new codeInformation(a,String.valueOf(b),"","");
        }
    }
}
