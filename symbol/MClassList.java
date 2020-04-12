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
            ErrorPrinter.getError(2, mclass, "class");
        }
    }

    public void setAllParent(){
        for(MClass obj: classList){
            String parentName = obj.getParentName();
            if(parentName != null){
                MClass parent = findClass(parentName);
                if(parent == null){
                    ErrorPrinter.getError(1, this, "class");
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
                ErrorPrinter.getError(6, obj);
            }
        }
    }

    public boolean overloading(MMethod method1, MMethod method2){
        if(!method1.getName().equals(method2.getName())){
            return false;
        }
        if(!method1.getReturn().equals(method2.getReturn())){
            return true;
        }
        ArrayList<MVar> params1 = method1.getParams();
        ArrayList<MVar> params2 = method2.getParams();
        if(params1.size()!=params2.size()){
            return true;
        }
        for(int i=0;i<params1.size();++i){
            if(!params1.get(i).getType().equals(params2.get(i).getType())){
                return true;
            }
        }
        return false;
    }

    public void checkOneOverloading(MClass obj){
        if(obj.noParent()){
            return;
        }
        for(MClass parent = obj.getParent(); parent!=null; parent = parent.getParent()){
            for(MMethod method1: obj.getMethodList()){
                for(MMethod method2: parent.getMethodList()){
                    if(overloading(method1, method2)){
                        ErrorPrinter.getError(0, method1);
                    }
                }
            }
        }
        return;
    }

    public void checkAllOverloading(){
        for(MClass obj: classList){
            checkOneOverloading(obj);
        }
    }



    public void printAll(){
        for(MClass obj: classList){
            obj.printAll();
        }
    }

    // ---piglet---
    public void classComplete(){
        for(MClass tClass :classList){
            tClass.classComplete();
        }
    }

    public int allocTemp(int currentTmep){
        for(MClass tClass :classList){
            currentTmep = tClass.allocTemp(currentTmep);
        }
        setTempNum(currentTmep+1);
        return currentTmep;
    }

} 