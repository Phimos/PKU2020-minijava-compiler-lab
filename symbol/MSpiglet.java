package symbol;
import java.util.ArrayList;

// import sun.security.ec.point.Point;
// import sun.net.www.content.text.plain;
import typecheck.*;

public class MSpiglet{
    protected StringBuilder code;
    // code to print
    // convenient to get information in the class
    
    // tackle code
    public MSpiglet(String a){
        code = new StringBuilder(a);
    }
    
    public void addStr(String a){
        code.append(a);
    }

    public void addCode(MSpiglet t, int space){
        // space -1 = '' 0 = '\n' 1 = ' '
        
        if(t == null)return;
        else{
            if(t.getCode().toString().length() == 0)
                return;

            if(space == 0)
                code.append("\n");
            else if(space == 1)
                code.append(" ");

            code.append(t.getCode());
        }
    }

    public StringBuilder getCode(){
        return code;
    }
    // exp -> temp
    protected String tempPre = null;
    public void setTempPre(String temp){
        this.tempPre = temp;
    }

    public String getTempPre(){
        return tempPre;
    }

    // if simpleExp or not
    protected String simpleExp = null;

    public void setSimpleExp(String simpleExp) {
        this.simpleExp = simpleExp;
    }
    public String getSimpleExp() {
        return simpleExp;
    }
    public boolean isSimpleExp() {
        return simpleExp != null;
    }

    // is Exp or not, Exp represent exp for Spiglet
    protected String exp = null;

    public String getExp() {
        return exp;
    }
    public boolean isExp() {
        return exp != null;
    }
    public void setExp(String exp) {
        this.exp = exp;
    }

    // expAll + exp = exp for piglet
    protected String expAll = null;

    public String getExpAll(){
        return expAll;
    }
    public void setExpAll(String exp){
        this.expAll = exp;
    }

    // templist for Call parameters list
    protected ArrayList<String> tempList = new ArrayList<String>();
    public ArrayList<String> getTempList() {
        return tempList;
    }

    public void addTemp(String temp) {
        tempList.add(temp);
    }

    // for operation
    protected int op;

    public void setOp(int opType) {
        this.op = opType;
    }

    public String getOp() {
        if(op == 0) return "LT";
        else if(op == 1) return "PLUS";
        else if(op == 2) return "MINUS";
        else return "TIMES";
    }
}