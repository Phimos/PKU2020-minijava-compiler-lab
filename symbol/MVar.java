package symbol;

public class MVar extends MIdentifier{
    protected String varType;
    protected MMethod methodBelong;
    protected MClass classBelong;

    public MVar(){}

    public MVar(String _name, String _type, MMethod _method, MClass _class, int _row, int _col){
        super(_name, "var", _row, _col);
        this.varType = _type;
        this.methodBelong = _method;
        this.classBelong = _class;
    }

    public String getType(){
        return this.varType;
    }

    public boolean checkType(String type){
        return this.varType == type;
    }

    public MMethod getMethodBelong(){
        return this.methodBelong;
    }

    public MClass getClassBelong(){
        return this.classBelong;
    }
}