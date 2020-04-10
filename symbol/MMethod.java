package symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import typecheck.*;
import visitor.TypeCheckVisitor;
import org.apache.commons.lang3.tuple.Pair;

public class MMethod extends MIdentifier{
    protected String returnType;
    protected MClass classBelong;
    protected HashMap<String, MVar> varList = new HashMap<String, MVar>();
    protected ArrayList<MVar> paramList = new ArrayList<MVar>();
    protected int paramCount;
    protected boolean paramCheckMode;

    public MMethod(){}

    public MMethod(String _name, String _return, MClass _class, int _row, int _col){
        super(_name, "method", _row, _col);
        this.returnType = _return;
        this.classBelong = _class;
    }

    public boolean existParam(String varName){
        for(MVar obj: paramList){
            if(obj.getName().equals(varName)){
                return true;
            }
        }
        return false;
    }

    public boolean addParam(MVar var){
        if(!existParam(var.getName())){
            paramList.add(var);
            varList.put(var.getName(), var);
            return true;
        }
        else{
            System.out.println("something wrong");
            ErrorPrinter.getError(0,var);
            return false;
        }
    }

    public boolean existVar(String varName){
        return varList.get(varName)!=null;
    }

    // ---piglet--- change
    public MVar getVar(String varName){
        if(existVar(varName)){
            return varList.get(varName);
        }
        else{
            return classBelong.getVar(varName);
        }
        return null;
        // don't find
    }

    public boolean addVar(MVar var){
        if(!existVar(var.getName())){
            varList.put(var.getName(), var);
            return true;
        }
        else{
            ErrorPrinter.getError(2, var, "variable");
            return false;
        }
    }

    public String getReturn(){
        return this.returnType;
    }

    public ArrayList<MVar> getParams(){
        return this.paramList;
    }

    public MClass getClassBelong() {
        return this.classBelong;
    }

    public boolean typeCheckMode(){
        return this.paramCheckMode;
    }

    public void setParamCount(int i){
        this.paramCount = i;
    }

    public int getParamCount(){
        return this.paramCount;
    }

    public void turnonCheckMode(){
        this.paramCheckMode = true;
        this.paramCount = 0;
    }

    public void turnoffCheckMode(int _row, int _col){
        this.paramCheckMode = false;
        if(paramList.size()!=paramCount){
            ErrorPrinter.getError(4, _row, _col, null);
        }
    }

    public void paramTypeCheck(MType inputType, TypeCheckVisitor tc){
        if(paramList.size()<=paramCount){
            ErrorPrinter.getError(4, inputType);
        }
        //System.out.println(paramList.get(paramCount).getName());
        tc.typeEquals(paramList.get(paramCount).getType(), inputType.getName(), paramList.get(paramCount).getRow(), paramList.get(paramCount).getCol());
        this.paramCount++;
    }

    // ---piglet---
    public String getPigDefineMethodName(){ // method define
        String t = getPigName()+" [ "+(paramCount+1)+" ] ";
        // first Temp is self, so para+1
        return t;
    }

    public int allocTemp(int currentTemp){
        // first parameters
        
        num = 1; // 0 is keep
        for(MVar tPar : paramList){
            tPar.setTempNum(num);
            num ++;
        }
        if(num>=20){
            System.out.println("Parameters outflow!\n");
        }

        Pair<String, MVar> tVar;
        Iterator<String, MVar> iter = varList.iterator();
        while(iter.hasNext()){
            tVar = iter.next();
            MVar tvar = tVar.value();
            if(tvar.getTempNum() == 0){
                tvar.setTempNum(currentTemp++);
            }
        }

        return currentTemp;
    }

}