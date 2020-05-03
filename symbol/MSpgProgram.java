package symbol;

import java.util.*;

public class MSpgProgram {
    public HashMap<String, MSpgProcedure> procedures = new HashMap<>();
    public MSpgProcedure currentProcedure = null;
    public MSpgStmt currentStmt = null;
    public boolean inStmt = false;
    public boolean inProcedure = false;
    public String code = "";
    public int passParams = -1;

    public void addProcedure(String name,MSpgProcedure procedure){
        procedures.put(name, procedure);
    }

    public MSpgProcedure getProcedure(String name){
        return procedures.get(name);
    }

    public void analyzeAll(){
        for(MSpgProcedure proc: procedures.values()){
            System.out.println(proc.callMethodName.size());
            for(String callproc: proc.callMethodName){
                if(procedures.containsKey(callproc)){
                    proc.maxParamsCnt = Math.max(proc.maxParamsCnt, procedures.get(callproc).paramsCnt);
                }
            }
        }

        for(MSpgProcedure proc: procedures.values()){
            proc.analyze();
        }
        for(MSpgProcedure proc: procedures.values()){
            //System.out.println("?????");
            proc.buildIntervals();
        }
    }

    public void joinProcedure(String name){
        inProcedure = true;
        currentProcedure = getProcedure(name);
    }

    public void outProcedure(){
        inProcedure = false;
        currentProcedure = null;
    }

    public void joinStmt(){
        inStmt = true;
        currentProcedure.stmtCnt += 1;
        code += "\t";
    }

    public void outStmt(){
        inStmt = false;
        code += "\n";
    }

    public void append(String c){
        if(!inProcedure){
            code += c+"\n";
        }
        else if(!inStmt){
            code += c+"\t";
        }
        else{
            code += c+" ";
        }
    }

    public void appendStmt(String s){
        code += "\n\t" + s;
    }

    public String getReg(int temp){
        return currentProcedure.getReg(temp);
    }
}