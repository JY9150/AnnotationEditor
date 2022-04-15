package annotationeditor.code.FileSystem;

public class DataPath
{

    final public static String resources = ".../resources/";
    final public static String codeType = resources + "codeType/";

    public static String workingDir = System.getProperty("user.dir");
    public static String projPath;

    public static String getWorkingDir (){
        return System.getProperty("user.dir");
    }

    public static void setProjDir(String path){
        projPath = path;
    }

    public static void setWorkingDir(String path){
        workingDir = path;
    }

    public static void printInfo() {
        System.out.println("Working Directory = " + workingDir);
        System.out.println("Project Directory = " + projPath);
    }
    // private void changeLocate(dataType dType , String path){
    //     switch(dType)
    //     {
    //         case codeType:
    //         break;
    //         case resources:
    //         break;
    //     }
    // }
    // public enum dataType
    // {
    //     codeType,
    //     resources
    // }
}
