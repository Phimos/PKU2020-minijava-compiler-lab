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


    // for Var and Method
    protected int offset = -1; //the offset in class
    public void setOffset(int off) {
        this.offset = off;
    }
    public int getOffset() {
        return this.offset;
    }

    protected int Temp = 0;
    // 0 represent class Var
    protected int Temp19_0ffset = -1;
    // for number of parameters
    public void setTempNum19(int offset_19){
        this.Temp19_0ffset = offset_19;
    }

    public int getTempNum19(){
        return Temp19_0ffset;
    }

    public void setTempNum(int tempNum) {
        this.Temp = tempNum;
    }

    public int getTempNum() {
        return this.Temp;
    }

    public boolean pdTemp(){
        return this.Temp > 0; // 0 represent not initialized
    }

    protected String paramTemp19 = null;

    public String getTemp19(){
        return paramTemp19;
    }

    public void setTemp19(String temp19){
        this.paramTemp19 = temp19;
    }

    protected int totalParam = -1;

    public int getTotalParam(){
        return totalParam;
    }

    public void setTotalParam(int total){
        this.totalParam = total;
    }

    // for son to override
    public void printAll(){}
    public int allocTemp(int currentTmep){return -1;}
    public void classComplete(){}
    public void setParamNow(int count){}
    public int getParamNow(){return -1;}
    public void setInitValue(MPiglet value){}
    public MPiglet getInitValue(){return null;}
    public int setParamInit(MPiglet value){return -1;}
}