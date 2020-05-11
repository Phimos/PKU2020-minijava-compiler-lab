package visitor;

import symbol.MSpgProgram;
import symbol.*;
import syntaxtree.*;

public class SpiglitToKanga extends GJDepthFirst<Object, MSpgProgram>{
 //
 // User-generated visitor methods below
 //

 /**
  * f0 -> "MAIN"
  * f1 -> StmtList()
  * f2 -> "END"
  * f3 -> ( Procedure() )*
  * f4 -> <EOF>
  */
 public Object visit(Goal n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.code += "MAIN [0][0]["+argu.getProcedure("MAIN").maxParamsCnt+"]\n";
    argu.joinProcedure("MAIN");
    n.f1.accept(this, argu);
    argu.outProcedure();
    n.f2.accept(this, argu);
    argu.code += "END\n";
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> ( ( Label() )? Stmt() )*
  */
 public Object visit(StmtList n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> Label()
  * f1 -> "["
  * f2 -> IntegerLiteral()
  * f3 -> "]"
  * f4 -> StmtExp()
  */
 public Object visit(Procedure n, MSpgProgram argu) {
    Object _ret=null;
    String name = (String)n.f0.accept(this, argu);
    argu.currentProcedure = argu.getProcedure(name);
    argu.append(name + " ["+argu.currentProcedure.paramsCnt+"][SPILLED_CNT]["+argu.currentProcedure.maxParamsCnt+"]");
    argu.append("BEGIN_CODE");
    n.f4.accept(this, argu);

    String BEGIN_CODE = "";
    String END_CODE = "";

    for(int i=0;i<argu.currentProcedure.sCnt;++i){
       int offset;
       offset = argu.currentProcedure.spilledCnt++;
       BEGIN_CODE += "\tASTORE SPILLEDARG "+offset+" s" + i +"\n";
       END_CODE += "\tALOAD s"+ i +" SPILLEDARG " + offset + "\n";
    }


    for(int i=0;i<Math.min(4, argu.currentProcedure.paramsCnt);++i){
       BEGIN_CODE += "\tMOVE s" + i + " a" + i + "\n";
    }


    argu.code = argu.code.replace("BEGIN_CODE", BEGIN_CODE);
    argu.code = argu.code.replace("SPILLED_CNT", argu.currentProcedure.spilledCnt.toString());
    argu.outProcedure();
    argu.append("\n"+END_CODE);
    argu.append("END");
    return _ret;
 }

 /**
  * f0 -> NoOpStmt()
  *       | ErrorStmt()
  *       | CJumpStmt()
  *       | JumpStmt()
  *       | HStoreStmt()
  *       | HLoadStmt()
  *       | MoveStmt()
  *       | PrintStmt()
  */
 public Object visit(Stmt n, MSpgProgram argu) {
    Object _ret=null;
    argu.joinStmt();
    //argu.allocForStmt();
    n.f0.accept(this, argu);
    argu.outStmt();
    return _ret;
 }

 /**
  * f0 -> "NOOP"
  */
 public Object visit(NoOpStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.append("NOOP");
    return _ret;
 }

 /**
  * f0 -> "ERROR"
  */
 public Object visit(ErrorStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.append("ERROR");
    return _ret;
 }

 /**
  * f0 -> "CJUMP"
  * f1 -> Temp()
  * f2 -> Label()
  */
 public Object visit(CJumpStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.append("CJUMP");
    String temp = (String)n.f1.accept(this, argu);
    String label = (String)n.f2.accept(this, argu);
    argu.append(temp);
    argu.append(label);
    return _ret;
 }

 /**
  * f0 -> "JUMP"
  * f1 -> Label()
  */
 public Object visit(JumpStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.append("JUMP");
    String label = (String)n.f1.accept(this, argu);
    argu.append(label);
    return _ret;
 }

 /**
  * f0 -> "HSTORE"
  * f1 -> Temp()
  * f2 -> IntegerLiteral()
  * f3 -> Temp()
  */
 public Object visit(HStoreStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    String temp1 = (String)n.f1.accept(this, argu);
    String val = (String)n.f2.accept(this, argu);
    String temp2 = (String)n.f3.accept(this, argu);
    if(temp1.contains("SPILLEDARG")){
      argu.appendStmt("ALOAD v0 "+temp1 + "\n");
      temp1 = "v0";
   }
   if(temp2.contains("SPILLEDARG")){
      argu.appendStmt("ALOAD v0 "+temp2 + "\n");
      temp2 = "v1";
   }
    argu.append("HSTORE");
    argu.append(temp1);
    argu.append(val);
    argu.append(temp2);
    return _ret;
 }

 /**
  * f0 -> "HLOAD"
  * f1 -> Temp()
  * f2 -> Temp()
  * f3 -> IntegerLiteral()
  */
 public Object visit(HLoadStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    String temp1 = (String)n.f1.accept(this, argu);
    String temp2 = (String)n.f2.accept(this, argu);
    String val = (String)n.f3.accept(this, argu);
    String store = null;
    if(temp1.contains("SPILLEDARG")){
       argu.appendStmt("ALOAD v0 "+temp1 + "\n");
       store = temp1;
       temp1 = "v0";
    }
    if(temp2.contains("SPILLEDARG")){
       argu.appendStmt("ALOAD v0 "+temp2 + "\n");
       temp2 = "v1";
    }
    argu.appendStmt("HLOAD " + temp1 + " " + temp2 + " "+val);
    if(store!=null){
       argu.appendStmt("ASTORE "+store+" v0");
    }
    return _ret;
 }

 /**
  * f0 -> "MOVE"
  * f1 -> Temp()
  * f2 -> Exp()
  */
 public Object visit(MoveStmt n, MSpgProgram argu) {
    Object _ret=null;
    if(n.f2.f0.choice.getClass() == Call.class){
      String temp = (String)n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      if(temp.contains("SPILLEDARG")){
         argu.appendStmt("ALOAD v1 "+temp);
         argu.appendStmt("MOVE v1 v0");
         argu.appendStmt("ASTORE "+temp+" v1");
      }
      else{
         argu.appendStmt("MOVE " + temp + " v0");
      }
    }
    else{
      String temp = (String)n.f1.accept(this, argu);
      String exp = (String)n.f2.accept(this, argu);
      if(exp.contains("v0")){
         argu.appendStmt("MOVE v1 "+exp);
         exp = "v1";
         System.out.println("NOT TEST YET");
      }
      if(temp.contains("SPILLEDARG")){
         argu.appendStmt("MOVE v0 "+exp);
         argu.appendStmt("ASTORE "+temp+" v0");
      }
      else{
         argu.appendStmt("MOVE "+temp + " "+exp);
      }
    }
    return _ret;
 }

 /**
  * f0 -> "PRINT"
  * f1 -> SimpleExp()
  */
 public Object visit(PrintStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    String temp = (String)n.f1.accept(this, argu);
    argu.append("PRINT");
    argu.append(temp);
    return _ret;
 }

 /**
  * f0 -> Call()
  *       | HAllocate()
  *       | BinOp()
  *       | SimpleExp()
  */
 public Object visit(Exp n, MSpgProgram argu) {
    String _ret=null;
    if(n.f0.choice.getClass() == Call.class){
       n.f0.accept(this, argu);
       return null;
    }
    else{
       _ret = (String)n.f0.accept(this, argu);
       return _ret;
    }
 }

 /**
  * f0 -> "BEGIN"
  * f1 -> StmtList()
  * f2 -> "RETURN"
  * f3 -> SimpleExp()
  * f4 -> "END"
  */
 public Object visit(StmtExp n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.inProcedure = true;
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    String temp = (String)n.f3.accept(this, argu);
    if(temp.contains("SPILLEDARG")){
       argu.appendStmt("ALOAD v1 "+temp+"\n");
       temp = "v1";
    }
    argu.append("\tMOVE v0 "+temp);
    n.f4.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> "CALL"
  * f1 -> SimpleExp()
  * f2 -> "("
  * f3 -> ( Temp() )*
  * f4 -> ")"
  */
 public Object visit(Call n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.passParams = 0;
    n.f3.accept(this, argu);
    argu.passParams = -1;
    String entry = (String)n.f1.accept(this, argu);
    argu.appendStmt("CALL "+entry+"\n");
    //System.out.println("?????");
    return _ret;
 }

 /**
  * f0 -> "HALLOCATE"
  * f1 -> SimpleExp()
  */
 public Object visit(HAllocate n, MSpgProgram argu) {
    String _ret=null;
    n.f0.accept(this, argu);
    String simple = (String)n.f1.accept(this, argu);
    _ret = "HALLOCATE " + simple;
    return _ret;
 }

 /**
  * f0 -> Operator()
  * f1 -> Temp()
  * f2 -> SimpleExp()
  */
 public Object visit(BinOp n, MSpgProgram argu) {
    Object _ret=null;
    String op = (String)n.f0.accept(this, argu);
    String temp = (String)n.f1.accept(this, argu);
    String simple = (String)n.f2.accept(this, argu);
    if(temp.contains("SPILLEDARG")){
       argu.appendStmt("ALOAD v0 " + temp + "\n");
       temp = "v0";
    }
    
    _ret = op + " " + temp + " " + simple;
    return _ret;
 }

 /**
  * f0 -> "LT"
  *       | "PLUS"
  *       | "MINUS"
  *       | "TIMES"
  */
 public Object visit(Operator n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    String op = ((NodeToken)n.f0.choice).tokenImage;
    return op;
 }

 /**
  * f0 -> Temp()
  *       | IntegerLiteral()
  *       | Label()
  */
 public Object visit(SimpleExp n, MSpgProgram argu) {
    String simple = (String)n.f0.accept(this, argu);
    if(simple.contains("SPILLEDARG")){
       argu.appendStmt("ALOAD v1 "+simple+"\n\t");
       simple = "v1";
    }
    return simple;
 }

 /**
  * f0 -> "TEMP"
  * f1 -> IntegerLiteral()
  */
 public Object visit(Temp n, MSpgProgram argu) {
    Object _ret=null;
    Integer temp = Integer.valueOf((String)n.f1.accept(this, argu));
    String reg = (String)argu.getReg(temp);
    if(argu.passParams >= 0){
      if(reg.contains("SPILLEDARG")){
         argu.appendStmt("ALOAD v1 "+reg+"\n");
         reg = "v1";
      }
       if(argu.passParams < 4){
          argu.inStmt = true;
         argu.append("\tMOVE");
         argu.append("a" + argu.passParams);
         argu.append((String)reg);
         argu.inStmt = false;
         argu.code +='\n';
         ++argu.passParams;
       }
       else{
         argu.inStmt = true;
         argu.append("\tPASSARG");
         argu.append(""+(argu.passParams-3));
         argu.append((String)reg);
         argu.inStmt = false;
         argu.code +='\n';
         ++argu.passParams;
       }
    }
    return reg;
 }

 /**
  * f0 -> <INTEGER_LITERAL>
  */
 public Object visit(IntegerLiteral n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    _ret = n.f0.tokenImage;
    return _ret;
 }

 /**
  * f0 -> <IDENTIFIER>
  */
 public Object visit(Label n, MSpgProgram argu) {
    Object _ret= n.f0.tokenImage;
    if(argu.inProcedure && !argu.inStmt){
        argu.append((String)_ret);
    }
    return _ret;
 }    
}