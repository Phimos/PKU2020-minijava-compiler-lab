package symbol;

import java.util.ArrayList;

public class MClass extends MIdentifier{
    protected ArrayList<MMethod> methodList = new ArrayList<MMethod>();
    protected ArrayList<MVar> varList = new ArrayList<MVar>();
    protected String parentName;
    protected MClass parentClass;


    public MClass(){}

    public MClass(String _name, String _parent, int _row, int _col){
        super(_name, "class", _row, _col);
        this.parentName = _parent;
        this.parentClass = null;
    }

    public void printAll(){
        System.out.printf("ClassName: %s\n", this.getName());
        System.out.printf("Method Num: %d\n", methodList.size());
        for(int i=0;i<methodList.size();++i){
            System.out.printf("%d Name: %s\n",i+1, methodList.get(i).getName());
        }
        System.out.printf("Variable Num: %d\n", varList.size());
        for(int i=0;i<varList.size();++i){
            System.out.printf("%d Name: %s\n",i+1, methodList.get(i).getName());
        }
    }

    public boolean existVar(String varName){
        for(MVar obj: varList){
            if(obj.getName().equals(varName)){
                return true;
            }
        }
        return false;
    }

    public MVar getVar(String varName){
        for(MVar obj: varList){
            if(obj.getName().equals(varName)){
                return obj;
            }
        }
        return null;
    }

    public boolean addVar(MVar var){
        if(!existVar(var.getName())){
            varList.add(var);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean existMethod(String methodName){
        for(MMethod obj: methodList){
            if(obj.getName().equals(methodName)){
                return true;
            }
        }
        return false;
    }

    public MMethod getMethod(String methodName){
        for(MMethod obj: methodList){
            if(obj.getName().equals(methodName)){
                return obj;
            }
        }
        return null;
    }

    public boolean addMethod(MMethod method){
        if(!existMethod(method.getName())){
            methodList.add(method);
            return true;
        }
        else{
            return false;
        }
    }

    public String getParentName(){
        return this.parentName;
    }



    public MClass getParent(){
        return this.parentClass;
    }

    public void setParent(MClass _parent){
        this.parentClass = _parent;
    }

    public boolean noParent(){
        return this.parentName == null;
    }

}