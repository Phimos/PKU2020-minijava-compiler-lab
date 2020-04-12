package symbol;
import java.util.ArrayList;

// import sun.net.www.content.text.plain;
import typecheck.*;

public class MPiglet{
    protected StringBuilder code;
    // code to print
    // convenient to get information in the class
    
    // tackle code
    public MPiglet(String a){
        code = new StringBuilder(a);
    }
    
    public void addStr(String a){
        code.append(a);
    }

    public void addCode(MPiglet t, int space){
        // space -1 = '' 0 = '\n' 1 = ' '
        if(t == null)return;
        else{
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

    // Var to get Temp
    protected MVar repreVar;

    public MVar getVar(){
        return repreVar;
    }

    public void setVar(MVar tVar){
        this.repreVar = tVar;
    }

    // classbelong when program running
    protected MClass classtype;

    public void setPigClass(MClass t){
        this.classtype = t;
    }

    public MClass getPigClass(){
        return classtype;
    }

}