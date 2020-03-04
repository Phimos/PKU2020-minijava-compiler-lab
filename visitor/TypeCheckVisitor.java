package visitor;

import symbol.*;
import syntaxtree.*;
import typecheck.*;

public class TypeCheckVisitor extends GJDepthFirst<MType, MType>{
    
    MClassList allClassList;

    public TypeCheckVisitor(MClassList _allclass){
        this.allClassList = _allclass;
        allClassList.setAllParent();
        allClassList.checkAllCycle();
    }

    public boolean checkTypeDeclared(MType type){
        String typeName = type.getName();
        if(typeName.equals("int") || typeName.equals("int[]") || typeName.equals("boolean") || allClassList.existClass(typeName)){
            return true;
        }
        else{
            ErrorPrinter.addError(String.format("ERROR 1:\n\tundefined type: can't find the type\n\tlocation: row %d, col %d", type.getRow(), type.getCol()));
            return false;
        }
    }

    public boolean typeEquals(MType type, String typeName){
        return type.getName().equals(typeName);
    }

    public boolean typeEquals(MType type1, MType type2){
        return type1.getName().equals(type2.getName());
    }

    public MVar findVar(MMethod methodBelong, String varName){
        if(methodBelong.existVar(varName)){
            return methodBelong.getVar(varName);
        }
        for(MClass classBelong = methodBelong.getClassBelong(); classBelong!=null ; classBelong = classBelong.getParent()){
            if(classBelong.existVar(varName)){
                return classBelong.getVar(varName);
            }
        }
        return null;
    }

    public MMethod findMethod(MClass classBelong, String methodName){
        for(;classBelong!=null;classBelong = classBelong.getParent()){
            if(classBelong.existMethod(methodName)){
                return classBelong.getMethod(methodName);
            }
        }
        return null;
    }



   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public MType visit(Goal n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
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
     public MType visit(MainClass n, MType argu) {
        MClassList classList = (MClassList)argu;
        n.f0.accept(this, argu);
        String mainClassName = n.f1.accept(this, argu).getName();
        MMethod mainMethod = classList.findClass(mainClassName).getMethod("main");
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
        n.f14.accept(this, mainMethod);
        n.f15.accept(this, mainMethod);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return null;
     }
  
     /**
      * f0 -> ClassDeclaration()
      *       | ClassExtendsDeclaration()
      */
     public MType visit(TypeDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
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
     public MType visit(ClassDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        String className = n.f1.accept(this, argu).getName();
        MClass classBelong = allClassList.findClass(className);
        n.f2.accept(this, argu);
        n.f3.accept(this, classBelong);
        n.f4.accept(this, classBelong);
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
     public MType visit(ClassExtendsDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        String className = n.f1.accept(this, argu).getName();
        MClass classBelong = allClassList.findClass(className);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, classBelong);
        n.f6.accept(this, classBelong);
        n.f7.accept(this, argu);
        return _ret;
     }
  
     /**
      * f0 -> Type()
      * f1 -> Identifier()
      * f2 -> ";"
      */
     public MType visit(VarDeclaration n, MType argu) {
        MType _ret=null;
        MType type = n.f0.accept(this, argu);
        checkTypeDeclared(type);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
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
     public MType visit(MethodDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        
        MType type = n.f1.accept(this, argu);
        if(type == null){
            System.out.println("FFFFFFFF");
        }
        checkTypeDeclared(type);

        String methodName = n.f2.accept(this, argu).getName();
        MMethod newMethod = ((MClass)argu).getMethod(methodName);

        n.f3.accept(this, argu);
        n.f4.accept(this, newMethod);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, newMethod);
        n.f8.accept(this, newMethod);
        n.f9.accept(this, argu);
        
        MType ret = n.f10.accept(this, argu);
        if(!typeEquals(ret, type)){
            ErrorPrinter.addError(String.format("ERROR 4:\n\treturn dismatch: the type of return value is wrong\n\tlocation: row %d, col %d", ret.getRow(), ret.getCol()));
        }
        
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        return _ret;
     }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public MType visit(FormalParameter n, MType argu) {
        MType _ret=null;
        MType type = n.f0.accept(this, argu);
        checkTypeDeclared(type);
        n.f1.accept(this, argu);
        return _ret;
     }

    /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
    public MType visit(Type n, MType argu) {
        return n.f0.accept(this, argu);
    }

    /**
     * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    public MType visit(ArrayType n, MType argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new MType("int[]", n.f0.beginLine, n.f0.beginColumn);
    }

    /**
     * f0 -> "boolean"
    */
    public MType visit(BooleanType n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("boolean", n.f0.beginLine, n.f0.beginColumn);
    }

    /**
     * f0 -> "int"
    */
    public MType visit(IntegerType n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("int", n.f0.beginLine, n.f0.beginColumn);
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public MType visit(AssignmentStatement n, MType argu) {
        MType _ret=null;
        MMethod methodBelong = (MMethod)argu;
        MVar leftAssign = findVar(methodBelong, n.f0.f0.tokenImage);
        if(leftAssign == null){
            ErrorPrinter.addError(String.format("ERROR 1:\n\tundefined variable: can't assign an undefined variable\n\tlocation: row %d, col %d", n.f0.f0.beginLine, n.f0.f0.beginColumn));
        }
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightAssign = n.f2.accept(this, argu);
        n.f3.accept(this, argu);

        if(!typeEquals(rightAssign, leftAssign.getType())){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the left part and right part must have the same type\n\tlocation: row %d, col %d", n.f0.f0.beginLine, n.f0.f0.beginColumn));
        }
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
    public MType visit(ArrayAssignmentStatement n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        MVar array = findVar((MMethod)argu, n.f0.f0.tokenImage);
        if(!array.getType().equals("int[]")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: it should be an array\n\tlocation: row %d, col %d", array.getRow(), array.getCol()));
        }
        n.f1.accept(this, argu);
        MType indexType = n.f2.accept(this, argu);
        if(!typeEquals(indexType, "int")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the index of array should be type \"int\"\n\tlocation: row %d, col %d", indexType.getRow(), indexType.getCol()));
        }
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        MType rightAssign = n.f5.accept(this, argu);
        if(!typeEquals(rightAssign, "int")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the left part and right part must have the same type\n\tlocation: row %d, col %d", n.f0.f0.beginLine, n.f0.f0.beginColumn));
        }
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
     public MType visit(IfStatement n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);

        MType exp = n.f2.accept(this, argu);
        if(!typeEquals(exp, "boolean")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the if statement only accept type \"boolean\"\n\tlocation: row %d, col %d", exp.getRow(), exp.getCol()));
        }

        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
     }
  
     /**
      * f0 -> "while"
      * f1 -> "("
      * f2 -> Expression()
      * f3 -> ")"
      * f4 -> Statement()
      */
     public MType visit(WhileStatement n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);

        MType exp = n.f2.accept(this, argu);
        if(!typeEquals(exp, "boolean")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the while statement only accept type \"boolean\"\n\tlocation: row %d, col %d", exp.getRow(), exp.getCol()));
        }

        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
     }
  
     /**
      * f0 -> "System.out.println"
      * f1 -> "("
      * f2 -> Expression()
      * f3 -> ")"
      * f4 -> ";"
      */
     public MType visit(PrintStatement n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType exp = n.f2.accept(this, argu);
        if(!typeEquals(exp, "int")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the print statement only accept type \"int\"\n\tlocation: row %d, col %d", exp.getRow(), exp.getCol()));
        }
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
     public MType visit(Expression n, MType argu) {
        return n.f0.accept(this, argu);
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "&&"
      * f2 -> PrimaryExpression()
      */
     public MType visit(AndExpression n, MType argu) {
        MType leftExp = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightExp = n.f2.accept(this, argu);
        if(!(typeEquals(leftExp, "boolean")&&typeEquals(rightExp, "boolean"))){
            ErrorPrinter.addError(String.format("ERROR 5:\n\toperator: AND expression needs type \"boolean\" on both sides\n\tlocation: row %d, col %d", leftExp.getRow(), leftExp.getCol()));
        }
        return new MType("boolean", leftExp.getRow(), leftExp.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "<"
      * f2 -> PrimaryExpression()
      */
     public MType visit(CompareExpression n, MType argu) {
        MType leftExp = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightExp = n.f2.accept(this, argu);
        if(!(typeEquals(leftExp, "int")&&typeEquals(rightExp, "int"))){
            ErrorPrinter.addError(String.format("ERROR 5:\n\toperator: COMPARE expression needs type \"int\" on both sides\n\tlocation: row %d, col %d", leftExp.getRow(), leftExp.getCol()));
        }
        return new MType("boolean", leftExp.getRow(), leftExp.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "+"
      * f2 -> PrimaryExpression()
      */
     public MType visit(PlusExpression n, MType argu) {
        MType leftExp = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightExp = n.f2.accept(this, argu);
        if(!(typeEquals(leftExp, "int")&&typeEquals(rightExp, "int"))){
            ErrorPrinter.addError(String.format("ERROR 5:\n\toperator: PLUS expression needs type \"int\" on both sides\n\tlocation: row %d, col %d", leftExp.getRow(), leftExp.getCol()));
        }
        return new MType("int", leftExp.getRow(), leftExp.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "-"
      * f2 -> PrimaryExpression()
      */
     public MType visit(MinusExpression n, MType argu) {
        MType leftExp = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightExp = n.f2.accept(this, argu);
        if(!(typeEquals(leftExp, "int")&&typeEquals(rightExp, "int"))){
            ErrorPrinter.addError(String.format("ERROR 5:\n\toperator: MINUS expression needs type \"int\" on both sides\n\tlocation: row %d, col %d", leftExp.getRow(), leftExp.getCol()));
        }
        return new MType("int", leftExp.getRow(), leftExp.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "*"
      * f2 -> PrimaryExpression()
      */
     public MType visit(TimesExpression n, MType argu) {
        MType leftExp = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightExp = n.f2.accept(this, argu);
        if(!(typeEquals(leftExp, "int")&&typeEquals(rightExp, "int"))){
            ErrorPrinter.addError(String.format("ERROR 5:\n\toperator: TIMES expression needs type \"int\" on both sides\n\tlocation: row %d, col %d", leftExp.getRow(), leftExp.getCol()));
        }
        return new MType("int", leftExp.getRow(), leftExp.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "["
      * f2 -> PrimaryExpression()
      * f3 -> "]"
      */
     public MType visit(ArrayLookup n, MType argu) {
        MType array = n.f0.accept(this, argu);
        if(!typeEquals(array, "int[]")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: it should be an array\n\tlocation: row %d, col %d", array.getRow(), array.getCol()));
        }
        n.f1.accept(this, argu);
        MType indexType = n.f2.accept(this, argu);
        if(!typeEquals(indexType, "int")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the index of array should be type \"int\"\n\tlocation: row %d, col %d", indexType.getRow(), indexType.getCol()));
        }
        n.f3.accept(this, argu);
        return new MType("int", array.getRow(), array.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "."
      * f2 -> "length"
      */
     public MType visit(ArrayLength n, MType argu) {
        MType array = n.f0.accept(this, argu);
        if(!typeEquals(array, "int[]")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: it should be an array\n\tlocation: row %d, col %d", array.getRow(), array.getCol()));
        }
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new MType("int", array.getRow(), array.getCol());
     }
  
     /**
      * f0 -> PrimaryExpression()
      * f1 -> "."
      * f2 -> Identifier()
      * f3 -> "("
      * f4 -> ( ExpressionList() )?
      * f5 -> ")"
      */
     public MType visit(MessageSend n, MType argu) {
        MType leftPart = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rightPart = n.f2.accept(this, argu);

        MMethod method = findMethod(allClassList.findClass(leftPart.getName()), rightPart.getName());
        if(method == null){
            ErrorPrinter.addError(String.format("ERROR 1:\n\tundefined method: can't use an undefined method\n\tlocation: row %d, col %d", n.f2.f0.beginLine, n.f2.f0.beginColumn));
        }

        n.f3.accept(this, argu);
        n.f4.accept(this, method);
        n.f5.accept(this, argu);
        return new MType(method.getReturn(), n.f2.f0.beginLine, n.f2.f0.beginColumn);
     }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public MType visit(ExpressionList n, MType argu) {
        MType _ret=null;
        MMethod methodBelong = (MMethod)argu;
        methodBelong.turnonCheckMode();
        MType firstParam = n.f0.accept(this, argu);
        methodBelong.paramTypeCheck(firstParam);
        n.f1.accept(this, argu);
        methodBelong.turnoffCheckMode(firstParam.getRow(), firstParam.getCol());
        return _ret;
    }
  
    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public MType visit(ExpressionRest n, MType argu) {
        n.f0.accept(this, argu);
        MType param = n.f1.accept(this, argu);
        ((MMethod)argu).paramTypeCheck(param);
        return null;
    }
/************************************* DONE BELOW *********************************************/
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
     public MType visit(PrimaryExpression n, MType argu) {
        MType temp = n.f0.accept(this, argu);
        if((!temp.getName().equals("int"))&&(!temp.getName().equals("int[]"))&&(!temp.getName().equals("boolean"))&&(!allClassList.existClass(temp.getName()))){
            String varType = findVar((MMethod)argu, temp.getName()).getType();
            temp.setName(varType);
        }
        return temp;
     }
  
     /**
      * f0 -> <INTEGER_LITERAL>
      */
     public MType visit(IntegerLiteral n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("int", n.f0.beginLine, n.f0.beginColumn);
     }
  
     /**
      * f0 -> "true"
      */
     public MType visit(TrueLiteral n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("boolean", n.f0.beginLine, n.f0.beginColumn);
     }
  
     /**
      * f0 -> "false"
      */
     public MType visit(FalseLiteral n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("boolean", n.f0.beginLine, n.f0.beginColumn);
     }

    /**
     * f0 -> <IDENTIFIER>
     */
    public MType visit(Identifier n, MType argu) {
        n.f0.accept(this, argu);
        return new MType(n.f0.tokenImage, n.f0.beginLine, n.f0.beginColumn);
    }

    /**
     * f0 -> "this"
     */
    public MType visit(ThisExpression n, MType argu) {
        MMethod methodBelong = (MMethod)argu;
        String typeName = methodBelong.getClassBelong().getName();
        n.f0.accept(this, argu);
        return new MType(typeName, n.f0.beginLine, n.f0.beginColumn);
    }

     /**
      * f0 -> "new"
      * f1 -> "int"
      * f2 -> "["
      * f3 -> Expression()
      * f4 -> "]"
      */
     public MType visit(ArrayAllocationExpression n, MType argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        
        MType indexType = n.f3.accept(this, argu);
        if(!typeEquals(indexType, "int")){
            ErrorPrinter.addError(String.format("ERROR 3:\n\ttype mismatch: the index of array should be type \"int\"\n\tlocation: row %d, col %d", indexType.getRow(), indexType.getCol()));
        }

        n.f4.accept(this, argu);
        return new MType("int[]", n.f1.beginLine, n.f1.beginColumn);
     }
  
    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public MType visit(AllocationExpression n, MType argu) {
       n.f0.accept(this, argu);
       MType temp = n.f1.accept(this, argu);
       n.f2.accept(this, argu);
       n.f3.accept(this, argu);
       return temp;
    }

     /**
      * f0 -> "!"
      * f1 -> Expression()
      */
     public MType visit(NotExpression n, MType argu) {
        n.f0.accept(this, argu);
        MType exp = n.f1.accept(this, argu);
        if(!typeEquals(exp, "boolean")){
            ErrorPrinter.addError(String.format("ERROR 5:\n\toperator: not expression needs type \"boolean\"\n\tlocation: row %d, col %d", exp.getRow(), exp.getCol()));
        }
        return new MType("boolean", n.f0.beginLine, n.f0.beginColumn);
     }
  
    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public MType visit(BracketExpression n, MType argu) {
       n.f0.accept(this, argu);
       MType exp = n.f1.accept(this, argu);
       n.f2.accept(this, argu);
       return exp;
    }
}