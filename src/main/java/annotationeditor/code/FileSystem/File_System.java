package annotationeditor.code.FileSystem;

import annotationeditor.code.App;
import com.google.gson.Gson;
import javafx.scene.image.Image;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

public class File_System {
    private static String imageFolderPath = new File("target/classes/annotationeditor/image").getAbsolutePath();

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
    public static <T> void saveAsJson(T saveObject , String path) {
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

    public static void saveAsTxt(String saveObject, String path){
        try{
            PrintWriter file = new PrintWriter(path);
            file.write(saveObject);
            file.flush();
            file.close();
        }
        catch (IOException e){
            System.out.println("File_System : save file to "+ path +" fail , please check the folder");
        }
    }
    public static void saveAsTxt(ArrayList<String> saveObjectList, String path){saveAsTxt(saveObjectList.toArray(new String[0]),path);}
    public static void saveAsTxt(String[] saveObjectArray, String path){
        String temp = "";
        for (int i=0;i < saveObjectArray.length;i++) {
            temp+=saveObjectArray[i] ;
            if(i < saveObjectArray.length-1) temp+='\n';
        }
        saveAsTxt(temp,path);
    }

    public static ArrayList<String> loadFromTxt(String path){
        ArrayList<String> dataArrayList = new ArrayList<>();
        try{
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNext()){
                dataArrayList.add(scanner.nextLine());
            }
        }
        catch (IOException e){
            System.out.println("File_System : load file from "+ path +" fail");
        }
        return dataArrayList;
    }

    public static Image getImage(String name ){
        return new Image(imageFolderPath+"/icon/"+name.toString());
    }
    public static Image getImage(String name ,double width ,double height){
        System.out.println(imageFolderPath+"/icon/"+name);
        return new Image(imageFolderPath+"/icon/"+name,width,height,true,false);
    }
}
