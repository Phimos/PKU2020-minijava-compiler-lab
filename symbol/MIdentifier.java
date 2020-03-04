package symbol;

public class MIdentifier extends MType{
    // type can only be "class", "method" and "var"
    protected String idType;

    public MIdentifier(){}

    public MIdentifier(String _name, String _idtype, int _row, int _col){
        super(_name, _row, _col);
        this.idType = _idtype;
    }

    public boolean isMethod(){
        return this.idType.equals("method");
    }

    public boolean isClass(){
        return this.idType.equals("class");
    }

    public boolean isVar(){
        return this.idType.equals("var");
    }
}