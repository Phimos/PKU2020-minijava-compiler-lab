package symbol;

import java.util.ArrayList;
import typecheck.*;

public class MClassList extends MType{

    protected ArrayList<MClass> classList = new ArrayList<MClass>();

    public MClassList(){}

    public MClass findClass(String className){
        for(MClass obj: classList){
            if(obj.getName().equals(className)){
                return obj;
            }
        }
        return null;
    }

    public boolean existClass(String className){
        return !(findClass(className) == null);
    }

    public void addClass(MClass mclass){
        if(!existClass(mclass.getName())){
            classList.add(mclass);
        }
        else{
            System.out.println("Class define repeatedly");
        }
    }

    public void setAllParent(){
        for(MClass obj: classList){
            String parentName = obj.getParentName();
            if(parentName != null){
                MClass parent = findClass(parentName);
                if(parent == null){
                    ErrorPrinter.addError(String.format("ERROR 6:\n\tno parent: can't extend from nonexist type\n\tlocation: row %d, col %d", obj.getRow(), obj.getCol()));
                }
                obj.setParent(parent);
            }
        }
    }

    public boolean checkOneCycle(MClass obj){
        if(obj.noParent()){
            return false;
        }
        String className = obj.getName();
        for(MClass parent = obj.getParent(); parent!=null; parent = parent.getParent()){
            if(parent.getName() == className){
                return true;
            }
        }
        return false;
    }

    public void checkAllCycle(){
        for(MClass obj: classList){
            if(checkOneCycle(obj)){
                ErrorPrinter.addError(String.format("ERROR 6:\n\tloop inherit: you can't be your own father\n\tlocation: row %d, col %d", obj.getRow(), obj.getCol()));
            }
        }
    }

    public void printAll(){
        for(MClass obj: classList){
            obj.printAll();
        }
    }
} 