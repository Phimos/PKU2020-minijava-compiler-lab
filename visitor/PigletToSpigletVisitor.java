//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import symbol.*;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class PigletToSpigletVisitor extends GJNoArguDepthFirst<MSpiglet>{
   protected int tempNow;

   public PigletToSpigletVisitor(int tempfrom){
      this.tempNow = tempfrom;
      //System.out.println(tempfrom);
   }
   
   public String getAvaTemp(){
      String s = "TEMP "+tempNow;
      tempNow += 1;
      return s;
   }
   
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   
   // dont call
     //
   // Auto class visitors--probably don't need to be overridden.
   //
   public MSpiglet visit(NodeList n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
      MSpiglet t1 = e.nextElement().accept(this);
      if(t1!=null){
         _ret.addCode(t1, 0);
      }
      _count++;
      }
      return _ret;
   }

   public MSpiglet visit(NodeListOptional n) {
      if ( n.present() ) {
         MSpiglet _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            MSpiglet t1 = e.nextElement().accept(this);
            if(_ret == null)
               _ret = t1;
            else
               _ret.addCode(t1,0);
            _count++;
            if(t1!=null&&t1.getTempPre()!=null)
               _ret.addTemp(t1.getTempPre());
         }
         return _ret;
      }
      else
         return null;
   }

   public MSpiglet visit(NodeOptional n) {
      if ( n.present() )
      {
         if (n.node instanceof Label) {
            return new MSpiglet(((Label) n.node).f0.tokenImage);
            // Lnum need to be added here
        }
         return n.node.accept(this);
      }
      else
         return null;
   }

   public MSpiglet visit(NodeSequence n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         MSpiglet t1 = e.nextElement().accept(this);
         if(t1!=null)
            _ret.addCode(t1, 0);
         _count++;
      }
      return _ret;
   }

   public MSpiglet visit(NodeToken n) { return null;}

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
   public MSpiglet visit(Goal n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("MAIN");
      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      _ret.addCode(t1,0);

      n.f2.accept(this);
      _ret.addCode(new MSpiglet("END"),0);
      MSpiglet t2 = n.f3.accept(this);
      _ret.addCode(t2,0);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public MSpiglet visit(StmtList n) {
      MSpiglet _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }
 
   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public MSpiglet visit(Procedure n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      String funcName;
      String paramNum;

      n.f0.accept(this);
      funcName = n.f0.f0.tokenImage;
      n.f1.accept(this);
      n.f2.accept(this);
      paramNum = n.f2.f0.tokenImage;
      MSpiglet funcExp = new MSpiglet(funcName +" [ "+paramNum+" ]");
      _ret.addCode(funcExp,0);
      _ret.addStr("\nBEGIN");
      

      n.f3.accept(this);
      MSpiglet stexp = n.f4.accept(this);

      if(!stexp.isSimpleExp()){
         // not Simple, need to hload to a temp
         _ret.addCode(stexp, 0);
         _ret.addStr("\nRETURN "+stexp.getTempPre());
      }
      else{
         _ret.addStr("\nRETURN "+stexp.getSimpleExp());
      }
      _ret.addStr("\nEND");
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
   public MSpiglet visit(Stmt n) {
      MSpiglet _ret=null;

      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public MSpiglet visit(NoOpStmt n) {
      MSpiglet _ret=null;
      n.f0.accept(this);
      _ret = new MSpiglet("NOOP\n");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public MSpiglet visit(ErrorStmt n) {
      MSpiglet _ret=null;
      n.f0.accept(this);
      _ret = new MSpiglet("ERROR");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Exp()
    * f2 -> Label()
    */
   public MSpiglet visit(CJumpStmt n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");

      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      n.f2.accept(this);

      _ret.addCode(t1, 0);

      _ret.addStr("\nCJUMP");
      _ret.addCode(new MSpiglet(t1.getTempPre()+" "+n.f2.f0.toString()), 1);
      return _ret;

   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public MSpiglet visit(JumpStmt n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("JUMP "+n.f1.f0.toString());

      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Exp()
    * f2 -> IntegerLiteral()
    * f3 -> Exp()
    */
   public MSpiglet visit(HStoreStmt n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");

      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      n.f2.accept(this);
      MSpiglet t2 = n.f3.accept(this);
      
      _ret.addCode(t1, -1);
      _ret.addCode(t2, 0);
      
      if(_ret.getCode().toString().length()==0)
      _ret.addStr(" HSTORE "+t1.getTempPre()+" "+n.f2.f0.toString()+" "+t2.getTempPre());
      else
      _ret.addStr("\nHSTORE "+t1.getTempPre()+" "+n.f2.f0.toString()+" "+t2.getTempPre());

      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Exp()
    * f3 -> IntegerLiteral()
    */
   public MSpiglet visit(HLoadStmt n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");

      n.f0.accept(this);
      MSpiglet t2 = n.f1.accept(this);
      MSpiglet t1 = n.f2.accept(this);
      n.f3.accept(this);
      _ret.addCode(t1, -1);

      if(_ret.getCode().toString().length()==0)
      _ret.addStr(" HLOAD "+t2.getTempPre()+" "+t1.getTempPre()+" "+n.f3.f0.toString());
      else
      _ret.addStr("\nHLOAD "+t2.getTempPre()+" "+t1.getTempPre()+" "+n.f3.f0.toString());
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public MSpiglet visit(MoveStmt n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      MSpiglet t2 = n.f2.accept(this);

      if(!t2.isExp()){
         // not a Exp
         _ret.addCode(t2, -1);
         if(_ret.getCode().toString().length()==0)
         _ret.addStr(" MOVE "+t1.getTempPre()+" "+t2.getTempPre());
         else
         _ret.addStr("\nMOVE "+t1.getTempPre()+" "+t2.getTempPre());
      }
      else{
         _ret.addCode(new MSpiglet(t2.getExpAll()), -1);
         if(_ret.getCode().toString().length()==0)
         _ret.addStr(" MOVE "+t1.getTempPre()+" "+t2.getExp());
         else
         _ret.addStr("\nMOVE "+t1.getTempPre()+" "+t2.getExp());
         // Exp for spiglet
      }
      
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> Exp()
    */
   public MSpiglet visit(PrintStmt n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");

      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);

      if(t1.isSimpleExp()){
         _ret.addStr("PRINT "+t1.getSimpleExp());
      }
      else{
         _ret.addCode(t1, -1);
         _ret.addStr("\nPRINT "+t1.getTempPre());
      }
      return _ret;
   }

   /**
    * f0 -> StmtExp()
    *       | Call()
    *       | HAllocate()
    *       | BinOp()
    *       | Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public MSpiglet visit(Exp n) {
      MSpiglet _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> Exp()
    * f4 -> "END"
    */
   public MSpiglet visit(StmtExp n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");

      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      _ret.addCode(t1, -1);

      n.f2.accept(this);
      MSpiglet t2 = n.f3.accept(this);
      _ret.addCode(t2, 0);
      _ret.setTempPre(t2.getTempPre());
      // _ret.temp = return.temp

      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> Exp()
    * f2 -> "("
    * f3 -> ( Exp() )*
    * f4 -> ")"
    */
   public MSpiglet visit(Call n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      String s1 = getAvaTemp();
      String expAll;
      String code;

      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      n.f2.accept(this);
      MSpiglet t2 = n.f3.accept(this);
      n.f4.accept(this);

      if(t1.isSimpleExp()){
         code = "CALL "+t1.getSimpleExp()+"( ";
      }
      else{
         _ret.addCode(t1, -1);
         code = "CALL "+t1.getTempPre()+"( ";
      }
      _ret.addCode(t2, 0);
      for(String mTemp : t2.getTempList()){
         code = code + mTemp;
         code = code + " ";
      }
      code += ")";
      //System.out.println(code);
      _ret.addCode(new MSpiglet("MOVE "+s1+" "+code), 0);
      _ret.setTempPre(s1);

      expAll = t1.getCode().toString()+"\n"+t2.getCode().toString();
      _ret.setExpAll(expAll);
      _ret.setExp(code);// CALL is a exp for spiglet

      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> Exp()
    */
   public MSpiglet visit(HAllocate n) {
      // is a exp
      MSpiglet _ret=null;
      String s1 = getAvaTemp();
      _ret = new MSpiglet("");
      n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);

      if(t1.isSimpleExp()){
         // SimpleExp
         _ret.addStr("MOVE "+s1+" HALLOCATE "+t1.getSimpleExp());
         _ret.setTempPre(s1);
         _ret.setExpAll("");// needn't to do anything
         _ret.setExp("HALLOCATE "+t1.getSimpleExp());
      }
      else{
         _ret.addCode(t1, -1);
         _ret.addStr("MOVE "+s1+" HALLOCATE "+t1.getTempPre());
         _ret.setTempPre(s1);
         // only use temp to represent
      }
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Exp()
    * f2 -> Exp()
    */
   public MSpiglet visit(BinOp n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      String s1 = getAvaTemp();
      String expAll;
      String exp;

      MSpiglet op = n.f0.accept(this);
      MSpiglet t1 = n.f1.accept(this);
      MSpiglet t2 = n.f2.accept(this);
      
      _ret.addCode(t1, -1); // op1 need to be a temp
      if(t2.isSimpleExp()){
         _ret.addCode(new MSpiglet("MOVE "+s1+" "+op.getOp()+" "+t1.getTempPre()+" "+t2.getSimpleExp()), -1);
         _ret.setTempPre(s1);
         expAll = t1.getCode().toString()+"\n";// get a new line
         exp = op.getOp()+" "+t1.getTempPre()+" "+t2.getSimpleExp();
      }
      else{
         _ret.addCode(t2, 0);
         _ret.addCode(new MSpiglet("MOVE "+s1+" "+op.getOp()+" "+t1.getTempPre()+" "+t2.getTempPre()), 0);
         _ret.setTempPre(s1);
         expAll = t1.getCode().toString()+"\n"+t2.getCode().toString();
         exp = op.getOp()+" "+t1.getTempPre()+" "+t2.getTempPre();
      }

      _ret.setExp(exp);
      _ret.setExpAll(expAll);

      return _ret;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    */
   public MSpiglet visit(Operator n) {
      MSpiglet _ret=null;
      _ret = new MSpiglet("");
      _ret.setOp(n.f0.which);// which represent "LT"| "PLUS"| "MINUS"| "TIMES"
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public MSpiglet visit(Temp n) {
      MSpiglet _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      
      _ret = new MSpiglet("");
      String s1 = "TEMP "+n.f1.f0.toString();

      _ret.setTempPre(s1);
      _ret.setSimpleExp(s1);
      _ret.setExp(s1);
      _ret.setExpAll("");
      // System.out.println(_ret.getTempPre()+"\n");
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public MSpiglet visit(IntegerLiteral n) {
      MSpiglet _ret=null;
      String s1 = getAvaTemp();
      _ret = new MSpiglet("MOVE "+s1+" "+n.f0.toString());

      _ret.setTempPre(s1);
      _ret.setExp(n.f0.toString());
      _ret.setExpAll("");
      _ret.setSimpleExp(n.f0.toString());

      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public MSpiglet visit(Label n) {
      MSpiglet _ret=null;
      String s1 = getAvaTemp();
      _ret = new MSpiglet("MOVE "+s1+" "+n.f0.toString());
      _ret.setTempPre(s1);
      _ret.setSimpleExp(n.f0.tokenImage);
      _ret.setExpAll("");
      _ret.setExp(n.f0.tokenImage);

      n.f0.accept(this);
      return _ret;
   }

}
