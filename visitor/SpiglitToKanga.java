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
    argu.code += "MAIN\n";
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
    argu.append(name + " ["+argu.currentProcedure.paramsCnt+"][SPILLED_CNT]["+20+"]");
    argu.append("BEGIN_CODE");
    n.f4.accept(this, argu);

    String BEGIN_CODE = "";
    String END_CODE = "";

    for(int i=0;i<Math.min(4, argu.currentProcedure.paramsCnt);++i){
       BEGIN_CODE += "\tASTORE SPILLEDARG "+argu.currentProcedure.spilledCnt+" s" + i +"\n";
       END_CODE += "\tALOAD s"+i +" SPILLEDARG " + argu.currentProcedure.spilledCnt + "\n";
       argu.currentProcedure.spilledCnt++;
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
    argu.append("HSTORE");
    String temp1 = (String)n.f1.accept(this, argu);
    argu.append(temp1);
    String val = (String)n.f2.accept(this, argu);
    argu.append(val);
    String temp2 = (String)n.f3.accept(this, argu);
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
    argu.append("HLOAD");
    String temp1 = (String)n.f1.accept(this, argu);
    String temp2 = (String)n.f2.accept(this, argu);
    String val = (String)n.f3.accept(this, argu);
    argu.append(temp1);
    argu.append(temp2);
    argu.append(val);
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
      argu.appendStmt("MOVE " + temp + " v0");
    }
    else{
      argu.append("MOVE");
      String temp = (String)n.f1.accept(this, argu);
      argu.append(temp);
      n.f2.accept(this, argu);
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
    argu.append("PRINT");
    n.f1.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> Call()
  *       | HAllocate()
  *       | BinOp()
  *       | SimpleExp()
  */
 public Object visit(Exp n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    return _ret;
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
    argu.append("\tMOVE v0 ");
    n.f3.accept(this, argu);
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
    argu.appendStmt("CALL ");
    n.f1.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> "HALLOCATE"
  * f1 -> SimpleExp()
  */
 public Object visit(HAllocate n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    argu.append("HALLOCATE");
    n.f1.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> Operator()
  * f1 -> Temp()
  * f2 -> SimpleExp()
  */
 public Object visit(BinOp n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    String temp = (String)n.f1.accept(this, argu);
    argu.append(temp);
    n.f2.accept(this, argu);
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
    argu.append(op);
    return _ret;
 }

 /**
  * f0 -> Temp()
  *       | IntegerLiteral()
  *       | Label()
  */
 public Object visit(SimpleExp n, MSpgProgram argu) {
    Object _ret=null;
    String simple = (String)n.f0.accept(this, argu);
    argu.append(simple);
    return _ret;
 }

 /**
  * f0 -> "TEMP"
  * f1 -> IntegerLiteral()
  */
 public Object visit(Temp n, MSpgProgram argu) {
    Object _ret=null;
    Integer temp = Integer.valueOf((String)n.f1.accept(this, argu));
    _ret = argu.getReg(temp);
    if(argu.passParams >= 0){
       if(argu.passParams < 4){
         argu.appendStmt("MOVE " + "a" + argu.passParams + " " + (String)_ret);
         ++argu.passParams;
       }
       else{
         ;
       }
    }
    return _ret;
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