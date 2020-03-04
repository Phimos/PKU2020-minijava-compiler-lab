package symbol;

public class MType{
    protected String name;
    protected int row;
    protected int col;

    public MType(){}

    public MType(String _name, int _row, int _col){
        this.name = _name;
        this.row = _row;
        this.col = _col;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String _name){
        this.name = _name;
    }
}