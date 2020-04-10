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

    // ---for piglet---

    //null represent it hasn't been initialized
    protected String pigName = null;
    public void setPigName(String pigName){
        this.pigName = pigName;
    }
    public String getPigName(){
        return this.pigName;
    }


    // preprocessing 

    protected int pigStatus = -1;
    // -1 : not initalized
    // 0 : already completed classComplete()
    // 1 : done allocTemp()
    public void setPigStatus(int pigSta){
        this.pigStatus = pigSta;
    }
    public int getPigStatus(){
        return this.pigStatus;
    }

    protected int offset = -1; //the offset in class
    public void setOffset(int off) {
        this.offset = off;
    }
    public int getOffset() {
        return this.offset;
    }

    protected int Temp = 0;
    public void setTempNum(int tempNum) {
        this.Temp = tempNum;
    }

    public int getTempNum() {
        return this.Temp;
    }

    public boolean pdTemp(){
        return this.Temp > 0; // -1 represent not initialized
    }

}