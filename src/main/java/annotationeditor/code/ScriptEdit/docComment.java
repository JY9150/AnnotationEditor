package annotationeditor.code.ScriptEdit;

import java.util.ArrayList;
import java.util.List;

public class docComment{
    public String description = "";
    public List<String[]> comment = new ArrayList<>();

    public void addComment(String type,String description){
        String[] input = {type,description};
        comment.add(input);
    }
}
