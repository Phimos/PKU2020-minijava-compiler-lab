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
            System.out.println("Program type checked successfully");

            MClassList tclist = new MClassList();
            // ---piglet---
            allClassList.classComplete();
            allClassList.allocTemp(20);
            //allClassList.printAll();
            PrintStream out = new PrintStream(args[0].replace(".java", "_my.pg"));

            MPiglet result = root.accept(new MiniJavaToPigletVistor((MClassList)allClassList), allClassList);
            out.println(result.getCode().toString());
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