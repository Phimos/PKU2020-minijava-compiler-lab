package visitor;
import symbol.MSpgProcedure;
import symbol.MSpgStmt;
import symbol.*;
import syntaxtree.*;

public class BuildSpiglitTable extends GJDepthFirst<Object, MSpgProgram>{
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
    MSpgProcedure mainProcedure = new MSpgProcedure();
    argu.addProcedure("MAIN", mainProcedure);
    argu.currentProcedure = mainProcedure;
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
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
    MSpgProcedure procedure = new MSpgProcedure();
    String name = (String)n.f0.accept(this, argu);
    Integer cnt = (Integer)n.f2.accept(this, argu);
    argu.addProcedure(name, procedure);
    procedure.setParamsCnt(cnt);
    argu.currentProcedure = procedure;
    n.f4.accept(this, argu);
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
    argu.inStmt = true;
    n.f0.accept(this, argu);
    argu.inStmt = false;
    return _ret;
 }

 /**
  * f0 -> "NOOP"
  */
 public Object visit(NoOpStmt n, MSpgProgram argu) {
    Object _ret=null;
    argu.currentProcedure.addStmt(new MSpgStmt(StmtType.NOOP));
    return _ret;
 }

 /**
  * f0 -> "ERROR"
  */
 public Object visit(ErrorStmt n, MSpgProgram argu) {
    Object _ret=null;
    argu.currentProcedure.addStmt(new MSpgStmt(StmtType.ERROR));
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
    MSpgStmt stmt = new MSpgStmt(StmtType.CJUMP);
    Integer temp = (Integer)n.f1.accept(this, argu);
    String label = (String)n.f2.accept(this, argu);
    stmt.jumpLabel = label;
    stmt.addVar(temp);
    argu.currentProcedure.addStmt(stmt);
    return _ret;
 }

 /**
  * f0 -> "JUMP"
  * f1 -> Label()
  */
 public Object visit(JumpStmt n, MSpgProgram argu) {
    Object _ret=null;
    MSpgStmt stmt = new MSpgStmt(StmtType.JUMP);
    n.f0.accept(this, argu);
    String label = (String)n.f1.accept(this, argu);
    stmt.jumpLabel = label;
    argu.currentProcedure.addStmt(stmt);
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
    MSpgStmt stmt = new MSpgStmt(StmtType.HSTORE);
    n.f0.accept(this, argu);
    Integer temp1 = (Integer)n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    Integer temp2 = (Integer)n.f3.accept(this, argu);
    stmt.addVar(temp1);
    stmt.addVar(temp2);
    argu.currentProcedure.addStmt(stmt);
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
    MSpgStmt stmt = new MSpgStmt(StmtType.HLOAD);
    n.f0.accept(this, argu);
    Integer temp1 = (Integer)n.f1.accept(this, argu);
    Integer temp2 = (Integer)n.f2.accept(this, argu);
    stmt.addID(temp1);
    stmt.addVar(temp2);
    n.f3.accept(this, argu);
    argu.currentProcedure.addStmt(stmt);
    return _ret;
 }

 /**
  * f0 -> "MOVE"
  * f1 -> Temp()
  * f2 -> Exp()
  */
 public Object visit(MoveStmt n, MSpgProgram argu) {
    Object _ret=null;
    MSpgStmt stmt = new MSpgStmt(StmtType.MOVE);
    n.f0.accept(this, argu);
    Integer temp = (Integer)n.f1.accept(this, argu);
    stmt.addID(temp);
    argu.currentProcedure.addStmt(stmt);
    argu.currentStmt = stmt;
    n.f2.accept(this, argu);
    argu.currentStmt = null;
    return _ret;
 }

 /**
  * f0 -> "PRINT"
  * f1 -> SimpleExp()
  */
 public Object visit(PrintStmt n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    MSpgStmt stmt = new MSpgStmt(StmtType.PRINT);
    argu.currentProcedure.addStmt(stmt);
    argu.currentStmt = stmt;
    n.f1.accept(this, argu);
    argu.currentStmt = null;
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
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    MSpgStmt stmt = new MSpgStmt(StmtType.RETURN);
    argu.currentProcedure.addStmt(stmt);
    argu.currentStmt = stmt;
    n.f3.accept(this, argu);
    argu.currentStmt = null;
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
    argu.currentStmt.incall();
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> "HALLOCATE"
  * f1 -> SimpleExp()
  */
 public Object visit(HAllocate n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
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
    Integer temp = (Integer)n.f1.accept(this, argu);
    argu.currentStmt.addVar(temp);
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
    return _ret;
 }

 /**
  * f0 -> Temp()
  *       | IntegerLiteral()
  *       | Label()
  */
 public Object visit(SimpleExp n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    return _ret;
 }

 /**
  * f0 -> "TEMP"
  * f1 -> IntegerLiteral()
  */
 public Object visit(Temp n, MSpgProgram argu) {
    Object _ret=null;
    n.f0.accept(this, argu);
    _ret = n.f1.accept(this, argu);
    if(argu.currentStmt != null){
      argu.currentStmt.addVar((Integer)_ret);
   }
    return _ret;
 }

 /**
  * f0 -> <INTEGER_LITERAL>
  */
 public Object visit(IntegerLiteral n, MSpgProgram argu) {
    Object _ret= Integer.valueOf(n.f0.tokenImage);
    return _ret;
 }

 /**
  * f0 -> <IDENTIFIER>
  */
 public Object visit(Label n, MSpgProgram argu) {
    Object _ret= n.f0.tokenImage;
    if(!argu.inStmt){
      argu.currentProcedure.tempLabel = (String)_ret;
   }
   if(argu.currentProcedure != null){
      argu.currentProcedure.addCall((String)_ret);
   }
    return _ret;
 }
}