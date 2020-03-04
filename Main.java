import java.io.*;
import syntaxtree.*;
import symbol.*;
import visitor.*;
import typecheck.*;

public class Main{
    public static void main(String args[]){
        try{
            InputStream in = new FileInputStream(args[0]);
            Node root = new MiniJavaParser(in).Goal();
            MType allClassList = new MClassList();
            root.accept(new BuildSymbolTableVisitor(), allClassList);
            root.accept(new TypeCheckVisitor((MClassList)allClassList), allClassList);
            if(ErrorPrinter.getsize()==0){
                System.out.println("Program type checked successfully");
            }
            else{
                System.out.println("Type error");
            }
            ErrorPrinter.printAll();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(TokenMgrError e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}