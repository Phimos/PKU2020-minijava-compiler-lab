package visitor;

import syntaxtree.*;
import symbol.*;

public class BuildSymbolTableVisitor extends GJDepthFirst<Object, Object>{
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
    public Object visit(MainClass n, Object argu) {
        Object _ret=null;
        MClassList classList = (MClassList)argu;
        
        n.f0.accept(this, argu);

        MClass mainClass = new MClass(n.f1.f0.tokenImage, null, n.f1.f0.beginLine, n.f1.f0.beginColumn);
        classList.addClass(mainClass);
        n.f1.accept(this, argu);

        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);

        MMethod mainMethod = new MMethod(n.f6.tokenImage, "void", mainClass, n.f6.beginLine, n.f6.beginColumn);
        mainClass.addMethod(mainMethod);
        n.f6.accept(this, argu);

        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);

        MVar mainArg = new MVar(n.f11.f0.tokenImage, "String[]", mainMethod, mainClass, n.f11.f0.beginLine, n.f11.f0.beginColumn);
        mainMethod.addParam(mainArg);
        n.f11.accept(this, argu);

        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, mainMethod);
        n.f15.accept(this, mainMethod);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
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
    public Object visit(ClassDeclaration n, Object argu) {
        Object _ret=null;
        MClassList classList = (MClassList)argu;
        
        n.f0.accept(this, argu);

        MClass newClass =new MClass(n.f1.f0.tokenImage, null, n.f1.f0.beginLine, n.f1.f0.beginColumn);
        classList.addClass(newClass);
        n.f1.accept(this, argu);

        n.f2.accept(this, argu);
        n.f3.accept(this, newClass);
        n.f4.accept(this, newClass);
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
    public Object visit(ClassExtendsDeclaration n, Object argu) {
        Object _ret=null;
        MClassList classList = (MClassList)argu;

        n.f0.accept(this, argu);
        MType parentClass = new MType(n.f3.f0.tokenImage, n.f3.f0.beginLine, n.f3.f0.beginColumn);
        MClass newClass = new MClass(n.f1.f0.tokenImage, parentClass, n.f1.f0.beginLine, n.f1.f0.beginColumn);
        classList.addClass(newClass);
        n.f1.accept(this, argu);

        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, newClass);
        n.f6.accept(this, newClass);
        n.f7.accept(this, argu);
        return _ret;
    }
    
    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public Object visit(VarDeclaration n, Object argu) {
        Object _ret=null;
        MIdentifier tempId = (MIdentifier)argu;
        MClass classBelong = null;
        MMethod methodBelong = null;
        if(tempId.isClass()){
            classBelong = (MClass)tempId;
        }
        else if(tempId.isMethod()){
            methodBelong = (MMethod)tempId;
            classBelong = methodBelong.getClassBelong();
        }

        String typeName = ((MType)n.f0.accept(this, argu)).getName();
        MVar newVar = new MVar(n.f1.f0.tokenImage, typeName, methodBelong, classBelong, n.f1.f0.beginLine, n.f1.f0.beginColumn);
        if(tempId.isClass()){
            classBelong.addVar(newVar);
        }
        else if(tempId.isMethod()){
            methodBelong.addVar(newVar);
        }
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
    public Object visit(MethodDeclaration n, Object argu) {
        Object _ret=null;
        MClass classBelong = (MClass)argu;
        n.f0.accept(this, argu);

        String typeName = ((MType)n.f1.accept(this, argu)).getName();
        n.f1.accept(this, argu);

        MMethod newMethod = new MMethod(n.f2.f0.tokenImage, typeName, (MClass)argu, n.f2.f0.beginLine, n.f2.f0.beginColumn);
        classBelong.addMethod(newMethod);
        n.f2.accept(this, argu);

        n.f3.accept(this, argu);
        n.f4.accept(this, newMethod);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, newMethod);
        n.f8.accept(this, newMethod);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        return _ret;
    }
    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public Object visit(FormalParameter n, Object argu) {
        Object _ret=null;
        MMethod methodBelong = (MMethod)argu;
        String typeName = ((MType)n.f0.accept(this, argu)).getName();
        n.f0.accept(this, argu);
        MVar newVar = new MVar(n.f1.f0.tokenImage, typeName, methodBelong, methodBelong.getClassBelong(), n.f1.f0.beginLine, n.f1.f0.beginColumn);
        methodBelong.addParam(newVar);
        n.f1.accept(this, argu);
        return _ret;
    }    

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public Object visit(Type n, Object argu) {
        return n.f0.accept(this, argu);
    }    

    /**
    * f0 -> <IDENTIFIER>
    */
    public Object visit(Identifier n, Object argu) {
        n.f0.accept(this, argu);
        return new MType(n.f0.tokenImage, n.f0.beginLine, n.f0.beginColumn);
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(ArrayType n, Object argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new MType("int[]", n.f0.beginLine, n.f0.beginColumn);
     }
  
     /**
      * f0 -> "boolean"
      */
    public Object visit(BooleanType n, Object argu) {
        n.f0.accept(this, argu);
        return new MType("boolean", n.f0.beginLine, n.f0.beginColumn);
    }

    /**
     * f0 -> "int"
     */
    public Object visit(IntegerType n, Object argu) {
        n.f0.accept(this, argu);
        return new MType("int", n.f0.beginLine, n.f0.beginColumn);
    } 

}