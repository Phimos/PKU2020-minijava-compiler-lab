//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

import symbol.MClass;
import symbol.MClassList;
import symbol.MMethod;
import symbol.MPiglet;
import symbol.MType;
import symbol.MVar;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class MiniJavaToPigletVistor extends GJDepthFirst<MPiglet,MType> {
   protected MClassList myClasslist;
   protected int label;

   public String getNextTemp() {
      int num = myClasslist.getTempNum();
      String _ret = "TEMP " + num;
      myClasslist.setTempNum(num + 1);
      return _ret;
  }

  public String getNextLabel(){
      int num = this.label;
      String _ret = "L"+num;
      this.label += 1;
      return _ret;
  }

  public MiniJavaToPigletVistor(MClassList classl){
      myClasslist = classl;
      label = 0;
  }
   
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   
   // dont call
   public MPiglet visit(NodeList n, MType argu) {
      MPiglet _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         if(_ret == null){
            System.out.println("AA");
            _ret = e.nextElement().accept(this,argu); //first new
            System.out.println("aaaa"+_ret.getCode()+"aaaa"); //test
         }
         else
            _ret.addCode(e.nextElement().accept(this,argu), 0);
         _count++;
      }
      return _ret;
   }

   public MPiglet visit(NodeListOptional n, MType argu) {
      if ( n.present() ) { // 
         MPiglet _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            if(_ret == null){
               _ret = e.nextElement().accept(this,argu); //first new
               //System.out.println(_ret.getCode()); //test
            }
            else
               _ret.addCode(e.nextElement().accept(this,argu), 0);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public MPiglet visit(NodeOptional n, MType argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public MPiglet visit(NodeSequence n, MType argu) {
      MPiglet _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         if(_ret == null){
            _ret = e.nextElement().accept(this,argu); //first new
            //System.out.println(_ret.getCode()); //test
         }
         else
            _ret.addCode(e.nextElement().accept(this,argu), 0);
         _count++;
      }
      return _ret;
   }

   public MPiglet visit(NodeToken n, MType argu) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public MPiglet visit(Goal n, MType argu) {
      MPiglet _ret=null;
      // System.out.println("main,bbb\n");
      _ret = n.f0.accept(this, argu);
      // System.out.println(_ret.getCode()+"\n");
      
      _ret.addStr("\nEND");
      _ret.addCode(n.f1.accept(this, argu), 0);
      _ret.addCode(n.f2.accept(this, argu), 0);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
   public MPiglet visit(MainClass n, MType argu) {
      MPiglet _ret=new MPiglet("MAIN");
      MClassList classlist = (MClassList) argu;

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MClass mainClass = classlist.findClass(n.f1.f0.toString());
      MMethod _main = mainClass.getMethod("main");

      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      
      _ret.addCode(n.f14.accept(this, _main), 0);
      _ret.addCode(n.f15.accept(this, _main), 0);
      n.f16.accept(this, argu);
      n.f17.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public MPiglet visit(TypeDeclaration n, MType argu) {
      // functions of class need to interpret
      MPiglet _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public MPiglet visit(ClassDeclaration n, MType argu) {
      MPiglet _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MClassList classlist = (MClassList) argu;
      String thisClass = n.f1.f0.toString();
      MClass classBelong = classlist.findClass(thisClass);

      n.f2.accept(this, argu);
      n.f3.accept(this, argu);

      //method
      _ret = n.f4.accept(this, classBelong);
      n.f5.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public MPiglet visit(ClassExtendsDeclaration n, MType argu) {
      MPiglet _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MClassList classlist = (MClassList) argu;
      String thisClass = n.f1.f0.toString();
      MClass classBelong = classlist.findClass(thisClass);

      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      _ret = n.f6.accept(this, classBelong);
      n.f7.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public MPiglet visit(VarDeclaration n, MType argu) {
      MPiglet _ret=null;

      _ret = new MPiglet("");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret; // only need to interpret statement
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public MPiglet visit(MethodDeclaration n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("");
      MClass classBelong = (MClass)argu;


      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      MMethod methodBelong = classBelong.getMethod(n.f2.f0.toString());
      _ret.addStr("\n"+methodBelong.getPigDefineMethodName());
      // change line to clearly read

      _ret.addCode(new MPiglet("BEGIN"), 0);

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      MPiglet t1 = n.f8.accept(this, methodBelong);

      _ret.addCode(t1, 0);

      n.f9.accept(this, argu);

      _ret.addStr("\nRETURN");
      MPiglet t2 =  n.f10.accept(this, methodBelong);
      _ret.addCode(t2, 1);
      _ret.addStr("\nEND");

      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public MPiglet visit(FormalParameterList n, MType argu) {
      MPiglet _ret=null;
      // bug1
      _ret = new MPiglet("");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public MPiglet visit(FormalParameter n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("");
      // Para decleration don't need to inter
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public MPiglet visit(FormalParameterRest n, MType argu) {
      MPiglet _ret = new MPiglet("");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public MPiglet visit(Type n, MType argu) {
      MPiglet _ret = new MPiglet("");
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public MPiglet visit(ArrayType n, MType argu) {
      MPiglet _ret = new MPiglet("");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public MPiglet visit(BooleanType n, MType argu) {
      MPiglet _ret = new MPiglet("");
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public MPiglet visit(IntegerType n, MType argu) {
      MPiglet _ret = new MPiglet("");
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public MPiglet visit(Statement n, MType argu) {
      MPiglet _ret=null;
      // argu represent Mmethod
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public MPiglet visit(Block n, MType argu) {
      MPiglet _ret=null;
      n.f0.accept(this, argu);
      _ret = n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public MPiglet visit(AssignmentStatement n, MType argu) {
      MPiglet _ret=null;
      MPiglet id = n.f0.accept(this, argu);
      MVar idVar = id.getVar();

      n.f1.accept(this, argu);
      MPiglet exp = n.f2.accept(this, argu);

      if(idVar.pdTemp()){
         // temp =
         _ret = new MPiglet("MOVE");
         _ret.addCode(id, 1);
      }
      else{
         // find address
         _ret = new MPiglet("HSTORE TEMP 0 " + Integer.toString(idVar.getOffset()));
      }

      _ret.addCode(exp,1);
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public MPiglet visit(ArrayAssignmentStatement n, MType argu) {
      MPiglet _ret=null;
      String s1 = getNextTemp();
      String s2 = getNextTemp();
      _ret = new MPiglet("MOVE "+s1);


      MPiglet t1 = n.f0.accept(this, argu);
      _ret.addCode(t1, 1);
      _ret.addStr("\nMOVE "+s2+" PLUS 4 TIMES 4");

      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);
      _ret.addCode(t2, 1);
      _ret.addStr("\nMOVE "+s1+" PLUS "+s1+" "+s2);
      _ret.addStr("\nHSTORE "+s1+" 0");
      // find addr

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      MPiglet t3 = n.f5.accept(this, argu);
      _ret.addCode(t3, 1);
      n.f6.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public MPiglet visit(IfStatement n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("CJUMP");
      String s1 = getNextLabel();
      String s2 = getNextLabel();

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t1 = n.f2.accept(this, argu);
      _ret.addCode(t1, 1);
      _ret.addStr(" "+s1);

      n.f3.accept(this, argu);
      MPiglet t2 = n.f4.accept(this, argu);
      _ret.addCode(t2, 0);
      _ret.addStr("\nJUMP "+s2+"\n"+s1);

      n.f5.accept(this, argu);
      MPiglet t3 = n.f6.accept(this, argu);
      _ret.addCode(t3, 0);
      _ret.addStr("\n"+s2+" NOOP");// do nothing

      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public MPiglet visit(WhileStatement n, MType argu) {
      MPiglet _ret=null;
      String s1 = getNextLabel();
      String s2 = getNextLabel();

      _ret = new MPiglet(s1);
      _ret.addCode(new MPiglet("CJUMP"), 0);

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t1 = n.f2.accept(this, argu);
      _ret.addCode(t1, 1);
      _ret.addStr(" "+s2); // s2 -- next stm

      n.f3.accept(this, argu);
      MPiglet t2 = n.f4.accept(this, argu);
      _ret.addCode(t2, 0);
      _ret.addStr("\nJUMP "+s1);
      _ret.addStr("\n"+s2);
      _ret.addStr(" NOOP");

      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public MPiglet visit(PrintStatement n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("PRINT");
   
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);

      MPiglet t1 = n.f2.accept(this, argu);
      _ret.addCode(t1, 1);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public MPiglet visit(Expression n, MType argu) {
      MPiglet _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public MPiglet visit(AndExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("TIMES");

      MPiglet t1 = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);

      _ret.addCode(t1, 1);
      _ret.addCode(t2, 1);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public MPiglet visit(CompareExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("LT");

      MPiglet t1 = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);

      _ret.addCode(t1, 1);
      _ret.addCode(t2, 1);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public MPiglet visit(PlusExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("PLUS");

      MPiglet t1 = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);

      _ret.addCode(t1, 1);
      _ret.addCode(t2, 1);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public MPiglet visit(MinusExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("MINUS");

      MPiglet t1 = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);

      _ret.addCode(t1, 1);
      _ret.addCode(t2, 1);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public MPiglet visit(TimesExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("TIMES");

      MPiglet t1 = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);

      _ret.addCode(t1, 1);
      _ret.addCode(t2, 1);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public MPiglet visit(ArrayLookup n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("\nBEGIN");// exp begin in new line
      String s1 = getNextTemp(); 
      String s2 = getNextTemp();
      String s3 = getNextTemp();

      MPiglet t1 = n.f0.accept(this, argu);
      _ret.addCode(new MPiglet("MOVE "+s1), 0);
      _ret.addCode(t1, 1);
      n.f1.accept(this, argu);
      MPiglet t2 = n.f2.accept(this, argu);
      _ret.addCode(new MPiglet("MOVE "+s2), 0);
      _ret.addCode(t2, 1);

      _ret.addCode(new MPiglet("MOVE "+s2+" PLUS 4 TIMES 4 "+s2), 0);
      _ret.addCode(new MPiglet("MOVE "+s1+" PLUS "+s1+" "+s2), 0);// Hload temp1 temp2 temp3 is wrong

      _ret.addCode(new MPiglet("HLOAD "+s3+" "+s1+" 0"), 0);
      _ret.addCode(new MPiglet("RETURN "+s3+"\nEND"), 0);
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public MPiglet visit(ArrayLength n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("\nBEGIN");
      String s1 = getNextTemp(); 
      String s2 = getNextTemp();

      _ret.addCode(new MPiglet("MOVE "+s1), 0);
      MPiglet t1 = n.f0.accept(this, argu);
      _ret.addCode(t1, 1); // " "
      _ret.addCode(new MPiglet("HLOAD "+s2+" "+s1+" 0"), 0);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);

      _ret.addCode(new MPiglet("RETURN "+s2+"\nEND"), 0);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public MPiglet visit(MessageSend n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("CALL\nBEGIN");

      // ????
      MPiglet t1 = n.f0.accept(this, argu);
      MClass expClassBelong = t1.getPigClass();

      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      MMethod methodBelong = expClassBelong.getMethod(n.f2.f0.toString());
      _ret.setPigClass(myClasslist.findClass(methodBelong.getReturn()));

      String s1 = getNextTemp(); 
      String s2 = getNextTemp();
      String s3 = getNextTemp(); 

      _ret.addStr("\nMOVE "+s1);
      _ret.addCode(t1, 1);
      _ret.addCode(new MPiglet("HLOAD "+s2+" "+s1+" 0"), 0);
      _ret.addCode(new MPiglet("HLOAD "+s3+" "+s2+" "+methodBelong.getOffset()), 0);

      _ret.addStr("\nRETURN "+s3+"\nEND");
      n.f3.accept(this, argu);

      _ret.addStr("\n("+s1); // s1 -> self

      MPiglet t2 = n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      if(t2!=null){
         _ret.addCode(t2, 1);
      }
      _ret.addStr(")");
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public MPiglet visit(ExpressionList n, MType argu) {
      MPiglet _ret=null;
      _ret = n.f0.accept(this, argu);
      MPiglet t1 = n.f1.accept(this, argu);
      _ret.addCode(t1, 1);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public MPiglet visit(ExpressionRest n, MType argu) {
      MPiglet _ret=null;
      // bug1
      _ret = new MPiglet("");
      n.f0.accept(this, argu);
      MPiglet t1 = n.f1.accept(this, argu);
      _ret.addCode(t1, 1);
      return _ret;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public MPiglet visit(PrimaryExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public MPiglet visit(IntegerLiteral n, MType argu) {
      MPiglet _ret=null;
      //n.f0.accept(this, argu);
      _ret = new MPiglet(n.f0.toString());
      return _ret;
   }

   /**
    * f0 -> "true"
    */
   public MPiglet visit(TrueLiteral n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("1");
      //n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "false"
    */
   public MPiglet visit(FalseLiteral n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("0");
      //n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public MPiglet visit(Identifier n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("");
      MVar varBelong = null;

      if(argu == null)return null;
      else if(argu instanceof MMethod){
         varBelong = ((MMethod)argu).getVar(n.f0.toString());
      }

      _ret.setVar(varBelong);
      if(varBelong == null){
         //System.out.println(n.f0.toString()+" Id Wrong\n");
      }
      else{
         String type_var = varBelong.getType();
         MClass classBelong = myClasslist.findClass(type_var);
         _ret.setPigClass(classBelong);

         if(varBelong.pdTemp()){
            _ret.addStr("TEMP "+varBelong.getTempNum());
         }
         else{
            // class var
            _ret.addStr("\nBEGIN");
            String s1 = getNextTemp();
            _ret.addCode(new MPiglet("HLOAD "+s1+" TEMP 0 "+varBelong.getOffset()), 0);
            _ret.addStr("\nRETURN "+s1+"\nEND");
         }
      }
      //n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "this"
    */
   public MPiglet visit(ThisExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("TEMP 0");
      MClass classBelong = ((MMethod) argu).getClassBelong();

      _ret.setPigClass(classBelong);
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public MPiglet visit(ArrayAllocationExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("\nBEGIN");
      String s1 = getNextTemp(), s2 = getNextTemp(), s3 = getNextTemp();
      // exp
      _ret.addStr("\nMOVE "+s1);
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      MPiglet t1 = n.f3.accept(this, argu);
      _ret.addCode(t1, 1);
      _ret.addStr("\nMOVE "+s2+"PLUS 4 "+"TIMES 4 "+s1);
      _ret.addStr("\nMOVE "+s3+" HALLOCATE "+s2);
      _ret.addStr("\nHSTORE "+s3+" 0 "+s1);
      _ret.addCode(new MPiglet("RETURN " + s3+"\nEND"),0);
      // s2 = length
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public MPiglet visit(AllocationExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("");
      MClass tClass = myClasslist.findClass(n.f1.f0.toString());
      _ret.setPigClass(tClass);

      int flag = 0;
      // first need to allc method table
      if(tClass.getMethodTemp() == null){
         flag = 1;
         tClass.setMethodTemp(getNextTemp());
      }
      String s1 = tClass.getMethodTemp(), s2 = getNextTemp();

      _ret.addCode(new MPiglet("BEGIN"), 0);
      // if(flag == 1){ // first
         MPiglet methodTable = new MPiglet("MOVE " + s1 + " HALLOCATE " + 4*tClass.getMethodList().size());
         // alloc method table
         for(MMethod mMethod : tClass.getMethodList()) {
            MPiglet tmp = new MPiglet("HSTORE " + s1 + " " + mMethod.getOffset());
            tmp.addStr(" " + mMethod.getPigName());
            methodTable.addCode(tmp,0);
        }
        _ret.addCode(methodTable, 0);

      MPiglet varTable = new MPiglet("MOVE " + s2 + " HALLOCATE " + (4+4*tClass.getVarList().size()));
      for(MVar mVar : tClass.getVarList()) {
         MPiglet tmp = new MPiglet("HSTORE " + s2 + " " + mVar.getOffset());
         tmp.addStr(" 0");
         // not initialized
         varTable.addCode(tmp, 0);
     }

     MPiglet tPig = new MPiglet("HSTORE "+s2+" 0 "+s1+"\nRETURN "+s2+"\nEND");
     // class temp0 -> methodtable

     _ret.addCode(varTable, 0);
     _ret.addCode(tPig, 0);
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public MPiglet visit(NotExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("MINUS 1");
      // 1 - exp
      n.f0.accept(this, argu);
      MPiglet t1 = n.f1.accept(this, argu);
      _ret.addCode(t1, 1);
      return _ret;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public MPiglet visit(BracketExpression n, MType argu) {
      MPiglet _ret=null;
      _ret = new MPiglet("");
      n.f0.accept(this, argu);
      MPiglet t1 = n.f1.accept(this, argu);
      _ret.addCode(t1, 1);
      _ret.setPigClass(t1.getPigClass());
      _ret.setVar(t1.getVar());
      n.f2.accept(this, argu);
      return _ret;
   }

}
