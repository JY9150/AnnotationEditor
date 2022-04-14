package annotationeditor.code.FileSystem;

import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Scanner;

public class File_System {
    public static <T> T loadFromJson(Type typeOfData, String path){
        T output ;
        String jsonData = new String();
        try{
            Scanner file = new Scanner(new FileReader(path));
            while (file.hasNext())
                jsonData += file.next();

        }
        catch (IOException e){
            System.out.println("File_System : load file from "+path+" fail");
        }

        output = new Gson().fromJson(jsonData,typeOfData);
        return output;
    }

    public static <T> void saveAsJson(T saveObject , String path)
    {
        String jsonObject = new Gson().toJson(saveObject);
        try{
            PrintWriter file = new PrintWriter(path);
            file.write(jsonObject);
            file.flush();
            file.close();
        }
        catch (IOException e){
            System.out.println("File_System : save file to "+path+" fail , please check the folder");
        }
    }

}
