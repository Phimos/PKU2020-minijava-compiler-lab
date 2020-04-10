package symbol;

import java.util.ArrayList;

import org.graalvm.compiler.asm.sparc.SPARCAssembler.MembarMask;

import sun.jvm.hotspot.oops.Oop;
import typecheck.*;

public class MClass extends MIdentifier{
    protected ArrayList<MMethod> methodList = new ArrayList<MMethod>();
    protected ArrayList<MVar> varList = new ArrayList<MVar>();
    protected MType parentName;
    protected MClass parentClass;


    public MClass(){}

    public MClass(String _name, MType _parent, int _row, int _col){
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
            ErrorPrinter.getError(2, var, "variable");
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
            ErrorPrinter.getError(2, method, "method");
            return false;
        }
    }

    public ArrayList<MMethod> getMethodList(){
        return methodList;
    }

    public String getParentName(){
        if(this.parentName != null){
            return this.parentName.getName();
        }
        else{
            return null;
        }
    }

    public MType getParentToken(){
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

    // ---piglet---
    public void classComplete(){
        // add method derive from father to class
        if(this.getPigStatus() == 0){
            return ; 
        }

        // complete parent class first
        MClass pClass;
        if(getParent()!=null){
            pClass = getParent();
            pClass.classComplete();

            // get derive method
            for(MMethod pMethod :pClass.methodList){
                if(existMethod(pMethod.getName()))continue;
                this.addMethod(pMethod);
            }
            
            for(MVar pVar :pClass.varList){
                this.addVar(pVar);
                // Var need to be added all
            }
        }

        // add pigName
        for(MMethod tMethod :this.methodList){
            if(tMethod.getPigName() == null){
                tMethod.setPigName(this.getName()+"_"+tMethod.getName());
            }
        }

        for(MVar tVar :this.varList){
            if(tVar.getPigName() == null){
                tVar.setPigName(this.getName()+"_"+tVar.getName());
            }
        }

        this.setPigStatus(0);
        // done
    }

    // for method offset find
    public boolean pdMehtodOffset(int offset){
        for(MMethod tMethod :methodList){
            if(tMethod.getOffset() == -1)continue;
            
            if(tMethod.getOffset() == offset){
                return false;
            }
        }
        return true;
    }

    public int allocTemp(int currentTemp){
        // Temp num alloc has to be continuous
        if(this.getPigStatus() == 1){
            return currentTemp;
        }
        MClass pClass;
        if(getParent()!=null){
            pClass = getParent();
            currentTemp = pClass.allocTemp(currentTemp);
            // parent first
        }

        // for Method
        offset = 0;
        for(MMethod tMethod :this.methodList){
            if(tMethod.getOffset() == -1){
                // alloc offset
                while(!pdMehtodOffset(offset)){
                    offset = offset+4;
                }
                tMethod.setOffset(offset);
                currentTemp = tMethod.allocTemp(currentTemp);
            }
        }
        setPigStatus(1);
        // all done
        return currentTemp;
    }

}