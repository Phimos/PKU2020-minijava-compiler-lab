package symbol;

import java.util.ArrayList;
import java.util.HashMap;
import typecheck.*;

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
            return false;
        }
    }

    public boolean existVar(String varName){
        return varList.get(varName)!=null;
    }

    public MVar getVar(String varName){
        return varList.get(varName);
    }

    public boolean addVar(MVar var){
        if(!existVar(var.getName())){
            varList.put(var.getName(), var);
            return true;
        }
        else{
            System.out.println("repeated var in method");
            return false;
        }
    }

    public String getReturn(){
        return this.returnType;
    }

    public MClass getClassBelong() {
        return this.classBelong;
    }

    public boolean typeCheckMode(){
        return this.paramCheckMode;
    }

    public void turnonCheckMode(){
        this.paramCheckMode = true;
        this.paramCount = 0;
    }

    public void turnoffCheckMode(int _row, int _col){
        this.paramCheckMode = false;
        if(paramList.size()!=paramCount){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: input params is not enough\n\tlocation: row %d, col %d", _row, _col));
        }
    }

    public void paramTypeCheck(MType inputType){
        if(paramList.size()<=paramCount){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: too many input params\n\tlocation: row %d, col %d", inputType.getRow(), inputType.getCol()));
        }
        if(!paramList.get(paramCount).getType().equals(inputType.getName())){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the type of input param mismatch\n\tlocation: row %d, col %d", inputType.getRow(), inputType.getCol()));
        }
        this.paramCount++;
    }
}